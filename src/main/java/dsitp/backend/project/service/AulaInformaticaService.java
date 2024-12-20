package dsitp.backend.project.service;

import dsitp.backend.project.domain.AulaInformatica;
import dsitp.backend.project.model.AulaInformaticaDTO;
import dsitp.backend.project.model.TipoPizarron;
import dsitp.backend.project.repos.AulaInformaticaRepository;
import dsitp.backend.project.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AulaInformaticaService {

    private final AulaInformaticaRepository aulaInformaticaRepository;

    public AulaInformaticaService(final AulaInformaticaRepository aulaInformaticaRepository) {
        this.aulaInformaticaRepository = aulaInformaticaRepository;
    }

    public List<AulaInformaticaDTO> findAll() {
        final List<AulaInformatica> aulaInformaticas = aulaInformaticaRepository.findAll(Sort.by("numero"));
        return aulaInformaticas.stream()
                .map(aulaInformatica -> mapToDTO(aulaInformatica, new AulaInformaticaDTO()))
                .toList();
    }

    public AulaInformaticaDTO get(final Integer numero) {
        return aulaInformaticaRepository.findById(numero)
                .map(aulaInformatica -> mapToDTO(aulaInformatica, new AulaInformaticaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final AulaInformaticaDTO aulaInformaticaDTO) {
        final AulaInformatica aulaInformatica = new AulaInformatica();
        mapToEntity(aulaInformaticaDTO, aulaInformatica);
        return aulaInformaticaRepository.save(aulaInformatica).getNumero();
    }

    public void update(final Integer numero, final AulaInformaticaDTO aulaInformaticaDTO) {
        final AulaInformatica aulaInformatica = aulaInformaticaRepository.findById(numero)
                .orElseThrow(NotFoundException::new);
        mapToEntity(aulaInformaticaDTO, aulaInformatica);
        aulaInformaticaRepository.save(aulaInformatica);
    }

    public void delete(final Integer numero) {
        aulaInformaticaRepository.deleteById(numero);
    }

    private AulaInformaticaDTO mapToDTO(final AulaInformatica aulaInformatica,
            final AulaInformaticaDTO aulaInformaticaDTO) {
        aulaInformaticaDTO.setNumero(aulaInformatica.getNumero());
        aulaInformaticaDTO.setNombre(aulaInformatica.getNombre());
        aulaInformaticaDTO.setPiso(aulaInformatica.getPiso());
        aulaInformaticaDTO.setCapacidad(aulaInformatica.getCapacidad());
        aulaInformaticaDTO.setTipoPizarron(aulaInformatica.getTipoPizarron().toInteger());
        aulaInformaticaDTO.setTieneAireAcondicionado(aulaInformatica.getTieneAireAcondicionado());
        aulaInformaticaDTO.setCantidadPCs(aulaInformatica.getCantidadPCs());
        aulaInformaticaDTO.setTieneCanon(aulaInformatica.getTieneCanon());
        return aulaInformaticaDTO;
    }

    private AulaInformatica mapToEntity(final AulaInformaticaDTO aulaInformaticaDTO,
            final AulaInformatica aulaInformatica) {
        aulaInformatica.setNombre(aulaInformaticaDTO.getNombre());
        aulaInformatica.setPiso(aulaInformaticaDTO.getPiso());
        aulaInformatica.setCapacidad(aulaInformaticaDTO.getCapacidad());
        aulaInformatica.setTipoPizarron(TipoPizarron.fromInteger(aulaInformaticaDTO.getTipoPizarron()));
        aulaInformatica.setTieneAireAcondicionado(aulaInformaticaDTO.getTieneAireAcondicionado());
        aulaInformatica.setCantidadPCs(aulaInformaticaDTO.getCantidadPCs());
        aulaInformatica.setTieneCanon(aulaInformaticaDTO.getTieneCanon());
        return aulaInformatica;
    }

}
