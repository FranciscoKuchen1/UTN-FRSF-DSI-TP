package dsitp.backend.project.service;

import dsitp.backend.project.domain.Aula;
import dsitp.backend.project.domain.Bedel;
import dsitp.backend.project.domain.DiaReservado;
import dsitp.backend.project.domain.Periodo;
import dsitp.backend.project.domain.Reserva;
import dsitp.backend.project.domain.ReservaPeriodica;
import dsitp.backend.project.mapper.DiaReservadoMapper;
import dsitp.backend.project.mapper.ReservaPeriodicaMapper;
import dsitp.backend.project.model.AulaDTO;
import dsitp.backend.project.model.AulaSolapadaDTO;
import dsitp.backend.project.model.DiaReservadoDTO;
import dsitp.backend.project.model.ReservaPeriodicaDTO;
import dsitp.backend.project.model.ReservaPeriodicaSinDiasDTO;
import dsitp.backend.project.model.ReservaRetornoDTO;
import dsitp.backend.project.model.TipoAula;
import dsitp.backend.project.model.TipoPeriodo;
import dsitp.backend.project.repos.AulaRepository;
import dsitp.backend.project.repos.BedelRepository;
import dsitp.backend.project.repos.DiaReservadoRepository;
import dsitp.backend.project.repos.PeriodoRepository;
import dsitp.backend.project.repos.ReservaPeriodicaRepository;
import dsitp.backend.project.util.NotFoundException;
import dsitp.backend.project.util.Trio;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

@Service
@Transactional
public class ReservaPeriodicaService {

    private final ReservaPeriodicaRepository reservaPeriodicaRepository;
    private final AulaRepository aulaRepository;
    private final ReservaPeriodicaMapper reservaPeriodicaMapper;
    private final PeriodoRepository periodoRepository;
    private final BedelRepository bedelRepository;
    private final DiaReservadoMapper diaReservadoMapper;
    private final Validator reservaPeriodicaValidator;
    private final Validator diaReservadoValidator;

    @Autowired
    public ReservaPeriodicaService(final ReservaPeriodicaRepository reservaPeriodicaRepository,
            final AulaRepository aulaRepository, final DiaReservadoRepository diaReservadoRepository,
            final ReservaPeriodicaMapper reservaPeriodicaMapper, final PeriodoRepository periodoRepository,
            final BedelRepository bedelRepository, DiaReservadoMapper diaReservadoMapper,
            Validator reservaPeriodicaValidator, Validator diaReservadoValidator) {
        this.reservaPeriodicaRepository = reservaPeriodicaRepository;
        this.reservaPeriodicaMapper = reservaPeriodicaMapper;
        this.aulaRepository = aulaRepository;
        this.periodoRepository = periodoRepository;
        this.bedelRepository = bedelRepository;
        this.diaReservadoMapper = diaReservadoMapper;
        this.reservaPeriodicaValidator = reservaPeriodicaValidator;
        this.diaReservadoValidator = diaReservadoValidator;

    }

    public List<ReservaPeriodicaDTO> findAll() {
        final List<ReservaPeriodica> reservaPeriodicas = reservaPeriodicaRepository.findAll(Sort.by("id"));
        return reservaPeriodicas.stream()
                .map(reservaPeriodicaMapper::toReservaPeriodicaDTO)
                .toList();
    }

    public ReservaPeriodicaDTO get(final Integer id) {
        return reservaPeriodicaRepository.findById(id)
                .map(reservaPeriodicaMapper::toReservaPeriodicaDTO)
                .orElseThrow(NotFoundException::new);
    }

    // public ReservaRetornoDTO getDisponibilidadAulaReservaPeriodica(
    // final ReservaPeriodicaSinDiasDTO reservaPeriodicaSinDiasDTO) {
    // final ReservaPeriodica reservaPeriodica = reservaPeriodicaMapper
    // .toReservaPeriodicaEntityDisponibilidad(reservaPeriodicaSinDiasDTO);
    // ReservaRetornoDTO reservaRetornoDTO = new ReservaRetornoDTO();
    // reservaRetornoDTO.setDiasDisponibles(new ArrayList<>());
    // reservaRetornoDTO.setDiasConSolapamiento(new ArrayList<>());
    // List<Aula> aulas =
    // aulaRepository.findByTipoAulaAndCapacidad(reservaPeriodica.getTipoAula().toInteger(),
    // reservaPeriodica.getCantAlumnos());
    // if (!aulas.isEmpty()) {
    // for (DiaReservado diaReservado : reservaPeriodica.getDiasReservados()) {
    // List<AulaDTO> aulasDisponibles = obtenerDisponibilidad(aulas, diaReservado);
    // if (!aulasDisponibles.isEmpty()) {
    // DiaDisponibilidadDTO diaDisponibilidadDTO = new DiaDisponibilidadDTO();
    // diaDisponibilidadDTO.setDiaReservado(diaReservadoMapper.toDiaReservadoDTO(diaReservado));
    // diaDisponibilidadDTO.setAulasDisponibles(aulasDisponibles);
    // reservaRetornoDTO.getDiasDisponibles().add(diaDisponibilidadDTO);
    // } else {
    // DiaSolapamientoDTO diaSolapamientoDTO = new DiaSolapamientoDTO();
    // diaSolapamientoDTO.setDiaReservado(diaReservadoMapper.toDiaReservadoDTO(diaReservado));
    // diaSolapamientoDTO.setAulasConSolapamiento(obtenerAulasConMenorSuperposicion(aulas,
    // diaReservado));
    // reservaRetornoDTO.getDiasConSolapamiento().add(diaSolapamientoDTO);
    // }
    // }
    // }

