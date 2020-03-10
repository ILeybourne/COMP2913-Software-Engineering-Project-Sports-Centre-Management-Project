alter table sports_centre_management.resource
    add column centre_id int8;

alter table sports_centre_management.resource
    add constraint resource__centre_id__centre_id
        foreign key (centre_id)
            references sports_centre_management.centre(id);
