package dsitp.backend.project.mapper;

import dsitp.backend.project.domain.Bedel;
import dsitp.backend.project.domain.DiaReservado;
import dsitp.backend.project.domain.Periodo;
import dsitp.backend.project.domain.ReservaPeriodica;
import dsitp.backend.project.model.DiaReservadoDTO;
import dsitp.backend.project.model.ReservaPeriodicaDTO;
import dsitp.backend.project.model.ReservaPeriodicaSinDiasDTO;
import dsitp.backend.project.model.ReservaSolapadaDTO;
import dsitp.backend.project.model.TipoAula;
import dsitp.backend.project.model.TipoPeriodo;
import dsitp.backend.project.repos.BedelRepository;
import dsitp.backend.project.repos.PeriodoRepository;
import dsitp.backend.project.util.NotFoundException;
import dsitp.backend.project.util.Trio;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

@Service
public class ReservaPeriodicaMapper {

    private final PeriodoRepository periodoRepository;
    private final BedelRepository bedelRepository;
    private final DiaReservadoMapper diaReservadoMapper;
    private final Validator reservaPeriodicaValidator;

    @Autowired
    public ReservaPeriodicaMapper(PeriodoRepository periodoRepository, DiaReservadoMapper diaReservadoMapper, Validator reservaPeriodicaValidator, BedelRepository bedelRepository) {
        this.periodoRepository = periodoRepository;
        this.diaReservadoMapper = diaReservadoMapper;
        this.reservaPeriodicaValidator = reservaPeriodicaValidator;
        this.bedelRepository = bedelRepository;
    }

    public ReservaPeriodica toReservaPeriodicaEntityDisponibilidad(ReservaPeriodicaSinDiasDTO reservaPeriodicaSinDiasDTO) {

        ReservaPeriodica reservaPeriodica = new ReservaPeriodica();
        reservaPeriodica.setIdCatedra(reservaPeriodicaSinDiasDTO.getIdCatedra());
        reservaPeriodica.setNombreCatedra(reservaPeriodicaSinDiasDTO.getNombreCatedra());
        reservaPeriodica.setIdDocente(reservaPeriodicaSinDiasDTO.getIdDocente());
        reservaPeriodica.setNombreDocente(reservaPeriodicaSinDiasDTO.getNombreDocente());
        reservaPeriodica.setApellidoDocente(reservaPeriodicaSinDiasDTO.getApellidoDocente());
        reservaPeriodica.setCorreoDocente(reservaPeriodicaSinDiasDTO.getCorreoDocente());
        reservaPeriodica.setCantAlumnos(reservaPeriodicaSinDiasDTO.getCantAlumnos());
        reservaPeriodica.setTipoAula(TipoAula.fromInteger(reservaPeriodicaSinDiasDTO.getTipoAula()));

        Periodo periodo = periodoRepository.findActivePeriodosByTipo(TipoPeriodo.fromInteger(reservaPeriodicaSinDiasDTO.getTipoPeriodo())).getFirst();
        reservaPeriodica.setPeriodo(periodo);

        List<DiaReservado> diasReservados = toDiasReservados(
                // TODO: ver si podemos hacer que el tipoPeriodo Anual se divida en 2?
                periodo,
                reservaPeriodicaSinDiasDTO.getDiasSemanaHorasDuracion(),
                reservaPeriodica
        );
        reservaPeriodica.setDiasReservados(diasReservados);

        Bedel bedel = bedelRepository.findByIdRegistro(reservaPeriodicaSinDiasDTO.getIdRegistroBedel())
                .orElseThrow(() -> new NotFoundException("Bedel no encontrado"));
        reservaPeriodica.setBedel(bedel);

        BindingResult bindingResult = new BeanPropertyBindingResult(reservaPeriodica, "reservaPeriodica");
        reservaPeriodicaValidator.validate(reservaPeriodica, bindingResult);

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

        return reservaPeriodica;
    }

    public ReservaPeriodica toReservaPeriodicaEntity(ReservaPeriodicaDTO reservaPeriodicaDTO) {

        ReservaPeriodica reservaPeriodica = new ReservaPeriodica();
        reservaPeriodica.setIdCatedra(reservaPeriodicaDTO.getIdCatedra());
        reservaPeriodica.setNombreCatedra(reservaPeriodicaDTO.getNombreCatedra());
        reservaPeriodica.setIdDocente(reservaPeriodicaDTO.getIdDocente());
        reservaPeriodica.setNombreDocente(reservaPeriodicaDTO.getNombreDocente());
        reservaPeriodica.setApellidoDocente(reservaPeriodicaDTO.getApellidoDocente());
        reservaPeriodica.setCorreoDocente(reservaPeriodicaDTO.getCorreoDocente());
        reservaPeriodica.setCantAlumnos(reservaPeriodicaDTO.getCantAlumnos());
        reservaPeriodica.setTipoAula(TipoAula.fromInteger(reservaPeriodicaDTO.getTipoAula()));

        Periodo periodo = periodoRepository.findActivePeriodosByTipo(TipoPeriodo.fromInteger(reservaPeriodicaDTO.getTipoPeriodo())).getFirst();
        reservaPeriodica.setPeriodo(periodo);

        List<DiaReservado> diasReservados = new ArrayList<>();
        for (DiaReservadoDTO diaReservadoDTO : reservaPeriodicaDTO.getDiasReservadosDTO()) {
            diasReservados.add(diaReservadoMapper.toDiaReservadoEntity(diaReservadoDTO));
        }

        reservaPeriodica.setDiasReservados(diasReservados);

        Bedel bedel = bedelRepository.findByIdRegistro(reservaPeriodicaDTO.getIdRegistroBedel())
                .orElseThrow(() -> new NotFoundException("Bedel no encontrado"));
        reservaPeriodica.setBedel(bedel);

        BindingResult bindingResult = new BeanPropertyBindingResult(reservaPeriodica, "reservaPeriodica");
        reservaPeriodicaValidator.validate(reservaPeriodica, bindingResult);

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

        return reservaPeriodica;
    }

