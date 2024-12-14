package dsitp.backend.project.repos;

import dsitp.backend.project.domain.Bedel;
import dsitp.backend.project.domain.Periodo;
import dsitp.backend.project.domain.ReservaPeriodica;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaPeriodicaRepository extends JpaRepository<ReservaPeriodica, Integer> {

    ReservaPeriodica findFirstByPeriodo(Periodo periodo);

    ReservaPeriodica findFirstByBedel(Bedel bedel);

    @Query("SELECT r FROM ReservaPeriodica r JOIN r.diasReservados d "
            + "WHERE d.aula.numero = :numeroAula "
            + "AND d.fechaReserva BETWEEN :fechaInicio AND :fechaFin")
    List<ReservaPeriodica> findByAulaAndFechaInicioAndFechaFin(@Param("numeroAula") Integer numeroAula, @Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);

    List<ReservaPeriodica> findByBedel(Bedel bedel);

}
