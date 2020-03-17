package uk.ac.leeds.comp2913.api.Domain.Model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

@Entity
public class Receipt {

    @Id
    @GeneratedValue
    private long id;

    @CreationTimestamp
    private Date created_at;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receipt")
    private Set<Payment> payments;

    private String product_description;

    private BigInteger cost;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Date created_at) {
        this.created_at = created_at;
    }

    public BigInteger getCost() {
        return cost;
    }

    public void setCost(BigInteger cost_gbp_pence) {
        this.cost = cost_gbp_pence;
    }

    public String getProductDescription() {
        return product_description;
    }

    public void setProductDescription(String product_description) {
        this.product_description = product_description;
    }

    public Set<Payment> getPayments() {
      return payments;
    }

    public void setPayments(Set<Payment> payments) {
      this.payments = payments;
    }
}
