alter table sports_centre_management.booking
    add column activity_id int8;

alter table if exists sports_centre_management.booking
    add constraint booking__activity_id__activity_id
        foreign key (activity_id)
            references sports_centre_management.activity(id);




