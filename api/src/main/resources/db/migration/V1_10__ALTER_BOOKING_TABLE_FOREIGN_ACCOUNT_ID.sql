alter table if exists sports_centre_management.booking
    add constraint booking__account__account_id
        foreign key (account_id)
            references sports_centre_management.account;
