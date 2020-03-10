package uk.ac.leeds.comp2913.api.DataAccessLayer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import java.util.List;


@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
  List<Activity> findByResourceId(Long resource_id);

  @Override
  void deleteById(Long aLong);
}
