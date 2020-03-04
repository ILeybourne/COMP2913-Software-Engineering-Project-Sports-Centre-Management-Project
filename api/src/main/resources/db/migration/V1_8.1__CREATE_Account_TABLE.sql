create table sports_centre_management.account
(
    id            int8 not null,
    bookings      varchar(255),
    date_of_birth timestamp,
    memberships   varchar(255),
    password      varchar(255),
    created_at    timestamp,
    updated_at    timestamp,
    centre_id     int8,
    primary key (id)
)
