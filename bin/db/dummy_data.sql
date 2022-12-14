# user dummy data (login test)
insert into user (user_id, password, name, phone, activated, role, register_date)
values ('test@test.com', '$2a$12$f5LnsHFy.cx28ekpAQfmouMu/Sq28sFAPYeRKqFcFftQKoWs7Dcu6', '테스트', '01022477742', 1, 'ROLE_TEACHER', now());