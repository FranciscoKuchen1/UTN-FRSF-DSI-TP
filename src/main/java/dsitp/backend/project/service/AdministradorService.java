package dsitp.backend.project.service;

import dsitp.backend.project.domain.Administrador;
import dsitp.backend.project.model.AdministradorDTO;
import dsitp.backend.project.repos.AdministradorRepository;
import dsitp.backend.project.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdministradorService {

    private final AdministradorRepository administradorRepository;

    public AdministradorService(final AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    public List<AdministradorDTO> findAll() {
        final List<Administrador> administradors = administradorRepository.findAll(Sort.by("id"));
        return administradors.stream()
                .map(administrador -> mapToDTO(administrador))
                .toList();
    }

    public AdministradorDTO get(final Integer id) {
        return administradorRepository.findById(id)
                .map(administrador -> mapToDTO(administrador))
                .orElseThrow(NotFoundException::new);
    }

    public AdministradorDTO getByIdRegistro(final String idRegistro) {
        return administradorRepository.findByIdRegistro(idRegistro)
                .map(administrador -> mapToDTO(administrador))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final AdministradorDTO administradorDTO) {
        final Administrador administrador = mapToEntity(administradorDTO);
        return administradorRepository.save(administrador).getId();
    }

    public void update(final Integer id, final AdministradorDTO administradorDTO) {
        final Administrador existingAdministrador = administradorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        Administrador updatedAdministrador = mapToEntity(administradorDTO);
        administradorRepository.save(updatedAdministrador);
    }

    public void delete(final Integer id) {
        administradorRepository.deleteById(id);
    }

    public Boolean idRegistroExists(final String idRegistro) {
        return administradorRepository.existsByIdRegistroIgnoreCase(idRegistro);
    }

    private AdministradorDTO mapToDTO(final Administrador administrador) {
        AdministradorDTO administradorDTO = new AdministradorDTO();
        administradorDTO.setIdRegistro(administrador.getIdRegistro());
        administradorDTO.setNombre(administrador.getNombre());
        administradorDTO.setApellido(administrador.getApellido());
        administradorDTO.setContrasena(administrador.getContrasena());
        return administradorDTO;
    }

    private Administrador mapToEntity(final AdministradorDTO administradorDTO) {
        Administrador administrador = new Administrador();
        administrador.setIdRegistro(administradorDTO.getIdRegistro());
        administrador.setNombre(administradorDTO.getNombre());
        administrador.setApellido(administradorDTO.getApellido());
        administrador.setContrasena(administradorDTO.getContrasena());
        return administrador;
    }

}
