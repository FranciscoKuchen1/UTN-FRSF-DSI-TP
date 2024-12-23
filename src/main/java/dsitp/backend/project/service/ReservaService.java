package dsitp.backend.project.service;

import dsitp.backend.project.domain.Aula;
import dsitp.backend.project.domain.AulaInformatica;
import dsitp.backend.project.domain.AulaMultimedio;
import dsitp.backend.project.domain.AulaSinRecursosAdic;
import dsitp.backend.project.domain.Bedel;
import dsitp.backend.project.domain.DiaReservado;
import dsitp.backend.project.domain.Periodo;
import dsitp.backend.project.domain.Reserva;
import dsitp.backend.project.domain.ReservaEsporadica;
import dsitp.backend.project.domain.ReservaPeriodica;
import dsitp.backend.project.domain.Reserva;
import dsitp.backend.project.model.AulaDTO;
import dsitp.backend.project.model.AulaSolapadaDTO;
import dsitp.backend.project.model.DiaReservadoConSolapamientoDTO;
import dsitp.backend.project.model.DiaReservadoDTO;
import dsitp.backend.project.model.DiaReservadoDisponibilidadDTO;
import dsitp.backend.project.model.DiaSemanaConSolapamientoDTO;
import dsitp.backend.project.model.DiaSemanaDTO;
import dsitp.backend.project.model.DiaSemanaDisponibilidadDTO;
import dsitp.backend.project.model.ReservaDTO;
import dsitp.backend.project.model.ReservaEsporadicaDTO;
import dsitp.backend.project.model.ReservaPeriodicaDTO;
import dsitp.backend.project.model.ReservaPeriodicaSinDiasDTO;
import dsitp.backend.project.model.ReservaSolapadaDTO;
import dsitp.backend.project.model.TipoAula;
import dsitp.backend.project.model.TipoPeriodo;
import dsitp.backend.project.model.ReservaRetornoDTO;
import dsitp.backend.project.repos.AulaRepository;
import dsitp.backend.project.repos.BedelRepository;
import dsitp.backend.project.repos.DiaReservadoRepository;
import dsitp.backend.project.repos.PeriodoRepository;
import dsitp.backend.project.repos.ReservaEsporadicaRepository;
import dsitp.backend.project.repos.ReservaPeriodicaRepository;
import dsitp.backend.project.repos.ReservaRepository;
import dsitp.backend.project.util.NotFoundException;
import dsitp.backend.project.util.Trio;
import jakarta.persistence.Tuple;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

/**
 * Servicio que maneja la lógica de reserva de aulas, tanto para reservas periódicas como esporádicas.
 * Esta clase incluye métodos para verificar la disponibilidad de aulas y gestionar reservas solapadas.
 *
 */

