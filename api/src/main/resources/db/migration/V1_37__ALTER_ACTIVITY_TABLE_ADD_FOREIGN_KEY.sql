alter table sports_centre_management.activity
    add column activity_type_id int8 references sports_centre_management.activity_type;

alter table sports_centre_management.activity
    add constraint activity__activity_type_id__activity_type_id
        foreign key (activity_type_id)
            references sports_centre_management.activity_type (id);
