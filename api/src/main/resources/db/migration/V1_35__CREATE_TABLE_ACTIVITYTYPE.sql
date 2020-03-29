create table sports_centre_management.activity_type
(
    id               bigserial primary key not null,
    name             varchar(255),
    created_at       timestamp,
    updated_at       timestamp,
    current_capacity int,
    total_capacity   int
)
