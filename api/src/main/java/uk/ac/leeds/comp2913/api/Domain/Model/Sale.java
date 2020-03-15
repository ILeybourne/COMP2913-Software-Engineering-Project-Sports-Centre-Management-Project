package uk.ac.leeds.comp2913.api.Domain.Model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Membership.class, name = "membership"),
        @JsonSubTypes.Type(value = Booking.class, name = "booking")
})
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Sale {
    @Id
    @GeneratedValue
    private long id;

    public abstract BigDecimal getCost();

    @OneToOne(mappedBy = "sale")
    private Payment payment;

    public long getId() {
        return id;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
