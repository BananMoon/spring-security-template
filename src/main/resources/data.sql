-- SUCCEED CASE, password: moon1234
INSERT INTO account (id, email, password, locked, use_yn, auth)
VALUES (1L, 'moon@a.com', '$2a$10$RjH3nYPmkgvekdHjwtxWIeZow/eSU2mpse7Angv.Zg8HfuTJBMvyS',
        false, 'N', 'GENERAL');
-- FAILED CASE, password: moon4321
INSERT INTO account (id, email, password, locked, use_yn, auth)
VALUES (2L, 'moon2@a.com', '$2a$10$.8BTqDWc4gUrwY8uZPZG/uNAo57m69Ptt1Ned3v7k38O1UiAZNyYi',
    true, 'Y', 'SUPER')
