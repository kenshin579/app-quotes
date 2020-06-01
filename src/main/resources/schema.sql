#
use app_quotes;
create table authors
(
    author_id  bigint auto_increment
        primary key,
    create_dt  datetime     not null,
    updated_dt datetime     not null,
    name       varchar(255) null
) ENGINE = InnoDB;

create table folders
(
    folder_id   bigint auto_increment
        primary key,
    create_dt   datetime     not null,
    updated_dt  datetime     not null,
    folder_name varchar(255) null
) ENGINE = InnoDB;

create table login_history
(
    login_history_id bigint auto_increment
        primary key,
    device_detail    varchar(255) null,
    last_loggedin    datetime     not null
) ENGINE = InnoDB;

create table privileges
(
    privilege_id   bigint auto_increment
        primary key,
    privilege_type varchar(255) null
) ENGINE = InnoDB;

create table roles
(
    role_id   bigint auto_increment
        primary key,
    role_type varchar(255) null
) ENGINE = InnoDB;

create table roles_privileges
(
    role_id      bigint not null,
    privilege_id bigint not null,
    constraint FK5duhoc7rwt8h06avv41o41cfy
        foreign key (privilege_id) references privileges (privilege_id),
    constraint FK629oqwrudgp5u7tewl07ayugj
        foreign key (role_id) references roles (role_id)
) ENGINE = InnoDB;

create table tags
(
    tag_id     bigint auto_increment
        primary key,
    create_dt  datetime     not null,
    updated_dt datetime     not null,
    tag_name   varchar(255) null
) ENGINE = InnoDB;

create table users
(
    user_id    bigint auto_increment
        primary key,
    create_dt  datetime     not null,
    updated_dt datetime     not null,
    email      varchar(255) null,
    enabled    bit          not null,
    name       varchar(255) null,
    password   varchar(255) null,
    username   varchar(255) null
) ENGINE = InnoDB;

create table folders_users
(
    folder_user_id bigint auto_increment
        primary key,
    create_dt      datetime not null,
    updated_dt     datetime not null,
    folder_id      bigint   null,
    user_id        bigint   null,
    constraint FK1c9jqjj47bf7754qghrh3yr2m
        foreign key (folder_id) references folders (folder_id),
    constraint FKf7huv0ir08j7u37a3v75tefo2
        foreign key (user_id) references users (user_id)
) ENGINE = InnoDB;

create table quotes
(
    quote_id   bigint auto_increment
        primary key,
    create_dt  datetime     not null,
    updated_dt datetime     not null,
    quote_text varchar(255) null,
    use_yn     varchar(255) null,
    author_id  bigint       null,
    user_id    bigint       not null,
    constraint FKbwr7u11tqw8jekeijdbngy3ku
        foreign key (user_id) references users (user_id),
    constraint FKsi54dky8vtdminfhdm3iicpep
        foreign key (author_id) references authors (author_id)
) ENGINE = InnoDB;

create table folders_quotes
(
    folder_quote_id bigint auto_increment
        primary key,
    create_dt       datetime not null,
    updated_dt      datetime not null,
    folder_id       bigint   null,
    quote_id        bigint   null,
    constraint FK8tc5bjwnpho94ulwjhewmdpj2
        foreign key (folder_id) references folders (folder_id),
    constraint FKnvb0dpbf810n369efbgxw1jbg
        foreign key (quote_id) references quotes (quote_id)
) ENGINE = InnoDB;

create table likes
(
    quote_like_id bigint auto_increment
        primary key,
    create_dt     datetime not null,
    quote_id      bigint   null,
    user_id       bigint   null,
    constraint FK1a7jtt5cmd388c65sj3ihn3va
        foreign key (quote_id) references quotes (quote_id),
    constraint FKnvx9seeqqyy71bij291pwiwrg
        foreign key (user_id) references users (user_id)
) ENGINE = InnoDB;

create table quote_history
(
    quote_history_id bigint auto_increment
        primary key,
    create_dt        datetime not null,
    quoted_id        bigint   null,
    constraint FKmu28ctu8m9myolo4rx5cxt18w
        foreign key (quoted_id) references quotes (quote_id)
) ENGINE = InnoDB;

create table quotes_tags
(
    quote_tag_id bigint auto_increment
        primary key,
    create_dt    datetime not null,
    updated_dt   datetime not null,
    quote_id     bigint   null,
    tag_id       bigint   null,
    constraint FK48m021vo14c3xiun7e7sxxji5
        foreign key (tag_id) references tags (tag_id),
    constraint FKpi1dcwkd38xwkm2xs74w5oolh
        foreign key (quote_id) references quotes (quote_id)
) ENGINE = InnoDB;

create table users_roles
(
    user_id bigint not null,
    role_id bigint not null,
    constraint FK2o0jvgh89lemvvo17cbqvdxaa
        foreign key (user_id) references users (user_id),
    constraint FKj6m8fwv7oqv74fcehir1a9ffy
        foreign key (role_id) references roles (role_id)
) ENGINE = InnoDB;
