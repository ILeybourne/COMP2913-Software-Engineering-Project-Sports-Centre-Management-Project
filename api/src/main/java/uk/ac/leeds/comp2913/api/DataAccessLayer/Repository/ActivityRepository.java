package uk.ac.leeds.comp2913.api.DataAccessLayer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long>, CustomActivityRepository {
  List<Activity> findByResourceId(Long resource_id);

  @Override
  void deleteById(Long aLong);

  @Query("select a from Activity a inner join fetch a.resource r")
  Collection<Activity> findAllWithResources();

  @Query(value = "select sum(b.participants) as currentCapacity from Booking b where Activity.id = :activityId", nativeQuery = true)
  Optional<Integer> calculateCurrentCapacity(Long activityId);
}
