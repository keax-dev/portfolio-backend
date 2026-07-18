drop index if exists idx_technology_deleted_position;

alter table technology
    drop column if exists technology_position;
