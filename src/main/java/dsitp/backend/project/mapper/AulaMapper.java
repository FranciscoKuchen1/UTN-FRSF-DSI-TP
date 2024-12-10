package dsitp.backend.project.mapper;

import dsitp.backend.project.domain.Aula;
import dsitp.backend.project.domain.AulaInformatica;
import dsitp.backend.project.domain.AulaMultimedio;
import dsitp.backend.project.domain.AulaSinRecursosAdic;
import dsitp.backend.project.domain.Reserva;
import dsitp.backend.project.domain.ReservaEsporadica;
import dsitp.backend.project.domain.ReservaPeriodica;
import dsitp.backend.project.model.AulaDTO;
import dsitp.backend.project.model.AulaSolapadaDTO;
import dsitp.backend.project.model.ReservaSolapadaDTO;
import java.util.HashMap;

import dsitp.backend.project.model.TipoPizarron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AulaMapper {

    private final ReservaEsporadicaMapper reservaEsporadicaMapper;
    private final ReservaPeriodicaMapper reservaPeriodicaMapper;

    @Autowired
    public AulaMapper(ReservaEsporadicaMapper reservaEsporadicaMapper, ReservaPeriodicaMapper reservaPeriodicaMapper) {
        this.reservaEsporadicaMapper = reservaEsporadicaMapper;
        this.reservaPeriodicaMapper = reservaPeriodicaMapper;
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
                aulaSinRecursos.setTieneVentiladores((Boolean) aulaDTO.getAtributosEspecificos().getOrDefault("tieneVentiladores", false));
                aula = aulaSinRecursos;
            }
            case 1 -> {
                AulaInformatica aulaInformatica = new AulaInformatica();
                aulaInformatica.setCantidadPCs((Integer) aulaDTO.getAtributosEspecificos().getOrDefault("cantidadPCs", 0));
                aulaInformatica.setTieneCanon((Boolean) aulaDTO.getAtributosEspecificos().getOrDefault("tieneCanon", false));
                aula = aulaInformatica;
            }
            case 2 -> {
                AulaMultimedio aulaMultimedio = new AulaMultimedio();
                aulaMultimedio.setTieneTelevisor((Boolean) aulaDTO.getAtributosEspecificos().getOrDefault("tieneTelevisor", false));
                aulaMultimedio.setTieneCanon((Boolean) aulaDTO.getAtributosEspecificos().getOrDefault("tieneCanon", false));
                aulaMultimedio.setTieneComputadora((Boolean) aulaDTO.getAtributosEspecificos().getOrDefault("tieneComputadora", false));
                aulaMultimedio.setTieneVentiladores((Boolean) aulaDTO.getAtributosEspecificos().getOrDefault("tieneVentiladores", false));
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

    public AulaSolapadaDTO toAulaSolapadaDTO(Aula aula, Reserva reserva) {
        AulaSolapadaDTO aulaSolapadaDTO = new AulaSolapadaDTO();
        aulaSolapadaDTO.setAula(toAulaDTO(aula));

        ReservaSolapadaDTO reservaSolapadaDTO;
        switch (reserva) {
            case ReservaPeriodica reservaPeriodica ->
                reservaSolapadaDTO = reservaPeriodicaMapper.toReservaSolapadaDTO(reservaPeriodica);
            case ReservaEsporadica reservaEsporadica ->
                reservaSolapadaDTO = reservaEsporadicaMapper.toReservaSolapadaDTO(reservaEsporadica);
            default ->
                throw new IllegalArgumentException("Tipo de reserva desconocido");
        }

        aulaSolapadaDTO.setReservaSolapada(reservaSolapadaDTO);

        return aulaSolapadaDTO;

    }
}
