package dsitp.backend.project.repos;

import dsitp.backend.project.domain.Bedel;
import dsitp.backend.project.domain.Periodo;
import dsitp.backend.project.domain.ReservaPeriodica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaPeriodicaRepository extends JpaRepository<ReservaPeriodica, Integer> {

    ReservaPeriodica findFirstByPeriodo(Periodo periodo);

    ReservaPeriodica findFirstByBedel(Bedel bedel);

}
