create table sports_centre_management.sale
(
    id             bigserial primary key not null,
    amount         decimal               not null default 0,
    receipt_id     bigint references sports_centre_management.receipt (id),
    transaction_id varchar(255) unique
)
