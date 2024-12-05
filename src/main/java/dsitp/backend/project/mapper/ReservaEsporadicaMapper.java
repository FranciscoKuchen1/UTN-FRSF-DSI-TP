package dsitp.backend.project.mapper;

import dsitp.backend.project.domain.Bedel;
import dsitp.backend.project.domain.DiaReservado;
import dsitp.backend.project.domain.ReservaEsporadica;
import dsitp.backend.project.model.DiaReservadoDTO;
import dsitp.backend.project.model.ReservaEsporadicaDTO;
import dsitp.backend.project.model.ReservaSolapadaDTO;
import dsitp.backend.project.model.TipoAula;
import dsitp.backend.project.repos.BedelRepository;
import dsitp.backend.project.util.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

@Service
public class ReservaEsporadicaMapper {

    private final BedelRepository bedelRepository;
    private final DiaReservadoMapper diaReservadoMapper;
    private final Validator reservaEsporadicaValidator;

    @Autowired
    public ReservaEsporadicaMapper(BedelRepository bedelRepository, DiaReservadoMapper diaReservadoMapper, Validator reservaEsporadicaValidator) {
        this.bedelRepository = bedelRepository;
        this.diaReservadoMapper = diaReservadoMapper;
        this.reservaEsporadicaValidator = reservaEsporadicaValidator;
    }

    public ReservaEsporadica toReservaEsporadicaEntity(ReservaEsporadicaDTO reservaEsporadicaDTO) {

        ReservaEsporadica reservaEsporadica = new ReservaEsporadica();
        reservaEsporadica.setIdCatedra(reservaEsporadicaDTO.getIdCatedra());
        reservaEsporadica.setNombreCatedra(reservaEsporadicaDTO.getNombreCatedra());
        reservaEsporadica.setIdDocente(reservaEsporadicaDTO.getIdDocente());
        reservaEsporadica.setNombreDocente(reservaEsporadicaDTO.getNombreDocente());
        reservaEsporadica.setApellidoDocente(reservaEsporadicaDTO.getApellidoDocente());
        reservaEsporadica.setCorreoDocente(reservaEsporadicaDTO.getCorreoDocente());
        reservaEsporadica.setCantAlumnos(reservaEsporadicaDTO.getCantAlumnos());
        reservaEsporadica.setTipoAula(TipoAula.fromInteger(reservaEsporadicaDTO.getTipoAula()));

        // TODO: Mapear los días reservados
        Bedel bedel = bedelRepository.findByIdRegistro(reservaEsporadicaDTO.getIdRegistroBedel())
                .orElseThrow(() -> new NotFoundException("Bedel no encontrado"));
        reservaEsporadica.setBedel(bedel);

        List<DiaReservado> diasReservados = new ArrayList<>();
        for (DiaReservadoDTO diaReservadoDTO : reservaEsporadicaDTO.getDiasReservadosDTO()) {
            diasReservados.add(diaReservadoMapper.toDiaReservadoEntity(diaReservadoDTO));
        }

        reservaEsporadica.setDiasReservados(diasReservados);

        BindingResult bindingResult = new BeanPropertyBindingResult(reservaEsporadica, "reservaEsporadica");
        reservaEsporadicaValidator.validate(reservaEsporadica, bindingResult);

        if (bindingResult.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> {
                String errorMessage = error.getDefaultMessage();
                if (errorMessage != null) {
                    errorMessages.append(errorMessage).append("\n");
                }
            });
            throw new IllegalArgumentException(errorMessages.toString());
        }

        return reservaEsporadica;
    }

    public ReservaEsporadicaDTO toReservaEsporadicaDTO(ReservaEsporadica reservaEsporadica) {
        if (reservaEsporadica == null) {
            return null;
        }

        ReservaEsporadicaDTO reservaEsporadicaDTO = new ReservaEsporadicaDTO();
        reservaEsporadicaDTO.setIdCatedra(reservaEsporadica.getIdCatedra());
        reservaEsporadicaDTO.setNombreCatedra(reservaEsporadica.getNombreCatedra());
        reservaEsporadicaDTO.setIdDocente(reservaEsporadica.getIdDocente());
        reservaEsporadicaDTO.setNombreDocente(reservaEsporadica.getNombreDocente());
        reservaEsporadicaDTO.setApellidoDocente(reservaEsporadica.getApellidoDocente());
        reservaEsporadicaDTO.setCorreoDocente(reservaEsporadica.getCorreoDocente());
        reservaEsporadicaDTO.setCantAlumnos(reservaEsporadica.getCantAlumnos());
        reservaEsporadicaDTO.setTipoAula(reservaEsporadica.getTipoAula().toInteger());
        reservaEsporadicaDTO.setIdRegistroBedel(reservaEsporadica.getBedel().getIdRegistro());

        // TODO: Mapear los días reservados
//        List<Trio<Integer, String, String>> diasSemanaHorasDuracion = reservaEsporadica.getDiasReservados().stream()
//                .map(diaReservado -> new Trio<>(
//                (int) diaReservado.getFechaReserva().toEpochDay(),
//                diaReservado.getHoraInicio().toString(),
//                diaReservado.getDuracion().toString()
//        ))
//                .collect(Collectors.toList());
//        reservaEsporadicaDTO.setDiasSemanaHorasDuracion(diasSemanaHorasDuracion);
        List<DiaReservadoDTO> diasReservadosDTO = new ArrayList<>();
        for (DiaReservado diaReservado : reservaEsporadica.getDiasReservados()) {
            diasReservadosDTO.add(diaReservadoMapper.toDiaReservadoDTO(diaReservado));
        }
        reservaEsporadicaDTO.setDiasReservadosDTO(diasReservadosDTO);

        return reservaEsporadicaDTO;
    }

    public ReservaSolapadaDTO toReservaSolapadaDTO(ReservaEsporadica reservaEsporadica) {
        ReservaSolapadaDTO reservaSolapadaDTO = new ReservaSolapadaDTO();
        reservaSolapadaDTO.setNombreCatedra(reservaEsporadica.getNombreCatedra());
        reservaSolapadaDTO.setNombreDocente(reservaEsporadica.getNombreDocente());
        reservaSolapadaDTO.setApellidoDocente(reservaEsporadica.getApellidoDocente());
        reservaSolapadaDTO.setCorreoDocente(reservaEsporadica.getCorreoDocente());

        return reservaSolapadaDTO;
    }
}
