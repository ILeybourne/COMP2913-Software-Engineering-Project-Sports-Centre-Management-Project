create table sports_centre_management.receipt
(
    id                  int8 not null,
    created_at          timestamp,
    product_description text,
    cost_gbp_pence      int,
    primary key (id)
);
