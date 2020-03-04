alter table if exists sports_centre_management.facility_timetable
    add constraint facility_timetable__centre__centre_id
        foreign key (centre_id)
            references sports_centre_management.centre
