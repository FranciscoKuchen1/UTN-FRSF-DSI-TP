package dsitp.backend.project.mapper;

import dsitp.backend.project.domain.Aula;
import dsitp.backend.project.domain.AulaInformatica;
import dsitp.backend.project.domain.AulaMultimedio;
import dsitp.backend.project.domain.AulaSinRecursosAdic;
import dsitp.backend.project.model.AulaDTO;
import dsitp.backend.project.model.TipoAula;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class AulaMapper {

    public AulaDTO toAulaDTO(Aula aula) {
        if (aula == null) {
            return null;
        }

        AulaDTO aulaDTO = new AulaDTO();
        aulaDTO.setNumero(aula.getNumero());
        aulaDTO.setNombre(aula.getNombre());
        aulaDTO.setPiso(aula.getPiso());
        aulaDTO.setCapacidad(aula.getCapacidad());
        aulaDTO.setTipoPizarron(aula.getTipoPizarron());
        aulaDTO.setTieneAireAcondicionado(aula.getTieneAireAcondicionado());
        aulaDTO.setTipoAula(getTipoAula(aula));

        Map<String, Object> atributosEspecificos = new HashMap<>();
        switch (aula) {
            case AulaSinRecursosAdic sinRecursos ->
                atributosEspecificos.put("tieneVentiladores", sinRecursos.getTieneVentiladores());
            case AulaInformatica informatica -> {
                atributosEspecificos.put("cantidadPCs", informatica.getCantidadPCs());
                atributosEspecificos.put("tieneCanon", informatica.getTieneCanon());
            }
            case AulaMultimedio multimedio -> {
                atributosEspecificos.put("tieneTelevisor", multimedio.getTieneTelevisor());
                atributosEspecificos.put("tieneCanon", multimedio.getTieneCanon());
                atributosEspecificos.put("tieneComputadora", multimedio.getTieneComputadora());
                atributosEspecificos.put("tieneVentiladores", multimedio.getTieneVentiladores());
            }
            default -> {
            }
        }
        aulaDTO.setAtributosEspecificos(atributosEspecificos);

        return aulaDTO;
    }

    public Aula toAulaEntity(AulaDTO aulaDTO) {
        Aula aula;

        switch (aulaDTO.getTipoAula()) {
            case 0:
                AulaSinRecursosAdic aulaSinRecursos = new AulaSinRecursosAdic();
                aulaSinRecursos.setTieneVentiladores(
                        (Boolean) aulaDTO.getAtributosEspecificos().getOrDefault("tieneVentiladores", false)
                );
                aula = aulaSinRecursos;
                break;

            case 1:
                AulaInformatica aulaInformatica = new AulaInformatica();
                aulaInformatica.setCantidadPCs(
                        (Integer) aulaDTO.getAtributosEspecificos().getOrDefault("cantidadPCs", 0)
                );
                aulaInformatica.setTieneCanon(
                        (Boolean) aulaDTO.getAtributosEspecificos().getOrDefault("tieneCanon", false)
                );
                aula = aulaInformatica;
                break;

            case 2:
                AulaMultimedio aulaMultimedio = new AulaMultimedio();
                aulaMultimedio.setTieneTelevisor(
                        (Boolean) aulaDTO.getAtributosEspecificos().getOrDefault("tieneTelevisor", false)
                );
                aulaMultimedio.setTieneCanon(
                        (Boolean) aulaDTO.getAtributosEspecificos().getOrDefault("tieneCanon", false)
                );
                aulaMultimedio.setTieneComputadora(
                        (Boolean) aulaDTO.getAtributosEspecificos().getOrDefault("tieneComputadora", false)
                );
                aulaMultimedio.setTieneVentiladores(
                        (Boolean) aulaDTO.getAtributosEspecificos().getOrDefault("tieneVentiladores", false)
                );
                aula = aulaMultimedio;
                break;

            default:
                throw new IllegalArgumentException("Tipo de aula no reconocido: " + aulaDTO.getTipoAula());
        }

        aula.setNumero(aulaDTO.getNumero());
        aula.setNombre(aulaDTO.getNombre());
        aula.setPiso(aulaDTO.getPiso());
        aula.setCapacidad(aulaDTO.getCapacidad());
        aula.setTipoPizarron(aulaDTO.getTipoPizarron());
        aula.setTieneAireAcondicionado(aulaDTO.getTieneAireAcondicionado());

        return aula;
    }

    public Integer getTipoAula(Aula aula) {
        if (aula instanceof AulaSinRecursosAdic) {
            return 0;
        } else if (aula instanceof AulaInformatica) {
            return 1;
        } else if (aula instanceof AulaMultimedio) {
            return 2;
        }
        return null;
    }

    public TipoAula toTipoAula(Integer tipoAula) {

        switch (tipoAula) {
            case 0 -> {
                return TipoAula.SIN_RECURSOS_ADICIONALES;
            }
            case 1 -> {
                return TipoAula.INFORMATICA;
            }

            case 2 -> {
                return TipoAula.MULTIMEDIO;
            }
            default -> {
                return null;
            }
        }

    }
}
