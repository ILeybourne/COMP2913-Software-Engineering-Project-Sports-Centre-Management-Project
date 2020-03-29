create table sports_centre_management.activity
(
    id          bigserial not null,
    booking_id  int8,
    created_at  timestamp,
    end_time    timestamp,
    start_time  timestamp,
    updated_at  timestamp,
    resource_id int8                  not null,
    primary key (id)
);
