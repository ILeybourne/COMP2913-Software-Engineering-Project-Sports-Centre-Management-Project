package uk.ac.leeds.comp2913.api.Controller;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.BookingRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.RegularSessionRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.RegularSession;
import uk.ac.leeds.comp2913.api.Domain.Service.Impl.RegularSessionServiceImpl;
import uk.ac.leeds.comp2913.api.Exception.ResourceNotFoundException;

import javax.validation.Valid;

@RestController
public class RegularSessionController {

  private final RegularSessionRepository regularSessionRepository;
  private final ActivityRepository activityRepository;
  private final BookingRepository bookingRepository;
  private final RegularSessionServiceImpl regularSessionServiceImpl;

  @Autowired
  public RegularSessionController(RegularSessionRepository regularSessionRepository, ActivityRepository activityRepository, BookingRepository bookingRepository, RegularSessionServiceImpl regularSessionServiceImpl) {
    this.regularSessionRepository = regularSessionRepository;
    this.activityRepository = activityRepository;
    this.bookingRepository = bookingRepository;
    this.regularSessionServiceImpl = regularSessionServiceImpl;
  }

  @GetMapping("/regularsession")
  public Page<RegularSession> getRegularSessions(Pageable pageable) {
    return regularSessionRepository.findAll(pageable);
  }

  @PostMapping("/regularsession/{activity_id}")
  public RegularSession createRegularSession(@PathVariable Long activity_id,
                                             @Valid @RequestBody RegularSession regularSession) {
    regularSessionRepository.save(regularSession);
    regularSessionServiceImpl.addRegularSessionToActivity(activity_id, regularSession);
    return regularSession;
  }

  @PutMapping("/regularsession/{regular_session_id}")
  public RegularSession updateRegularSession(@PathVariable Long regular_session_id,
                                             @Valid @RequestBody RegularSession regularSessionRequest) {
    return regularSessionRepository.findById(regular_session_id)
        .map(regularSession -> {
          regularSession.setInterval(regularSession.getInterval());
//                    TODO: Others
          return regularSessionRepository.save(regularSession);
        }).orElseThrow(() -> new ResourceNotFoundException("Regular Session not found with ID " + regular_session_id));
  }

  @DeleteMapping("/regularsession/{regular_session_id}")
  public ResponseEntity<?> deleteRegularSession(@PathVariable Long regular_session_id) {
    try {
      regularSessionServiceImpl.deleteRegularSession(regular_session_id);
      return ResponseEntity
          .noContent()
          .build();
    } catch (EmptyResultDataAccessException e) {
      throw new ResourceNotFoundException("Regular Session not found with that ID " + regular_session_id);
    }
  }


  //@DeleteMapping("/regularsession/{regular_session_id}")
 //public ResponseEntity<?> deleteRegularSession(@PathVariable Long regular_session_id) {
 //  try {
 //   // bookingRepository.removeRegularSessionsFromBooking(regular_session_id);
 //  //  activityRepository.removeRegularSessionsFromActivity(regular_session_id);
 //    regularSessionRepository.deleteById(regular_session_id);
 //    return ResponseEntity
 //        .noContent()
 //        .build();
 //  } catch (EmptyResultDataAccessException e) {
 //    throw new ResourceNotFoundException("Regular Session not found with that ID " + regular_session_id);
 //  }
 //}
}
