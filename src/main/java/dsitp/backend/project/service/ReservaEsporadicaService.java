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
import dsitp.backend.project.model.ReservaEsporadicaDTO;
import dsitp.backend.project.model.ReservaRetornoDTO;
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
    public ReservaEsporadicaService(final ReservaEsporadicaRepository reservaEsporadicaRepository,
            final DiaReservadoRepository diaReservadoRepository, final AulaRepository aulaRepository,
            final ReservaEsporadicaMapper reservaEsporadicaMapper, final DiaReservadoMapper diaReservadoMapper,
            final AulaMapper aulaMapper) {
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
                .map(reservaEsporadicaMapper::toReservaEsporadicaDTO)
                .toList();
    }

    public ReservaEsporadicaDTO get(final Integer id) {
        return reservaEsporadicaRepository.findById(id)
                .map(reservaEsporadicaMapper::toReservaEsporadicaDTO)
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ReservaEsporadicaDTO reservaEsporadicaDTO) {
        final ReservaEsporadica reservaEsporadica = reservaEsporadicaMapper
                .toReservaEsporadicaEntity(reservaEsporadicaDTO);

        return reservaEsporadicaRepository.save(reservaEsporadica).getId();
    }

    public void update(final Integer id, final ReservaEsporadicaDTO reservaEsporadicaDTO) {
        final ReservaEsporadica existingReservaEsporadica = reservaEsporadicaRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        ReservaEsporadica updatedReservaEsporadica = reservaEsporadicaMapper
                .toReservaEsporadicaEntity(reservaEsporadicaDTO);

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
