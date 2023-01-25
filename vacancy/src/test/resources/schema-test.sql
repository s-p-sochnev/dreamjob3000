set mode MySQL;

drop table if exists vacancy_skill_xref;
drop table if exists skill;
drop table if exists vacancy;
drop table if exists currency;
drop table if exists experience;
drop table if exists info;
drop table if exists users;

create table if not exists users (
    id int primary key,
    username varchar(255) unique not null,
    hashed_password varchar(255) not null,
    eligible boolean not null
);

create table if not exists info (
    last_update_date date not null default current_date()
);

create table if not exists currency (
    id int primary key,
    code varchar(20) unique not null,
    exchange_rate numeric(10,4) not null
);

create table if not exists experience (
    id int primary key,
    description varchar(255) unique not null
);

create table if not exists vacancy (
    id int not null primary key,
    title varchar(255) not null,
    salary_from int,
    salary_to int,
    currency_id int,
    description clob(10K),
    experience_id int,
    constraint fk_currency foreign key (currency_id) references currency(id),
    constraint fk_experience foreign key (experience_id) references experience(id)
);

create table if not exists skill (
    id int primary key,
    description varchar(255) unique not null
);

create table if not exists vacancy_skill_xref (
    id int primary key,
    vacancy_id int not null,
    skill_id int not null,
    constraint fk_vacancy foreign key (vacancy_id) references vacancy(id),
    constraint fk_skill foreign key (skill_id) references skill(id),
    constraint uq_vacancy_skill unique(vacancy_id, skill_id)
);
