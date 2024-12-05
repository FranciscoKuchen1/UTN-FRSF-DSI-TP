package dsitp.backend.project.repos;

import dsitp.backend.project.domain.Bedel;
import dsitp.backend.project.model.TipoTurno;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BedelRepository extends JpaRepository<Bedel, Integer> {

    Boolean existsByIdAndEliminadoFalse(Integer id);

    Boolean existsByIdRegistroIgnoreCaseAndEliminadoFalse(String idRegistro);

    Boolean existsByContrasenaAndEliminadoFalse(String contrasena);

    List<Bedel> findByEliminadoFalse(Sort sort);

    List<Bedel> findByEliminadoFalse();

    List<Bedel> findByApellidoAndEliminadoFalse(String apellido);

    List<Bedel> findByTipoTurnoAndEliminadoFalse(TipoTurno tipoTurno);

    List<Bedel> findByTipoTurnoAndApellidoAndEliminadoFalse(TipoTurno tipoTurno, String apellido);

    Optional<Bedel> findByIdRegistroAndEliminadoFalse(String idRegistro);

}
