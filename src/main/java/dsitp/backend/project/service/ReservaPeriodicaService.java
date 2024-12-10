package dsitp.backend.project.service;

import dsitp.backend.project.domain.Aula;
import dsitp.backend.project.domain.DiaReservado;
import dsitp.backend.project.domain.Reserva;
import dsitp.backend.project.domain.ReservaPeriodica;
import dsitp.backend.project.mapper.AulaMapper;
import dsitp.backend.project.mapper.DiaReservadoMapper;
import dsitp.backend.project.mapper.ReservaPeriodicaMapper;
import dsitp.backend.project.model.AulaDTO;
import dsitp.backend.project.model.AulaSolapadaDTO;
import dsitp.backend.project.model.DiaDisponibilidadDTO;
import dsitp.backend.project.model.DiaSolapamientoDTO;
import dsitp.backend.project.model.ReservaPeriodicaDTO;
import dsitp.backend.project.model.ReservaPeriodicaSinDiasDTO;
import dsitp.backend.project.model.ReservaRespuestaDTO;
import dsitp.backend.project.repos.AulaRepository;
import dsitp.backend.project.repos.DiaReservadoRepository;
import dsitp.backend.project.repos.ReservaPeriodicaRepository;
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
public class ReservaPeriodicaService {

    private final ReservaPeriodicaRepository reservaPeriodicaRepository;
    private final DiaReservadoRepository diaReservadoRepository;
    private final AulaRepository aulaRepository;
    private final ReservaPeriodicaMapper reservaPeriodicaMapper;
    private final DiaReservadoMapper diaReservadoMapper;
    private final AulaMapper aulaMapper;

    @Autowired
    public ReservaPeriodicaService(final ReservaPeriodicaRepository reservaPeriodicaRepository, final AulaRepository aulaRepository, final DiaReservadoRepository diaReservadoRepository, final ReservaPeriodicaMapper reservaPeriodicaMapper, final DiaReservadoMapper diaReservadoMapper, final AulaMapper aulaMapper) {
        this.reservaPeriodicaRepository = reservaPeriodicaRepository;
        this.reservaPeriodicaMapper = reservaPeriodicaMapper;
        this.aulaRepository = aulaRepository;
        this.aulaMapper = aulaMapper;
        this.diaReservadoRepository = diaReservadoRepository;
        this.diaReservadoMapper = diaReservadoMapper;

    }

    public List<ReservaPeriodicaDTO> findAll() {
        final List<ReservaPeriodica> reservaPeriodicas = reservaPeriodicaRepository.findAll(Sort.by("id"));
        return reservaPeriodicas.stream()
                .map(reservaPeriodicaMapper::toReservaPeriodicaDTO)
                .toList();
    }

    public ReservaPeriodicaDTO get(final Integer id) {
        return reservaPeriodicaRepository.findById(id)
                .map(reservaPeriodicaMapper::toReservaPeriodicaDTO)
                .orElseThrow(NotFoundException::new);
    }

    public ReservaRespuestaDTO getDisponibilidadAulaReservaPeriodica(final ReservaPeriodicaSinDiasDTO reservaPeriodicaSinDiasDTO) {
        final ReservaPeriodica reservaPeriodica = reservaPeriodicaMapper.toReservaPeriodicaEntityDisponibilidad(reservaPeriodicaSinDiasDTO);
        ReservaRespuestaDTO reservaRespuestaDTO = new ReservaRespuestaDTO();
        reservaRespuestaDTO.setDiasDisponibles(new ArrayList<>());
        reservaRespuestaDTO.setDiasConSolapamiento(new ArrayList<>());
        List<Aula> aulas = aulaRepository.findByTipoAulaAndCapacidad(reservaPeriodica.getCantAlumnos(), reservaPeriodica.getTipoAula().toInteger());
        if (!aulas.isEmpty()){
            for (DiaReservado diaReservado : reservaPeriodica.getDiasReservados()) {
                List<AulaDTO> aulasDisponibles = obtenerDisponibilidad(aulas, diaReservado);
                if (!aulasDisponibles.isEmpty()) {
                    DiaDisponibilidadDTO diaDisponibilidadDTO = new DiaDisponibilidadDTO();
                    diaDisponibilidadDTO.setDiaReservado(diaReservadoMapper.toDiaReservadoDTO(diaReservado));
                    diaDisponibilidadDTO.setAulasDisponibles(aulasDisponibles);
                    reservaRespuestaDTO.getDiasDisponibles().add(diaDisponibilidadDTO);
                } else {
                    DiaSolapamientoDTO diaSolapamientoDTO = new DiaSolapamientoDTO();
                    diaSolapamientoDTO.setDiaReservado(diaReservadoMapper.toDiaReservadoDTO(diaReservado));
                    diaSolapamientoDTO.setAulasConSolapamiento(obtenerAulasConMenorSuperposicion(aulas, diaReservado));
                    reservaRespuestaDTO.getDiasConSolapamiento().add(diaSolapamientoDTO);
                }
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

            Optional<DiaReservado> diaConMayorSuperposicionOptional = Optional.empty();

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

    public Integer create(final ReservaPeriodicaDTO reservaPeriodicaDTO) {
        final ReservaPeriodica reservaPeriodica = reservaPeriodicaMapper.toReservaPeriodicaEntity(reservaPeriodicaDTO);

        return reservaPeriodicaRepository.save(reservaPeriodica).getId();
    }

    public void update(final Integer id, final ReservaPeriodicaDTO reservaPeriodicaDTO) {
        final ReservaPeriodica existingReservaPeriodica = reservaPeriodicaRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        ReservaPeriodica updatedReservaPeriodica = reservaPeriodicaMapper.toReservaPeriodicaEntity(reservaPeriodicaDTO);

        updatedReservaPeriodica.setId(existingReservaPeriodica.getId());
        reservaPeriodicaRepository.save(updatedReservaPeriodica);
    }

    public void delete(final Integer id) {
        final ReservaPeriodica existingReservaPeriodica = reservaPeriodicaRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        existingReservaPeriodica.setBedel(null);
        existingReservaPeriodica.setDiasReservados(null);
        existingReservaPeriodica.setPeriodo(null);
        reservaPeriodicaRepository.save(existingReservaPeriodica);

        reservaPeriodicaRepository.deleteById(id);
    }

}
