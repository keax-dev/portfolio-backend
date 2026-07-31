alter table institution add column version bigint not null default 0;
alter table education add column version bigint not null default 0;
alter table skill add column version bigint not null default 0;
alter table technology add column version bigint not null default 0;
alter table social_network add column version bigint not null default 0;
alter table project add column version bigint not null default 0;
alter table course add column version bigint not null default 0;
alter table profile add column version bigint not null default 0;
