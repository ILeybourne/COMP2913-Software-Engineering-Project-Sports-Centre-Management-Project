package uk.ac.leeds.comp2913.api.DataAccessLayer.Repository;

import org.springframework.data.jpa.repository.Query;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;

import java.util.Collection;

public interface CustomBookingRepository {

 void unsubscribeFromRegularSession(Long regular_session_id, Long account_id);
}
