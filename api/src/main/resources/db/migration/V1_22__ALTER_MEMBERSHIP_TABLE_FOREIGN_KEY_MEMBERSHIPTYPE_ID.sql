alter table sports_centre_management.membership add column membership_type_id int8;
alter table if exists sports_centre_management.membership
    add constraint membership__membership_type__membership_type_id
        foreign key (membership_type_id)
            references sports_centre_management.membership_type;
