create table users
(
    user_id                serial
        constraint pk_users
            primary key,
    username               varchar(255) not null,
    password               varchar(255) not null,
    account                varchar(255) not null
        constraint uc_users_account
            unique,
    student_status         integer      not null,
    personal_rating_buyer  double precision,
    personal_rating_seller double precision,
    avatar                 varchar(255),
    address                varchar(255)
)
    with (orientation = row, compression = no);

alter table users
    owner to remote_user;

INSERT INTO public.users (user_id, username, password, account, student_status, personal_rating_buyer, personal_rating_seller, avatar, address) VALUES (11, 'cuso4', '$2a$10$Y2z0v2bj6vEE8MHPGBi.TOLvrwVQLl/WJBmezUDmN5pu1lrOk/n3O', 'zengqin.huang@qq.com', 0, 5, 5, null, null);
INSERT INTO public.users (user_id, username, password, account, student_status, personal_rating_buyer, personal_rating_seller, avatar, address) VALUES (13, 'cuso5', '$2a$10$p53W9FZ5XwB.sizPJTqwoOyjH0oP6eoOcMo4qtqfD.6cMLu545pvm', 'zengqin.huang1@qq.com', 0, 0, 0, null, null);
