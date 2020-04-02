create table sports_centre_management.regular_session
(
    id             bigserial primary key not null,
    interval        int
);

alter table if exists sports_centre_management.activity
    add column regular_session_id bigint references sports_centre_management.regular_session (id);

alter table if exists sports_centre_management.booking
    add column regular_session_id bigint references sports_centre_management.regular_session (id)
