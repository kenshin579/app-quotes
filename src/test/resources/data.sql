INSERT INTO roles (role_id, role_type) VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles (role_id, role_type) VALUES (2, 'ROLE_USER');

INSERT INTO privileges (privilege_id, privilege_type) VALUES (1, 'READ_PRIVILEGE');
INSERT INTO privileges (privilege_id, privilege_type) VALUES (2, 'WRITE_PRIVILEGE');
INSERT INTO privileges (privilege_id, privilege_type) VALUES (3, 'CHANGE_PASSWORD_PRIVILEGE');

INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 1);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 2);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 3);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 1);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 3);

INSERT INTO users (user_id, username, create_dt, updated_dt, enabled) VALUES(1, 'frank', CURRENT_TIME, CURRENT_TIME, true);

INSERT INTO folders (folder_id, folder_name, create_dt, updated_dt) VALUES (1, 'test_folder1', CURRENT_TIME, CURRENT_TIME);
INSERT INTO folders (folder_id, folder_name, create_dt, updated_dt) VALUES (2, 'test_folder2', CURRENT_TIME, CURRENT_TIME);

INSERT INTO folders_users(folder_id, user_id, create_dt, updated_dt) VALUES(1, 1, CURRENT_TIME, CURRENT_TIME);


