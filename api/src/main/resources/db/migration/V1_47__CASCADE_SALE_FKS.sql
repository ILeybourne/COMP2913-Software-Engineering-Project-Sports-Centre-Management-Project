alter table sports_centre_management.membership drop constraint membership__sale;

alter table sports_centre_management.membership
    add constraint membership__sale
        foreign key (id) references sports_centre_management.sale
            on delete cascade;

alter table sports_centre_management.booking drop constraint booking__sale;

alter table sports_centre_management.booking
    add constraint booking__sale
        foreign key (id) references sports_centre_management.sale
            on delete cascade;