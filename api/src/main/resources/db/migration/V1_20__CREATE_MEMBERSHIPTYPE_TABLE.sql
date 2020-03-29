create table sports_centre_management.membership_type
(
    id              bigserial not null,
    name            varchar(255),
    created_at      timestamp,
    updated_at      timestamp,
    duration        int,
    cost            decimal,
    primary key (id)
)
