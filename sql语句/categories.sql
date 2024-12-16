create table categories
(
    category_id        serial
        constraint pk_categories
            primary key,
    category_name      varchar(255),
    parent_category_id integer
)
    with (orientation = row, compression = no);

alter table categories
    owner to remote_user;

INSERT INTO public.categories (category_id, category_name, parent_category_id) VALUES (1, '教材书籍', 0);
INSERT INTO public.categories (category_id, category_name, parent_category_id) VALUES (3, '日常用品', 0);
INSERT INTO public.categories (category_id, category_name, parent_category_id) VALUES (4, '其他', 4);
INSERT INTO public.categories (category_id, category_name, parent_category_id) VALUES (2, '电子产品', 3);
