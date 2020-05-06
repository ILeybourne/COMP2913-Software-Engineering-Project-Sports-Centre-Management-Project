package uk.ac.leeds.comp2913.api.ViewModel;

import org.springframework.hateoas.PagedModel;

import java.util.Date;

public class AccountDTO extends PagedModel<AccountDTO> {

    public AccountDTO(){}

    private long id;
    private Date created_at;
    private Date updated_at;
    private Long customerId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
