alter table sports_centre_management.activity_type
    add column resource_id int8;

alter table if exists sports_centre_management.activity_type
    add constraint activity_type__resource_id__resource_id
        foreign key (resource_id)
            references sports_centre_management.resource(id);