package dsitp.backend.project.service;

import dsitp.backend.project.domain.Bedel;
import dsitp.backend.project.domain.ReservaEsporadica;
import dsitp.backend.project.domain.ReservaPeriodica;
import dsitp.backend.project.model.BedelDTO;
import dsitp.backend.project.model.TipoTurno;
import dsitp.backend.project.repos.BedelRepository;
import dsitp.backend.project.repos.ReservaEsporadicaRepository;
import dsitp.backend.project.repos.ReservaPeriodicaRepository;
import dsitp.backend.project.util.BedelNotFoundException;
import dsitp.backend.project.util.NotFoundException;
import dsitp.backend.project.util.ReferencedWarning;
import static java.time.OffsetDateTime.now;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con los objetos {@link Bedel}.
 * Proporciona métodos para crear, actualizar, eliminar y consultar {@link Bedel} y sus relaciones.
 * Utiliza repositorios de {@link Bedel}, {@link ReservaEsporadica} y {@link ReservaPeriodica}
 * para realizar las operaciones de acceso a datos.
 */
@Service
@Transactional
public class BedelService {

    private final BedelRepository bedelRepository;
    private final ReservaEsporadicaRepository reservaEsporadicaRepository;
    private final ReservaPeriodicaRepository reservaPeriodicaRepository;
    private final Validator validator;

    /**
     * Constructor que inicializa el servicio con los repositorios necesarios y un validador.
     *
     * @param bedelRepository el repositorio de {@link Bedel}
     * @param reservaEsporadicaRepository el repositorio de {@link ReservaEsporadica}
     * @param reservaPeriodicaRepository el repositorio de {@link ReservaPeriodica}
     * @param validator el validador para las operaciones de validación
     */
    @Autowired
    public BedelService(final BedelRepository bedelRepository,
            final ReservaEsporadicaRepository reservaEsporadicaRepository,
            final ReservaPeriodicaRepository reservaPeriodicaRepository,
            final Validator validator) {
        this.bedelRepository = bedelRepository;
        this.reservaEsporadicaRepository = reservaEsporadicaRepository;
        this.reservaPeriodicaRepository = reservaPeriodicaRepository;
        this.validator = validator;
    }

    /**
     * Obtiene todos los {@link Bedel} activos ordenados por ID.
     *
     * @return una lista de {@link BedelDTO} que representa a todos los {@link Bedel} activos
     */
    public List<BedelDTO> findAll() {
        final List<Bedel> bedeles = bedelRepository.findByEliminadoFalse(Sort.by("id"));
        return bedeles.stream()
                .map(bedel -> toBedelDTO(bedel))
                .toList();
    }

    /**
     * Obtiene un {@link BedelDTO} por su ID.
     *
     * @param id el ID del {@link Bedel} a buscar
     * @return el {@link BedelDTO} correspondiente al {@link Bedel} encontrado
     * @throws NotFoundException si no se encuentra un {@link Bedel} con el ID proporcionado
     */
    public BedelDTO get(final Integer id) {
        return bedelRepository.findById(id)
                .map(bedel -> toBedelDTO(bedel))
                .orElseThrow(NotFoundException::new);
    }

    /**
     * Obtiene un {@link BedelDTO} por su ID de registro.
     *
     * @param idRegistro el ID de registro del {@link Bedel} a buscar
     * @return el {@link BedelDTO} correspondiente al {@link Bedel} encontrado
     * @throws NotFoundException si no se encuentra un {@link Bedel} con el ID de registro proporcionado
     */
    public BedelDTO getByIdRegistro(final String idRegistro) {
        return bedelRepository.findByIdRegistroAndEliminadoFalse(idRegistro)
                .map(bedel -> toBedelDTO(bedel))
                .orElseThrow(NotFoundException::new);
    }

    /**
     * Obtiene todos los {@link BedelDTO} que coinciden con el apellido proporcionado.
     *
     * @param apellido el apellido a buscar
     * @return una lista de {@link BedelDTO} que representan a los {@link Bedel} encontrados
     */
    public List<BedelDTO> getBedelesByApellido(final String apellido) {
        final List<Bedel> bedeles = bedelRepository.findByApellidoAndEliminadoFalse(apellido);
        return bedeles.stream()
                .map(bedel -> toBedelDTO(bedel))
                .toList();
    }

