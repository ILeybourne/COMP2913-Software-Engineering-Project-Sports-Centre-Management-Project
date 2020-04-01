alter table sports_centre_management.booking drop constraint activity_regular_session_id_fkey;

alter table sports_centre_management.booking
    add constraint booking_regular_session_id_fkey
        foreign key (regular_session_id) references sports_centre_management.regular_session
            on delete set null;