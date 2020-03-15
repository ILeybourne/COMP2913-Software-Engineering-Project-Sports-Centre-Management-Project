package uk.ac.leeds.comp2913.api.Domain.Model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Receipt {

    @Id
    @GeneratedValue
    private long id;

    //@OneToMany(mappedBy = "receipt", fetch = FetchType.EAGER)
    //todo add list of payments

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "product_description")
    private String productDescription;

    @Column(name = "cost")
    private BigDecimal cost;

    //todo add receipt pdf member

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date created_at) {
        this.createdAt = created_at;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
}