    /**
     * Obtiene todos los {@link BedelDTO} que coinciden con el tipo de turno proporcionado.
     *
     * @param tipoTurno el tipo de turno a buscar
     * @return una lista de {@link BedelDTO} que representan a los {@link Bedel} encontrados
     */
    public List<BedelDTO> getBedelesByTipoTurno(final Integer tipoTurno) {
        final List<Bedel> bedeles = bedelRepository.findByTipoTurnoAndEliminadoFalse(TipoTurno.fromInteger(tipoTurno));
        return bedeles.stream()
                .map(bedel -> toBedelDTO(bedel))
                .toList();
    }

    /**
     * Obtiene un {@link BedelDTO} por su ID de registro.
     *
     * @param idRegistro el ID de registro del {@link Bedel} a buscar
     * @return el {@link BedelDTO} correspondiente al {@link Bedel} encontrado
     * @throws NotFoundException si no se encuentra un {@link Bedel} con el ID de registro proporcionado
     */
    public BedelDTO getBedelByIdRegistro(final String idRegistro) {
        return bedelRepository.findByIdRegistroAndEliminadoFalse(idRegistro)
                .map(bedel -> toBedelDTO(bedel))
                .orElseThrow(NotFoundException::new);
    }

    /**
     * Realiza una búsqueda de {@link Bedel} filtrando por tipo de turno y/o apellido.
     *
     * @param tipoTurno el tipo de turno a buscar (opcional)
     * @param apellido el apellido a buscar (opcional)
     * @return una lista de {@link BedelDTO} que representan a los {@link Bedel} encontrados
     */
    public List<BedelDTO> findBedeles(Integer tipoTurno, String apellido) {
        if (tipoTurno != null && apellido != null) {
            return bedelRepository
                    .findByTipoTurnoAndApellidoAndEliminadoFalse(TipoTurno.fromInteger(tipoTurno), apellido).stream()
                    .map(bedel -> toBedelDTO(bedel))
                    .toList();
        } else if (tipoTurno != null) {
            return bedelRepository.findByTipoTurnoAndEliminadoFalse(TipoTurno.fromInteger(tipoTurno)).stream()
                    .map(bedel -> toBedelDTO(bedel))
                    .toList();
        } else if (apellido != null) {
            return bedelRepository.findByApellidoAndEliminadoFalse(apellido).stream()
                    .map(bedel -> toBedelDTO(bedel))
                    .toList();
        } else {
            return bedelRepository.findByEliminadoFalse().stream()
                    .map(bedel -> toBedelDTO(bedel))
                    .toList();
        }
    }

    /**
     * Crea un nuevo {@link Bedel} a partir de un {@link BedelDTO}.
     *
     * @param bedelDTO el DTO con los datos del nuevo {@link Bedel}
     * @return el ID del {@link Bedel} creado
     */
    public Integer create(final BedelDTO bedelDTO) {
        final Bedel bedel = toBedelEntity(bedelDTO);
        return bedelRepository.save(bedel).getId();
    }

    /**
     * Actualiza los datos de un {@link Bedel} existente.
     *
     * @param idRegistro el ID de registro del {@link Bedel} a actualizar
     * @param bedelDTO el DTO con los nuevos datos para el {@link Bedel}
     * @throws NotFoundException si no se encuentra un {@link Bedel} con el ID de registro proporcionado
     */
    public void update(final String idRegistro, final BedelDTO bedelDTO) {
        Bedel existingBedel = bedelRepository.findByIdRegistroAndEliminadoFalse(idRegistro)
                .orElseThrow(NotFoundException::new);

        existingBedel.setNombre(bedelDTO.getNombre());
        existingBedel.setApellido(bedelDTO.getApellido());
        existingBedel.setContrasena(bedelDTO.getContrasena());
        existingBedel.setLastUpdated(now());
        existingBedel.setTipoTurno(TipoTurno.fromInteger(bedelDTO.getTipoTurno()));

        bedelRepository.save(existingBedel);
    }

