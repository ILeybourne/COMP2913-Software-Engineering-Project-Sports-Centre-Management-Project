package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.lang.reflect.Member;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.*;

/**
 * Membership data, including account number & membership type chosen
 * Start date and end date (based on duration of chosen membership)
 */
@Entity
public class Membership extends Sale {

    @CreationTimestamp
    private Date created_at;
    @UpdateTimestamp
    private Date updated_at;

    //Foreign key, membership type (annual, monthly etc)
    @OneToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "membership_type_id", nullable = false)
    private MembershipType membershipType;

    //foreign key, account id to link user
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "repeating_payment")
    private Boolean repeatingPayment;

    public Membership(){}

    public static Membership renewMembership(Membership membership){
        Membership renewedMembership = new Membership();
        renewedMembership.setMembershipType(membership.getMembershipType());
        renewedMembership.setStartDate(membership.getEndDate());
        renewedMembership.setAccount(membership.getAccount());
        renewedMembership.setRepeatingPayment(membership.getRepeatingPayment());
        return renewedMembership;
    }

    public Date getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdatedAt() {
        return updated_at;
    }

    public void setUpdatedAt(Date updated_at) {
        this.updated_at = updated_at;
    }

    @JsonIgnore
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
        this.amount = membershipType.getCost();
    }

    @JsonIgnore
    public MembershipType getMembershipType() {
        return membershipType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
        setEndDate(startDate);
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date startDate) {
        this.endDate = (calculateEndDate(startDate, membershipType.getDuration()));
    }

    public Date calculateEndDate(Date startDate, Integer duration){
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.DATE, duration);
        endDate = c.getTime();
        return endDate;
    }

    public Boolean getRepeatingPayment() {
        return repeatingPayment;
    }

    public void setRepeatingPayment(Boolean repeatingPayment) {
        this.repeatingPayment = repeatingPayment;
    }

    @Override
    public String getName() {
        return "Membership " + this.getMembershipType().getName();
    }
}
