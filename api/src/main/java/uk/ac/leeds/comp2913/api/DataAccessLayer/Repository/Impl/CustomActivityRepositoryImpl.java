package uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.Impl;

import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.data.rest.core.mapping.ResourceType;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.CustomActivityRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.Resource;
import uk.ac.leeds.comp2913.api.ViewModel.ActivityDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.swing.text.html.Option;
import java.util.Optional;

public class CustomActivityRepositoryImpl implements CustomActivityRepository {
  @PersistenceContext
  EntityManager em;

  public ActivityDTO calculateCurrentCapacity(Long activityId) {
    Query query = em.createNativeQuery("SELECT activity.id, " +
        "       activity.end_time, " +
        "       start_time," +
        "       activity.created_at, " +
        "       activity.updated_at," +
        "       activity.name, " +
        "       activity_type_id as activityType, " +
        "       cost, " +
        "       social, " +
        "       resource_id as resource, " +
        "       SUM(booking.participants) as currentCapacity " +
        "FROM sports_centre_management.activity " +
        "         LEFT OUTER JOIN sports_centre_management.booking on activity.id = booking.activity_id " +
        "WHERE activity.id = :activityId " +
        "GROUP BY activity.id")
        .setParameter("activityId", activityId);

    query.unwrap(org.hibernate.query.NativeQuery.class)
        .addScalar("id", StandardBasicTypes.LONG)
        .addScalar("name", StandardBasicTypes.STRING)
        .addScalar("currentCapacity", StandardBasicTypes.INTEGER)
        //.addScalar("resource")
        //.addScalar("activity_type_id", StandardBasicTypes.LONG)
        .addScalar("cost", StandardBasicTypes.BIG_DECIMAL)
        .addScalar("social", StandardBasicTypes.BOOLEAN)
        .addScalar("end_time", StandardBasicTypes.DATE)
        .addScalar("start_time", StandardBasicTypes.DATE)
        .addScalar("created_at", StandardBasicTypes.DATE)
        .addScalar("updated_at", StandardBasicTypes.DATE)
        .setResultTransformer(Transformers.aliasToBean(ActivityDTO.class));

    return (ActivityDTO) query.getSingleResult();

    //Things to note- Scalar required to change value to correct type in DTO
    //DTO setters and getters must match the column names in native queries, getStartTime changed to getStart_time etc
    //Query must include all columns otherwise it returns mismatch of result set

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
    //     .getSingleResult();
  }
}
