package uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.Impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.CustomMembershipRepository;

public class CustomMembershipRepositoryImpl implements CustomMembershipRepository {
    @PersistenceContext
    EntityManager em;

    //Used to remove all regular session flags from regular bookings
    @Override
    public void stopRepeatPayment(Long membership_type_id, Long account_id){
        em.createQuery("update Membership set repeatingPayment = false where membershipType.id = :membership_type_id and account.id = :account_id")
                .setParameter("account_id", account_id)
                .setParameter("membership_type_id", membership_type_id)
                .executeUpdate();
    }
}
