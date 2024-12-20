package dsitp.backend.project.repos;

import dsitp.backend.project.domain.Periodo;
import dsitp.backend.project.model.TipoPeriodo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PeriodoRepository extends JpaRepository<Periodo, Integer> {

    @Query("SELECT p FROM Periodo p "
            + "WHERE p.tipoPeriodo = :tipoPeriodo "
            + "AND p.fechaFin >= CURRENT_TIMESTAMP "
            + "ORDER BY YEAR(p.fechaFin) ASC")
    List<Periodo> findActivePeriodosByTipoAndYear(@Param("tipoPeriodo") TipoPeriodo tipoPeriodo);

}
