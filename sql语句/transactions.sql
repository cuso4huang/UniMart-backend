create table transactions
(
    transaction_id     integer default nextval('transactions_tansaction_id_seq'::regclass) not null
        constraint pk_transactions
            primary key,
    product_id         integer
        constraint fkcdpkn7bkq15bjvlw9mo46l9ft
            references products,
    buyer_id           integer
        constraint fkijk7ii2fvalv59schbb3e0bof
            references users,
    seller_id          integer
        constraint fk50i2jege2ukupf1ynv0dq6eax
            references users,
    transaction_status varchar(255),
    transaction_time   timestamp,
    payment            integer,
    total_amount       double precision,
    payment_intent_id  varchar(255),
    payment_method     varchar(255)                                                        not null
        constraint transactions_payment_method_check
            check ((payment_method)::text = ANY
                   ((ARRAY ['WECHAT'::character varying, 'ALIPAY'::character varying])::text[]))
)
    with (orientation = row, compression = no);

alter table transactions
    owner to remote_user;

INSERT INTO public.transactions (transaction_id, product_id, buyer_id, seller_id, transaction_status, transaction_time, payment, total_amount, payment_intent_id, payment_method) VALUES (20, 24, 13, 11, 'PAID', '2024-12-16 22:12:41.936085', null, 10, null, 'ALIPAY');
INSERT INTO public.transactions (transaction_id, product_id, buyer_id, seller_id, transaction_status, transaction_time, payment, total_amount, payment_intent_id, payment_method) VALUES (21, 23, 13, 11, 'PAID', '2024-12-16 22:23:59.539081', null, 10, null, 'WECHAT');
INSERT INTO public.transactions (transaction_id, product_id, buyer_id, seller_id, transaction_status, transaction_time, payment, total_amount, payment_intent_id, payment_method) VALUES (22, 16, 11, 13, 'PENDING_PAYMENT', '2024-12-16 22:24:38.664821', null, 10, null, 'WECHAT');
