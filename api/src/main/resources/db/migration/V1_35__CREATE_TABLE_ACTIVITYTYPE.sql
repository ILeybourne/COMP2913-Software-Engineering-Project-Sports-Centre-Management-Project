create table sports_centre_management.activity_type
(
    id              int8 not null,
    name            varchar(255),
    created_at      timestamp,
    updated_at      timestamp,
    current_capacity int,
    total_capacity int,
    primary key (id)
)
