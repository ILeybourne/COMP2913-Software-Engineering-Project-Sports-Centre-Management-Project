package uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.Impl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.CustomResourceRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Model.Resource;

public class CustomResourceRepositoryImpl implements CustomResourceRepository {
  @PersistenceContext
  EntityManager em;
//
//  @Override
//  public Collection<Booking> findAllWithBookings(Long resourceId) {
//    return em.createQuery("select b "
//      + "from Booking b "
//      + "inner join fetch b.activity a "
//      + "inner join fetch a.resource r "
//      + "where r.id = :resourceId", Booking.class)
//      .setParameter("resourceId", resourceId)
//      .getResultList();
//  }
}
