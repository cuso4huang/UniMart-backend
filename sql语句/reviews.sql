create table reviews
(
    review_id      serial
        constraint pk_reviews
            primary key,
    transaction_id integer   not null,
    from_user_id   integer   not null,
    to_user_id     integer   not null,
    rating         integer   not null,
    comment        varchar(255),
    is_anonymous   boolean   not null,
    create_time    timestamp not null,
    update_time    timestamp not null
)
    with (orientation = row, compression = no);

alter table reviews
    owner to remote_user;

INSERT INTO public.reviews (review_id, transaction_id, from_user_id, to_user_id, rating, comment, is_anonymous, create_time, update_time) VALUES (3, 8, 11, 11, 5, 'good', false, '2024-12-13 16:55:52.984890', '2024-12-13 16:55:52.984890');
