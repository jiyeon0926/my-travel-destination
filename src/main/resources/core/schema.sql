create database if not exists my_travel_destination;
use my_travel_destination;

create table user (
	id bigint primary key auto_increment,
	email varchar(255) unique not null,
	password varchar(200) not null,
	display_name varchar(60) not null,
	phone varchar(20) not null,
	role varchar(30) not null,
	is_deleted boolean not null default false,
	created_at datetime not null,
	updated_at datetime not null
);

create table partner (
	id bigint primary key auto_increment,
	user_id bigint not null,
	business_number varchar(50) unique not null,
	address varchar(255) not null,
	created_at datetime not null,
	updated_at datetime not null
);

alter table partner add foreign key (user_id) references user (id);

create table ticket (
	id bigint primary key auto_increment,
	user_id bigint not null,
	name varchar(50) not null,
	sale_start_date datetime not null,
	sale_end_date datetime not null,
	base_price integer,
	phone varchar(20) not null,
	address varchar(255) not null,
	description text,
	sale_status varchar(30) not null,
	created_at datetime not null,
	updated_at datetime not null
);

alter table ticket add foreign key (user_id) references user (id);

create table ticket_option (
	id bigint primary key auto_increment,
	ticket_id bigint not null,
	name varchar(100) not null,
	price int not null,
	created_at datetime not null,
	updated_at datetime not null
);

alter table ticket_option add foreign key (ticket_id) references ticket (id);

create table ticket_schedule (
	id bigint primary key auto_increment,
	ticket_id bigint not null,
	is_active boolean not null default true,
	start_date date not null,
	start_time time,
	quantity int not null,
	remaining_quantity int not null,
	created_at datetime not null,
	updated_at datetime not null
);

alter table ticket_schedule add foreign key (ticket_id) references ticket (id);

create table ticket_image (
	id bigint primary key auto_increment,
	ticket_id bigint not null,
	image_url varchar(500) not null,
	image_key varchar(500) not null,
	file_name varchar(255) not null,
	is_main boolean not null default false,
	created_at datetime not null,
	updated_at datetime not null
);

alter table ticket_image add foreign key (ticket_id) references ticket (id);

create table reservation (
	id bigint primary key auto_increment,
	user_id bigint not null,
	ticket_schedule_id bigint not null,
	reservation_number varchar(20) unique not null,
	total_quantity int not null,
	total_amount int not null,
	reservation_name varchar(50) not null,
	reservation_phone varchar(20) not null,
	status varchar(30) not null,
	cancelled_at datetime,
	created_at datetime not null,
	updated_at datetime not null
);

alter table reservation add foreign key (user_id) references user (id);
alter table reservation add foreign key (ticket_schedule_id) references ticket_schedule (id);

create table reservation_option (
	id bigint primary key auto_increment,
	reservation_id bigint not null,
	ticket_option_id bigint not null,
	quantity int not null,
	unit_price int not null,
	total_price int not null,
	created_at datetime not null
);

alter table reservation_option add foreign key (reservation_id) references reservation (id);
alter table reservation_option add foreign key (ticket_option_id) references ticket_option (id);

create table payment (
	id bigint primary key auto_increment,
	reservation_id bigint not null,
	amount int not null,
	quantity int not null,
	payment_method varchar(50),
	tid varchar(100) not null,
	status varchar(30) not null,
	approved_at datetime,
	created_at datetime not null,
	updated_at datetime not null
);

alter table payment add foreign key (reservation_id) references reservation (id);

create table blog (
	id bigint primary key auto_increment,
	user_id bigint not null,
	title varchar(50) not null,
	content text not null,
	travel_start_date date not null,
	travel_end_date date not null,
	estimated_expense int not null,
	total_expense int not null,
	created_at datetime not null,
	updated_at datetime not null
);

alter table blog add foreign key (user_id) references user (id);

create table blog_image (
	id bigint primary key auto_increment,
	blog_id bigint not null,
	image_url varchar(500) not null,
    image_key varchar(500) not null,
    file_name varchar(255) not null,
    is_main boolean not null default false,
    created_at datetime not null,
    updated_at datetime not null
);

alter table blog_image add foreign key (blog_id) references blog (id);

create table blog_ticket_item (
	id bigint primary key auto_increment,
	blog_id bigint not null,
	reservation_id bigint not null,
	created_at datetime not null
);

alter table blog_ticket_item add foreign key (blog_id) references blog (id);
alter table blog_ticket_item add foreign key (reservation_id) references reservation (id);

-- 관리자 (admin, admin)
insert into user (email, password, display_name, phone, role, is_deleted, created_at, updated_at)
values ('admin', '$2a$10$VPrBFOayEQd.JqkxnLPkGO2KUPnSRD0mjA1mXCVQr/i0kGohCBM5y', '관리자', '01012345678', 'ADMIN', 0, now(), now())