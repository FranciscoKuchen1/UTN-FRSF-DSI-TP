package dsitp.backend.project.service;

import dsitp.backend.project.domain.Administrador;
import dsitp.backend.project.domain.Bedel;
import dsitp.backend.project.repos.AdministradorRepository;
import dsitp.backend.project.repos.BedelRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsuarioService {

    private final BedelRepository bedelRepository;
    private final AdministradorRepository administradorRepository;

    @Autowired
    public UsuarioService(final BedelRepository bedelRepository, final AdministradorRepository administradorRepository) {
        this.bedelRepository = bedelRepository;
        this.administradorRepository = administradorRepository;
    }

    public Boolean autenticarUsuario(final String idRegistro, final String contrasena) {
        final Optional<Bedel> bedel = bedelRepository.findByIdRegistroAndEliminadoFalse(idRegistro);
        final Optional<Administrador> administrador = administradorRepository.findByIdRegistro(idRegistro);

        return true;
    }
}
