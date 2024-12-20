package dsitp.backend.project.repos;

import dsitp.backend.project.domain.DiaReservado;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaReservadoRepository extends JpaRepository<DiaReservado, Integer> {

        @Query(value = "SELECT * FROM dia_reservado dr "
                        + "WHERE dr.id_aula = :idAula "
                        + "AND dr.fecha_reserva = :fechaReserva "
                        + "AND (:horaInicio < dr.hora_inicio + make_interval(secs => dr.duracion * 60) OR dr.hora_inicio < :horaFin)", nativeQuery = true)
        List<DiaReservado> findOverlappingDays(
                        @Param("idAula") Integer idAula,
                        @Param("fechaReserva") LocalDate fechaReserva,
                        @Param("horaInicio") LocalTime horaInicio,
                        @Param("horaFin") LocalTime horaFin);

        @Query(value = "SELECT * FROM dia_reservado dr "
                        + "WHERE dr.id_aula = :idAula "
                        + "AND dr.fecha_reserva = :fechaReserva "
                        + "AND (:horaInicio < dr.hora_inicio + make_interval(secs => dr.duracion * 60) OR dr.hora_inicio < :horaFin) "
                        + "AND NOT (dr.hora_inicio <= :horaInicio AND :horaFin <= dr.hora_inicio + make_interval(secs => dr.duracion * 60))", nativeQuery = true)
        List<DiaReservado> findPartiallyOverlappingDays(
                        @Param("idAula") Integer idAula,
                        @Param("fechaReserva") LocalDate fechaReserva,
                        @Param("horaInicio") LocalTime horaInicio,
                        @Param("horaFin") LocalTime horaFin);
}
