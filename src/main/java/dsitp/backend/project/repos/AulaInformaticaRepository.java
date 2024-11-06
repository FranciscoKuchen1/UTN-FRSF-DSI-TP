package dsitp.backend.project.repos;

import dsitp.backend.project.domain.AulaInformatica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AulaInformaticaRepository extends JpaRepository<AulaInformatica, Integer> {
}
