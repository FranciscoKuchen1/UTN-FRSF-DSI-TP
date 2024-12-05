package dsitp.backend.project.mapper;

import dsitp.backend.project.domain.DiaReservado;
import dsitp.backend.project.model.DiaReservadoDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

@Service
public class DiaReservadoMapper {

    private final Validator diaReservadoValidator;

    public DiaReservadoMapper(Validator diaReservadoValidator) {

        this.diaReservadoValidator = diaReservadoValidator;
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
        BindingResult bindingResult = new BeanPropertyBindingResult(diaReservadoDTO, "diaReservadoDTO");
        diaReservadoValidator.validate(diaReservadoDTO, bindingResult);

        if (bindingResult.hasErrors()) {

            StringBuilder errorMessages = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> {
                String errorMessage = error.getDefaultMessage();
                if (errorMessage != null) {
                    errorMessages.append(errorMessage);
                }
            });

            throw new IllegalArgumentException(errorMessages.toString());
        }

        DiaReservado diaReservado = new DiaReservado();
        diaReservado.setFechaReserva(diaReservadoDTO.getFechaReserva());
        diaReservado.setDuracion(diaReservadoDTO.getDuracion());
        diaReservado.setHoraInicio(diaReservadoDTO.getHoraInicio());

        return diaReservado;
    }
}
