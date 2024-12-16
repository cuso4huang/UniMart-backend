create table roles
(
    role_id integer      not null
        constraint pk_roles
            primary key,
    name    varchar(255) not null
        constraint uc_roles_name
            unique
)
    with (orientation = row, compression = no);

alter table roles
    owner to remote_user;

INSERT INTO public.roles (role_id, name) VALUES (1, 'ROLE_USER');
INSERT INTO public.roles (role_id, name) VALUES (2, 'ROLE_ADMIN');
