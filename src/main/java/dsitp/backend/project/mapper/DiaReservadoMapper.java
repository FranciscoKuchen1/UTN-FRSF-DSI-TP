package dsitp.backend.project.mapper;

import dsitp.backend.project.domain.DiaReservado;
import dsitp.backend.project.model.DiaReservadoDTO;
import org.springframework.stereotype.Service;

@Service
public class DiaReservadoMapper {

    public DiaReservadoMapper() {
    }

    public DiaReservadoDTO toDiaReservadoDTO(DiaReservado diaReservado) {
        if (diaReservado == null) {
            return null;
        }

        DiaReservadoDTO diaReservadoDTO = new DiaReservadoDTO();
        diaReservadoDTO.setFechaReserva(diaReservado.getFechaReserva());
        diaReservadoDTO.setDuracion(diaReservado.getDuracion());
        diaReservadoDTO.setHoraInicio(diaReservado.getHoraInicio());

        return diaReservadoDTO;
    }

    public DiaReservado toDiaReservadoEntity(DiaReservadoDTO diaReservadoDTO) {

        DiaReservado diaReservado = new DiaReservado();
        diaReservado.setFechaReserva(diaReservadoDTO.getFechaReserva());
        diaReservado.setDuracion(diaReservadoDTO.getDuracion());
        diaReservado.setHoraInicio(diaReservadoDTO.getHoraInicio());

        return diaReservado;
    }
}
