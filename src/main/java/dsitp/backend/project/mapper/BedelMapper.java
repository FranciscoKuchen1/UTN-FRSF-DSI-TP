package dsitp.backend.project.mapper;

import dsitp.backend.project.domain.Bedel;
import dsitp.backend.project.model.BedelDTO;
import dsitp.backend.project.model.TipoTurno;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class BedelMapper {

    private final ModelMapper modelMapper;

    public BedelMapper() {
        this.modelMapper = new ModelMapper();
    }

    public BedelDTO toBedelDTO(Bedel bedel) {
        BedelDTO bedelDTO = modelMapper.map(bedel, BedelDTO.class);
        bedelDTO.setTipoTurno(bedel.getTipoTurno() != null ? bedel.getTipoTurno().ordinal() : null);
        return bedelDTO;
    }

    public Bedel toBedelEntity(BedelDTO bedelDTO) {
        Bedel bedel = modelMapper.map(bedelDTO, Bedel.class);
        bedel.setTipoTurno(TipoTurno.values()[bedelDTO.getTipoTurno()]);
        return bedel;
    }

    public TipoTurno toTipoTurno(Integer tipoTurno) {

        switch (tipoTurno) {
            case 0 -> {
                return TipoTurno.MANANA;
            }
            case 1 -> {
                return TipoTurno.TARDE;
            }

            case 2 -> {
                return TipoTurno.NOCHE;
            }
            default -> {
                return null;
            }
        }

    }

    public Integer toInteger(TipoTurno tipoTurno) {
        switch (tipoTurno) {
            case TipoTurno.MANANA -> {
                return 0;
            }
            case TipoTurno.TARDE -> {
                return 1;
            }

            case TipoTurno.NOCHE -> {
                return 2;
            }
            default -> {
                return null;
            }
        }

    }

}
