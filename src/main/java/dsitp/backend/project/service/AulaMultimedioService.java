package dsitp.backend.project.service;

import dsitp.backend.project.domain.AulaMultimedio;
import dsitp.backend.project.model.AulaMultimedioDTO;
import dsitp.backend.project.model.TipoPizarron;
import dsitp.backend.project.repos.AulaMultimedioRepository;
import dsitp.backend.project.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AulaMultimedioService {

    private final AulaMultimedioRepository aulaMultimedioRepository;

    public AulaMultimedioService(final AulaMultimedioRepository aulaMultimedioRepository) {
        this.aulaMultimedioRepository = aulaMultimedioRepository;
    }

    public List<AulaMultimedioDTO> findAll() {
        final List<AulaMultimedio> aulaMultimedios = aulaMultimedioRepository.findAll(Sort.by("numero"));
        return aulaMultimedios.stream()
                .map(aulaMultimedio -> mapToDTO(aulaMultimedio, new AulaMultimedioDTO()))
                .toList();
    }

    public AulaMultimedioDTO get(final Integer numero) {
        return aulaMultimedioRepository.findById(numero)
                .map(aulaMultimedio -> mapToDTO(aulaMultimedio, new AulaMultimedioDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final AulaMultimedioDTO aulaMultimedioDTO) {
        final AulaMultimedio aulaMultimedio = new AulaMultimedio();
        mapToEntity(aulaMultimedioDTO, aulaMultimedio);
        return aulaMultimedioRepository.save(aulaMultimedio).getNumero();
    }

    public void update(final Integer numero, final AulaMultimedioDTO aulaMultimedioDTO) {
        final AulaMultimedio aulaMultimedio = aulaMultimedioRepository.findById(numero)
                .orElseThrow(NotFoundException::new);
        mapToEntity(aulaMultimedioDTO, aulaMultimedio);
        aulaMultimedioRepository.save(aulaMultimedio);
    }

    public void delete(final Integer numero) {
        aulaMultimedioRepository.deleteById(numero);
    }

    private AulaMultimedioDTO mapToDTO(final AulaMultimedio aulaMultimedio,
            final AulaMultimedioDTO aulaMultimedioDTO) {
        aulaMultimedioDTO.setNumero(aulaMultimedio.getNumero());
        aulaMultimedioDTO.setNombre(aulaMultimedio.getNombre());
        aulaMultimedioDTO.setPiso(aulaMultimedio.getPiso());
        aulaMultimedioDTO.setCapacidad(aulaMultimedio.getCapacidad());
        aulaMultimedioDTO.setTipoPizarron(aulaMultimedio.getTipoPizarron().toInteger());
        aulaMultimedioDTO.setTieneAireAcondicionado(aulaMultimedio.getTieneAireAcondicionado());
        aulaMultimedioDTO.setTieneTelevisor(aulaMultimedio.getTieneTelevisor());
        aulaMultimedioDTO.setTieneCanon(aulaMultimedio.getTieneCanon());
        aulaMultimedioDTO.setTieneComputadora(aulaMultimedio.getTieneComputadora());
        aulaMultimedioDTO.setTieneVentiladores(aulaMultimedio.getTieneVentiladores());
        return aulaMultimedioDTO;
    }

    private AulaMultimedio mapToEntity(final AulaMultimedioDTO aulaMultimedioDTO,
            final AulaMultimedio aulaMultimedio) {
        aulaMultimedio.setNombre(aulaMultimedioDTO.getNombre());
        aulaMultimedio.setPiso(aulaMultimedioDTO.getPiso());
        aulaMultimedio.setCapacidad(aulaMultimedioDTO.getCapacidad());
        aulaMultimedio.setTipoPizarron(TipoPizarron.fromInteger(aulaMultimedioDTO.getTipoPizarron()));
        aulaMultimedio.setTieneAireAcondicionado(aulaMultimedioDTO.getTieneAireAcondicionado());
        aulaMultimedio.setTieneTelevisor(aulaMultimedioDTO.getTieneTelevisor());
        aulaMultimedio.setTieneCanon(aulaMultimedioDTO.getTieneCanon());
        aulaMultimedio.setTieneComputadora(aulaMultimedioDTO.getTieneComputadora());
        aulaMultimedio.setTieneVentiladores(aulaMultimedioDTO.getTieneVentiladores());
        return aulaMultimedio;
    }

}
