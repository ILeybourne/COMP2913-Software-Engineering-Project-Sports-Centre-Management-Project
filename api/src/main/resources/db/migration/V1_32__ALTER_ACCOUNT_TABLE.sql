alter table sports_centre_management.account
    DROP COLUMN password,
    DROP COLUMN date_of_birth,
    DROP COLUMN bookings,
    DROP COLUMN membership_id;

alter  table  sports_centre_management.membership
    drop constraint membership__account__account_id;