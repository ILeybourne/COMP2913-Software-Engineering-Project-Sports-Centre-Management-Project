package uk.ac.leeds.comp2913.api.DataAccessLayer.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, CustomBookingRepository {
  @Override
  Optional<Booking> findById(Long aLong);

  @Override
  void delete(Booking booking);

  @Override
  void deleteById(Long aLong);
}
