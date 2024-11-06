package dsitp.backend.project.repos;

import dsitp.backend.project.domain.DiaReservado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaReservadoRepository extends JpaRepository<DiaReservado, Long> {
}
