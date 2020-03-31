package uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.Impl;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.CustomBookingRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CustomBookingRepositoryImpl implements CustomBookingRepository {
  @PersistenceContext
  EntityManager em;

  @Override
  public void removeRegularSessionFromAccountBookings(Long regular_session_id, Long account_id){
    em.createQuery("update Booking set regularSession.id = null where regularSession.id = :regular_session_id and account.id = :account_id")
    .setParameter("account_id", account_id)
        .setParameter("regular_session_id", regular_session_id)
        .executeUpdate();
  }
}
