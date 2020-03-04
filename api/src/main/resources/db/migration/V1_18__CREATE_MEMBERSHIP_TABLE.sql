create table sports_centre_management.membership
(
    id              int8 not null,
    end_date        timestamp,
    membership_type varchar(255),
    start_date      timestamp,
    created_at      timestamp,
    updated_at      timestamp,
    primary key (id)
)
