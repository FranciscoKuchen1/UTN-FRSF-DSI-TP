package dsitp.backend.project.repos;

import dsitp.backend.project.domain.Administrador;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {

    Optional<Administrador> findByIdRegistro(String idRegistro);

    Boolean existsByIdRegistroIgnoreCase(String idRegistro);
}