@Service
@Transactional
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ReservaPeriodicaRepository reservaPeriodicaRepository;
    private final ReservaEsporadicaRepository reservaEsporadicaRepository;
    private final DiaReservadoRepository diaReservadoRepository;
    private final AulaRepository aulaRepository;
    private final PeriodoRepository periodoRepository;
    private final BedelRepository bedelRepository;
    private final Validator reservaPeriodicaValidator;
    private final Validator reservaEsporadicaValidator;
    private final Validator diaReservadoValidator;

    /**
     * Constructor para inicializar el servicio con los repositorios necesarios y tres validador.
     *
     * @param reservaRepository Repositorio de reservas.
     * @param reservaPeriodicaRepository Repositorio de reservas periódicas.
     * @param reservaEsporadicaRepository Repositorio de reservas esporádicas.
     * @param diaReservadoRepository Repositorio de días reservados.
     * @param aulaRepository Repositorio de aulas.
     * @param periodoRepository Repositorio de periodos.
     * @param bedelRepository Repositorio de bedeles.
     * @param reservaPeriodicaValidator Validador de reservas periódicas.
     * @param reservaEsporadicaValidator Validador de reservas esporádicas.
     * @param diaReservadoValidator Validador de días reservados.
     */
    @Autowired
    public ReservaService(final ReservaRepository reservaRepository,
            final ReservaPeriodicaRepository reservaPeriodicaRepository,
            final ReservaEsporadicaRepository reservaEsporadicaRepository,
            final DiaReservadoRepository diaReservadoRepository,
            final AulaRepository aulaRepository,
            final PeriodoRepository periodoRepository,
            final BedelRepository bedelRepository,
            final Validator reservaPeriodicaValidator,
            final Validator reservaEsporadicaValidator,
            final Validator diaReservadoValidator) {
        this.reservaRepository = reservaRepository;
        this.reservaPeriodicaRepository = reservaPeriodicaRepository;
        this.reservaEsporadicaRepository = reservaEsporadicaRepository;
        this.diaReservadoRepository = diaReservadoRepository;
        this.aulaRepository = aulaRepository;
        this.periodoRepository = periodoRepository;
        this.bedelRepository = bedelRepository;
        this.reservaPeriodicaValidator = reservaPeriodicaValidator;
        this.reservaEsporadicaValidator = reservaEsporadicaValidator;
        this.diaReservadoValidator = diaReservadoValidator;
    }

    /**
     * Obtiene la disponibilidad de aula para una reserva, ya sea periódica o esporádica.
     *
     * @param reservaDTO Objeto que contiene los datos de la reserva.
     * @param tipoReserva Tipo de reserva (0 para periódica, 1 para esporádica).
     * @return Un objeto {@link ReservaRetornoDTO} que contiene las aulas disponibles y con solapamiento.
     * @throws IllegalArgumentException Si hay errores de validación.
     */
    public ReservaRetornoDTO getDisponibilidadAulaReserva(final ReservaDTO reservaDTO, final Integer tipoReserva) {

        ReservaRetornoDTO reservaRetornoDTO = new ReservaRetornoDTO();
        reservaRetornoDTO.setDiasReservadosDisponibles(new ArrayList<>());
        reservaRetornoDTO.setDiasReservadosConSolapamiento(new ArrayList<>());
        reservaRetornoDTO.setDiasSemanaDisponibles(new ArrayList<>());
        reservaRetornoDTO.setDiasSemanaConSolapamiento(new ArrayList<>());

        List<Aula> aulasObtenidas = aulaRepository.findByTipoAulaAndCapacidad(reservaDTO.getTipoAula(),
                reservaDTO.getCantAlumnos());

        if (!aulasObtenidas.isEmpty()) {
            if (tipoReserva == 0) {

                final ReservaPeriodica reservaPeriodica = toReservaPeriodicaEntityDisponibilidad(reservaDTO);
                for (DiaSemanaDTO diaSemanaDTO : reservaDTO.getDiasSemanaDTO()) {
                    BindingResult bindingResult = new BeanPropertyBindingResult(diaSemanaDTO, "diaSemanaDTO");
                    diaReservadoValidator.validate(diaSemanaDTO, bindingResult);

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

                    List<AulaDTO> aulasDisponiblesDTO = obtenerAulasDisponibleEnPeriodo(reservaPeriodica,
                            aulasObtenidas, diaSemanaDTO);

                    if (aulasDisponiblesDTO.isEmpty() == false) {
                        DiaSemanaDisponibilidadDTO diaSemanaDisponibilidadDTO = new DiaSemanaDisponibilidadDTO();
                        diaSemanaDisponibilidadDTO.setDiaSemana(diaSemanaDTO);
                        // diag
                        diaSemanaDisponibilidadDTO.setAulasDisponibles(aulasDisponiblesDTO);
                        reservaRetornoDTO.getDiasSemanaDisponibles().add(diaSemanaDisponibilidadDTO);
                    } else {

                        List<AulaSolapadaDTO> aulasSolapadasDTO = obtenerDisponibilidadSuperposicionReservaPeriodica(
                                reservaPeriodica,
                                diaSemanaDTO, aulasObtenidas);

                        DiaSemanaConSolapamientoDTO diaSemanaConSolapamientoDTO = new DiaSemanaConSolapamientoDTO();
                        // diag
                        diaSemanaConSolapamientoDTO.setDiaSemana(diaSemanaDTO);
                        diaSemanaConSolapamientoDTO.setAulasConSolapamiento(aulasSolapadasDTO);

                        reservaRetornoDTO.getDiasSemanaConSolapamiento().add(diaSemanaConSolapamientoDTO);
                    }
                }
            } else if (tipoReserva == 1) {

                final ReservaEsporadica reservaEsporadica = toReservaEsporadicaEntityDisponibilidad(reservaDTO);
                for (DiaReservadoDTO diaReservadoDTO : reservaDTO.getDiasReservadosDTO()) {

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

                    List<AulaDTO> aulasDisponibles = obtenerDisponibilidadReservaEsporadica(aulasObtenidas,
                            diaReservadoDTO);
                    if (!aulasDisponibles.isEmpty()) {
                        DiaReservadoDisponibilidadDTO diaDisponibilidadDTO = new DiaReservadoDisponibilidadDTO();
                        diaDisponibilidadDTO.setDiaReservado(diaReservadoDTO);
                        // diag
                        diaDisponibilidadDTO.setAulasDisponibles(aulasDisponibles);
                        reservaRetornoDTO.getDiasReservadosDisponibles().add(diaDisponibilidadDTO);

                    } else {
                        List<AulaSolapadaDTO> aulasSolapadasDTO = obtenerDisponibilidadSuperposicionReservaEsporadica(
                                diaReservadoDTO, aulasObtenidas);

                        DiaReservadoConSolapamientoDTO diaSolapamientoDTO = new DiaReservadoConSolapamientoDTO();
                        diaSolapamientoDTO.setDiaReservado(diaReservadoDTO);
                        // diag
                        diaSolapamientoDTO.setAulasConSolapamiento(aulasSolapadasDTO);
                        reservaRetornoDTO.getDiasReservadosConSolapamiento().add(diaSolapamientoDTO);
                    }

                }
            }
        }

        return reservaRetornoDTO;
    }

    /**
     * Obtiene las aulas disponibles en un periodo específico para una reserva periódica.
     *
     * @param reservaPeriodica Objeto que representa la reserva periódica.
     * @param aulasObtenidas Lista de aulas obtenidas según los criterios de búsqueda.
     * @param diaSemanaDTO Objeto que representa el día de la semana para la reserva.
     * @return Una lista de objetos {@link AulaDTO} que representan las aulas disponibles.
     */
    public List<AulaDTO> obtenerAulasDisponibleEnPeriodo(ReservaPeriodica reservaPeriodica, List<Aula> aulasObtenidas,
            DiaSemanaDTO diaSemanaDTO) {

        List<DiaReservado> diasReservados = obtenerDiasReservados(reservaPeriodica, diaSemanaDTO);
        List<AulaDTO> aulasDisponiblesDTO = obtenerDisponibilidadReservaPeriodica(reservaPeriodica.getCantAlumnos(),
                aulasObtenidas, diasReservados);

        return aulasDisponiblesDTO;
    }

    // diag nombre y no hay parametro cantAlumnos
    /**
     * Obtiene las aulas que presentan solapamiento con la reserva periódica.
     *
     * @param reservaPeriodica Objeto que representa la reserva periódica.
     * @param diaSemanaDTO Objeto que representa el día de la semana para la reserva.
     * @param aulasObtenidas Lista de aulas obtenidas según los criterios de búsqueda.
     * @return Una lista de objetos {@link AulaSolapadaDTO} que representan las aulas con solapamiento.
     */
    public List<AulaSolapadaDTO> obtenerDisponibilidadSuperposicionReservaPeriodica(ReservaPeriodica reservaPeriodica,
            DiaSemanaDTO diaSemanaDTO, List<Aula> aulasObtenidas) {
        List<AulaSolapadaDTO> aulasSolapadasDTO = new ArrayList<>();
        LocalDate fechaIterador = null;
        Periodo periodo = reservaPeriodica.getPeriodo();

        if (periodo.getFechaInicio().isBefore(LocalDate.now())) {
            fechaIterador = LocalDate.now();
            fechaIterador = fechaIterador.plusDays(1);
        } else {
            fechaIterador = periodo.getFechaInicio();
        }

        // diag
        Integer menorSolapamiento = null;
        Map<Aula, Tuple> aulasMenosSolap = new HashMap<>();

        while (!fechaIterador.isAfter(periodo.getFechaFin())) {
            DayOfWeek diaSemana = fechaIterador.getDayOfWeek();
            Integer idDiaSemana = diaSemana.getValue() % 7;
            if (diaSemanaDTO.getDia() == idDiaSemana) {
                LocalTime horaInicio = LocalTime.parse(diaSemanaDTO.getHoraInicio(),
                        DateTimeFormatter.ofPattern("HH:mm"));
                Integer duracion = Integer.valueOf(diaSemanaDTO.getDuracion());

                DiaReservado diaReservado = new DiaReservado();
                diaReservado.setFechaReserva(fechaIterador);
                diaReservado.setHoraInicio(horaInicio);
                diaReservado.setDuracion(duracion);

                for (Aula aula : aulasObtenidas) {
                    // diag

                    Tuple reservaEsporadicaSolapadaConTiempoSolap = reservaRepository
                            .obtenerReservaEsporadicaQueSuperpone(
                                    aula.getNumero(),
                                    diaReservado.getFechaReserva(),
                                    diaReservado.getHoraInicio(),
                                    diaReservado.getHoraInicio().plusMinutes(diaReservado.getDuracion()));

                    Tuple reservaPeriodicaSolapadaConTiempoSolap = reservaRepository
                            .obtenerReservaPeriodicaQueSuperpone(
                                    aula.getNumero(),
                                    diaReservado.getFechaReserva(),
                                    diaReservado.getHoraInicio(),
                                    diaReservado.getHoraInicio().plusMinutes(diaReservado.getDuracion()));

                    Map<Aula, Tuple> aulasSolapadas = new HashMap<>();

                    if (reservaEsporadicaSolapadaConTiempoSolap == null
                            && reservaPeriodicaSolapadaConTiempoSolap != null) {
                        aulasSolapadas.put(aula, reservaPeriodicaSolapadaConTiempoSolap);
                    } else if (reservaEsporadicaSolapadaConTiempoSolap != null
                            && reservaPeriodicaSolapadaConTiempoSolap == null) {
                        aulasSolapadas.put(aula, reservaEsporadicaSolapadaConTiempoSolap);
                    } else if (reservaEsporadicaSolapadaConTiempoSolap != null
                            && reservaPeriodicaSolapadaConTiempoSolap != null) {
                        if (reservaEsporadicaSolapadaConTiempoSolap.get("superposicion",
                                Integer.class) < reservaPeriodicaSolapadaConTiempoSolap.get("superposicion",
                                        Integer.class)) {
                            aulasSolapadas.put(aula, reservaEsporadicaSolapadaConTiempoSolap);
                        } else {
                            aulasSolapadas.put(aula, reservaPeriodicaSolapadaConTiempoSolap);
                        }
                    }

                    if (aulasSolapadas.get(aula) != null) {
                        if (menorSolapamiento == null) {
                            aulasMenosSolap = aulasSolapadas;
                            menorSolapamiento = aulasSolapadas.get(aula).get("superposicion", Integer.class);
                        } else if (aulasSolapadas.get(aula).get("superposicion", Integer.class) < menorSolapamiento) {
                            aulasMenosSolap = aulasSolapadas;
                        } else if (aulasSolapadas.get(aula).get("superposicion", Integer.class)
                                .equals(menorSolapamiento)) {
                            aulasMenosSolap.putAll(aulasSolapadas);
                        }
                    }
                }

            }
            fechaIterador = fechaIterador.plusDays(1);
        }

        for (Map.Entry<Aula, Tuple> aulaMenosSolap : aulasMenosSolap.entrySet()) {
            Tuple tuple = aulaMenosSolap.getValue();

            Integer idReserva = tuple.get("id_reserva", Integer.class);
            Optional<Reserva> reservaObtenida = reservaRepository.findById(idReserva);

            Integer idDiaReservado = tuple.get("id_dia_reservado", Integer.class);
            Optional<DiaReservado> diaReservadoObtenido = reservaRepository.findDiaReservadoById(idDiaReservado);

            if (reservaObtenida.isPresent()) {
                if (diaReservadoObtenido.isPresent()) {
                    Reserva reserva = reservaObtenida.get();
                    AulaSolapadaDTO aulaSolapadaDTO = new AulaSolapadaDTO();
                    aulaSolapadaDTO.setAula(toAulaDTO(aulaMenosSolap.getKey()));
                    aulaSolapadaDTO
                            .setReservaSolapada(toReservaSolapadaDTO(reserva, diaReservadoObtenido.get()));

                    aulasSolapadasDTO.add(aulaSolapadaDTO);
                } else {
                    throw new NoSuchElementException("Dia reservado de la reserva no fue encontrado");
                }
            } else {
                throw new NoSuchElementException("Reserva no fue encontrada");
            }

        }

        return aulasSolapadasDTO;
    }

    /**
     * Obtiene los días reservados para una reserva periódica en función de los datos proporcionados.
     *
     * @param reservaPeriodica Objeto que representa la reserva periódica.
     * @param diaSemanaDTO Objeto que representa el día de la semana para la reserva.
     * @return Una lista de objetos {@link DiaReservado} que representan los días reservados.
     */
    private List<DiaReservado> obtenerDiasReservados(ReservaPeriodica reservaPeriodica, DiaSemanaDTO diaSemanaDTO) {

        LocalDate fechaIterador = null;
        List<DiaReservado> diasReservados = new ArrayList<>();
        Periodo periodo = reservaPeriodica.getPeriodo();

        int diaSemanaActual = LocalDate.now().getDayOfWeek().getValue() % 7;

        if (periodo.getFechaInicio().isBefore(LocalDate.now())) {
            fechaIterador = LocalDate.now();
            DayOfWeek diaSemanaHoy = LocalDate.now().getDayOfWeek();
            Integer diaHoy = diaSemanaHoy.getValue() % 7;

            if (diaSemanaDTO.getDia() == diaHoy) {

                LocalTime horaInicioDTO = LocalTime.parse(diaSemanaDTO.getHoraInicio(),
                        DateTimeFormatter.ofPattern("HH:mm"));

                if (LocalTime.now().isAfter(horaInicioDTO)) {
                    fechaIterador = fechaIterador.plusDays(1);
                }
            }

        } else {
            fechaIterador = periodo.getFechaInicio();
        }

        while (!fechaIterador.isAfter(periodo.getFechaFin())) {
            DayOfWeek diaSemana = fechaIterador.getDayOfWeek();
            Integer idDiaSemana = diaSemana.getValue() % 7;
            if (diaSemanaDTO.getDia() == idDiaSemana) {
                LocalTime horaInicio = LocalTime.parse(diaSemanaDTO.getHoraInicio(),
                        DateTimeFormatter.ofPattern("HH:mm"));
                Integer duracion = Integer.valueOf(diaSemanaDTO.getDuracion());

                DiaReservado diaReservado = new DiaReservado();
                diaReservado.setFechaReserva(fechaIterador);
                diaReservado.setHoraInicio(horaInicio);
                diaReservado.setDuracion(duracion);
                diaReservado.setReserva(reservaPeriodica);

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

                diasReservados.add(diaReservado);

            }
            fechaIterador = fechaIterador.plusDays(1);
        }

        return diasReservados;
    }

    // diag nombre
    /**
     * Obtiene la disponibilidad de aulas para una reserva periódica, teniendo en cuenta los días reservados.
     *
     * @param cantAlumnos Cantidad de alumnos para la reserva.
     * @param aulasObtenidas Lista de aulas obtenidas según los criterios de búsqueda.
     * @param diasReservados Lista de días reservados para la reserva.
     * @return Una lista de objetos {@link AulaDTO} que representan las aulas disponibles.
     */
    private List<AulaDTO> obtenerDisponibilidadReservaPeriodica(Integer cantAlumnos, List<Aula> aulasObtenidas,
            List<DiaReservado> diasReservados) {
        List<AulaDTO> aulasDisponiblesDTO = new ArrayList<>();
        for (Aula aula : aulasObtenidas) {
            Boolean aulaDisponible = true;
            Integer i = 0;
            while (i < diasReservados.size() && aulaDisponible) {
                aulaDisponible = (reservaRepository.obtenerDisponibilidadAulaPeriodica(aula.getNumero(),
                        diasReservados.get(i).getFechaReserva(), diasReservados.get(i).getHoraInicio(),
                        diasReservados.get(i).getHoraInicio().plusMinutes(diasReservados.get(i).getDuracion()))
                        && reservaRepository.obtenerDisponibilidadAulaEsporadica(aula.getNumero(),
                                diasReservados.get(i).getFechaReserva(), diasReservados.get(i).getHoraInicio(),
                                diasReservados.get(i).getHoraInicio()
                                        .plusMinutes(diasReservados.get(i).getDuracion())));

                i++;
            }
            if (aulaDisponible) {
                aulasDisponiblesDTO.add(toAulaDTO(aula));
            }

        }

        Collections.sort(aulasDisponiblesDTO, Comparator.comparingInt(AulaDTO::getCapacidad));

        aulasDisponiblesDTO = aulasDisponiblesDTO.stream()
                .limit(3)
                .collect(Collectors.toList());

        return aulasDisponiblesDTO;
    }

    // diag nombre
    /**
     * Convierte un objeto {@link ReservaDTO} en un objeto {@link ReservaPeriodica}.
     *
     * @param reservaDTO Objeto que contiene los datos de la reserva.
     * @return Un objeto {@link ReservaPeriodica} con los datos de la reserva.
     */
    public List<AulaSolapadaDTO> obtenerDisponibilidadSuperposicionReservaEsporadica(
            DiaReservadoDTO diaReservadoDTO, List<Aula> aulasObtenidas) {
        List<AulaSolapadaDTO> aulasSolapadasDTO = new ArrayList<>();

        Integer menorSolapamiento = null;
        Map<Aula, Tuple> aulasMenosSolap = new HashMap<>();

        for (Aula aula : aulasObtenidas) {

            Tuple reservaEsporadicaSolapadaConTiempoSolap = reservaRepository.obtenerReservaEsporadicaQueSuperpone(
                    aula.getNumero(),
                    diaReservadoDTO.getFechaReserva(),
                    diaReservadoDTO.getHoraInicio(),
                    diaReservadoDTO.getHoraInicio().plusMinutes(diaReservadoDTO.getDuracion()));

            Tuple reservaPeriodicaSolapadaConTiempoSolap = reservaRepository.obtenerReservaPeriodicaQueSuperpone(
                    aula.getNumero(),
                    diaReservadoDTO.getFechaReserva(),
                    diaReservadoDTO.getHoraInicio(),
                    diaReservadoDTO.getHoraInicio().plusMinutes(diaReservadoDTO.getDuracion()));

            Map<Aula, Tuple> aulasSolapadas = new HashMap<>();

            if (reservaEsporadicaSolapadaConTiempoSolap == null && reservaPeriodicaSolapadaConTiempoSolap != null) {
                aulasSolapadas.put(aula, reservaPeriodicaSolapadaConTiempoSolap);
            } else if (reservaEsporadicaSolapadaConTiempoSolap != null
                    && reservaPeriodicaSolapadaConTiempoSolap == null) {
                aulasSolapadas.put(aula, reservaEsporadicaSolapadaConTiempoSolap);
            } else if (reservaEsporadicaSolapadaConTiempoSolap != null
                    && reservaPeriodicaSolapadaConTiempoSolap != null) {
                if (reservaEsporadicaSolapadaConTiempoSolap.get("superposicion",
                        Integer.class) < reservaPeriodicaSolapadaConTiempoSolap.get("superposicion", Integer.class)) {
                    aulasSolapadas.put(aula, reservaEsporadicaSolapadaConTiempoSolap);
                } else {
                    aulasSolapadas.put(aula, reservaPeriodicaSolapadaConTiempoSolap);
                }
            }

            if (menorSolapamiento == null) {
                aulasMenosSolap = aulasSolapadas;
                menorSolapamiento = aulasSolapadas.get(aula).get("superposicion", Integer.class);
            } else if (aulasSolapadas.get(aula).get("superposicion", Integer.class) < menorSolapamiento) {
                aulasMenosSolap = aulasSolapadas;
            } else if (aulasSolapadas.get(aula).get("superposicion", Integer.class).equals(menorSolapamiento)) {
                aulasMenosSolap.putAll(aulasSolapadas);
            }

        }

        for (Map.Entry<Aula, Tuple> aulaMenosSolap : aulasMenosSolap.entrySet()) {
            Tuple tuple = aulaMenosSolap.getValue();

            Integer idReserva = tuple.get("id_reserva", Integer.class);
            Optional<Reserva> reservaObtenida = reservaRepository.findById(idReserva);

            Integer idDiaReservado = tuple.get("id_dia_reservado", Integer.class);
            Optional<DiaReservado> diaReservadoObtenido = reservaRepository.findDiaReservadoById(idDiaReservado);

            if (reservaObtenida.isPresent()) {
                if (diaReservadoObtenido.isPresent()) {
                    Reserva reserva = reservaObtenida.get();
                    AulaSolapadaDTO aulaSolapadaDTO = new AulaSolapadaDTO();
                    aulaSolapadaDTO.setAula(toAulaDTO(aulaMenosSolap.getKey()));
                    aulaSolapadaDTO
                            .setReservaSolapada(toReservaSolapadaDTO(reserva, diaReservadoObtenido.get()));

                    aulasSolapadasDTO.add(aulaSolapadaDTO);
                } else {
                    throw new NoSuchElementException("Dia reservado de la reserva no fue encontrado");
                }
            } else {
                throw new NoSuchElementException("Reserva no fue encontrada");
            }

        }

        return aulasSolapadasDTO;
    }

    // diag nombre y no hay parametro cantAlumnos
    /**
     * Obtiene las aulas que presentan solapamiento con la reserva esporádica.
     *
     * @param diaReservadoDTO Objeto que representa el día reservado para la reserva esporádica.
     * @param aulasObtenidas Lista de aulas obtenidas según los criterios de búsqueda.
     * @return Una lista de objetos {@link AulaSolapadaDTO} que representan las aulas con solapamiento.
     */
    private List<AulaDTO> obtenerDisponibilidadReservaEsporadica(List<Aula> aulasObtenidas,
            DiaReservadoDTO diaReservadoDTO) {
        List<AulaDTO> aulasDisponiblesDTO = new ArrayList<>();
        for (Aula aula : aulasObtenidas) {

            Boolean aulaDisponible = (reservaRepository.obtenerDisponibilidadAulaPeriodica(aula.getNumero(),
                    diaReservadoDTO.getFechaReserva(), diaReservadoDTO.getHoraInicio(),
                    diaReservadoDTO.getHoraInicio().plusMinutes(diaReservadoDTO.getDuracion()))
                    && reservaRepository.obtenerDisponibilidadAulaEsporadica(aula.getNumero(),
                            diaReservadoDTO.getFechaReserva(), diaReservadoDTO.getHoraInicio(),
                            diaReservadoDTO.getHoraInicio().plusMinutes(diaReservadoDTO.getDuracion())));
            if (aulaDisponible) {
                aulasDisponiblesDTO.add(toAulaDTO(aula));
            }

        }

        Collections.sort(aulasDisponiblesDTO, Comparator.comparingInt(AulaDTO::getCapacidad));

        return aulasDisponiblesDTO.stream()
                .limit(3)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene la disponibilidad de aulas para una reserva esporádica.
     *
     * @param aulasObtenidas Lista de aulas obtenidas según los criterios de búsqueda.
     * @param diaReservadoDTO Objeto que representa el día reservado para la reserva esporádica.
     * @return Una lista de objetos {@link AulaDTO} que representan las aulas disponibles.
     */
    public ReservaPeriodica toReservaPeriodicaEntityDisponibilidad(
            ReservaDTO reservaDTO) {

        ReservaPeriodica reservaPeriodica = new ReservaPeriodica();
        reservaPeriodica.setIdCatedra(reservaDTO.getIdCatedra());
        reservaPeriodica.setNombreCatedra(reservaDTO.getNombreCatedra());
        reservaPeriodica.setIdDocente(reservaDTO.getIdDocente());
        reservaPeriodica.setNombreDocente(reservaDTO.getNombreDocente());
        reservaPeriodica.setApellidoDocente(reservaDTO.getApellidoDocente());
        reservaPeriodica.setCorreoDocente(reservaDTO.getCorreoDocente());
        reservaPeriodica.setCantAlumnos(reservaDTO.getCantAlumnos());
        reservaPeriodica.setTipoAula(TipoAula.fromInteger(reservaDTO.getTipoAula()));

        Periodo periodo = periodoRepository
                .findActivePeriodosByTipoAndYear(TipoPeriodo.fromInteger(reservaDTO.getTipoPeriodo()))
                .getFirst();
        reservaPeriodica.setPeriodo(periodo);

        Bedel bedel = bedelRepository.findByIdRegistroAndEliminadoFalse(reservaDTO.getIdRegistroBedel())
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

    /**
     * Convierte un objeto ReservaDTO en una entidad ReservaPeriodica.
     *
     * @param reservaDTO El objeto DTO que contiene la información de la reserva.
     * @return La entidad ReservaPeriodica construida a partir del DTO.
     * @throws NotFoundException Si no se encuentra un Bedel con el ID proporcionado.
     * @throws IllegalArgumentException Si hay errores de validación en la reserva.
     */
    public ReservaPeriodica toReservaPeriodicaEntity(ReservaDTO reservaDTO) {

        ReservaPeriodica reservaPeriodica = new ReservaPeriodica();
        reservaPeriodica.setIdCatedra(reservaDTO.getIdCatedra());
        reservaPeriodica.setNombreCatedra(reservaDTO.getNombreCatedra());
        reservaPeriodica.setIdDocente(reservaDTO.getIdDocente());
        reservaPeriodica.setNombreDocente(reservaDTO.getNombreDocente());
        reservaPeriodica.setApellidoDocente(reservaDTO.getApellidoDocente());
        reservaPeriodica.setCorreoDocente(reservaDTO.getCorreoDocente());
        reservaPeriodica.setCantAlumnos(reservaDTO.getCantAlumnos());
        reservaPeriodica.setTipoAula(TipoAula.fromInteger(reservaDTO.getTipoAula()));

        Periodo periodo = periodoRepository
                .findActivePeriodosByTipoAndYear(TipoPeriodo.fromInteger(reservaDTO.getTipoPeriodo())).getFirst();
        reservaPeriodica.setPeriodo(periodo);

        Bedel bedel = bedelRepository.findByIdRegistroAndEliminadoFalse(reservaDTO.getIdRegistroBedel())
                .orElseThrow(() -> new NotFoundException("Bedel no encontrado"));
        reservaPeriodica.setBedel(bedel);

        List<DiaReservado> diasReservados = new ArrayList<>();

        for (DiaSemanaDTO diaSemanaDTO : reservaDTO.getDiasSemanaDTO()) {
            LocalDate fechaIterador = null;

            if (periodo.getFechaInicio().isBefore(LocalDate.now())) {
                fechaIterador = LocalDate.now();
                DayOfWeek diaSemanaHoy = LocalDate.now().getDayOfWeek();
                Integer diaHoy = diaSemanaHoy.getValue() % 7;

                if (diaSemanaDTO.getDia() == diaHoy) {

                    LocalTime horaInicioDTO = LocalTime.parse(diaSemanaDTO.getHoraInicio(),
                            DateTimeFormatter.ofPattern("HH:mm"));

                    if (LocalTime.now().isAfter(horaInicioDTO)) {
                        fechaIterador = fechaIterador.plusDays(1);
                    }
                }

            } else {
                fechaIterador = periodo.getFechaInicio();
            }

            while (!fechaIterador.isAfter(periodo.getFechaFin())) {
                DayOfWeek diaSemana = fechaIterador.getDayOfWeek();
                Integer idDiaSemana = diaSemana.getValue() % 7;
                if (diaSemanaDTO.getDia() == idDiaSemana) {
                    LocalTime horaInicio = LocalTime.parse(diaSemanaDTO.getHoraInicio(),
                            DateTimeFormatter.ofPattern("HH:mm"));
                    Integer duracion = Integer.valueOf(diaSemanaDTO.getDuracion());

                    DiaReservado diaReservado = new DiaReservado();
                    diaReservado.setFechaReserva(fechaIterador);
                    diaReservado.setHoraInicio(horaInicio);
                    diaReservado.setDuracion(duracion);
                    diaReservado.setReserva(reservaPeriodica);

                    Optional<Aula> aula = aulaRepository.findById(diaSemanaDTO.getIdAula());
                    if (aula.isPresent()) {
                        diaReservado.setAula(aula.get());
                    }

                    if (diaReservado.getAula() == null) {
                        throw new IllegalArgumentException("El aula es obligatoria.");
                    }

                    diasReservados.add(diaReservado);

                }

                fechaIterador = fechaIterador.plusDays(1);
            }
        }
        reservaPeriodica.setDiasReservados(diasReservados);

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

    /**
     * Convierte una entidad ReservaPeriodica en un objeto DTO.
     *
     * @param reservaPeriodica La entidad ReservaPeriodica que se desea convertir.
     * @return El objeto DTO correspondiente a la entidad.
     */
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
            DiaReservadoDTO diaReservadoDTO = new DiaReservadoDTO();
            diaReservadoDTO.setFechaReserva(diaReservado.getFechaReserva());
            diaReservadoDTO.setHoraInicio(diaReservado.getHoraInicio());
            diaReservadoDTO.setDuracion(diaReservado.getDuracion());
            diaReservadoDTO.setIdAula(diaReservado.getAula().getNumero());

            diasReservadosDTO.add(diaReservadoDTO);
        }
        reservaPeriodicaDTO.setDiasReservadosDTO(diasReservadosDTO);

        return reservaPeriodicaDTO;
    }

    /**
     * Convierte un objeto ReservaDTO en una entidad ReservaEsporadica, considerando la disponibilidad.
     *
     * @param reservaDTO El objeto DTO que contiene la información de la reserva.
     * @return La entidad ReservaEsporadica construida a partir del DTO.
     * @throws NotFoundException Si no se encuentra un Bedel con el ID proporcionado.
     * @throws IllegalArgumentException Si hay errores de validación en la reserva.
     */
    public ReservaEsporadica toReservaEsporadicaEntityDisponibilidad(ReservaDTO reservaDTO) {

        ReservaEsporadica reservaEsporadica = new ReservaEsporadica();
        reservaEsporadica.setIdCatedra(reservaDTO.getIdCatedra());
        reservaEsporadica.setNombreCatedra(reservaDTO.getNombreCatedra());
        reservaEsporadica.setIdDocente(reservaDTO.getIdDocente());
        reservaEsporadica.setNombreDocente(reservaDTO.getNombreDocente());
        reservaEsporadica.setApellidoDocente(reservaDTO.getApellidoDocente());
        reservaEsporadica.setCorreoDocente(reservaDTO.getCorreoDocente());
        reservaEsporadica.setCantAlumnos(reservaDTO.getCantAlumnos());
        reservaEsporadica.setTipoAula(TipoAula.fromInteger(reservaDTO.getTipoAula()));

        List<DiaReservadoDTO> diasReservadosDTO = reservaDTO.getDiasReservadosDTO();
        diasReservadosDTO.sort(Comparator.comparing(DiaReservadoDTO::getHoraInicio));

        List<DiaReservado> diasReservados = new ArrayList<>();

        for (DiaReservadoDTO diaReservadoDTO : diasReservadosDTO) {
            DiaReservado diaReservado = new DiaReservado();
            diaReservado.setFechaReserva(diaReservadoDTO.getFechaReserva());
            diaReservado.setHoraInicio(diaReservadoDTO.getHoraInicio());
            diaReservado.setDuracion(diaReservadoDTO.getDuracion());


            Optional<Aula> aula = aulaRepository.findById(diaReservadoDTO.getIdAula());
            if (aula.isPresent()) {
                diaReservado.setAula(aula.get());
            }

            diaReservado.setReserva(reservaEsporadica);
            diasReservados.add(diaReservado);
        }

        reservaEsporadica.setDiasReservados(diasReservados);

        Bedel bedel = bedelRepository.findByIdRegistroAndEliminadoFalse(reservaDTO.getIdRegistroBedel())
                .orElseThrow(() -> new NotFoundException("Bedel no encontrado"));
        reservaEsporadica.setBedel(bedel);

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

    /**
     * Convierte un objeto ReservaDTO en una entidad ReservaEsporadica.
     *
     * @param reservaDTO El objeto DTO que contiene la información de la reserva.
     * @return La entidad ReservaEsporadica construida a partir del DTO.
     * @throws NotFoundException Si no se encuentra un Bedel con el ID proporcionado.
     * @throws IllegalArgumentException Si hay errores de validación en la reserva.
     */
    public ReservaEsporadica toReservaEsporadicaEntity(ReservaDTO reservaDTO) {

        ReservaEsporadica reservaEsporadica = new ReservaEsporadica();
        reservaEsporadica.setIdCatedra(reservaDTO.getIdCatedra());
        reservaEsporadica.setNombreCatedra(reservaDTO.getNombreCatedra());
        reservaEsporadica.setIdDocente(reservaDTO.getIdDocente());
        reservaEsporadica.setNombreDocente(reservaDTO.getNombreDocente());
        reservaEsporadica.setApellidoDocente(reservaDTO.getApellidoDocente());
        reservaEsporadica.setCorreoDocente(reservaDTO.getCorreoDocente());
        reservaEsporadica.setCantAlumnos(reservaDTO.getCantAlumnos());
        reservaEsporadica.setTipoAula(TipoAula.fromInteger(reservaDTO.getTipoAula()));

        Bedel bedel = bedelRepository.findByIdRegistroAndEliminadoFalse(reservaDTO.getIdRegistroBedel())
                .orElseThrow(() -> new NotFoundException("Bedel no encontrado"));
        reservaEsporadica.setBedel(bedel);

        List<DiaReservado> diasReservados = new ArrayList<>();
        for (DiaReservadoDTO diaReservadoDTO : reservaDTO.getDiasReservadosDTO()) {
            DiaReservado diaReservado = new DiaReservado();
            diaReservado.setFechaReserva(diaReservadoDTO.getFechaReserva());
            diaReservado.setHoraInicio(diaReservadoDTO.getHoraInicio());
            diaReservado.setDuracion(diaReservadoDTO.getDuracion());
            diaReservado.setReserva(reservaEsporadica);

            Optional<Aula> aula = aulaRepository.findById(diaReservadoDTO.getIdAula());
            if (aula.isPresent()) {
                diaReservado.setAula(aula.get());
            }

            if (diaReservado.getAula() == null) {
                throw new IllegalArgumentException("El aula es obligatoria.");
            }

            diasReservados.add(diaReservado);
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

    /**
     * Convierte una entidad Reserva y un objeto DiaReservado en un objeto DTO ReservaSolapadaDTO.
     *
     * @param reserva La entidad Reserva que contiene los detalles de la reserva.
     * @param diaReservado El objeto DiaReservado que contiene los detalles del día reservado.
     * @return El objeto DTO ReservaSolapadaDTO con la información de la reserva y el día reservado.
     */
    public ReservaSolapadaDTO toReservaSolapadaDTO(Reserva reserva, DiaReservado diaReservado) {

        ReservaSolapadaDTO reservaSolapadaDTO = new ReservaSolapadaDTO();
        reservaSolapadaDTO.setNombreCatedra(reserva.getNombreCatedra());
        reservaSolapadaDTO.setNombreDocente(reserva.getNombreDocente());
        reservaSolapadaDTO.setApellidoDocente(reserva.getApellidoDocente());
        reservaSolapadaDTO.setCorreoDocente(reserva.getCorreoDocente());
        reservaSolapadaDTO.setInicioReserva(diaReservado.getHoraInicio());
        reservaSolapadaDTO.setFinReserva(diaReservado.getHoraInicio().plusMinutes(diaReservado.getDuracion()));

        return reservaSolapadaDTO;
    }

    /**
     * Convierte una entidad Aula en un objeto DTO AulaDTO.
     *
     * @param aula La entidad Aula que se desea convertir.
     * @return El objeto DTO AulaDTO correspondiente a la entidad.
     */
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

    /**
     * Crea una nueva reserva a partir de un objeto ReservaDTO y el tipo de reserva.
     *
     * @param reservaDTO El objeto DTO que contiene la información de la reserva.
     * @param tipoReserva El tipo de reserva (0 para ReservaPeriodica, 1 para ReservaEsporadica).
     * @return El ID de la reserva creada.
     */
    public Integer create(final ReservaDTO reservaDTO, final Integer tipoReserva) {

        if (tipoReserva == 0) {
            ReservaPeriodica reservaPeriodica = toReservaPeriodicaEntity(reservaDTO);
            return reservaPeriodicaRepository.save(reservaPeriodica).getId();
        } else if (tipoReserva == 1) {
            ReservaEsporadica reservaEsporadica = toReservaEsporadicaEntity(reservaDTO);
            return reservaEsporadicaRepository.save(reservaEsporadica).getId();
        }

        return null;
    }

}