    // return reservaRetornoDTO;
    // }

    public Integer create(final ReservaPeriodicaDTO reservaPeriodicaDTO) {
        final ReservaPeriodica reservaPeriodica = reservaPeriodicaMapper.toReservaPeriodicaEntity(reservaPeriodicaDTO);

        return reservaPeriodicaRepository.save(reservaPeriodica).getId();
    }

    public void update(final Integer id, final ReservaPeriodicaDTO reservaPeriodicaDTO) {
        final ReservaPeriodica existingReservaPeriodica = reservaPeriodicaRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        ReservaPeriodica updatedReservaPeriodica = reservaPeriodicaMapper.toReservaPeriodicaEntity(reservaPeriodicaDTO);

        updatedReservaPeriodica.setId(existingReservaPeriodica.getId());
        reservaPeriodicaRepository.save(updatedReservaPeriodica);
    }

    public void delete(final Integer id) {
        final ReservaPeriodica existingReservaPeriodica = reservaPeriodicaRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        existingReservaPeriodica.setBedel(null);
        existingReservaPeriodica.setDiasReservados(null);
        existingReservaPeriodica.setPeriodo(null);
        reservaPeriodicaRepository.save(existingReservaPeriodica);

        reservaPeriodicaRepository.deleteById(id);
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

        Periodo periodo = periodoRepository
                .findActivePeriodosByTipoAndYear(TipoPeriodo.fromInteger(reservaPeriodicaDTO.getTipoPeriodo()))
                .getFirst();
        reservaPeriodica.setPeriodo(periodo);

        List<DiaReservado> diasReservados = new ArrayList<>();
        for (DiaReservadoDTO diaReservadoDTO : reservaPeriodicaDTO.getDiasReservadosDTO()) {
            DiaReservado diaReservado = diaReservadoMapper.toDiaReservadoEntity(diaReservadoDTO);
            diaReservado.setReserva(reservaPeriodica);
            diasReservados.add(diaReservado);

        }

        reservaPeriodica.setDiasReservados(diasReservados);

        Bedel bedel = bedelRepository.findByIdRegistroAndEliminadoFalse(reservaPeriodicaDTO.getIdRegistroBedel())
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

    public List<DiaReservado> toDiasReservados(Periodo periodo,
            List<Trio<Integer, String, String>> diasSemanaHorasDuracion, ReservaPeriodica reservaPeriodica) {

        LocalDate fechaInicio = periodo.getFechaInicio();
        LocalDate fechaFin = periodo.getFechaFin();

        List<DiaReservado> diasReservados = new ArrayList<>();

        LocalDate fechaActual = fechaInicio;
        if (fechaInicio.isBefore(LocalDate.now())) {
            fechaActual = LocalDate.now();
        }
        while (!fechaActual.isAfter(fechaFin)) {
            DayOfWeek diaSemana = fechaActual.getDayOfWeek();
            Integer idDiaSemana = diaSemana.getValue() % 7;
            // NOTE: si hay 2 lunes, se toma el primero, por ejemplo
            if (Trio.containsFirst(diasSemanaHorasDuracion, idDiaSemana)) {
                Trio<Integer, String, String> trio = Trio.findByFirst(diasSemanaHorasDuracion, idDiaSemana);
                LocalTime horaInicio = LocalTime.parse(trio.getHoraInicio(), DateTimeFormatter.ofPattern("HH:mm"));
                Integer duracion = Integer.valueOf(trio.getDuracion());

                DiaReservado diaReservado = new DiaReservado();
                diaReservado.setFechaReserva(fechaActual);
                diaReservado.setHoraInicio(horaInicio);
                diaReservado.setDuracion(duracion);

                BindingResult bindingResult = new BeanPropertyBindingResult(diaReservado, "diaReservado");
                diaReservadoValidator.validate(diaReservado, bindingResult);

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

                diaReservado.setReserva(reservaPeriodica);
                diasReservados.add(diaReservado);
            }

            fechaActual = fechaActual.plusDays(1);
        }

        return diasReservados;
    }

}
