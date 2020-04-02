alter table sports_centre_management.sale drop constraint sale_receipt_id_fkey;

alter table sports_centre_management.sale
    add constraint sale_receipt_id_fkey
        foreign key (receipt_id) references sports_centre_management.receipt
            on delete set default;