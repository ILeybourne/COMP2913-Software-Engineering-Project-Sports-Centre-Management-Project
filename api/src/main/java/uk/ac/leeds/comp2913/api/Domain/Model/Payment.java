package uk.ac.leeds.comp2913.api.Domain.Model;

import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.MetaValue;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import uk.ac.leeds.comp2913.api.DataAccessLayer.Repository.Sale;

@Entity
public class Payment {
    @Id
    @GeneratedValue
    private long id;

    private BigDecimal sale_price;

    private Date sale_date;

    @CreationTimestamp
    private Date created_at;
    @UpdateTimestamp
    private Date updated_at;

    @Any (metaColumn = @Column(name = "sale_type"))
    @AnyMetaDef(idType = "long", metaType = "string",
    metaValues = {
    @MetaValue(targetEntity = Membership.class, value = "membership"),
    @MetaValue(targetEntity = Booking.class, value = "booking")
    })
    @Cascade( { org.hibernate.annotations.CascadeType.ALL})
    @JoinColumn(name = "sale_id")
    private Sale sale;

    public Payment(){
    }


    public Payment(final Date sale_date, final Sale sale,final BigDecimal sale_price) {
        this.sale_date = sale_date;
        this.sale = sale;
        this.sale_price = sale_price;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getSale_price() {
        return sale_price;
    }

    public void setSale_price(BigDecimal sale_price) {
        this.sale_price = sale_price;
    }

    public Date getSale_date() {
        return sale_date;
    }

    public void setSale_date(Date sale_date) {
        this.sale_date = sale_date;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }
}
