package dsitp.backend.project.repos;

import dsitp.backend.project.domain.Aula;
import dsitp.backend.project.domain.Bedel;
import dsitp.backend.project.domain.DiaReservado;
import dsitp.backend.project.domain.Periodo;
import dsitp.backend.project.domain.Reserva;
import dsitp.backend.project.domain.ReservaEsporadica;
import dsitp.backend.project.domain.ReservaPeriodica;
import dsitp.backend.project.model.TipoAula;
import jakarta.persistence.Tuple;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

        Optional<Reserva> findById(Integer id);

        Reserva findFirstByBedel(Bedel bedel);

        List<Reserva> findByBedel(Bedel bedel);

        @Query("SELECT r FROM ReservaEsporadica r " +
                        "JOIN r.diasReservados dr " +
                        "WHERE dr.aula.id = :idAula " +
                        "AND dr.fechaReserva BETWEEN :fechaInicio AND :fechaFin")
        List<Reserva> findByAulaAndFechaInicioAndFechaFinEsporadica(
                        @Param("idAula") Integer idAula,
                        @Param("fechaInicio") LocalDate fechaInicio,
                        @Param("fechaFin") LocalDate fechaFin);

        @Query("SELECT r FROM ReservaPeriodica r " +
                        "JOIN r.diasReservados dr " +
                        "WHERE dr.aula.id = :idAula " +
                        "AND dr.fechaReserva BETWEEN :fechaInicio AND :fechaFin")
        List<Reserva> findByAulaAndFechaInicioAndFechaFinPeriodica(
                        @Param("idAula") Integer idAula,
                        @Param("fechaInicio") LocalDate fechaInicio,
                        @Param("fechaFin") LocalDate fechaFin);

        @Query(value = "SELECT CASE WHEN COUNT(r) > 0 THEN FALSE ELSE TRUE END " +
                        "FROM reserva_esporadica r " +
                        "JOIN dia_reservado dr ON dr.id_reserva = r.id " +
                        "WHERE dr.id_aula = :idAula " +
                        "AND dr.fecha_reserva = :fecha " +
                        "AND (:horaInicio < dr.hora_inicio + make_interval(secs => dr.duracion * 60) OR dr.hora_inicio < :horaFin)", nativeQuery = true)
        Boolean obtenerDisponibilidadAulaEsporadica(
                        @Param("idAula") Integer idAula,
                        @Param("fecha") LocalDate fecha,
                        @Param("horaInicio") LocalTime horaInicio,
                        @Param("horaFin") LocalTime horaFin);

        @Query(value = "SELECT CASE WHEN COUNT(r) > 0 THEN FALSE ELSE TRUE END " +
                        "FROM reserva_periodica r " +
                        "JOIN dia_reservado dr ON dr.id_reserva = r.id " +
                        "WHERE dr.id_aula = :idAula " +
                        "AND dr.fecha_reserva = :fecha " +
                        "AND (:horaInicio < dr.hora_inicio + make_interval(secs => dr.duracion * 60) OR dr.hora_inicio < :horaFin)", nativeQuery = true)
        Boolean obtenerDisponibilidadAulaPeriodica(
                        @Param("idAula") Integer idAula,
                        @Param("fecha") LocalDate fecha,
                        @Param("horaInicio") LocalTime horaInicio,
                        @Param("horaFin") LocalTime horaFin);

        @Query(value = "SELECT r.id AS id, " +
                        "LEAST(EXTRACT(EPOCH FROM (:horaFin - dr.hora_inicio)), " +
                        "EXTRACT(EPOCH FROM (dr.hora_inicio + make_interval(secs => dr.duracion * 60) - :horaInicio)))::int AS superposicion "
                        +
                        "FROM reserva_esporadica r " +
                        "JOIN dia_reservado dr ON dr.id_reserva = r.id " +
                        "WHERE dr.id_aula = :idAula " +
                        "AND dr.fecha_reserva = :fecha " +
                        "AND ((:horaInicio < dr.hora_inicio + make_interval(secs => dr.duracion * 60)) " +
                        "AND (:horaFin > dr.hora_inicio)) " +
                        "ORDER BY superposicion DESC " +
                        "LIMIT 1", nativeQuery = true)
        Tuple obtenerReservaEsporadicaQueSuperpone(
                        @Param("idAula") Integer idAula,
                        @Param("fecha") LocalDate fecha,
                        @Param("horaInicio") LocalTime horaInicio,
                        @Param("horaFin") LocalTime horaFin);

        @Query(value = "SELECT r.id AS id, " +
                        "LEAST(EXTRACT(EPOCH FROM (:horaFin - dr.hora_inicio)), " +
                        "EXTRACT(EPOCH FROM (dr.hora_inicio + make_interval(secs => dr.duracion * 60) - :horaInicio)))::int AS superposicion "
                        +
                        "FROM reserva_periodica r " +
                        "JOIN dia_reservado dr ON dr.id_reserva = r.id " +
                        "WHERE dr.id_aula = :idAula " +
                        "AND dr.fecha_reserva = :fecha " +
                        "AND ((:horaInicio < dr.hora_inicio + make_interval(secs => dr.duracion * 60)) " +
                        "AND (:horaFin > dr.hora_inicio)) " +
                        "ORDER BY superposicion DESC " +
                        "LIMIT 1", nativeQuery = true)
        Tuple obtenerReservaPeriodicaQueSuperpone(
                        @Param("idAula") Integer idAula,
                        @Param("fecha") LocalDate fecha,
                        @Param("horaInicio") LocalTime horaInicio,
                        @Param("horaFin") LocalTime horaFin);

}
