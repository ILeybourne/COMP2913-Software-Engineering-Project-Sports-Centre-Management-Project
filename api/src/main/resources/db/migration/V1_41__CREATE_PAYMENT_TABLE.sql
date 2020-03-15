create table if not exists sports_centre_management.payment
(
    id         int8 not null,
    created_at timestamp,
    name       varchar(255),
    updated_at timestamp,
    sale_date timestamp,
    sale_price decimal,
    sale_type varchar(255),
    sale_id int8,
    primary key (id)
);

alter table sports_centre_management.membership
    add column cost decimal;

alter table sports_centre_management.booking
    add column cost decimal;
