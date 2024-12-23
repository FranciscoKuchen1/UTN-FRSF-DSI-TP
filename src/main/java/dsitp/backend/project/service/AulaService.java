package dsitp.backend.project.service;

import dsitp.backend.project.domain.Aula;
import dsitp.backend.project.model.AulaDTO;
import dsitp.backend.project.repos.AulaRepository;
import dsitp.backend.project.repos.ReservaEsporadicaRepository;
import dsitp.backend.project.repos.ReservaPeriodicaRepository;
import dsitp.backend.project.util.NotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import dsitp.backend.project.domain.AulaInformatica;
import dsitp.backend.project.domain.AulaMultimedio;
import dsitp.backend.project.domain.AulaSinRecursosAdic;
import dsitp.backend.project.domain.Reserva;
import dsitp.backend.project.domain.ReservaEsporadica;
import dsitp.backend.project.domain.ReservaPeriodica;
import dsitp.backend.project.model.AulaSolapadaDTO;
import dsitp.backend.project.model.ReservaSolapadaDTO;
import java.util.HashMap;
import dsitp.backend.project.model.TipoPizarron;

@Service
@Transactional
public class AulaService {

    private final AulaRepository aulaRepository;
    private final ReservaEsporadicaRepository reservaEsporadicaRepository;
    private final ReservaPeriodicaRepository reservaPeriodicaRepository;

    @Autowired
    public AulaService(final AulaRepository aulaRepository,
            ReservaEsporadicaRepository reservaEsporadicaRepository,
            ReservaPeriodicaRepository reservaPeriodicaRepository) {
        this.aulaRepository = aulaRepository;
        this.reservaEsporadicaRepository = reservaEsporadicaRepository;
        this.reservaPeriodicaRepository = reservaPeriodicaRepository;
    }

    @Transactional(readOnly = true)
    public List<AulaDTO> findAll() {
        final List<Aula> aulas = aulaRepository.findAll(Sort.by("id"));
        return aulas
                .stream()
                .map(aula -> toAulaDTO(aula))
                .toList();
    }

    public List<AulaDTO> findAulas(Integer numero, Integer tipoAula, Integer capacidad) {
        if (numero != null && tipoAula != null && capacidad != null) {
            return aulaRepository.findByNumeroAndTipoAulaAndCapacidad(numero, tipoAula, capacidad).stream()
                    .map(aula -> toAulaDTO(aula))
                    .toList();
        } else if (numero != null) {
            return aulaRepository.findById(numero).stream()
                    .map(aula -> toAulaDTO(aula))
                    .toList();
        } else if (tipoAula != null) {
            return aulaRepository.findByTipoAula(tipoAula).stream()
                    .map(aula -> toAulaDTO(aula))
                    .toList();
        } else if (capacidad != null) {
            return aulaRepository.findByCapacidad(capacidad).stream()
                    .map(aula -> toAulaDTO(aula))
                    .toList();
        } else {
            return aulaRepository.findAll().stream()
                    .map(aula -> toAulaDTO(aula))
                    .toList();
        }
    }

    @Transactional(readOnly = true)
    public AulaDTO get(final Integer numero) {
        return aulaRepository.findById(numero)
                .map(aula -> toAulaDTO(aula))
                .orElseThrow(NotFoundException::new);

    }

    public Integer create(final AulaDTO aulaDTO) {
        final Aula aula = toAulaEntity(aulaDTO);
        return aulaRepository.save(aula).getNumero();

    }

    public void update(final Integer numero, final AulaDTO aulaDTO) {
        Aula existingAula = aulaRepository.findById(numero)
                .orElseThrow(NotFoundException::new);
        Aula updatedAula = toAulaEntity(aulaDTO);
        updatedAula.setNumero(existingAula.getNumero());

        aulaRepository.save(updatedAula);
    }

    public void delete(final Integer numero) {
        if (!aulaRepository.existsById(numero)) {
            throw new IllegalArgumentException("Aula no encontrada con el número: " + numero);
        }
        aulaRepository.deleteById(numero);
    }

