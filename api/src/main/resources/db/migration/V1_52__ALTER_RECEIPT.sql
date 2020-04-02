alter table sports_centre_management.receipt
    add customer_id  bigint references customer (id),
    add pdf_location varchar(255) unique;
