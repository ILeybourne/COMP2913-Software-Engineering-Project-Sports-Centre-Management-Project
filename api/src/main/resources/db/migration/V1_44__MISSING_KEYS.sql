alter table if exists sports_centre_management.booking
    add constraint booking__sale
        foreign key (id)
            references sports_centre_management.sale;


alter table if exists sports_centre_management.membership
    add constraint membership__account
        foreign key (account_id)
            references sports_centre_management.account;


alter table if exists sports_centre_management.membership
    add constraint membership__sale
        foreign key (id)
            references sports_centre_management.sale;
