package dsitp.backend.project.service;

import dsitp.backend.project.domain.Bedel;
import dsitp.backend.project.domain.Periodo;
import dsitp.backend.project.domain.ReservaPeriodica;
import dsitp.backend.project.model.ReservaPeriodicaDTO;
import dsitp.backend.project.repos.BedelRepository;
import dsitp.backend.project.repos.BedelRepositoryListCrud;
import dsitp.backend.project.repos.PeriodoRepository;
import dsitp.backend.project.repos.ReservaPeriodicaRepository;
import dsitp.backend.project.util.NotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ReservaPeriodicaService {

    private final ReservaPeriodicaRepository reservaPeriodicaRepository;
    private final PeriodoRepository periodoRepository;
    private final BedelRepository bedelRepository;
    private final BedelRepositoryListCrud bedelRepositoryListCrud;

    public ReservaPeriodicaService(final ReservaPeriodicaRepository reservaPeriodicaRepository, final PeriodoRepository periodoRepository, final BedelRepository bedelRepository, dsitp.backend.project.repos.BedelRepositoryListCrud bedelRepositoryListCrud) {
        this.reservaPeriodicaRepository = reservaPeriodicaRepository;
        this.periodoRepository = periodoRepository;
        this.bedelRepository = bedelRepository;
        this.bedelRepositoryListCrud = bedelRepositoryListCrud;
    }

    public List<ReservaPeriodicaDTO> findAll() {
        final List<ReservaPeriodica> reservaPeriodicas = reservaPeriodicaRepository.findAll(Sort.by("id"));
        return reservaPeriodicas.stream()
                .map(reservaPeriodica -> mapToDTO(reservaPeriodica, new ReservaPeriodicaDTO()))
                .toList();
    }

    public ReservaPeriodicaDTO get(final Integer id) {
        return reservaPeriodicaRepository.findById(id)
                .map(reservaPeriodica -> mapToDTO(reservaPeriodica, new ReservaPeriodicaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ReservaPeriodicaDTO reservaPeriodicaDTO) {
        final ReservaPeriodica reservaPeriodica = new ReservaPeriodica();
        mapToEntity(reservaPeriodicaDTO, reservaPeriodica);
        return reservaPeriodicaRepository.save(reservaPeriodica).getId();
    }

    public void update(final Integer id, final ReservaPeriodicaDTO reservaPeriodicaDTO) {
        final ReservaPeriodica reservaPeriodica = reservaPeriodicaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(reservaPeriodicaDTO, reservaPeriodica);
        reservaPeriodicaRepository.save(reservaPeriodica);
    }

    public void delete(final Integer id) {
        reservaPeriodicaRepository.deleteById(id);
    }

    private ReservaPeriodicaDTO mapToDTO(final ReservaPeriodica reservaPeriodica,
            final ReservaPeriodicaDTO reservaPeriodicaDTO) {
        reservaPeriodicaDTO.setId(reservaPeriodica.getId());
        reservaPeriodicaDTO.setIdCatedra(reservaPeriodica.getIdCatedra());
        reservaPeriodicaDTO.setNombreCatedra(reservaPeriodica.getNombreCatedra());
        reservaPeriodicaDTO.setIdDocente(reservaPeriodica.getIdDocente());
        reservaPeriodicaDTO.setNombreDocente(reservaPeriodica.getNombreDocente());
        reservaPeriodicaDTO.setApellidoDocente(reservaPeriodica.getApellidoDocente());
        reservaPeriodicaDTO.setCorreoDocente(reservaPeriodica.getCorreoDocente());
        reservaPeriodicaDTO.setCantAlumnos(reservaPeriodica.getCantAlumnos());
        reservaPeriodicaDTO.setIdPeriodo(reservaPeriodica.getPeriodo() == null ? null : reservaPeriodica.getPeriodo().getId());

        return reservaPeriodicaDTO;
    }

    private ReservaPeriodica mapToEntity(final ReservaPeriodicaDTO reservaPeriodicaDTO,
            final ReservaPeriodica reservaPeriodica) {
        reservaPeriodica.setIdCatedra(reservaPeriodicaDTO.getIdCatedra());
        reservaPeriodica.setNombreCatedra(reservaPeriodicaDTO.getNombreCatedra());
        reservaPeriodica.setIdDocente(reservaPeriodicaDTO.getIdDocente());
        reservaPeriodica.setNombreDocente(reservaPeriodicaDTO.getNombreDocente());
        reservaPeriodica.setApellidoDocente(reservaPeriodicaDTO.getApellidoDocente());
        reservaPeriodica.setCorreoDocente(reservaPeriodicaDTO.getCorreoDocente());
        reservaPeriodica.setCantAlumnos(reservaPeriodicaDTO.getCantAlumnos());
        final Periodo periodo = reservaPeriodicaDTO.getIdPeriodo() == null ? null : periodoRepository.findById(reservaPeriodicaDTO.getIdPeriodo())
                .orElseThrow(() -> new NotFoundException("periodo not found"));
        reservaPeriodica.setPeriodo(periodo);
        Optional<Bedel> optionalBedel = bedelRepositoryListCrud.findById(reservaPeriodicaDTO.getIdBedel());
        if (optionalBedel.isPresent()) {
            optionalBedel.get().getReservas().add(reservaPeriodica);
        }

        return reservaPeriodica;
    }

}
