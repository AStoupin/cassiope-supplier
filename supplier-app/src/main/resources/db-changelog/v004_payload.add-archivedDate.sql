--liquibase formatted sql

--changeset Art:1586032267021-28
ALTER TABLE PUBLIC.PAYLOAD
 ADD COLUMN archivedDate date
