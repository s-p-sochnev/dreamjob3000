insert ignore into currency (code, exchange_rate) values ('RUR', 1);
insert ignore into currency (code, exchange_rate) values ('USD', 60);
insert ignore into currency (code, exchange_rate) values ('EUR', 62);
insert ignore into info (last_update_date) values (dateadd('DAY', -8, current_date()));

insert ignore into users (username, hashed_password, eligible) values ('admin', '$2a$11$sj61S6aqwivbA1AIaHHYOO3ZlHd1Dp6T65af8jeY2wzWcyKFgOzk2', true);
