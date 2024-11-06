package dsitp.backend.project.service;

import dsitp.backend.project.domain.DiaReservado;
import dsitp.backend.project.model.DiaReservadoDTO;
import dsitp.backend.project.repos.DiaReservadoRepository;
import dsitp.backend.project.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class DiaReservadoService {

    private final DiaReservadoRepository diaReservadoRepository;

    public DiaReservadoService(final DiaReservadoRepository diaReservadoRepository) {
        this.diaReservadoRepository = diaReservadoRepository;
    }

    public List<DiaReservadoDTO> findAll() {
        final List<DiaReservado> diaReservadoes = diaReservadoRepository.findAll(Sort.by("id"));
        return diaReservadoes.stream()
                .map(diaReservado -> mapToDTO(diaReservado, new DiaReservadoDTO()))
                .toList();
    }

    public DiaReservadoDTO get(final Long id) {
        return diaReservadoRepository.findById(id)
                .map(diaReservado -> mapToDTO(diaReservado, new DiaReservadoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final DiaReservadoDTO diaReservadoDTO) {
        final DiaReservado diaReservado = new DiaReservado();
        mapToEntity(diaReservadoDTO, diaReservado);
        return diaReservadoRepository.save(diaReservado).getId();
    }

    public void update(final Long id, final DiaReservadoDTO diaReservadoDTO) {
        final DiaReservado diaReservado = diaReservadoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(diaReservadoDTO, diaReservado);
        diaReservadoRepository.save(diaReservado);
    }

    public void delete(final Long id) {
        diaReservadoRepository.deleteById(id);
    }

    private DiaReservadoDTO mapToDTO(final DiaReservado diaReservado,
            final DiaReservadoDTO diaReservadoDTO) {
        diaReservadoDTO.setId(diaReservado.getId());
        diaReservadoDTO.setFechaReserva(diaReservado.getFechaReserva());
        diaReservadoDTO.setDuracion(diaReservado.getDuracion());
        diaReservadoDTO.setHoraInicio(diaReservado.getHoraInicio());
        return diaReservadoDTO;
    }

    private DiaReservado mapToEntity(final DiaReservadoDTO diaReservadoDTO,
            final DiaReservado diaReservado) {
        diaReservado.setFechaReserva(diaReservadoDTO.getFechaReserva());
        diaReservado.setDuracion(diaReservadoDTO.getDuracion());
        diaReservado.setHoraInicio(diaReservadoDTO.getHoraInicio());
        return diaReservado;
    }

}
