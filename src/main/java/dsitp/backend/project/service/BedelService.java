package dsitp.backend.project.service;

import dsitp.backend.project.domain.Bedel;
import dsitp.backend.project.domain.ReservaEsporadica;
import dsitp.backend.project.domain.ReservaPeriodica;
import dsitp.backend.project.mapper.BedelMapper;
import dsitp.backend.project.model.BedelDTO;
import dsitp.backend.project.model.TipoTurno;
import dsitp.backend.project.repos.BedelRepository;
import dsitp.backend.project.repos.ReservaEsporadicaRepository;
import dsitp.backend.project.repos.ReservaPeriodicaRepository;
import dsitp.backend.project.util.BedelNotFoundException;
import dsitp.backend.project.util.NotFoundException;
import dsitp.backend.project.util.ReferencedWarning;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BedelService {

    private final BedelRepository bedelRepository;
    private final ReservaEsporadicaRepository reservaEsporadicaRepository;
    private final ReservaPeriodicaRepository reservaPeriodicaRepository;
    private final BedelMapper bedelMapper;

    @Autowired
    public BedelService(final BedelRepository bedelRepository, final ReservaEsporadicaRepository reservaEsporadicaRepository, final ReservaPeriodicaRepository reservaPeriodicaRepository, final BedelMapper bedelMapper) {
        this.bedelRepository = bedelRepository;
        this.reservaEsporadicaRepository = reservaEsporadicaRepository;
        this.reservaPeriodicaRepository = reservaPeriodicaRepository;
        this.bedelMapper = bedelMapper;
    }

    public List<BedelDTO> findAll() {
        final List<Bedel> bedeles = bedelRepository.findAll(Sort.by("id"));
        return bedeles.stream()
                .map(bedel -> bedelMapper.toBedelDTO(bedel))
                .toList();
    }

    public BedelDTO get(final Integer id) {
        return bedelRepository.findById(id)
                .map(bedel -> bedelMapper.toBedelDTO(bedel))
                .orElseThrow(NotFoundException::new);
    }

    public List<BedelDTO> getBedelesByApellido(final String apellido) {
        final List<Bedel> bedeles = bedelRepository.findByApellido(apellido);
        return bedeles.stream()
                .map(bedel -> bedelMapper.toBedelDTO(bedel))
                .toList();
    }

    public List<BedelDTO> getBedelesByTipoTurno(final Integer tipoTurno) {
        final List<Bedel> bedeles = bedelRepository.findByTipoTurno(TipoTurno.fromInteger(tipoTurno));
        return bedeles.stream()
                .map(bedel -> bedelMapper.toBedelDTO(bedel))
                .toList();
    }

    public BedelDTO getBedelByIdRegistro(final String idRegistro) {
        return bedelRepository
                .findByIdRegistro(idRegistro)
                .map(bedel -> bedelMapper.toBedelDTO(bedel))
                .orElseThrow(NotFoundException::new);
    }

    public List<BedelDTO> findBedeles(Integer tipoTurno, String apellido) {
        if (tipoTurno != null && apellido != null) {
            return bedelRepository.findByTipoTurnoAndApellido(TipoTurno.fromInteger(tipoTurno), apellido).stream()
                    .map(bedel -> bedelMapper.toBedelDTO(bedel))
                    .toList();
        } else if (tipoTurno != null) {
            return bedelRepository.findByTipoTurno(TipoTurno.fromInteger(tipoTurno)).stream()
                    .map(bedel -> bedelMapper.toBedelDTO(bedel))
                    .toList();
        } else if (apellido != null) {
            return bedelRepository.findByApellido(apellido).stream()
                    .map(bedel -> bedelMapper.toBedelDTO(bedel))
                    .toList();
        } else {
            return bedelRepository.findAll().stream()
                    .map(bedel -> bedelMapper.toBedelDTO(bedel))
                    .toList();
        }
    }

    public Integer create(final BedelDTO bedelDTO) {
        final Bedel bedel = bedelMapper.toBedelEntity(bedelDTO);
        return bedelRepository.save(bedel).getId();
    }

    public void update(final Integer id, final BedelDTO bedelDTO) {
        Bedel existingBedel = bedelRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        Bedel updatedBedel = bedelMapper.toBedelEntity(bedelDTO);
        BeanUtils.copyProperties(updatedBedel, existingBedel, "id", "idRegistro", "reservas");
        bedelRepository.save(existingBedel);
    }

    public void delete(final Integer id) {
        bedelRepository.deleteById(id);
    }

    public void deleteLogico(final String idRegistro) {
        Bedel bedel = bedelRepository.findByIdRegistro(idRegistro)
                .orElseThrow(BedelNotFoundException::new);
        bedel.setEliminado(true);
        bedelRepository.save(bedel);
    }

//    private BedelDTO mapToDTO(final Bedel bedel, final BedelDTO bedelDTO) {
//        bedelDTO.setIdRegistro(bedel.getIdRegistro());
//        bedelDTO.setNombre(bedel.getNombre());
//        bedelDTO.setApellido(bedel.getApellido());
//        bedelDTO.setContrasena(bedel.getContrasena());
//        bedelDTO.setIdRegistro(bedel.getIdRegistro());
//        bedelDTO.setTipoTurno(mapTipoTurnoToInteger(bedel.getTipoTurno()));
//        return bedelDTO;
//    }
//
//    private Bedel mapToEntity(final BedelDTO bedelDTO, final Bedel bedel) {
//        bedel.setNombre(bedelDTO.getNombre());
//        bedel.setApellido(bedelDTO.getApellido());
//        bedel.setContrasena(bedelDTO.getContrasena());
//        bedel.setIdRegistro(bedelDTO.getIdRegistro());
//        bedel.setEliminado(false);
//        bedel.setTipoTurno(mapIntegerToTipoTurno(bedelDTO.getTipoTurno()));
//        return bedel;
//    }
//
//    private BedelDTO mapToDTOModelMapper(final Bedel bedel) {
//        modelMapper.typeMap(Bedel.class, BedelDTO.class).addMappings(mapper
//                -> mapper.map(src -> src.getTipoTurno().toString(), BedelDTO::setTipoTurno)
//        );
//        return modelMapper.map(bedel, BedelDTO.class);
//    }
//
//    private Bedel mapToEntityModelMapper(final BedelDTO bedelDTO) {
//        return modelMapper.map(bedelDTO, Bedel.class);
//    }
    public boolean idRegistroExists(final String idRegistro) {
        return bedelRepository.existsByIdRegistroIgnoreCase(idRegistro);
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
