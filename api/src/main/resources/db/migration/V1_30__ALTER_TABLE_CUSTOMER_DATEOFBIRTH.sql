alter table sports_centre_management.customer
    drop column date_of_birth;

alter table sports_centre_management.customer
    add column date_of_birth timestamp;