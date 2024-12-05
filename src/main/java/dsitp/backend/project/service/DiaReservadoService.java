package dsitp.backend.project.service;

import dsitp.backend.project.domain.DiaReservado;
import dsitp.backend.project.mapper.DiaReservadoMapper;
import dsitp.backend.project.model.DiaReservadoDTO;
import dsitp.backend.project.repos.DiaReservadoRepository;
import dsitp.backend.project.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DiaReservadoService {

    private final DiaReservadoRepository diaReservadoRepository;
    private final DiaReservadoMapper diaReservadoMapper;

    public DiaReservadoService(final DiaReservadoRepository diaReservadoRepository, dsitp.backend.project.mapper.DiaReservadoMapper diaReservadoMapper) {
        this.diaReservadoRepository = diaReservadoRepository;
        this.diaReservadoMapper = diaReservadoMapper;
    }

    public List<DiaReservadoDTO> findAll() {
        final List<DiaReservado> diaReservadoes = diaReservadoRepository.findAll(Sort.by("id"));
        return diaReservadoes.stream()
                .map(diaReservado -> mapToDTO(diaReservado, new DiaReservadoDTO()))
                .toList();
    }

    public DiaReservadoDTO get(final Integer id) {
        return diaReservadoRepository.findById(id)
                .map(diaReservado -> mapToDTO(diaReservado, new DiaReservadoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final DiaReservadoDTO diaReservadoDTO) {
        final DiaReservado diaReservado = diaReservadoMapper.toDiaReservadoEntity(diaReservadoDTO);
        return diaReservadoRepository.save(diaReservado).getId();
    }

    public void update(final Integer id, final DiaReservadoDTO diaReservadoDTO) {
        DiaReservado existingDiaReservado = diaReservadoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        DiaReservado updatedDiaReservado = diaReservadoMapper.toDiaReservadoEntity(diaReservadoDTO);

        updatedDiaReservado.setId(existingDiaReservado.getId());
        diaReservadoRepository.save(updatedDiaReservado);

    }

    public void delete(final Integer id) {
        diaReservadoRepository.deleteById(id);
    }

    private DiaReservadoDTO mapToDTO(final DiaReservado diaReservado,
            final DiaReservadoDTO diaReservadoDTO) {
//        diaReservadoDTO.setId(diaReservado.getId());
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
