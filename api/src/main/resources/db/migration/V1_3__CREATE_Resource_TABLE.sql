create table if not exists sports_centre_management.resource
(
    id         bigserial not null,
    created_at timestamp,
    name       varchar(255),
    updated_at timestamp,
    primary key (id)
);
