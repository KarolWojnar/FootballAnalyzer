INSERT INTO role (role_name)
VALUES ('ADMIN');
INSERT INTO role (role_name)
VALUES ('TRENER');
INSERT INTO role (role_name)
VALUES ('ANALITYK');

INSERT INTO users (uuid, login, password, role, email, isLocked, isEnabled)
VALUES ('uuid', 'admin', '$2a$12$.cQNsTEWaS1hN3GPEqM57OgZ2Qe55vgzV4BUKTehSb3DRywAD5PBW', 'ADMIN', 0, 1);