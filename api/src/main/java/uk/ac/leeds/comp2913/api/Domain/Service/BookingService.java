package uk.ac.leeds.comp2913.api.Domain.Service;

import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.ViewModel.BookingDTO;

public interface BookingService {
  Booking createNewBookingForActivity(Booking booking, Long activity_id, Long account_id, Boolean regularBooking);

  void cancelRegularSession(Long activity_id, Long account_id);
}