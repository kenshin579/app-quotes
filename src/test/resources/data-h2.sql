INSERT IGNORE INTO roles (role_id, role_type)
VALUES (1, 'ROLE_ADMIN');
INSERT IGNORE INTO roles (role_id, role_type)
VALUES (2, 'ROLE_USER');

INSERT IGNORE INTO privileges (privilege_id, privilege_type)
VALUES (1, 'READ_PRIVILEGE');
INSERT IGNORE INTO privileges (privilege_id, privilege_type)
VALUES (2, 'WRITE_PRIVILEGE');
INSERT IGNORE INTO privileges (privilege_id, privilege_type)
VALUES (3, 'CHANGE_PASSWORD_PRIVILEGE');

INSERT IGNORE INTO roles_privileges (role_id, privilege_id)
VALUES (1, 1);
INSERT IGNORE INTO roles_privileges (role_id, privilege_id)
VALUES (1, 2);
INSERT IGNORE INTO roles_privileges (role_id, privilege_id)
VALUES (1, 3);
INSERT IGNORE INTO roles_privileges (role_id, privilege_id)
VALUES (2, 1);
INSERT IGNORE INTO roles_privileges (role_id, privilege_id)
VALUES (2, 3);

INSERT IGNORE INTO users (user_id, create_dt, updated_dt, email, enabled, name, password, username)
VALUES (1, '2020-11-22 13:43:24', '2020-11-22 13:43:24', 'test@gmail.com', true, 'Test User',
        '$2a$10$eIYRFeQaJayoN2uPqkxH3u32J/0VxNWGa2RQR4dOaGY.y/4nF0LOK', 'testuser');
INSERT IGNORE INTO users (user_id, create_dt, updated_dt, email, enabled, name, password, username)
VALUES (2, '2020-11-22 13:43:24', '2020-11-22 13:43:24', 'sdf@sdf.com', true, 'Frank',
        '$2a$10$SV2390s2LVf3e8cVdsgwDu1q46yA5MsZKGBGxbR3X5oUF.bm4UTMm', 'kenshin579');

INSERT IGNORE INTO users_roles (user_id, role_id)
VALUES (1, 2);

INSERT IGNORE INTO authors (author_id, create_dt, updated_dt, name)
VALUES (1, '2020-12-19 21:43:09', '2020-12-19 21:43:09', 'frank');
INSERT IGNORE INTO authors (author_id, create_dt, updated_dt, name)
VALUES (2, '2020-12-19 21:44:20', '2020-12-19 21:44:20', 'joe');

INSERT IGNORE INTO folders (folder_id, create_dt, updated_dt, folder_name)
VALUES (1, '2020-11-22 13:43:24', '2020-11-22 13:43:24', 'My Quote');

INSERT IGNORE INTO quotes (quote_id, create_dt, updated_dt, quote_text, use_yn, author_id, user_id)
VALUES (1, '2020-12-19 21:43:09', '2020-12-19 21:43:09', 'this is another quote', 'Y', 1, 1);
INSERT IGNORE INTO quotes (quote_id, create_dt, updated_dt, quote_text, use_yn, author_id, user_id)
VALUES (2, '2020-12-19 21:47:26', '2020-12-19 21:47:26', 'this is a quote', 'Y', 2, 2);

INSERT IGNORE INTO tags (tag_id, create_dt, updated_dt, tag_name)
VALUES (1, '2020-12-19 21:43:09', '2020-12-19 21:43:09', 'tag1');
INSERT IGNORE INTO tags (tag_id, create_dt, updated_dt, tag_name)
VALUES (2, '2020-12-19 21:47:26', '2020-12-19 21:47:26', 'tag2');

INSERT IGNORE INTO folders_quotes (folder_quote_id, create_dt, updated_dt, folder_id, quote_id)
VALUES (1, '2020-12-19 21:43:09', '2020-12-19 21:43:09', 1, 1);
INSERT IGNORE INTO folders_quotes (folder_quote_id, create_dt, updated_dt, folder_id, quote_id)
VALUES (2, '2020-12-19 21:47:26', '2020-12-19 21:47:26', 1, 2);

INSERT IGNORE INTO folders_users (folder_user_id, create_dt, updated_dt, folder_id, user_id)
VALUES (1, '2020-11-22 13:43:24', '2020-11-22 13:43:24', 1, 1);

INSERT IGNORE INTO quotes_tags (quote_tag_id, create_dt, updated_dt, quote_id, tag_id)
VALUES (1, '2020-12-19 21:43:09', '2020-12-19 21:43:09', 1, 1);
INSERT IGNORE INTO quotes_tags (quote_tag_id, create_dt, updated_dt, quote_id, tag_id)
VALUES (2, '2020-12-19 21:47:26', '2020-12-19 21:47:26', 2, 2);



