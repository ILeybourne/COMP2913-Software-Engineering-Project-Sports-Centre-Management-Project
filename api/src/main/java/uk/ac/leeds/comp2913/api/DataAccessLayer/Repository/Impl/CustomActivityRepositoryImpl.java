package uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.Impl;

import org.hibernate.transform.Transformers;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.CustomActivityRepository;
import uk.ac.leeds.comp2913.api.ViewModel.ActivityDTO;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.text.html.Option;
import java.util.Optional;

public class CustomActivityRepositoryImpl implements CustomActivityRepository {
//  @PersistenceContext
  EntityManager em;

  public ActivityDTO calculateCurrentCapacity(Long activityId) {
    Query query = em.createNativeQuery("SELECT activity.id, " +
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
        "GROUP BY activity.id");
    query.setParameter("activityId", activityId);
    ActivityDTO activityDTO = (ActivityDTO) query.getSingleResult();
    return activityDTO;






 // return em.createNativeQuery(
 //     "SELECT activity.id, " +
 //     "       activity.created_at, " +
 //     "       end_time, " +
 //     "       start_time," +
 //     "       activity.updated_at, " +
 //     "       resource_id, " +
 //     "       name, " +
 //     "       current_capacity, " +
 //     "       activity_type_id, " +
 //     "       cost, " +
 //     "       social, " +
 //     "       SUM(booking.participants) as current_capacity " +
 //     "FROM activity " +
 //     "         LEFT OUTER JOIN booking on activity.id = booking.activity_id " +
 //     "WHERE activity.id = :activityId " +
 //     "GROUP BY activity.id", ActivityDTO.class)
 //     .setParameter("activityId", activityId)
 //     .unwrap(org.hibernate.query.NativeQuery.class)
 //     .setResultTransformer(Transformers.aliasToBean(ActivityDTO.class))
 //     .getSingleResult();
  }
}