    public ReservaPeriodicaDTO toReservaPeriodicaDTO(ReservaPeriodica reservaPeriodica) {
        if (reservaPeriodica == null) {
            return null;
        }

        ReservaPeriodicaDTO reservaPeriodicaDTO = new ReservaPeriodicaDTO();
        reservaPeriodicaDTO.setIdCatedra(reservaPeriodica.getIdCatedra());
        reservaPeriodicaDTO.setNombreCatedra(reservaPeriodica.getNombreCatedra());
        reservaPeriodicaDTO.setIdDocente(reservaPeriodica.getIdDocente());
        reservaPeriodicaDTO.setNombreDocente(reservaPeriodica.getNombreDocente());
        reservaPeriodicaDTO.setApellidoDocente(reservaPeriodica.getApellidoDocente());
        reservaPeriodicaDTO.setCorreoDocente(reservaPeriodica.getCorreoDocente());
        reservaPeriodicaDTO.setCantAlumnos(reservaPeriodica.getCantAlumnos());
        reservaPeriodicaDTO.setTipoAula(reservaPeriodica.getTipoAula().toInteger());
        reservaPeriodicaDTO.setIdRegistroBedel(reservaPeriodica.getBedel().getIdRegistro());

        if (reservaPeriodica.getPeriodo() != null) {
            reservaPeriodicaDTO.setTipoPeriodo(reservaPeriodica.getPeriodo().getTipoPeriodo().toInteger());
        }

        List<DiaReservadoDTO> diasReservadosDTO = new ArrayList<>();
        for (DiaReservado diaReservado : reservaPeriodica.getDiasReservados()) {
            diasReservadosDTO.add(diaReservadoMapper.toDiaReservadoDTO(diaReservado));
        }
        reservaPeriodicaDTO.setDiasReservadosDTO(diasReservadosDTO);

        return reservaPeriodicaDTO;
    }

    public List<DiaReservado> toDiasReservados(Periodo periodo, List<Trio<Integer, String, String>> diasSemanaHorasDuracion, ReservaPeriodica reservaPeriodica) {

        LocalDate fechaInicio = periodo.getFechaInicio();
        LocalDate fechaFin = periodo.getFechaFin();

        List<DiaReservado> diasReservados = new ArrayList<>();

        LocalDate fechaActual = fechaInicio;
        while (!fechaActual.isAfter(fechaFin)) {
            DayOfWeek diaSemana = fechaActual.getDayOfWeek();
            Integer idDiaSemana = diaSemana.getValue() % 7;
            // NOTE: si hay 2 domingos, se toma el primero, por ejemplo
            if (Trio.containsFirst(diasSemanaHorasDuracion, idDiaSemana)) {
                Trio<Integer, String, String> trio = Trio.findByFirst(diasSemanaHorasDuracion, idDiaSemana);
                LocalTime horaInicio = LocalTime.parse(trio.getHoraInicio(), DateTimeFormatter.ofPattern("HH:mm"));
                Integer duracion = Integer.valueOf(trio.getDuracion());
                // TODO: ver si mejorar
                DiaReservadoDTO diaReservadoDTO = new DiaReservadoDTO();
                diaReservadoDTO.setFechaReserva(fechaActual);
                diaReservadoDTO.setHoraInicio(horaInicio);
                diaReservadoDTO.setDuracion(duracion);

                DiaReservado diaReservado = diaReservadoMapper.toDiaReservadoEntity(diaReservadoDTO);
                diaReservado.setReserva(reservaPeriodica);
                diasReservados.add(diaReservado);
            }

            fechaActual = fechaActual.plusDays(1);
        }

        return diasReservados;
    }

    public ReservaSolapadaDTO toReservaSolapadaDTO(ReservaPeriodica reservaPeriodica) {
        ReservaSolapadaDTO reservaSolapadaDTO = new ReservaSolapadaDTO();
        reservaSolapadaDTO.setNombreCatedra(reservaPeriodica.getNombreCatedra());
        reservaSolapadaDTO.setNombreDocente(reservaPeriodica.getNombreDocente());
        reservaSolapadaDTO.setApellidoDocente(reservaPeriodica.getApellidoDocente());
        reservaSolapadaDTO.setCorreoDocente(reservaPeriodica.getCorreoDocente());
        reservaSolapadaDTO.setInicioReserva(reservaPeriodica.getPeriodo().getFechaInicio());
        reservaSolapadaDTO.setFinReserva(reservaPeriodica.getPeriodo().getFechaFin());

        return reservaSolapadaDTO;
    }
}
