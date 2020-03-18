alter table if exists sports_centre_management.activity
    add constraint activity__resource_id__resource__id
        foreign key (resource_id)
            references sports_centre_management.resource
