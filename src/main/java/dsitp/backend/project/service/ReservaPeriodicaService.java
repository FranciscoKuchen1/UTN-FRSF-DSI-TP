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
import dsitp.backend.project.model.ReservaPeriodicaDTO;
import dsitp.backend.project.model.ReservaPeriodicaSinDiasDTO;
import dsitp.backend.project.model.ReservaRetornoDTO;
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
    public ReservaPeriodicaService(final ReservaPeriodicaRepository reservaPeriodicaRepository,
            final AulaRepository aulaRepository, final DiaReservadoRepository diaReservadoRepository,
            final ReservaPeriodicaMapper reservaPeriodicaMapper, final DiaReservadoMapper diaReservadoMapper,
            final AulaMapper aulaMapper) {
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

    // public ReservaRetornoDTO getDisponibilidadAulaReservaPeriodica(
    // final ReservaPeriodicaSinDiasDTO reservaPeriodicaSinDiasDTO) {
    // final ReservaPeriodica reservaPeriodica = reservaPeriodicaMapper
    // .toReservaPeriodicaEntityDisponibilidad(reservaPeriodicaSinDiasDTO);
    // ReservaRetornoDTO reservaRetornoDTO = new ReservaRetornoDTO();
    // reservaRetornoDTO.setDiasDisponibles(new ArrayList<>());
    // reservaRetornoDTO.setDiasConSolapamiento(new ArrayList<>());
    // List<Aula> aulas =
    // aulaRepository.findByTipoAulaAndCapacidad(reservaPeriodica.getTipoAula().toInteger(),
    // reservaPeriodica.getCantAlumnos());
    // if (!aulas.isEmpty()) {
    // for (DiaReservado diaReservado : reservaPeriodica.getDiasReservados()) {
    // List<AulaDTO> aulasDisponibles = obtenerDisponibilidad(aulas, diaReservado);
    // if (!aulasDisponibles.isEmpty()) {
    // DiaDisponibilidadDTO diaDisponibilidadDTO = new DiaDisponibilidadDTO();
    // diaDisponibilidadDTO.setDiaReservado(diaReservadoMapper.toDiaReservadoDTO(diaReservado));
    // diaDisponibilidadDTO.setAulasDisponibles(aulasDisponibles);
    // reservaRetornoDTO.getDiasDisponibles().add(diaDisponibilidadDTO);
    // } else {
    // DiaSolapamientoDTO diaSolapamientoDTO = new DiaSolapamientoDTO();
    // diaSolapamientoDTO.setDiaReservado(diaReservadoMapper.toDiaReservadoDTO(diaReservado));
    // diaSolapamientoDTO.setAulasConSolapamiento(obtenerAulasConMenorSuperposicion(aulas,
    // diaReservado));
    // reservaRetornoDTO.getDiasConSolapamiento().add(diaSolapamientoDTO);
    // }
    // }
    // }

    // return reservaRetornoDTO;
    // }

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
