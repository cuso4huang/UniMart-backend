create table review_images
(
    image_id    serial
        constraint pk_review_images
            primary key,
    review_id   integer      not null
        constraint fk_review_images_on_review
            references reviews,
    image_url   varchar(255) not null,
    create_time timestamp    not null
)
    with (orientation = row, compression = no);

alter table review_images
    owner to remote_user;

