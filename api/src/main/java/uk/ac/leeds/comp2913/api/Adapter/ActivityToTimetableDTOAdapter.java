package uk.ac.leeds.comp2913.api.Adapter;

import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.ViewModel.TimetableDTO;

public class ActivityToTimetableDTOAdapter implements Adapter<Activity, TimetableDTO> {
  BookingToBookingDTOAdapter bookingDTOAdapter;

  @Override
  public TimetableDTO map(Activity a) {
    TimetableDTO t = new TimetableDTO();
//    t.setBookings(bookingDTOAdapter.map());
//    t.setResource();
    t.setName(a.getName());
    t.setStartTime(a.getStartTime());
    t.setEndTime(a.getEndTime());
    return t;
  }
}
