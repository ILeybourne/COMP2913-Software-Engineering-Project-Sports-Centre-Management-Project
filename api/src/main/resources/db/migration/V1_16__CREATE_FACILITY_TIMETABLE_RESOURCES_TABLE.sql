create table sports_centre_management.facility_timetable_resources
(
    facility_timetable_id int8 not null,
    resources_id          int8 not null
);
alter table if exists sports_centre_management.facility_timetable_resources
    add constraint UK_1f65pw0gxnckhfjs0bmcxykoj unique (resources_id);

alter table if exists sports_centre_management.facility_timetable_resources
    add constraint FK8at9o34vh1wq0gf74gegg6ice
        foreign key (resources_id)
            references sports_centre_management.resource;


alter table if exists sports_centre_management.facility_timetable_resources
    add constraint FKqe13mjldpp5gfrp5nrg7direw
        foreign key (facility_timetable_id)
            references sports_centre_management.facility_timetable;
