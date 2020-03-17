create table sports_centre_management.sale
(
    amount         decimal               not null default 0,
    receipt_id     bigint references sports_centre_management.receipt (id),
    id             bigserial primary key not null,
    transaction_id varchar(255)
)
