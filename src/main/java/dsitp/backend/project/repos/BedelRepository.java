package dsitp.backend.project.repos;

import dsitp.backend.project.domain.Bedel;
import dsitp.backend.project.model.TipoTurno;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BedelRepository extends JpaRepository<Bedel, Integer> {

    boolean existsById(Integer id);

    boolean existsByIdRegistroIgnoreCase(String idRegistro);

    List<Bedel> findByApellido(String apellido);

    List<Bedel> findByTipoTurno(TipoTurno tipoTurno);

    List<Bedel> findByTipoTurnoAndApellido(TipoTurno tipoTurno, String apellido);
}
