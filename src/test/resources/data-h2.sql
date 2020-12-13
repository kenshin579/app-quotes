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

# user 추가하기

# 기본 폴더하나와 명언 추가하기



