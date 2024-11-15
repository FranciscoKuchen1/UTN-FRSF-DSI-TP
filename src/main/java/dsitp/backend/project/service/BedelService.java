package dsitp.backend.project.service;

import dsitp.backend.project.domain.Bedel;
import dsitp.backend.project.domain.ReservaEsporadica;
import dsitp.backend.project.domain.ReservaPeriodica;
import dsitp.backend.project.model.BedelDTO;
import dsitp.backend.project.model.TipoTurno;
import dsitp.backend.project.repos.BedelRepository;
import dsitp.backend.project.repos.ReservaEsporadicaRepository;
import dsitp.backend.project.repos.ReservaPeriodicaRepository;
import dsitp.backend.project.util.NotFoundException;
import dsitp.backend.project.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BedelService {

    private final BedelRepository bedelRepository;
    private final ReservaEsporadicaRepository reservaEsporadicaRepository;
    private final ReservaPeriodicaRepository reservaPeriodicaRepository;

    public BedelService(final BedelRepository bedelRepository,
            final ReservaEsporadicaRepository reservaEsporadicaRepository,
            final ReservaPeriodicaRepository reservaPeriodicaRepository) {
        this.bedelRepository = bedelRepository;
        this.reservaEsporadicaRepository = reservaEsporadicaRepository;
        this.reservaPeriodicaRepository = reservaPeriodicaRepository;
    }

    public List<BedelDTO> findAll() {
        final List<Bedel> bedeles = bedelRepository.findAll(Sort.by("id"));
        return bedeles.stream()
                .map(bedel -> mapToDTO(bedel, new BedelDTO()))
                .toList();
    }

    public BedelDTO get(final Integer id) {
        return bedelRepository.findById(id)
                .map(bedel -> mapToDTO(bedel, new BedelDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public List<BedelDTO> getBedelesByApellido(String apellido) {
        final List<Bedel> bedeles = bedelRepository.findByApellido(apellido);
        return bedeles.stream()
                .map(bedel -> mapToDTO(bedel, new BedelDTO()))
                .toList();
    }

    public List<BedelDTO> getBedelesByTipoTurno(TipoTurno tipoTurno) {
        final List<Bedel> bedeles = bedelRepository.findByTipoTurno(tipoTurno);
        return bedeles.stream()
                .map(bedel -> mapToDTO(bedel, new BedelDTO()))
                .toList();
    }

    public List<Bedel> findBedeles(TipoTurno tipoTurno, String apellido) {
        if (tipoTurno != null && apellido != null) {
            return bedelRepository.findByTipoTurnoAndApellido(tipoTurno, apellido);
        } else if (tipoTurno != null) {
            return bedelRepository.findByTipoTurno(tipoTurno);
        } else if (apellido != null) {
            return bedelRepository.findByApellido(apellido);
        } else {
            return bedelRepository.findAll();
        }
    }

    public Integer create(final BedelDTO bedelDTO) {
        final Bedel bedel = new Bedel();
        mapToEntity(bedelDTO, bedel);
        return bedelRepository.save(bedel).getId();
    }

    public void update(final Integer id, final BedelDTO bedelDTO) {
        final Bedel bedel = bedelRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(bedelDTO, bedel);
        bedelRepository.save(bedel);
    }

    public void delete(final Integer id) {
        bedelRepository.deleteById(id);
    }

    public void deleteLogico(final String idRegistro) {
        Bedel bedel = bedelRepository.findByIdRegistro(idRegistro)
                .orElseThrow(NotFoundException::new);
        bedel.setEliminado(true);
        bedelRepository.save(bedel);
    }

    public TipoTurno mapIntegerToTipoTurno(Integer tipoTurno) {

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

    public Integer mapTipoTurnoToInteger(TipoTurno tipoTurno) {

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

    private BedelDTO mapToDTO(final Bedel bedel, final BedelDTO bedelDTO) {
        bedelDTO.setIdRegistro(bedel.getIdRegistro());
        bedelDTO.setNombre(bedel.getNombre());
        bedelDTO.setApellido(bedel.getApellido());
        bedelDTO.setContrasena(bedel.getContrasena());
        bedelDTO.setIdRegistro(bedel.getIdRegistro());
        bedelDTO.setTipoTurno(mapTipoTurnoToInteger(bedel.getTipoTurno()));
        return bedelDTO;
    }

    private Bedel mapToEntity(final BedelDTO bedelDTO, final Bedel bedel) {
        bedel.setNombre(bedelDTO.getNombre());
        bedel.setApellido(bedelDTO.getApellido());
        bedel.setContrasena(bedelDTO.getContrasena());
        bedel.setIdRegistro(bedelDTO.getIdRegistro());
        bedel.setEliminado(false);
        bedel.setTipoTurno(mapIntegerToTipoTurno(bedelDTO.getTipoTurno()));
        return bedel;
    }

    public boolean idRegistroExists(final String idRegistro) {
        return bedelRepository.existsByIdRegistroIgnoreCase(idRegistro);
    }

    public ReferencedWarning getReferencedWarning(final Integer id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Bedel bedel = bedelRepository.findById(id)
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

    public ReferencedWarning getReferencedWarning(final String idRegistro) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Bedel bedel = bedelRepository.findByIdRegistro(idRegistro)
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

}
