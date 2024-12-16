create table report_images
(
    image_id    serial
        constraint pk_report_images
            primary key,
    report_id   integer      not null
        constraint fk_report_images_on_report
            references reports,
    image_url   varchar(255) not null,
    create_time timestamp    not null
)
    with (orientation = row, compression = no);

alter table report_images
    owner to remote_user;

