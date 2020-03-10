alter table sports_centre_management.account
    add column membership_id int;

alter table sports_centre_management.account
    add constraint account__membership_id__membership_id
        foreign key (membership_id)
            references sports_centre_management.membership(id);
