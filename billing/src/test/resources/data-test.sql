insert into users (id, username, hashed_password, balance) values (1, '1',  '$2a$11$VdOj/hsL8ao2RJB7dBUPRe5D3CKTRTdGRUJczPt6jFFBB2xt5sRiq', 30);

insert into payment (user_id, payment_time, amount) values (1, parsedatetime('2022 09 03 00:00', 'yyyy MM dd HH:mm'), 10);
insert into payment (user_id, payment_time, amount) values (1, parsedatetime('2022 09 03 00:00', 'yyyy MM dd HH:mm'), 20);

insert into statistics_endpoint (id, endpoint, price) values (1, '/statistics/salary', 10);
insert into statistics_endpoint (id, endpoint, price) values (2, '/statistics/skills', 20);

insert into statistics_request (user_id, endpoint_id, request_time, price) values (1, 1, parsedatetime('2022 09 03 00:00', 'yyyy MM dd HH:mm'), 10);
insert into statistics_request (user_id, endpoint_id, request_time, price) values (1, 2, parsedatetime('2022 09 03 00:00', 'yyyy MM dd HH:mm'), 20);
