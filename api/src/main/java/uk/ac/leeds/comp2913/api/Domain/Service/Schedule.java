package uk.ac.leeds.comp2913.api.Domain.Service;

import com.stripe.exception.StripeException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import uk.ac.leeds.comp2913.api.Domain.Model.Membership;
import uk.ac.leeds.comp2913.api.Domain.Service.ActivityService;
@Component
public class Schedule {
    private final ActivityService activityService;
    private final MembershipService membershipService;

    public Schedule(ActivityService activityService, MembershipService membershipService){
        this.activityService = activityService;
        this.membershipService = membershipService;
    }

    //@Scheduled(cron = "0 1 0 * * ?")// Every day at 0:01 am
    //@Scheduled(cron = "0 0 1 * * MON") //Every Monday at 1am
    //@Scheduled(fixedDelay=5000)  // EVERY 5 Seconds: Used for testing
    @Transactional
    public void schedule() throws StripeException {
        activityService.automatedRegularSessionAndBookings();
        membershipService.automatedMembershipRenewals();
    }
}
