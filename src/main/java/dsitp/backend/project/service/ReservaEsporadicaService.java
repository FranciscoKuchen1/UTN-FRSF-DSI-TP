package dsitp.backend.project.service;

import dsitp.backend.project.domain.Aula;
import dsitp.backend.project.domain.DiaReservado;
import dsitp.backend.project.domain.Reserva;
import dsitp.backend.project.domain.ReservaEsporadica;
import dsitp.backend.project.mapper.AulaMapper;
import dsitp.backend.project.mapper.DiaReservadoMapper;
import dsitp.backend.project.mapper.ReservaEsporadicaMapper;
import dsitp.backend.project.model.AulaDTO;
import dsitp.backend.project.model.AulaSolapadaDTO;
import dsitp.backend.project.model.DiaDisponibilidadDTO;
import dsitp.backend.project.model.DiaSolapamientoDTO;
import dsitp.backend.project.model.ReservaEsporadicaDTO;
import dsitp.backend.project.model.ReservaRespuestaDTO;
import dsitp.backend.project.repos.AulaRepository;
import dsitp.backend.project.repos.DiaReservadoRepository;
import dsitp.backend.project.repos.ReservaEsporadicaRepository;
import dsitp.backend.project.util.NotFoundException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReservaEsporadicaService {

    private final ReservaEsporadicaRepository reservaEsporadicaRepository;
    private final DiaReservadoRepository diaReservadoRepository;
    private final AulaRepository aulaRepository;
    private final ReservaEsporadicaMapper reservaEsporadicaMapper;
    private final DiaReservadoMapper diaReservadoMapper;
    private final AulaMapper aulaMapper;

    @Autowired
    public ReservaEsporadicaService(final ReservaEsporadicaRepository reservaEsporadicaRepository, final DiaReservadoRepository diaReservadoRepository, final AulaRepository aulaRepository, final ReservaEsporadicaMapper reservaEsporadicaMapper, final DiaReservadoMapper diaReservadoMapper, final AulaMapper aulaMapper) {
        this.reservaEsporadicaRepository = reservaEsporadicaRepository;
        this.diaReservadoRepository = diaReservadoRepository;
        this.aulaRepository = aulaRepository;
        this.reservaEsporadicaMapper = reservaEsporadicaMapper;
        this.diaReservadoMapper = diaReservadoMapper;
        this.aulaMapper = aulaMapper;
    }

    public List<ReservaEsporadicaDTO> findAll() {
        final List<ReservaEsporadica> reservaEsporadicas = reservaEsporadicaRepository.findAll(Sort.by("id"));
        return reservaEsporadicas.stream()
                .map(reservaEsporadica -> reservaEsporadicaMapper.toReservaEsporadicaDTO(reservaEsporadica))
                .toList();
    }

    public ReservaEsporadicaDTO get(final Integer id) {
        return reservaEsporadicaRepository.findById(id)
                .map(reservaEsporadica -> reservaEsporadicaMapper.toReservaEsporadicaDTO(reservaEsporadica))
                .orElseThrow(NotFoundException::new);
    }

    public ReservaRespuestaDTO getDisponibilidadAulaReservaEsporadica(final ReservaEsporadicaDTO reservaEsporadicaDTO) {
        final ReservaEsporadica reservaEsporadica = reservaEsporadicaMapper.toReservaEsporadicaEntity(reservaEsporadicaDTO);
        ReservaRespuestaDTO reservaRespuestaDTO = new ReservaRespuestaDTO();
        List<Aula> aulas = aulaRepository.findByTipoAulaAndCapacidad(reservaEsporadica.getCantAlumnos(), reservaEsporadica.getTipoAula().toInteger());
        for (DiaReservado diaReservado : reservaEsporadica.getDiasReservados()) {
            List<AulaDTO> aulasDisponibles = obtenerDisponibilidad(aulas, diaReservado);
            if (!aulasDisponibles.isEmpty()) {
                DiaDisponibilidadDTO diaDisponibilidadDTO = new DiaDisponibilidadDTO();
                diaDisponibilidadDTO.setDiaReservado(diaReservadoMapper.toDiaReservadoDTO(diaReservado));
                diaDisponibilidadDTO.setAulasDisponibles(aulasDisponibles);
            } else {
                DiaSolapamientoDTO diaSolapamientoDTO = new DiaSolapamientoDTO();
                diaSolapamientoDTO.setDiaReservado(diaReservadoMapper.toDiaReservadoDTO(diaReservado));
                diaSolapamientoDTO.setAulasConSolapamiento(obtenerAulasConMenorSuperposicion(aulas, diaReservado));
            }
        }

        return reservaRespuestaDTO;
    }

    public List<AulaDTO> obtenerDisponibilidad(List<Aula> aulas, DiaReservado diaReservado) {
        List<AulaDTO> aulasDisponibles = new ArrayList<>();

        for (Aula aula : aulas) {
            Boolean disponible = verificarDisponibilidad(aula, diaReservado);
            if (disponible) {
                aulasDisponibles.add(aulaMapper.toAulaDTO(aula));
            }
        }

        aulasDisponibles.sort(Comparator.comparing(AulaDTO::getCapacidad).reversed());

        return aulasDisponibles;
    }

    private Boolean verificarDisponibilidad(Aula aula, DiaReservado diaReservado) {

        List<DiaReservado> diasReservados = diaReservadoRepository.findOverlappingDays(aula.getNumero(), diaReservado.getFechaReserva(), diaReservado.getHoraInicio(), diaReservado.getHoraInicio().plusMinutes(diaReservado.getDuracion()));

        return diasReservados.isEmpty();
    }

    public List<AulaSolapadaDTO> obtenerAulasConMenorSuperposicion(List<Aula> aulas, DiaReservado diaReservado) {

        List<AulaSolapadaDTO> aulasSolapadasDTO = new ArrayList<>();

        Map<Aula, Long> aulaCantidadHorasSuper = new HashMap<>();

        Map<Aula, Reserva> aulaReservaSolapada = new HashMap<>();

        for (Aula aula : aulas) {
            List<DiaReservado> diasReservados = diaReservadoRepository.findPartiallyOverlappingDays(
                    aula.getNumero(),
                    diaReservado.getFechaReserva(),
                    diaReservado.getHoraInicio(),
                    diaReservado.getHoraInicio().plusMinutes(diaReservado.getDuracion())
            );

            Optional<DiaReservado> diaConMayorSuperposicionOptional = Optional.ofNullable(null);

            Long maxHorasSuperpuestas = 0L;

            for (DiaReservado dr : diasReservados) {

                Long horasSuperpuestas = calcularHorasSuperpuestas(diaReservado, dr);

                if (horasSuperpuestas > maxHorasSuperpuestas) {
                    maxHorasSuperpuestas = horasSuperpuestas;
                    diaConMayorSuperposicionOptional = Optional.of(dr);
                }
            }
            diaConMayorSuperposicionOptional.ifPresent(dia -> aulaReservaSolapada.put(aula, dia.getReserva()));
            aulaCantidadHorasSuper.put(aula, maxHorasSuperpuestas);
        }

        Long superposicionMinima = aulaCantidadHorasSuper.values()
                .stream()
                .min(Long::compare)
                .orElse(0L);

        List<Aula> aulasConMenorSuperposicion = aulaCantidadHorasSuper.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), superposicionMinima))
                .map(Map.Entry::getKey)
                .toList();

        for (Aula aula : aulasConMenorSuperposicion) {
            aulasSolapadasDTO.add(aulaMapper.toAulaSolapadaDTO(aula, aulaReservaSolapada.get(aula)));
        }

        return aulasSolapadasDTO;
    }

    private Long calcularHorasSuperpuestas(DiaReservado dia1, DiaReservado dia2) {
        LocalTime inicio1 = dia1.getHoraInicio();
        LocalTime fin1 = dia1.getHoraInicio().plusMinutes(dia1.getDuracion());
        LocalTime inicio2 = dia2.getHoraInicio();
        LocalTime fin2 = dia2.getHoraInicio().plusMinutes(dia2.getDuracion());

        LocalTime inicioSuperposicion = inicio1.isAfter(inicio2) ? inicio1 : inicio2;
        LocalTime finSuperposicion = fin1.isBefore(fin2) ? fin1 : fin2;

        if (inicioSuperposicion.isBefore(finSuperposicion)) {
            return Duration.between(inicioSuperposicion, finSuperposicion).toMinutes() / 60;
        } else {
            return 0L;
        }
    }

    public Integer create(final ReservaEsporadicaDTO reservaEsporadicaDTO) {
        final ReservaEsporadica reservaEsporadica = reservaEsporadicaMapper.toReservaEsporadicaEntity(reservaEsporadicaDTO);

        return reservaEsporadicaRepository.save(reservaEsporadica).getId();
    }

    public void update(final Integer id, final ReservaEsporadicaDTO reservaEsporadicaDTO) {
        final ReservaEsporadica existingReservaEsporadica = reservaEsporadicaRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        ReservaEsporadica updatedReservaEsporadica = reservaEsporadicaMapper.toReservaEsporadicaEntity(reservaEsporadicaDTO);

        updatedReservaEsporadica.setId(existingReservaEsporadica.getId());
        reservaEsporadicaRepository.save(updatedReservaEsporadica);
    }

    public void delete(final Integer id) {
        final ReservaEsporadica existingReservaEsporadica = reservaEsporadicaRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        existingReservaEsporadica.setBedel(null);
        existingReservaEsporadica.setDiasReservados(null);
        reservaEsporadicaRepository.save(existingReservaEsporadica);

        reservaEsporadicaRepository.deleteById(id);
    }

}
