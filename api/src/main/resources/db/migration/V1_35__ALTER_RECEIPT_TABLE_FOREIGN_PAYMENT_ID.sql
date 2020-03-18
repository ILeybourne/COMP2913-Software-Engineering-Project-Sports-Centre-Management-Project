alter table if exists sports_centre_management.receipt
    add column payment_id int8,
    drop column cost_gbp_pence,
    add column cost decimal,
    add constraint receipt__payment_id
        foreign key (payment_id)
            references sports_centre_management.payment;
