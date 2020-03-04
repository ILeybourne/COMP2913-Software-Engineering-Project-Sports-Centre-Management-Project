create table sports_centre_management.customer
(
    id         int8 not null,
    booking_id int8,
    primary key (id)
);
alter table if exists sports_centre_management.customer
    add constraint FK135jvc1sphsxqk2h2edsctxms
        foreign key (booking_id)
            references sports_centre_management.account;

