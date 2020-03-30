package uk.ac.leeds.comp2913.api.DataAccessLayer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;

import java.util.Collection;
import java.util.List;


@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long>, CustomActivityRepository {
  List<Activity> findByResourceId(Long resource_id);

  @Override
  void deleteById(Long aLong);

  @Query("select a from Activity a inner join fetch a.resource r")
  Collection<Activity> findAllWithResources();

 // @Query("select a.regularSession.id as regular_session_id, MAX(a.startTime) from Activity a " +
 //     //"         LEFT OUTER JOIN RegularSession on Activity.regularSession.id = RegularSession.id " +
 //     "group by a.regularSession")
 // List<Activity> findAllWithRegularSession();

  @Query("select a from Activity a " +
      "where a.startTime = (SELECT MAX(aa.startTime)" +
      "from Activity aa where aa.regularSession.id = a.regularSession.id)")
  List<Activity> findAllWithRegularSession();




}
