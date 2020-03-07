create table sports_centre_management.manager
(
    id int8 not null,
    primary key (id)
);
alter table if exists sports_centre_management.manager
    add constraint FKobm0udnqxv4jiddvi5oykkgdr
        foreign key (id)
            references sports_centre_management.employee
