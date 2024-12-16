create table messages
(
    message_id  serial
        constraint pk_messages
            primary key,
    sender_id   integer
        constraint fk_messages_on_sender
            references users,
    receiver_id integer
        constraint fk_messages_on_receiver
            references users,
    content     varchar(500),
    create_time timestamp,
    is_read     boolean not null
)
    with (orientation = row, compression = no);

alter table messages
    owner to remote_user;

INSERT INTO public.messages (message_id, sender_id, receiver_id, content, create_time, is_read) VALUES (2, 13, 11, '想要', '2024-12-15 17:29:59.894318', true);
INSERT INTO public.messages (message_id, sender_id, receiver_id, content, create_time, is_read) VALUES (1, 13, 11, '你好', '2024-12-15 17:25:31.343126', true);
INSERT INTO public.messages (message_id, sender_id, receiver_id, content, create_time, is_read) VALUES (3, 11, 13, '还在', '2024-12-15 17:42:14.903893', true);
INSERT INTO public.messages (message_id, sender_id, receiver_id, content, create_time, is_read) VALUES (4, 13, 11, 'nihao', '2024-12-16 16:48:25.250489', true);
INSERT INTO public.messages (message_id, sender_id, receiver_id, content, create_time, is_read) VALUES (5, 11, 13, '好', '2024-12-16 16:49:05.714364', true);
INSERT INTO public.messages (message_id, sender_id, receiver_id, content, create_time, is_read) VALUES (6, 11, 13, '想要蓝牙耳机', '2024-12-16 23:18:07.884297', false);
