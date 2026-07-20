alter table profile
    alter column profile_cv type varchar(2048);

alter table profile
    add column if not exists profile_cv_es varchar(2048);

update profile
set profile_cv_es = profile_cv
where profile_cv_es is null;
