package uk.ac.leeds.comp2913.api.Domain.Model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Entity
public class Receipt {

    @Id
    @GeneratedValue
    private long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "receipt",
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.PERSIST
            }
    )
    private Set<Sale> sales;

    @Column(
            nullable = true,
            name = "pdf_location",
            unique = true
    )
    @Size(max = 255)
    private String pdfLocation;

    @Column(nullable = true, name = "product_description")
    private String productDescription;

    @Column(name = "total")
    private BigInteger total;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Receipt() {

    }

    public Receipt(Collection<Sale> sales, String transactionId) {

        this.setSales(Set.copyOf(sales));
        this.total = BigInteger.valueOf(0);
        for (Sale sale : sales) {
            /*TODO: fix types misalignment*/
            total = total.add(sale.getAmount().toBigInteger());
            sale.setReceipt(this);
//            sale.setTransactionId(transactionId);
        }
    }

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

    public BigInteger getTotal() {
        return total;
    }

    public void setTotal(BigInteger cost_gbp_pence) {
        this.total = cost_gbp_pence;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String product_description) {
        this.productDescription = product_description;
    }

    public Set<Sale> getSales() {
        return sales;
    }

    public void setSales(Set<Sale> sales) {
        this.sales = sales;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getPdfLocation() {
        return pdfLocation;
    }

    public void setPdfLocation(String pdfLocation) {
        this.pdfLocation = pdfLocation;
    }

    public String generateFilename() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d-MM-yyyy");
        return id + "-" + simpleDateFormat.format(createdAt);
    }
}
