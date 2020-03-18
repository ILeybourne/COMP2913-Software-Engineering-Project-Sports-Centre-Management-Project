create table sports_centre_management.facility_timetable
(
    id         bigserial not null,
    name       varchar(255),
    centre_id  int8                  not null,
    created_at timestamp,
    updated_at timestamp,
    primary key (id)
)
