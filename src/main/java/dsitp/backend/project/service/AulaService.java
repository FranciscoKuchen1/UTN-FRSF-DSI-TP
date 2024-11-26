package dsitp.backend.project.service;

import dsitp.backend.project.domain.Aula;
import dsitp.backend.project.mapper.AulaMapper;
import dsitp.backend.project.model.AulaDTO;
import dsitp.backend.project.repos.AulaRepository;
import dsitp.backend.project.util.NotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AulaService {

    private final AulaRepository aulaRepository;
    private final AulaMapper aulaMapper;

    @Autowired
    public AulaService(final AulaRepository aulaRepository, final AulaMapper aulaMapper) {
        this.aulaRepository = aulaRepository;
        this.aulaMapper = aulaMapper;
    }

    @Transactional(readOnly = true)
    public List<AulaDTO> findAll() {
        final List<Aula> aulas = aulaRepository.findAll(Sort.by("id"));
        return aulas
                .stream()
                .map(aulaMapper::toAulaDTO)
                .toList();
    }

    public List<AulaDTO> findAulas(Integer numero, Integer tipoAula, Integer capacidad) {
        if (numero != null && tipoAula != null && capacidad != null) {
            return aulaRepository.findByNumeroAndTipoAulaAndCapacidad(numero, aulaMapper.toTipoAula(tipoAula), capacidad).stream()
                    .map(aula -> aulaMapper.toAulaDTO(aula))
                    .toList();
        } else if (numero != null) {
            return aulaRepository.findById(numero).stream()
                    .map(aula -> aulaMapper.toAulaDTO(aula))
                    .toList();
        } else if (tipoAula != null) {
            return aulaRepository.findByTipoAula(tipoAula).stream()
                    .map(aula -> aulaMapper.toAulaDTO(aula))
                    .toList();
        } else if (capacidad != null) {
            return aulaRepository.findByCapacidad(capacidad).stream()
                    .map(aula -> aulaMapper.toAulaDTO(aula))
                    .toList();
        } else {
            return aulaRepository.findAll().stream()
                    .map(aula -> aulaMapper.toAulaDTO(aula))
                    .toList();
        }
    }

    @Transactional(readOnly = true)
    public AulaDTO get(final Integer numero) {
        return aulaRepository.findById(numero)
                .map(aula -> aulaMapper.toAulaDTO(aula))
                .orElseThrow(NotFoundException::new);

    }

    public Integer create(final AulaDTO aulaDTO) {
        final Aula aula = aulaMapper.toAulaEntity(aulaDTO);
        return aulaRepository.save(aula).getNumero();

    }

    public void update(final Integer numero, final AulaDTO aulaDTO) {
        Aula existingAula = aulaRepository.findById(numero)
                .orElseThrow(() -> new IllegalArgumentException("Aula no encontrada con el número: " + numero));
        Aula updatedAula = aulaMapper.toAulaEntity(aulaDTO);
        updatedAula.setNumero(existingAula.getNumero()); // Preserva el número original

        aulaRepository.save(updatedAula);
    }

    public void delete(final Integer numero) {
        if (!aulaRepository.existsById(numero)) {
            throw new IllegalArgumentException("Aula no encontrada con el número: " + numero);
        }
        aulaRepository.deleteById(numero);
    }

}
