package dsitp.backend.project.repos;

import dsitp.backend.project.domain.AulaSinRecursosAdic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AulaSinRecursosAdicRepository extends JpaRepository<AulaSinRecursosAdic, Integer> {
}
