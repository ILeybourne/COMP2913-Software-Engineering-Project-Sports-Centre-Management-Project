package uk.ac.leeds.comp2913.api.DataAccessLayer.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import uk.ac.leeds.comp2913.api.Domain.Model.ActivityType;

@Repository

public interface ActivityTypeRepository extends JpaRepository<ActivityType, Long> {
    Page<ActivityType> findByResourceId(Pageable pageable, Long resource_id);
}
