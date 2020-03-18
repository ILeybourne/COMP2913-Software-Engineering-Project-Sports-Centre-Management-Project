alter table sports_centre_management.account
    add column customer_id int;

alter table sports_centre_management.account
    add constraint account__customer_id__customer_id
        foreign key (customer_id)
            references sports_centre_management.customer(id);