    /**
     * Elimina un {@link Bedel} de forma lógica, marcándolo como eliminado.
     *
     * @param idRegistro el ID de registro del {@link Bedel} a eliminar
     * @throws BedelNotFoundException si no se encuentra un {@link Bedel} con el ID de registro proporcionado
     */
    public void delete(final Integer id) {
        bedelRepository.deleteById(id);
    }

    public void deleteLogico(final String idRegistro) {
        Bedel bedel = bedelRepository.findByIdRegistroAndEliminadoFalse(idRegistro)
                .orElseThrow(BedelNotFoundException::new);
        List<ReservaEsporadica> reservasEsporadicas = reservaEsporadicaRepository.findByBedel(bedel);
        for (ReservaEsporadica reserva : reservasEsporadicas) {
            reserva.setBedel(null);
            reservaEsporadicaRepository.save(reserva);
        }

        List<ReservaPeriodica> reservasPeriodicas = reservaPeriodicaRepository.findByBedel(bedel);
        for (ReservaPeriodica reserva : reservasPeriodicas) {
            reserva.setBedel(null);
            reservaPeriodicaRepository.save(reserva);
        }

        bedel.setEliminado(true);
        bedelRepository.save(bedel);
    }

    /**
     * Verifica si un {@link Bedel} con el ID de registro proporcionado existe.
     *
     * @param idRegistro el ID de registro a verificar
     * @return {@code true} si existe un {@link Bedel} con el ID de registro proporcionado, de lo contrario {@code false}
     */
    public Boolean idRegistroExists(final String idRegistro) {
        return bedelRepository.existsByIdRegistroIgnoreCaseAndEliminadoFalse(idRegistro);
    }

    /**
     * Verifica si la contraseña proporcionada existe en el historial de contraseñas de los {@link Bedel}.
     *
     * @param contrasena la contraseña a verificar
     * @return {@code true} si la contraseña existe en el historial de contraseñas, de lo contrario {@code false}
     */
    public Boolean isPasswordInHistory(final String contrasena) {
        return bedelRepository.existsByContrasenaAndEliminadoFalse(contrasena);
    }

    /**
     * Obtiene una advertencia relacionada con un {@link Bedel} por su ID de registro si está asociado a una reserva.
     *
     * @param idRegistro el ID de registro del {@link Bedel}
     * @return una advertencia con los detalles si el {@link Bedel} está referenciado en alguna reserva, o {@code null} si no está referenciado
     * @throws NotFoundException si no se encuentra un {@link Bedel} con el ID de registro proporcionado
     */
    public ReferencedWarning getReferencedWarning(final String idRegistro) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Bedel bedel = bedelRepository.findByIdRegistroAndEliminadoFalse(idRegistro)
                .orElseThrow(NotFoundException::new);
        final ReservaEsporadica bedelReservaEsporadica = reservaEsporadicaRepository.findFirstByBedel(bedel);
        if (bedelReservaEsporadica != null) {
            referencedWarning.setKey("bedel.reserva.bedel.referenced");
            referencedWarning.addParam(bedelReservaEsporadica.getId());
            return referencedWarning;
        }
        final ReservaPeriodica bedelReservaPeriodica = reservaPeriodicaRepository.findFirstByBedel(bedel);
        if (bedelReservaPeriodica != null) {
            referencedWarning.setKey("bedel.reserva.bedel.referenced");
            referencedWarning.addParam(bedelReservaPeriodica.getId());
            return referencedWarning;
        }
        return null;
    }

    /**
     * Convierte un objeto {@link Bedel} a un {@link BedelDTO}.
     *
     * @param bedel el {@link Bedel} a convertir
     * @return el {@link BedelDTO} correspondiente
     */
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

    /**
     * Convierte un {@link BedelDTO} a un objeto {@link Bedel}.
     *
     * @param bedelDTO el {@link BedelDTO} a convertir
     * @return el objeto {@link Bedel} correspondiente
     */
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
