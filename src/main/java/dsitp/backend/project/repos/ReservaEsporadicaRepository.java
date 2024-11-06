package dsitp.backend.project.repos;

import dsitp.backend.project.domain.Bedel;
import dsitp.backend.project.domain.ReservaEsporadica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaEsporadicaRepository extends JpaRepository<ReservaEsporadica, Integer> {

    ReservaEsporadica findFirstByBedel(Bedel bedel);

}
