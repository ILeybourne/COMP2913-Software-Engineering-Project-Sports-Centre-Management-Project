package uk.ac.leeds.comp2913.api.Adapter;

import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.ViewModel.BookingDTO;

public class BookingToBookingDTOAdapter implements Adapter<Booking, BookingDTO> {
  @Override
  public BookingDTO map(Booking booking) {
    return null;
  }
}
