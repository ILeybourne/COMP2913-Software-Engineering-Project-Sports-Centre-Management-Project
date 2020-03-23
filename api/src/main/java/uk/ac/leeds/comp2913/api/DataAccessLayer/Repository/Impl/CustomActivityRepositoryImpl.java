package uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.Impl;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.CustomActivityRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.ViewModel.ActivityDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;

public class CustomActivityRepositoryImpl implements CustomActivityRepository {
  @PersistenceContext
  EntityManager em;

  public ActivityDTO calculateCurrentCapacity(Long activityId) {
    return em.createNativeQuery(
        "SELECT activity.id, " +
        "       activity.created_at, " +
        "       end_time, " +
        "       start_time," +
        "       activity.updated_at, " +
        "       resource_id, " +
        "       name, " +
        "       current_capacity, " +
        "       activity_type_id, " +
        "       cost, " +
        "       social, " +
        "       SUM(booking.participants) as current_capacity " +
        "FROM activity " +
        "         LEFT OUTER JOIN booking on activity.id = booking.activity_id " +
        "WHERE activity.id = :activityId " +
        "GROUP BY activity.id", ActivityDTO.class)
        .setParameter("activityId", activityId)
        .getSingleResult();
  }
}
