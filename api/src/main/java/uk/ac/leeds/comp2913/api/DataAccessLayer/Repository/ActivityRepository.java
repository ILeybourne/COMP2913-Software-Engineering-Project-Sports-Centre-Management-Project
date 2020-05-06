package uk.ac.leeds.comp2913.api.DataAccessLayer.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import uk.ac.leeds.comp2913.api.Domain.Model.Activity;

import java.util.Collection;
import java.util.List;


@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long>, CustomActivityRepository {
    @Query(value = "select a from Activity a inner join fetch a.activityType at", countQuery = "select count(a) from Activity a")
    Page<Activity> findAllWithPagination(Pageable page);

    Page<Activity> findByResourceId(Pageable pageable, Long resource_id);

    @Override
    void deleteById(Long aLong);

    @Query("select a from Activity a " +
            "inner join fetch a.resource r " +
            "inner join fetch a.activityType at")
    List<Activity> findAllWithResources();

    //Query used in the scheduler to automatically post activities that are a regular session and place bookings
    //locate last activity made with a regular session id (meaning its a regular session)
    @Query("select a from Activity a " +
            "where a.startTime = (SELECT MAX(aa.startTime)" +
            "from Activity aa where aa.regularSession.id = a.regularSession.id)")
    List<Activity> findAllWithRegularSession();

    //@Query("select a from Activity a " +
    //        "where a.startTime = (SELECT MAX(aa.startTime)" +
    //        "from Activity aa where aa.regularSession.id = a.regularSession.id and a.regularSession.id = :regular_session_id)")
    //Activity findLastScheduledRegularSessionById(Long regular_session_id);


}
