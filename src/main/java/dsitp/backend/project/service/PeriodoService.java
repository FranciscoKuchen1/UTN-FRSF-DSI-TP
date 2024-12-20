package dsitp.backend.project.service;

import dsitp.backend.project.domain.Periodo;
import dsitp.backend.project.domain.ReservaPeriodica;
import dsitp.backend.project.model.PeriodoDTO;
import dsitp.backend.project.model.TipoPeriodo;
import dsitp.backend.project.repos.PeriodoRepository;
import dsitp.backend.project.repos.ReservaEsporadicaRepository;
import dsitp.backend.project.repos.ReservaPeriodicaRepository;
import dsitp.backend.project.util.NotFoundException;
import dsitp.backend.project.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PeriodoService {

    private final PeriodoRepository periodoRepository;
    private final ReservaEsporadicaRepository reservaEsporadicaRepository;
    private final ReservaPeriodicaRepository reservaPeriodicaRepository;

    public PeriodoService(final PeriodoRepository periodoRepository,
            final ReservaEsporadicaRepository reservaEsporadicaRepository,
            final ReservaPeriodicaRepository reservaPeriodicaRepository) {
        this.periodoRepository = periodoRepository;
        this.reservaEsporadicaRepository = reservaEsporadicaRepository;
        this.reservaPeriodicaRepository = reservaPeriodicaRepository;
    }

    public List<PeriodoDTO> findAll() {
        final List<Periodo> periodos = periodoRepository.findAll(Sort.by("id"));
        return periodos.stream()
                .map(periodo -> mapToDTO(periodo, new PeriodoDTO()))
                .toList();
    }

    public PeriodoDTO get(final Integer id) {
        return periodoRepository.findById(id)
                .map(periodo -> mapToDTO(periodo, new PeriodoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final PeriodoDTO periodoDTO) {
        final Periodo periodo = new Periodo();
        mapToEntity(periodoDTO, periodo);
        return periodoRepository.save(periodo).getId();
    }

    public void update(final Integer id, final PeriodoDTO periodoDTO) {
        final Periodo periodo = periodoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(periodoDTO, periodo);
        periodoRepository.save(periodo);
    }

    public void delete(final Integer id) {
        periodoRepository.deleteById(id);
    }

    private PeriodoDTO mapToDTO(final Periodo periodo, final PeriodoDTO periodoDTO) {
        periodoDTO.setTipoPerido(periodo.getTipoPeriodo().toInteger());
        periodoDTO.setFechaInicio(periodo.getFechaInicio());
        periodoDTO.setFechaFin(periodo.getFechaFin());
        return periodoDTO;
    }

    private Periodo mapToEntity(final PeriodoDTO periodoDTO, final Periodo periodo) {
        periodo.setTipoPeriodo(TipoPeriodo.fromInteger(periodoDTO.getTipoPerido()));
        periodo.setFechaInicio(periodoDTO.getFechaInicio());
        periodo.setFechaFin(periodoDTO.getFechaFin());
        return periodo;
    }

    public ReferencedWarning getReferencedWarning(final Integer id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Periodo periodo = periodoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final ReservaPeriodica periodoReservaPeriodica = reservaPeriodicaRepository.findFirstByPeriodo(periodo);
        if (periodoReservaPeriodica != null) {
            referencedWarning.setKey("periodo.reserva.periodo.referenced");
            referencedWarning.addParam(periodoReservaPeriodica.getId());
            return referencedWarning;
        }
        return null;
    }

}
