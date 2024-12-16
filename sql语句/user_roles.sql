create table user_roles
(
    role_id integer not null
        constraint fk_userol_on_role
            references roles,
    user_id integer not null
        constraint fk_userol_on_user
            references users,
    constraint pk_user_roles
        primary key (role_id, user_id)
)
    with (orientation = row, compression = no);

alter table user_roles
    owner to remote_user;

INSERT INTO public.user_roles (role_id, user_id) VALUES (2, 11);
INSERT INTO public.user_roles (role_id, user_id) VALUES (1, 13);
