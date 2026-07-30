alter table course
    rename column course_certificate_url to course_certificate_img;

alter table course
    add column course_name_en varchar(200);

update course
set course_name_en = course_name
where course_name_en is null or trim(course_name_en) = '';

alter table course
    alter column course_name_en set not null;

alter table course
    add column course_certificate_url varchar(2048);
