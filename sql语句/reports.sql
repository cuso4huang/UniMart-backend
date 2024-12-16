create table reports
(
    report_id        serial
        constraint pk_reports
            primary key,
    reporter_user_id integer      not null,
    accused_user_id  integer,
    transaction_id   integer,
    description      varchar(255) not null,
    status           varchar(255) not null,
    handler_user_id  integer,
    create_time      timestamp    not null,
    update_time      timestamp    not null
)
    with (orientation = row, compression = no);

alter table reports
    owner to remote_user;

INSERT INTO public.reports (report_id, reporter_user_id, accused_user_id, transaction_id, description, status, handler_user_id, create_time, update_time) VALUES (5, 11, 13, 8, '有问题', 'completed', 11, '2024-12-13 11:19:53.763225', '2024-12-13 11:21:57.799938');
INSERT INTO public.reports (report_id, reporter_user_id, accused_user_id, transaction_id, description, status, handler_user_id, create_time, update_time) VALUES (7, 11, 11, 8, '小黑子', 'pending', null, '2024-12-13 18:45:02.249048', '2024-12-13 18:45:02.249048');
INSERT INTO public.reports (report_id, reporter_user_id, accused_user_id, transaction_id, description, status, handler_user_id, create_time, update_time) VALUES (8, 11, 13, 8, '有问题', 'pending', null, '2024-12-16 22:56:11.153310', '2024-12-16 22:56:11.153310');
