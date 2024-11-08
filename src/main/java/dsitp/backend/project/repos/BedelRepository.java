package dsitp.backend.project.repos;

import dsitp.backend.project.domain.Bedel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BedelRepository extends JpaRepository<Bedel, Integer> {

    boolean existsById(Integer id);

    boolean existsByIdRegistroIgnoreCase(String idRegistro);
}