    public AulaDTO toAulaDTO(Aula aula) {
        if (aula == null) {
            return null;
        }

        AulaDTO aulaDTO = new AulaDTO();
        aulaDTO.setNumero(aula.getNumero());
        aulaDTO.setNombre(aula.getNombre());
        aulaDTO.setPiso(aula.getPiso());
        aulaDTO.setCapacidad(aula.getCapacidad());
        aulaDTO.setTipoPizarron(aula.getTipoPizarron().toInteger());
        aulaDTO.setTieneAireAcondicionado(aula.getTieneAireAcondicionado());
        aulaDTO.setAtributosEspecificos(new HashMap<>());

        switch (aula) {
            case AulaSinRecursosAdic sinRecursos -> {
                aulaDTO.setTipoAula(0);
                aulaDTO.getAtributosEspecificos().put("tieneVentiladores", sinRecursos.getTieneVentiladores());
            }
            case AulaInformatica informatica -> {
                aulaDTO.setTipoAula(1);
                aulaDTO.getAtributosEspecificos().put("cantidadPCs", informatica.getCantidadPCs());
                aulaDTO.getAtributosEspecificos().put("tieneCanon", informatica.getTieneCanon());
            }
            case AulaMultimedio multimedio -> {
                aulaDTO.setTipoAula(2);
                aulaDTO.getAtributosEspecificos().put("tieneTelevisor", multimedio.getTieneTelevisor());
                aulaDTO.getAtributosEspecificos().put("tieneCanon", multimedio.getTieneCanon());
                aulaDTO.getAtributosEspecificos().put("tieneComputadora", multimedio.getTieneComputadora());
                aulaDTO.getAtributosEspecificos().put("tieneVentiladores", multimedio.getTieneVentiladores());
            }
            default -> {
            }
        }

        return aulaDTO;
    }

    public Aula toAulaEntity(AulaDTO aulaDTO) {
        if (aulaDTO == null) {
            return null;
        }

        Aula aula;

        switch (aulaDTO.getTipoAula()) {
            case 0 -> {
                AulaSinRecursosAdic aulaSinRecursos = new AulaSinRecursosAdic();
                aulaSinRecursos.setTieneVentiladores(
                        (Boolean) aulaDTO.getAtributosEspecificos().getOrDefault("tieneVentiladores", false));
                aula = aulaSinRecursos;
            }
            case 1 -> {
                AulaInformatica aulaInformatica = new AulaInformatica();
                aulaInformatica
                        .setCantidadPCs((Integer) aulaDTO.getAtributosEspecificos().getOrDefault("cantidadPCs", 0));
                aulaInformatica
                        .setTieneCanon((Boolean) aulaDTO.getAtributosEspecificos().getOrDefault("tieneCanon", false));
                aula = aulaInformatica;
            }
            case 2 -> {
                AulaMultimedio aulaMultimedio = new AulaMultimedio();
                aulaMultimedio.setTieneTelevisor(
                        (Boolean) aulaDTO.getAtributosEspecificos().getOrDefault("tieneTelevisor", false));
                aulaMultimedio
                        .setTieneCanon((Boolean) aulaDTO.getAtributosEspecificos().getOrDefault("tieneCanon", false));
                aulaMultimedio.setTieneComputadora(
                        (Boolean) aulaDTO.getAtributosEspecificos().getOrDefault("tieneComputadora", false));
                aulaMultimedio.setTieneVentiladores(
                        (Boolean) aulaDTO.getAtributosEspecificos().getOrDefault("tieneVentiladores", false));
                aula = aulaMultimedio;
            }
            default ->
                throw new IllegalArgumentException("Tipo de aula no reconocido: " + aulaDTO.getTipoAula());
        }

        aula.setNumero(aulaDTO.getNumero());
        aula.setNombre(aulaDTO.getNombre());
        aula.setPiso(aulaDTO.getPiso());
        aula.setCapacidad(aulaDTO.getCapacidad());
        aula.setTipoPizarron(TipoPizarron.fromInteger(aulaDTO.getTipoPizarron()));
        aula.setTieneAireAcondicionado(aulaDTO.getTieneAireAcondicionado());

        return aula;
    }

}