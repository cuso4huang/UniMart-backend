create table tags
(
    tag_id   serial
        constraint pk_tags
            primary key,
    tag_name varchar(255)
)
    with (orientation = row, compression = no);

alter table tags
    owner to remote_user;

INSERT INTO public.tags (tag_id, tag_name) VALUES (1, 'test2');
INSERT INTO public.tags (tag_id, tag_name) VALUES (2, '真爱粉');
INSERT INTO public.tags (tag_id, tag_name) VALUES (3, '全新未拆封');
INSERT INTO public.tags (tag_id, tag_name) VALUES (9, '女生自用');
INSERT INTO public.tags (tag_id, tag_name) VALUES (10, '九九新');
INSERT INTO public.tags (tag_id, tag_name) VALUES (11, 'Pro');
INSERT INTO public.tags (tag_id, tag_name) VALUES (12, 'test');
