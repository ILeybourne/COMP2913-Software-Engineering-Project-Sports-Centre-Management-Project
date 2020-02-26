alter table if exists sports_centre_management.activity
    add constraint activity__booking__resource_id
        foreign key (booking_id)
            references sports_centre_management.booking
