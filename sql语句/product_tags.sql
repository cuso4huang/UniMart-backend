create table product_tags
(
    product_id integer not null
        constraint fk5rk6s19k3risy7q7wqdr41uss
            references products,
    tag_id     integer not null
        constraint fkpur2885qb9ae6fiquu77tcv1o
            references tags,
    primary key (product_id, tag_id)
)
    with (orientation = row, compression = no);

alter table product_tags
    owner to remote_user;

