package dsitp.backend.project.repos;

import dsitp.backend.project.domain.AulaMultimedio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AulaMultimedioRepository extends JpaRepository<AulaMultimedio, Integer> {
}
