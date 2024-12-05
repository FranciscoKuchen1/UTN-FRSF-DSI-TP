package dsitp.backend.project.mapper;

import dsitp.backend.project.domain.Bedel;
import dsitp.backend.project.model.BedelDTO;
import dsitp.backend.project.model.TipoTurno;
import org.springframework.stereotype.Service;

@Service
public class BedelMapper {

    public BedelDTO toBedelDTO(Bedel bedel) {
        if (bedel == null) {
            return null;
        }

        BedelDTO bedelDTO = new BedelDTO();
        bedelDTO.setIdRegistro(bedel.getIdRegistro());
        bedelDTO.setNombre(bedel.getNombre());
        bedelDTO.setApellido(bedel.getApellido());
        bedelDTO.setContrasena(bedel.getContrasena());
        bedelDTO.setConfirmacionContrasena(bedel.getContrasena()); // Suponiendo que es la misma
        bedelDTO.setTipoTurno(bedel.getTipoTurno() != null ? bedel.getTipoTurno().ordinal() : null);

        return bedelDTO;
    }

    public Bedel toBedelEntity(BedelDTO bedelDTO) {
        if (bedelDTO == null) {
            return null;
        }

        Bedel bedel = new Bedel();
        bedel.setIdRegistro(bedelDTO.getIdRegistro());
        bedel.setNombre(bedelDTO.getNombre());
        bedel.setApellido(bedelDTO.getApellido());
        bedel.setContrasena(bedelDTO.getContrasena());
        bedel.setEliminado(false);
        bedel.setTipoTurno(bedelDTO.getTipoTurno() != null ? TipoTurno.values()[bedelDTO.getTipoTurno()] : null);

        return bedel;
    }
}
