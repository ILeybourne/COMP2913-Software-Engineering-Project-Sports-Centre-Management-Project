package uk.ac.leeds.comp2913.api.Domain.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Model.RegularSession;

public interface RegularSessionService {

  void cancelRegularSession(Long regular_session_id, Long account_id);

  void deleteRegularSession(Long regular_session_id);

  void addRegularSessionToActivity(Long activity_id, RegularSession regularSession);

  void createRegularSessionBooking(Long regular_session_id, Booking booking);
  }
