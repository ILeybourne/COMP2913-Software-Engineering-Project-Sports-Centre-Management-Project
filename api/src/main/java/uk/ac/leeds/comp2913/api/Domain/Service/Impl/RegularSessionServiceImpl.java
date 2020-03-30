package uk.ac.leeds.comp2913.api.Domain.Service.Impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.ActivityRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.BookingRepository;
import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.RegularSessionRepository;
import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.RegularSession;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityService;
import uk.ac.leeds.comp2913.api.Domain.Service.RegularSessionService;

@Service
public class RegularSessionServiceImpl implements RegularSessionService {
  RegularSessionRepository regularSessionRepository;

  @Autowired
  public RegularSessionServiceImpl(RegularSessionRepository regularSessionRepository) {
    this.regularSessionRepository = regularSessionRepository;
  }

  @Override
  public RegularSession createRegularSession(Activity activity, RegularSession regularSession){
    return regularSessionRepository.save(regularSession);
  }

}
