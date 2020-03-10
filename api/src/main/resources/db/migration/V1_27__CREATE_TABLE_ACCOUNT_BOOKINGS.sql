create table sports_centre_management.account_bookings
(
    account_id      int8 not null,
    bookings_id    int8 not null
);
alter table if exists sports_centre_management.account_bookings
    add constraint account_bookings__account_id unique (account_id);

alter table if exists sports_centre_management.account_bookings
    add constraint account_bookings__account_id__account_id
        foreign key (account_id)
            references sports_centre_management.account(id);


alter table if exists sports_centre_management.account_bookings
    add constraint account_bookings__bookings_id__bookings_id
        foreign key (bookings_id)
            references sports_centre_management.booking(id);
