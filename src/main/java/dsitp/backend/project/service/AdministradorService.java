package dsitp.backend.project.service;

import dsitp.backend.project.domain.Administrador;
import dsitp.backend.project.model.AdministradorDTO;
import dsitp.backend.project.repos.AdministradorRepository;
import dsitp.backend.project.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AdministradorService {

    private final AdministradorRepository administradorRepository;

    public AdministradorService(final AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    public List<AdministradorDTO> findAll() {
        final List<Administrador> administradors = administradorRepository.findAll(Sort.by("id"));
        return administradors.stream()
                .map(administrador -> mapToDTO(administrador, new AdministradorDTO()))
                .toList();
    }

    public AdministradorDTO get(final Integer id) {
        return administradorRepository.findById(id)
                .map(administrador -> mapToDTO(administrador, new AdministradorDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final AdministradorDTO administradorDTO) {
        final Administrador administrador = new Administrador();
        mapToEntity(administradorDTO, administrador);
        return administradorRepository.save(administrador).getId();
    }

    public void update(final Integer id, final AdministradorDTO administradorDTO) {
        final Administrador administrador = administradorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(administradorDTO, administrador);
        administradorRepository.save(administrador);
    }

    public void delete(final Integer id) {
        administradorRepository.deleteById(id);
    }

    private AdministradorDTO mapToDTO(final Administrador administrador,
            final AdministradorDTO administradorDTO) {
        administradorDTO.setId(administrador.getId());
        administradorDTO.setNombre(administrador.getNombre());
        administradorDTO.setApellido(administrador.getApellido());
        administradorDTO.setContrasena(administrador.getContrasena());
        return administradorDTO;
    }

    private Administrador mapToEntity(final AdministradorDTO administradorDTO,
            final Administrador administrador) {
        administrador.setNombre(administradorDTO.getNombre());
        administrador.setApellido(administradorDTO.getApellido());
        administrador.setContrasena(administradorDTO.getContrasena());
        return administrador;
    }

}
