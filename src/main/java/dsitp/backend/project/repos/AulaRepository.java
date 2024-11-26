package dsitp.backend.project.repos;

import dsitp.backend.project.domain.Aula;
import dsitp.backend.project.model.TipoAula;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AulaRepository extends JpaRepository<Aula, Integer> {

    List<Aula> findByNumeroAndTipoAulaAndCapacidad(Integer numero, TipoAula toTipoAula, Integer capacidad);

    List<Aula> findByTipoAula(Integer tipoAula);

    List<Aula> findByCapacidad(Integer capacidad);
}
