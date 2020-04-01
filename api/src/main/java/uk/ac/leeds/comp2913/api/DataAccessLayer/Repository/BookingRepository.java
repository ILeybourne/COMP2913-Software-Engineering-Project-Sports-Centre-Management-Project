package uk.ac.leeds.comp2913.api.DataAccessLayer.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.ac.leeds.comp2913.api.Domain.Model.Account;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Model.RegularSession;

import java.util.List;
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