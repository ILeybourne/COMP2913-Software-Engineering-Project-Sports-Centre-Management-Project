package uk.ac.leeds.comp2913.api.DataAccessLayer.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.ac.leeds.comp2913.api.Domain.Model.Account;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.Booking;
import uk.ac.leeds.comp2913.api.Domain.Model.RegularSession;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, CustomBookingRepository {
  @Override
  Optional<Booking> findById(Long aLong);

  Page<Booking> findByAccountId(Pageable pageable, Long account_id);

  Page<Booking> findByActivityId(Pageable pageable, Long account_id);

  @Override
  void delete(Booking booking);

  @Override
  void deleteById(Long aLong);

}