create table sports_centre_management.centre
(
    id         bigserial not null,
    name       varchar(255),
    created_at timestamp,
    updated_at timestamp,
    primary key (id)
);
alter table if exists sports_centre_management.account
    add constraint FKax72jh0dpxbdxusjii1b54008
        foreign key (centre_id)
            references sports_centre_management.centre
