package uk.ac.leeds.comp2913.api.DataAccessLayer.Repository;

import org.springframework.stereotype.Repository;

public interface CustomMembershipRepository {

    void stopRepeatPayment(Long membership_type_id, Long account_id);
}
