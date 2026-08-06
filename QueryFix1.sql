SELECT  * FROM courses;

CREATE TABLE courses_backup_periodid AS SELECT id, period_id FROM courses;
SHOW CREATE TABLE courses;

ALTER TABLE courses DROP FOREIGN KEY FKsnysrgxngn3kcp2kjy0abu510;
ALTER TABLE courses DROP COLUMN period_id;