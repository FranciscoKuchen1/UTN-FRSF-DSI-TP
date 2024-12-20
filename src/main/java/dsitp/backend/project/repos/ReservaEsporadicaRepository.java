package dsitp.backend.project.repos;

import dsitp.backend.project.domain.Bedel;
import dsitp.backend.project.domain.ReservaEsporadica;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaEsporadicaRepository extends JpaRepository<ReservaEsporadica, Integer> {

    ReservaEsporadica findFirstByBedel(Bedel bedel);

    @Query("SELECT r FROM ReservaEsporadica r JOIN r.diasReservados d "
            + "WHERE d.aula.numero = :numeroAula "
            + "AND d.fechaReserva BETWEEN :fechaInicio AND :fechaFin")
    List<ReservaEsporadica> findByAulaAndFechaInicioAndFechaFin(@Param("numeroAula") Integer numeroAula, @Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);

    List<ReservaEsporadica> findByBedel(Bedel bedel);

}
