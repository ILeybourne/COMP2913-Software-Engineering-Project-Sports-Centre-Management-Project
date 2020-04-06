package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
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

    public void setStartDate() {
      this.startDate = new Date();
      setEndDate(startDate);
    }

    public Date getEndDate() {
      return endDate;
    }

    public void setEndDate(Date startDate) {
        this.endDate = (calculateEndDate(startDate, membershipType.getDuration()));
    }

    public Date calculateEndDate(Date startDate, Integer duration){
        Date endDate = new Date(startDate.getTime()+((24*60*60*1000) * duration)); //Math at the end converts a day into milliseconds
        return endDate;
    }

    @Override
    public String getName() {
        return "Membership: " + membershipType.getName();
    }

    public void makePayment() {
    }

    public void emailReceipt() {
    }

    public void printReceipt() {
    }

    public void storeReceipt() {
    }

    public Boolean getRepeatingPayment() {
        return repeatingPayment;
    }

    public void setRepeatingPayment(Boolean repeatingPayment) {
        this.repeatingPayment = repeatingPayment;
    }

}
