create table notifications
(
    notification_id serial
        constraint pk_notifications
            primary key,
    user_id         integer      not null,
    type            varchar(255) not null,
    content         text         not null,
    is_read         boolean      not null,
    created_at      timestamp    not null
)
    with (orientation = row, compression = no);

alter table notifications
    owner to remote_user;

