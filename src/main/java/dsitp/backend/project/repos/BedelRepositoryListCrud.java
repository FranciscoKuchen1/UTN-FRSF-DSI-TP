package dsitp.backend.project.repos;

import dsitp.backend.project.domain.Bedel;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BedelRepositoryListCrud extends ListCrudRepository<Bedel, Integer> {

}
