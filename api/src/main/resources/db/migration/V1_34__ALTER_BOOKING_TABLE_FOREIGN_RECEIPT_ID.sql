alter table if exists sports_centre_management.booking
    add column receipt_id int8,
    add constraint booking__receipt_id
        foreign key (receipt_id)
            references sports_centre_management.receipts;
