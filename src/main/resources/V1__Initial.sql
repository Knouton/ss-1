create schema "security_test";
set schema "security_test";
create table "security_test"."users" (
                       "id"                    bigserial,
                       "name"                  varchar(30) not null unique,
                       "password"              varchar(80) not null,
                       primary key ("id")
);

create table "security_test"."roles" (
                       "id"                    serial,
                       "name"                  varchar(50) not null,
                       primary key ("id")
);

CREATE TABLE "security_test"."users_roles" (
                             "user_id"               bigint not null,
                             "role_id"               int not null,
                             primary key ("user_id", "role_id"),
                             foreign key ("user_id") references "users" ("id"),
                             foreign key ("role_id") references "roles" ("id")
);

insert into "security_test"."roles" ("name")
values ('ROLE_USER'), ('ROLE_ADMIN');

insert into "security_test"."users" ("name", "password")
values
    ('user', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i'),
    ('admin', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i');

insert into "security_test"."users_roles" ("user_id", "role_id")
values
    (1, 1),
    (2, 2);
