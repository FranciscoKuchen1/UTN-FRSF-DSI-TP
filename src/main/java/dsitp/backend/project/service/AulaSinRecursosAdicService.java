package dsitp.backend.project.service;

import dsitp.backend.project.domain.AulaSinRecursosAdic;
import dsitp.backend.project.model.AulaSinRecursosAdicDTO;
import dsitp.backend.project.repos.AulaSinRecursosAdicRepository;
import dsitp.backend.project.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AulaSinRecursosAdicService {

    private final AulaSinRecursosAdicRepository aulaSinRecursosAdicRepository;

    public AulaSinRecursosAdicService(
            final AulaSinRecursosAdicRepository aulaSinRecursosAdicRepository) {
        this.aulaSinRecursosAdicRepository = aulaSinRecursosAdicRepository;
    }

    public List<AulaSinRecursosAdicDTO> findAll() {
        final List<AulaSinRecursosAdic> aulaSinRecursosAdics = aulaSinRecursosAdicRepository.findAll(Sort.by("numero"));
        return aulaSinRecursosAdics.stream()
                .map(aulaSinRecursosAdic -> mapToDTO(aulaSinRecursosAdic, new AulaSinRecursosAdicDTO()))
                .toList();
    }

    public AulaSinRecursosAdicDTO get(final Integer numero) {
        return aulaSinRecursosAdicRepository.findById(numero)
                .map(aulaSinRecursosAdic -> mapToDTO(aulaSinRecursosAdic, new AulaSinRecursosAdicDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final AulaSinRecursosAdicDTO aulaSinRecursosAdicDTO) {
        final AulaSinRecursosAdic aulaSinRecursosAdic = new AulaSinRecursosAdic();
        mapToEntity(aulaSinRecursosAdicDTO, aulaSinRecursosAdic);
        return aulaSinRecursosAdicRepository.save(aulaSinRecursosAdic).getNumero();
    }

    public void update(final Integer numero, final AulaSinRecursosAdicDTO aulaSinRecursosAdicDTO) {
        final AulaSinRecursosAdic aulaSinRecursosAdic = aulaSinRecursosAdicRepository.findById(numero)
                .orElseThrow(NotFoundException::new);
        mapToEntity(aulaSinRecursosAdicDTO, aulaSinRecursosAdic);
        aulaSinRecursosAdicRepository.save(aulaSinRecursosAdic);
    }

    public void delete(final Integer numero) {
        aulaSinRecursosAdicRepository.deleteById(numero);
    }

    private AulaSinRecursosAdicDTO mapToDTO(final AulaSinRecursosAdic aulaSinRecursosAdic,
            final AulaSinRecursosAdicDTO aulaSinRecursosAdicDTO) {
        aulaSinRecursosAdicDTO.setNumero(aulaSinRecursosAdic.getNumero());
        aulaSinRecursosAdicDTO.setNombre(aulaSinRecursosAdic.getNombre());
        aulaSinRecursosAdicDTO.setPiso(aulaSinRecursosAdic.getPiso());
        aulaSinRecursosAdicDTO.setCapacidad(aulaSinRecursosAdic.getCapacidad());
        aulaSinRecursosAdicDTO.setTipoPizarron(aulaSinRecursosAdic.getTipoPizarron());
        aulaSinRecursosAdicDTO.setTieneAireAcondicionado(aulaSinRecursosAdic.getTieneAireAcondicionado());
        aulaSinRecursosAdicDTO.setTieneVentiladores(aulaSinRecursosAdic.getTieneVentiladores());
        return aulaSinRecursosAdicDTO;
    }

    private AulaSinRecursosAdic mapToEntity(final AulaSinRecursosAdicDTO aulaSinRecursosAdicDTO,
            final AulaSinRecursosAdic aulaSinRecursosAdic) {
        aulaSinRecursosAdic.setNombre(aulaSinRecursosAdicDTO.getNombre());
        aulaSinRecursosAdic.setPiso(aulaSinRecursosAdicDTO.getPiso());
        aulaSinRecursosAdic.setCapacidad(aulaSinRecursosAdicDTO.getCapacidad());
        aulaSinRecursosAdic.setTipoPizarron(aulaSinRecursosAdicDTO.getTipoPizarron());
        aulaSinRecursosAdic.setTieneAireAcondicionado(aulaSinRecursosAdicDTO.getTieneAireAcondicionado());
        aulaSinRecursosAdic.setTieneVentiladores(aulaSinRecursosAdicDTO.getTieneVentiladores());
        return aulaSinRecursosAdic;
    }

}
