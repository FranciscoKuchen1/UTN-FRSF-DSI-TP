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
import dsitp.backend.project.mapper.AulaMapper;
import dsitp.backend.project.mapper.DiaReservadoMapper;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ReservaPeriodicaRepository reservaPeriodicaRepository;
    private final ReservaEsporadicaRepository reservaEsporadicaRepository;
    private final DiaReservadoRepository diaReservadoRepository;
    private final AulaRepository aulaRepository;
    private final DiaReservadoMapper diaReservadoMapper;
    private final AulaMapper aulaMapper;
    private final PeriodoRepository periodoRepository;
    private final BedelRepository bedelRepository;
    private final Validator reservaPeriodicaValidator;
    private final Validator reservaEsporadicaValidator;
    private final Validator diaReservadoValidator;

    @Autowired
    public ReservaService(final ReservaRepository reservaRepository,
            final ReservaPeriodicaRepository reservaPeriodicaRepository,
            final ReservaEsporadicaRepository reservaEsporadicaRepository,
            final DiaReservadoRepository diaReservadoRepository,
            final AulaRepository aulaRepository,
            final DiaReservadoMapper diaReservadoMapper,
            final AulaMapper aulaMapper,
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
        this.diaReservadoMapper = diaReservadoMapper;
        this.aulaMapper = aulaMapper;
        this.periodoRepository = periodoRepository;
        this.bedelRepository = bedelRepository;
        this.reservaPeriodicaValidator = reservaPeriodicaValidator;
        this.reservaEsporadicaValidator = reservaEsporadicaValidator;
        this.diaReservadoValidator = diaReservadoValidator;
    }

    // public List<ReservaDTO> findAll() {
    // final List<Reserva> reservas = reservaRepository.findAll(Sort.by("id"));
    // return reservas.stream()
    // .map(reservaMapper::toReservaDTO)
    // .toList();
    // }

    // public ReservaDTO get(final Integer id) {
    // return reservaRepository.findById(id)
    // .map(reservaMapper::toReservaDTO)
    // .orElseThrow(NotFoundException::new);
    // }

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
                        // dif diag
                        diaSemanaDisponibilidadDTO.setAulasDisponibles(aulasDisponiblesDTO);
                        reservaRetornoDTO.getDiasSemanaDisponibles().add(diaSemanaDisponibilidadDTO);
                    } else {
                        // DiaSolapamientoDTO diaSolapamientoDTO = new DiaSolapamientoDTO();
                        // diaSolapamientoDTO.setDiaReservado(diaReservadoMapper.toDiaReservadoDTO(diaReservado));
                        // diaSolapamientoDTO
                        // .setAulasConSolapamiento(obtenerAulasConMenorSuperposicion(aulas,
                        // diaReservado));
                        // reservaRetornoDTO.getDiasConSolapamiento().add(diaSolapamientoDTO);
                        List<AulaSolapadaDTO> aulasSolapadasDTO = obtenerDisponibilidadSuperposicionReservaPeriodica(
                                reservaPeriodica,
                                diaSemanaDTO, aulasObtenidas);

                        DiaSemanaConSolapamientoDTO diaSemanaConSolapamientoDTO = new DiaSemanaConSolapamientoDTO();
                        // dif diag
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
                        // dif diag
                        diaDisponibilidadDTO.setAulasDisponibles(aulasDisponibles);
                        reservaRetornoDTO.getDiasReservadosDisponibles().add(diaDisponibilidadDTO);

                    } else {
                        List<AulaSolapadaDTO> aulasSolapadasDTO = obtenerDisponibilidadSuperposicionReservaEsporadica(
                                diaReservadoDTO, aulasObtenidas);

                        DiaReservadoConSolapamientoDTO diaSolapamientoDTO = new DiaReservadoConSolapamientoDTO();
                        diaSolapamientoDTO.setDiaReservado(diaReservadoDTO);
                        // dif diag
                        diaSolapamientoDTO.setAulasConSolapamiento(aulasSolapadasDTO);
                        reservaRetornoDTO.getDiasReservadosConSolapamiento().add(diaSolapamientoDTO);
                    }

                }
            }
        }

        return reservaRetornoDTO;
    }

    public List<AulaDTO> obtenerAulasDisponibleEnPeriodo(ReservaPeriodica reservaPeriodica, List<Aula> aulasObtenidas,
            DiaSemanaDTO diaSemanaDTO) {
        List<AulaDTO> aulasDisponiblesDTO = new ArrayList<>();
        List<DiaReservado> diasReservados = obtenerDiasReservados(reservaPeriodica, diaSemanaDTO);
        obtenerDisponibilidadReservaPeriodica(reservaPeriodica.getCantAlumnos(), aulasObtenidas, diasReservados,
                aulasDisponiblesDTO);

        return aulasDisponiblesDTO;
    }

    // dif diag nombre y no hay parametro cantAlumnos
    public List<AulaSolapadaDTO> obtenerDisponibilidadSuperposicionReservaPeriodica(ReservaPeriodica reservaPeriodica,
            DiaSemanaDTO diaSemanaDTO, List<Aula> aulasObtenidas) {
        List<AulaSolapadaDTO> aulasSolapadasDTO = new ArrayList<>();
        LocalDate fechaIterador = null;
        Periodo periodo = reservaPeriodica.getPeriodo();

        if (periodo.getFechaInicio().isBefore(LocalDate.now())) {
            fechaIterador = LocalDate.now();
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

                // dif diag
                Integer menorSolapamiento = null;
                Map<Aula, Tuple> aulasMenosSolap = new HashMap<>();

                for (Aula aula : aulasObtenidas) {
                    // dif diag

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
                                Integer.class) < reservaPeriodicaSolapadaConTiempoSolap.get(1, Integer.class)) {
                            aulasSolapadas.put(aula, reservaEsporadicaSolapadaConTiempoSolap);
                        } else {
                            aulasSolapadas.put(aula, reservaPeriodicaSolapadaConTiempoSolap);
                        }
                    }

                    if (menorSolapamiento == null) {
                        aulasMenosSolap = aulasSolapadas;
                    } else if (aulasSolapadas.get(aula).get("superposicion", Integer.class) < menorSolapamiento) {
                        aulasMenosSolap = aulasSolapadas;
                    }

                }

                for (Map.Entry<Aula, Tuple> aulaMenosSolap : aulasMenosSolap.entrySet()) {
                    Tuple tuple = aulaMenosSolap.getValue();

                    Integer idReserva = tuple.get("id", Integer.class);
                    Optional<Reserva> reservaObtenida = reservaRepository.findById(idReserva);

                    if (reservaObtenida.isPresent()) {
                        Reserva reserva = reservaObtenida.get();
                        AulaSolapadaDTO aulaSolapadaDTO = new AulaSolapadaDTO();
                        aulaSolapadaDTO.setAula(toAulaDTO(aulaMenosSolap.getKey()));
                        aulaSolapadaDTO
                                .setReservaSolapada(toReservaSolapadaDTO(reserva));

                        aulasSolapadasDTO.add(aulaSolapadaDTO);

                    } else {
                        throw new NoSuchElementException("Reserva no fue encontrada");
                    }

                }

                // BindingResult bindingResult = new BeanPropertyBindingResult(diaReservado,
                // "diaReservado");
                // diaReservadoValidator.validate(diaReservado, bindingResult);

                // if (bindingResult.hasErrors()) {

                // StringBuilder errorMessages = new StringBuilder();
                // bindingResult.getAllErrors().forEach(error -> {
                // String errorMessage = error.getDefaultMessage();
                // if (errorMessage != null) {
                // errorMessages.append(errorMessage);
                // }
                // });

                // throw new IllegalArgumentException(errorMessages.toString());
                // }

                // diaReservado.setReserva(reservaPeriodica);
                // diasReservados.add(diaReservado);

            }
            fechaIterador = fechaIterador.plusDays(1);
        }
        return aulasSolapadasDTO;
    }

    private List<DiaReservado> obtenerDiasReservados(ReservaPeriodica reservaPeriodica, DiaSemanaDTO diaSemanaDTO) {

        LocalDate fechaIterador = null;
        List<DiaReservado> diasReservados = new ArrayList<>();
        Periodo periodo = reservaPeriodica.getPeriodo();

        if (periodo.getFechaInicio().isBefore(LocalDate.now())) {
            fechaIterador = LocalDate.now();
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
            fechaIterador = fechaIterador.plusDays(1);
        }

        return diasReservados;
    }

    // dif diag nombre
    private void obtenerDisponibilidadReservaPeriodica(Integer cantAlumnos, List<Aula> aulasObtenidas,
            List<DiaReservado> diasReservados, List<AulaDTO> aulasDisponiblesDTO) {
        for (Aula aula : aulasObtenidas) {
            Boolean aulaDisponible = true;
            Integer i = 0;
            while (i < diasReservados.size() && aulaDisponible) {
                // TODO: ver que ande obtenerDisponibilidadAula
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
    }

    // dif diag nombre
    public List<AulaSolapadaDTO> obtenerDisponibilidadSuperposicionReservaEsporadica(
            DiaReservadoDTO diaReservadoDTO, List<Aula> aulasObtenidas) {
        List<AulaSolapadaDTO> aulasSolapadasDTO = new ArrayList<>();

        Integer menorSolapamiento = null;
        Map<Aula, Tuple> aulasMenosSolap = new HashMap<>();

        for (Aula aula : aulasObtenidas) {
            // TODO: ver que ande obtenerDisponibilidadAula

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
                if (reservaEsporadicaSolapadaConTiempoSolap.get(1,
                        Integer.class) < reservaPeriodicaSolapadaConTiempoSolap.get("superposicion", Integer.class)) {
                    aulasSolapadas.put(aula, reservaEsporadicaSolapadaConTiempoSolap);
                } else {
                    aulasSolapadas.put(aula, reservaPeriodicaSolapadaConTiempoSolap);
                }
            }

            if (menorSolapamiento == null) {
                aulasMenosSolap = aulasSolapadas;
            } else if (aulasSolapadas.get(aula).get("superposicion", Integer.class) < menorSolapamiento) {
                aulasMenosSolap = aulasSolapadas;
            }

        }

        for (Map.Entry<Aula, Tuple> aulaMenosSolap : aulasMenosSolap.entrySet()) {
            Tuple tuple = aulaMenosSolap.getValue();

            Integer idReserva = tuple.get("id", Integer.class);
            Optional<Reserva> reservaObtenida = reservaRepository.findById(idReserva);

            if (reservaObtenida.isPresent()) {
                Reserva reserva = reservaObtenida.get();
                AulaSolapadaDTO aulaSolapadaDTO = new AulaSolapadaDTO();
                aulaSolapadaDTO.setAula(toAulaDTO(aulaMenosSolap.getKey()));
                aulaSolapadaDTO
                        .setReservaSolapada(toReservaSolapadaDTO(reserva));

                aulasSolapadasDTO.add(aulaSolapadaDTO);

            } else {
                throw new NoSuchElementException("Reserva no fue encontrada");
            }

        }

        return aulasSolapadasDTO;
    }

    // dif diag nombre y no hay parametro cantAlumnos
    private List<AulaDTO> obtenerDisponibilidadReservaEsporadica(List<Aula> aulasObtenidas,
            DiaReservadoDTO diaReservadoDTO) {
        List<AulaDTO> aulasDisponiblesDTO = new ArrayList<>();
        for (Aula aula : aulasObtenidas) {

            // TODO: ver que ande obtenerDisponibilidadAula
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
        return aulasDisponiblesDTO;
    }

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

        // TODO: ver mejorar periodos
        // TODO: ver si podemos hacer que el tipoPeriodo Anual se divida en 2?
        Periodo periodo = periodoRepository
                .findActivePeriodosByTipo(TipoPeriodo.fromInteger(reservaDTO.getTipoPeriodo()))
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
                .findActivePeriodosByTipo(TipoPeriodo.fromInteger(reservaDTO.getTipoPeriodo())).getFirst();
        reservaPeriodica.setPeriodo(periodo);

        List<DiaReservado> diasReservados = new ArrayList<>();

        for (DiaSemanaDTO diaSemanaDTO : reservaDTO.getDiasSemanaDTO()) {
            LocalDate fechaIterador = null;

            if (periodo.getFechaInicio().isBefore(LocalDate.now())) {
                fechaIterador = LocalDate.now();
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
                    diasReservados.add(diaReservado);

                }

                fechaIterador = fechaIterador.plusDays(1);
            }
        }
        reservaPeriodica.setDiasReservados(diasReservados);

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

        Bedel bedel = bedelRepository.findByIdRegistroAndEliminadoFalse(reservaDTO.getIdRegistroBedel())
                .orElseThrow(() -> new NotFoundException("Bedel no encontrado"));
        reservaEsporadica.setBedel(bedel);

        List<DiaReservado> diasReservados = new ArrayList<>();
        for (DiaReservadoDTO diaReservadoDTO : reservaDTO.getDiasReservadosDTO()) {
            DiaReservado diaReservado = diaReservadoMapper.toDiaReservadoEntity(diaReservadoDTO);
            diaReservado.setReserva(reservaEsporadica);
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
            DiaReservado diaReservado = diaReservadoMapper.toDiaReservadoEntity(diaReservadoDTO);
            diaReservado.setReserva(reservaEsporadica);
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

    public List<DiaReservado> toDiasReservados(Periodo periodo,
            List<Trio<Integer, String, String>> diasSemanaHorasDuracion, ReservaPeriodica reservaPeriodica) {

        LocalDate fechaInicio = periodo.getFechaInicio();
        LocalDate fechaFin = periodo.getFechaFin();

        List<DiaReservado> diasReservados = new ArrayList<>();

        LocalDate fechaActual = fechaInicio;
        if (fechaInicio.isBefore(LocalDate.now())) {
            fechaActual = LocalDate.now();
        }
        // TODO: cambiar a como está en el diagrama
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

    // public ReservaSolapadaDTO toReservaSolapadaDTO(ReservaPeriodica
    // reservaPeriodica) {
    // ReservaSolapadaDTO reservaSolapadaDTO = new ReservaSolapadaDTO();
    // reservaSolapadaDTO.setNombreCatedra(reservaPeriodica.getNombreCatedra());
    // reservaSolapadaDTO.setNombreDocente(reservaPeriodica.getNombreDocente());
    // reservaSolapadaDTO.setApellidoDocente(reservaPeriodica.getApellidoDocente());
    // reservaSolapadaDTO.setCorreoDocente(reservaPeriodica.getCorreoDocente());
    // reservaSolapadaDTO.setInicioReserva(reservaPeriodica.getPeriodo().getFechaInicio());
    // reservaSolapadaDTO.setFinReserva(reservaPeriodica.getPeriodo().getFechaFin());

    // return reservaSolapadaDTO;
    // }

    public ReservaSolapadaDTO toReservaSolapadaDTO(Reserva reserva) {

        ReservaSolapadaDTO reservaSolapadaDTO = new ReservaSolapadaDTO();
        reservaSolapadaDTO.setNombreCatedra(reserva.getNombreCatedra());
        reservaSolapadaDTO.setNombreDocente(reserva.getNombreDocente());
        reservaSolapadaDTO.setApellidoDocente(reserva.getApellidoDocente());
        reservaSolapadaDTO.setCorreoDocente(reserva.getCorreoDocente());
        // TODO: terminar
        // reservaSolapadaDTO.setInicioReserva(reserva);
        // reservaSolapadaDTO.setFinReserva(reserva);

        return reservaSolapadaDTO;
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

    // public DiaSemanaDTO toDiaSemanaDTO(DiaSemana diaSemana) {
    // if (diaSemana == null) {
    // return null;
    // }

    // DiaSemanaDTO diaSemanaDTO = new DiaSemanaDTO();
    // diaSemanaDTO.setFechaReserva(diaSemana.getFechaReserva());
    // diaSemanaDTO.setDuracion(diaSemana.getDuracion());
    // diaSemanaDTO.setHoraInicio(diaSemana.getHoraInicio());

    // return diaReservadoDTO;
    // }

    public Integer create(final ReservaDTO reservaDTO, final Integer tipoReserva) {

        if (tipoReserva == 0) {
            ReservaPeriodica reservaPeriodica = toReservaPeriodicaEntity(reservaDTO);
            return reservaPeriodicaRepository.save(reservaPeriodica).getId();
        } else if (tipoReserva == 1) {
            ReservaEsporadica reservaEsporadica = toReservaEsporadicaEntity(reservaDTO);
            return reservaEsporadicaRepository.save(reservaEsporadica).getId();
        }

        // TODO: ver mejorar
        return null;
    }

    // public void update(final Integer id, final ReservaDTO reservaDTO) {
    // final Reserva existingReserva = reservaRepository.findById(id)
    // .orElseThrow(NotFoundException::new);

    // Reserva updatedReserva = reservaMapper.toReservaEntity(reservaDTO);

    // updatedReserva.setId(existingReserva.getId());
    // reservaRepository.save(updatedReserva);
    // }

    // public void delete(final Integer id) {
    // final Reserva existingReserva = reservaRepository.findById(id)
    // .orElseThrow(NotFoundException::new);

    // existingReserva.setBedel(null);
    // existingReserva.setDiasReservados(null);
    // existingReserva.setPeriodo(null);
    // reservaRepository.save(existingReserva);

    // reservaRepository.deleteById(id);
    // }

}
