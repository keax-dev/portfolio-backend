alter table course
    add column course_position integer;

update course current_course
set course_position = (
    select count(*)
    from course ordered_course
    where ordered_course.course_id <= current_course.course_id
);

alter table course
    alter column course_position set not null;

alter table course
    add constraint chk_course_position_positive
        check (course_position > 0);

create index idx_course_deleted_position
    on course (course_deleted, course_position);
