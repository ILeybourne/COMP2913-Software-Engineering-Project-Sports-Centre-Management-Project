alter table sports_centre_management.customer
    add email_address varchar(255) unique;

drop table sports_centre_management.manager;
drop table sports_centre_management.employee;
drop table sports_centre_management.user CASCADE;
