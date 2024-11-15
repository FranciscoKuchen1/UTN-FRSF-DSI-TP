package dsitp.backend.project.repos;

import dsitp.backend.project.domain.Bedel;
import dsitp.backend.project.model.TipoTurno;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BedelRepository extends JpaRepository<Bedel, Integer> {

    boolean existsById(Integer id);

    boolean existsByIdRegistroIgnoreCase(String idRegistro);

    // @Query("SELECT b FROM Bedel b WHERE b.eliminado = false")
    List<Bedel> findAll();

    // @Query("SELECT b FROM Bedel b WHERE b.apellido = :apellido AND b.eliminado = false")
    List<Bedel> findByApellido(String apellido);

    // @Query("SELECT b FROM Bedel b WHERE b.tipoTurno = :tipoTurno AND b.eliminado = false")
    List<Bedel> findByTipoTurno(TipoTurno tipoTurno);

    // @Query("SELECT b FROM Bedel b WHERE b.tipoTurno = :tipoTurno AND b.apellido = :apellido AND b.eliminado = false")
    List<Bedel> findByTipoTurnoAndApellido(TipoTurno tipoTurno, String apellido);

    Optional<Bedel> findByIdRegistro(String id);
}
