create table sports_centre_management.user
(
    id            int8         not null,
    created_at    timestamp,
    email_address varchar(255) not null,
    first_name    varchar(255) not null,
    last_name     varchar(255) not null,
    password      varchar(255) not null,
    updated_at    timestamp,
    primary key (id)
);

alter table if exists sports_centre_management.customer
    add constraint FKg2o3t8h0g17smtr9jgypagdtv
        foreign key (id)
            references sports_centre_management.user;

alter table if exists sports_centre_management.employee
    add constraint FKd8il4lxw1wi74qh8b7uoy6e0a
        foreign key (id)
            references sports_centre_management.user
