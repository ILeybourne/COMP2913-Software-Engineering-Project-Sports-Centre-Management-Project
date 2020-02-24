alter table if exists sports_centre_management.activity
    add constraint booking__resource__resource_id
        foreign key (resource_id)
            references sports_centre_management.resource
