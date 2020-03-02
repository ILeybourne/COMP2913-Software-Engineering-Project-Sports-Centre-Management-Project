create table if not exists sports_centre_management.resource
(
    id         int8 not null,
    created_at timestamp,
    name       varchar(255),
    updated_at timestamp,
    primary key (id)
);
