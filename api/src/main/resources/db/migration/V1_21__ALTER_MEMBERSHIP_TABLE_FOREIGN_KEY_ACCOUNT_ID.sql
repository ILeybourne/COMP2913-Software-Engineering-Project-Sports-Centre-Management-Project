alter table sports_centre_management.membership add column account_id int8;
alter table if exists sports_centre_management.membership
    add constraint membership__account__account_id
        foreign key (account_id)
            references sports_centre_management.account;