alter table if exists sports_centre_management.activity
    add constraint FKa95dg7v5euwkge245q7me29m5
        foreign key (resource_id)
            references sports_centre_management.resource
