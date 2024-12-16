create table products
(
    product_id             serial
        constraint pk_products
            primary key,
    seller_id              integer           not null,
    product_name           varchar(255)      not null,
    production_description varchar(255)      not null,
    category_id            integer
        constraint fkog2rp4qthbtt2lfyhfo32lsw9
            references categories,
    price                  double precision  not null,
    publish_status         integer,
    image                  varchar(255),
    publish_time           timestamp,
    view_count             integer default 0 not null
)
    with (orientation = row, compression = no);

alter table products
    owner to remote_user;

INSERT INTO public.products (product_id, seller_id, product_name, production_description, category_id, price, publish_status, image, publish_time, view_count) VALUES (24, 11, '四六级耳机', '这是一个四六级耳机，因为四六级已过，所以便宜出', 2, 10, 0, '/uploads/fd3c1aad-a416-4d9e-9240-eb50994e4f74.jpg', '2024-12-16 21:57:43.862419', 3);
INSERT INTO public.products (product_id, seller_id, product_name, production_description, category_id, price, publish_status, image, publish_time, view_count) VALUES (23, 11, '四六级耳机', '这是一个四六级耳机，因为四六级已过，所以便宜出', 2, 10, 0, '/uploads/fd3c1aad-a416-4d9e-9240-eb50994e4f74.jpg', '2024-12-16 21:56:17.190144', 4);
INSERT INTO public.products (product_id, seller_id, product_name, production_description, category_id, price, publish_status, image, publish_time, view_count) VALUES (20, 13, '四六级耳机', '四六级已过，出四六级耳机', 2, 10, 0, '/uploads/aef97b9a-6e37-47ac-8ebc-c71a0d408150.jpg', '2024-12-16 21:49:00.530765', 1);
INSERT INTO public.products (product_id, seller_id, product_name, production_description, category_id, price, publish_status, image, publish_time, view_count) VALUES (16, 13, '四六级耳机', '四六级已过，出四六级耳机', 2, 10, 0, '/uploads/aef97b9a-6e37-47ac-8ebc-c71a0d408150.jpg', '2024-12-16 21:46:04.367333', 6);
INSERT INTO public.products (product_id, seller_id, product_name, production_description, category_id, price, publish_status, image, publish_time, view_count) VALUES (19, 13, '四六级耳机', '四六级已过，出四六级耳机', 2, 10, 0, '/uploads/aef97b9a-6e37-47ac-8ebc-c71a0d408150.jpg', '2024-12-16 21:47:31.964399', 1);
INSERT INTO public.products (product_id, seller_id, product_name, production_description, category_id, price, publish_status, image, publish_time, view_count) VALUES (12, 13, '水杯', '这是学校举行活动送的水杯，全新，没用过', 3, 20, 0, '/uploads/7962b28b-b4d5-4d10-ac93-387dcb3789b9.jpg', '2024-12-16 21:17:13.308634', 2);
INSERT INTO public.products (product_id, seller_id, product_name, production_description, category_id, price, publish_status, image, publish_time, view_count) VALUES (17, 13, '四六级耳机', '四六级已过，出四六级耳机', 2, 10, 0, '/uploads/aef97b9a-6e37-47ac-8ebc-c71a0d408150.jpg', '2024-12-16 21:46:14.142503', 1);
INSERT INTO public.products (product_id, seller_id, product_name, production_description, category_id, price, publish_status, image, publish_time, view_count) VALUES (13, 13, '数据库系统概论', '这是一本数据库概率书，内含丰富笔记', 1, 15, 0, '/uploads/9e235d32-b3be-4494-87b7-017936d55c22.jpg', '2024-12-16 21:38:35.994178', 0);
INSERT INTO public.products (product_id, seller_id, product_name, production_description, category_id, price, publish_status, image, publish_time, view_count) VALUES (14, 13, 'redmi buds5 蓝牙耳机', '用了一年，延迟略高，音质不错，准备转buds6了，所以出了', 2, 90, 0, '/uploads/35c29de5-33a0-4d64-b8bf-54ee82bf2b73.jpg', '2024-12-16 21:41:13.210949', 0);
INSERT INTO public.products (product_id, seller_id, product_name, production_description, category_id, price, publish_status, image, publish_time, view_count) VALUES (15, 13, '四六级耳机', '四六级已过，出四六级耳机', 2, 10, 0, '/uploads/aef97b9a-6e37-47ac-8ebc-c71a0d408150.jpg', '2024-12-16 21:46:01.845239', 0);
INSERT INTO public.products (product_id, seller_id, product_name, production_description, category_id, price, publish_status, image, publish_time, view_count) VALUES (18, 13, '四六级耳机', '四六级已过，出四六级耳机', 2, 10, 0, '/uploads/aef97b9a-6e37-47ac-8ebc-c71a0d408150.jpg', '2024-12-16 21:46:19.984340', 0);
INSERT INTO public.products (product_id, seller_id, product_name, production_description, category_id, price, publish_status, image, publish_time, view_count) VALUES (22, 13, '四六级耳机', '四六级已过，出四六级耳机', 2, 10, 0, '/uploads/aef97b9a-6e37-47ac-8ebc-c71a0d408150.jpg', '2024-12-16 21:54:57.334577', 0);
INSERT INTO public.products (product_id, seller_id, product_name, production_description, category_id, price, publish_status, image, publish_time, view_count) VALUES (11, 11, '鼠标', '这是一个无线鼠标，用了两年了', 2, 30, 0, '/uploads/5db716b1-4f7f-485b-9275-a1ba7a3a4a90.jpg', '2024-12-16 21:16:02.815529', 6);
INSERT INTO public.products (product_id, seller_id, product_name, production_description, category_id, price, publish_status, image, publish_time, view_count) VALUES (10, 11, '键盘', '这是一个键盘，九九新，便宜出', 2, 66, 0, '/uploads/b174617b-0c16-439d-a077-0c8efacad16d.jpg', '2024-12-16 21:14:15.840129', 5);
INSERT INTO public.products (product_id, seller_id, product_name, production_description, category_id, price, publish_status, image, publish_time, view_count) VALUES (29, 11, '键盘', '这是一个键盘，全新键盘', 2, 10, 0, '/uploads/5db230bd-7d7c-468c-b961-66e1fdb99ff0.jpg', '2024-12-16 23:14:40.798212', 3);
INSERT INTO public.products (product_id, seller_id, product_name, production_description, category_id, price, publish_status, image, publish_time, view_count) VALUES (21, 13, '四六级耳机', '四六级已过，出四六级耳机', 2, 10, 0, '/uploads/aef97b9a-6e37-47ac-8ebc-c71a0d408150.jpg', '2024-12-16 21:54:44.488085', 4);
