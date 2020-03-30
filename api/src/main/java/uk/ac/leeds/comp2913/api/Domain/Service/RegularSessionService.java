package uk.ac.leeds.comp2913.api.Domain.Service;

import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.RegularSession;

public interface RegularSessionService {
  RegularSession createRegularSession(Activity activity, RegularSession regularSession);
}
