package dsitp.backend.project.service;

import dsitp.backend.project.domain.ReservaEsporadica;
import dsitp.backend.project.model.ReservaEsporadicaDTO;
import dsitp.backend.project.repos.BedelRepository;
import dsitp.backend.project.repos.DiaReservadoRepository;
import dsitp.backend.project.repos.PeriodoRepository;
import dsitp.backend.project.repos.ReservaEsporadicaRepository;
import dsitp.backend.project.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ReservaEsporadicaService {

    private final ReservaEsporadicaRepository reservaEsporadicaRepository;
    private final PeriodoRepository periodoRepository;
    private final DiaReservadoRepository diaReservadoRepository;
    private final BedelRepository bedelRepository;

    public ReservaEsporadicaService(final ReservaEsporadicaRepository reservaEsporadicaRepository, final DiaReservadoRepository diaReservadoRepository,
            final PeriodoRepository periodoRepository, final BedelRepository bedelRepository) {
        this.reservaEsporadicaRepository = reservaEsporadicaRepository;
        this.periodoRepository = periodoRepository;
        this.diaReservadoRepository = diaReservadoRepository;
        this.bedelRepository = bedelRepository;
    }

    public List<ReservaEsporadicaDTO> findAll() {
        final List<ReservaEsporadica> reservaEsporadicas = reservaEsporadicaRepository.findAll(Sort.by("id"));
        return reservaEsporadicas.stream()
                .map(reservaEsporadica -> mapToDTO(reservaEsporadica, new ReservaEsporadicaDTO()))
                .toList();
    }

    public ReservaEsporadicaDTO get(final Integer id) {
        return reservaEsporadicaRepository.findById(id)
                .map(reservaEsporadica -> mapToDTO(reservaEsporadica, new ReservaEsporadicaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ReservaEsporadicaDTO reservaEsporadicaDTO) {
        final ReservaEsporadica reservaEsporadica = new ReservaEsporadica();
        mapToEntity(reservaEsporadicaDTO, reservaEsporadica);
        return reservaEsporadicaRepository.save(reservaEsporadica).getId();
    }

    public void update(final Integer id, final ReservaEsporadicaDTO reservaEsporadicaDTO) {
        final ReservaEsporadica reservaEsporadica = reservaEsporadicaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(reservaEsporadicaDTO, reservaEsporadica);
        reservaEsporadicaRepository.save(reservaEsporadica);
    }

    public void delete(final Integer id) {
        reservaEsporadicaRepository.deleteById(id);
    }

    private ReservaEsporadicaDTO mapToDTO(final ReservaEsporadica reservaEsporadica,
            final ReservaEsporadicaDTO reservaEsporadicaDTO) {
        reservaEsporadicaDTO.setId(reservaEsporadica.getId());
        reservaEsporadicaDTO.setIdCatedra(reservaEsporadica.getIdCatedra());
        reservaEsporadicaDTO.setNombreCatedra(reservaEsporadica.getNombreCatedra());
        reservaEsporadicaDTO.setIdDocente(reservaEsporadica.getIdDocente());
        reservaEsporadicaDTO.setNombreDocente(reservaEsporadica.getNombreDocente());
        reservaEsporadicaDTO.setApellidoDocente(reservaEsporadica.getApellidoDocente());
        reservaEsporadicaDTO.setCorreoDocente(reservaEsporadica.getCorreoDocente());
        reservaEsporadicaDTO.setCantAlumnos(reservaEsporadica.getCantAlumnos());
        reservaEsporadicaDTO.setTipoAula(reservaEsporadica.getTipoAula());
        //reservaEsporadicaDTO.setDiasReservadosDTO(reservaEsporadica.getDiasReservados());
        return reservaEsporadicaDTO;
    }

    private ReservaEsporadica mapToEntity(final ReservaEsporadicaDTO reservaEsporadicaDTO,
            final ReservaEsporadica reservaEsporadica) {
        reservaEsporadica.setIdCatedra(reservaEsporadicaDTO.getIdCatedra());
        reservaEsporadica.setNombreCatedra(reservaEsporadicaDTO.getNombreCatedra());
        reservaEsporadica.setIdDocente(reservaEsporadicaDTO.getIdDocente());
        reservaEsporadica.setNombreDocente(reservaEsporadicaDTO.getNombreDocente());
        reservaEsporadica.setApellidoDocente(reservaEsporadicaDTO.getApellidoDocente());
        reservaEsporadica.setCorreoDocente(reservaEsporadicaDTO.getCorreoDocente());
        reservaEsporadica.setCantAlumnos(reservaEsporadicaDTO.getCantAlumnos());
        reservaEsporadica.setTipoAula(reservaEsporadicaDTO.getTipoAula());
        //reservaEsporadica.setDiasReservados(diaReservadoRepository reservaEsporadicaDTO.getDiasReservadosDTO());
        return reservaEsporadica;
    }

}
