alter table if exists sports_centre_management.activity drop column regular_session_id;

alter table if exists sports_centre_management.activity
    add column regular_session_id bigint references sports_centre_management.regular_session (id);