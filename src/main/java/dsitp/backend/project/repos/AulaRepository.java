package dsitp.backend.project.repos;

import dsitp.backend.project.domain.Aula;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AulaRepository extends JpaRepository<Aula, Integer> {

    @Query(value = "SELECT * FROM aula "
            + "WHERE numero = :numero "
            + "AND tipo_aula = :tipoAula "
            + "AND capacidad >= :capacidad",
            nativeQuery = true)
    List<Aula> findByNumeroAndTipoAulaAndCapacidad(@Param("numero") Integer numero, @Param("tipoAula") Integer tipoAula, @Param("capacidad") Integer capacidad);

    @Query(value = "SELECT * FROM aula "
            + "WHERE tipo_aula = :tipoAula "
            + "AND capacidad >= :capacidad",
            nativeQuery = true)
    List<Aula> findByTipoAulaAndCapacidad(@Param("tipoAula") Integer tipoAula, @Param("capacidad") Integer capacidad);

    @Query(value = "SELECT * FROM aula "
            + "WHERE tipo_aula = :tipoAula",
            nativeQuery = true)
    List<Aula> findByTipoAula(@Param("tipoAula") Integer tipoAula);

    List<Aula> findByCapacidad(Integer capacidad);
}
