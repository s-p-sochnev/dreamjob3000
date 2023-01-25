insert ignore into users (username, hashed_password, balance) values ('admin', '$2a$11$sj61S6aqwivbA1AIaHHYOO3ZlHd1Dp6T65af8jeY2wzWcyKFgOzk2', 30);

insert ignore into payment (user_id, amount) values (1, 10);
insert ignore into payment (user_id, amount) values (1, 20);

insert ignore into statistics_endpoint (endpoint, price) values ('/statistics/salary', 10);
insert ignore into statistics_endpoint (endpoint, price) values ('/statistics/skills', 20);

insert ignore into statistics_request (user_id, endpoint_id, price) values (1, 1, 10);
insert ignore into statistics_request (user_id, endpoint_id, price) values (1, 2, 20);
