package uk.ac.leeds.comp2913.api.DataAccessLayer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.leeds.comp2913.api.Domain.Model.Resource;

import java.util.Optional;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long>, CustomResourceRepository {
    @Override
    Optional<Resource> findById(Long aLong);

    @Override
    <S extends Resource> S save(S s);

    @Override
    void delete(Resource resource);
}
