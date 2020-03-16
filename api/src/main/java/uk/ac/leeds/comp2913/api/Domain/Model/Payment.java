package uk.ac.leeds.comp2913.api.Domain.Model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

/**
 * Represents a payment from a Customer.
 *
 * The payment is associated with a sale, marking the sale as paid.
 *
 * A customer can choose to pay via Cash or via Card
 */
@Entity
public class Payment {
    @Id
    @GeneratedValue
    private long id;

    @Column(name = "sale_price")
    private BigDecimal salePrice;

    @Column(name = "sale_date")
    private Date saleDate;

  /**
   * The Receipt in which the payment was sent out on
   */
    @ManyToOne
    @JoinColumn(name = "receipt_id")
    private Receipt receipt;

  /**
   * The Transaction ID verifying the payment
   *
   * In case of card handling system eg Stripe, or Transaction ID if cash payment
   */
    @Column(name = "transaction_id")
    @Length(max = 255)
    private String transactionId;

    @OneToOne(mappedBy = "payment")
    private Sale sale;


    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;

  public Payment(){
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal sale_price) {
        this.salePrice = sale_price;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date sale_date) {
        this.saleDate = sale_date;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date created_at) {
        this.createdAt = created_at;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updated_at) {
        this.updatedAt = updated_at;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Receipt getReceipt() {
      return receipt;
    }

    public void setReceipt(Receipt receipt) {
      this.receipt = receipt;
    }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }
}
