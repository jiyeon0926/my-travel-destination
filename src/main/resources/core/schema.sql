create database my_travel_destination

create table my_travel_destination.user (
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

create table my_travel_destination.partner (
	id bigint primary key auto_increment,
	user_id bigint not null,
	business_number varchar(50) unique not null,
	address varchar(255) not null,
	created_at datetime not null,
	updated_at datetime not null
);

alter table my_travel_destination.partner add foreign key (user_id) references my_travel_destination.user (id);

create table my_travel_destination.ticket (
	id bigint primary key auto_increment,
	user_id bigint not null,
	name varchar(50) not null,
	sale_start_date datetime not null,
	sale_end_date datetime,
	base_price integer,
	phone varchar(20) not null,
	address varchar(255) not null,
	description text,
	status varchar(30) not null,
	created_at datetime not null,
	updated_at datetime not null
);

alter table my_travel_destination.ticket add foreign key (user_id) references my_travel_destination.user (id);

create table my_travel_destination.ticket_option (
	id bigint primary key auto_increment,
	ticket_id bigint not null,
	name varchar(100) not null,
	price int not null,
	created_at datetime not null,
	updated_at datetime not null
);

alter table my_travel_destination.ticket_option add foreign key (ticket_id) references my_travel_destination.ticket (id);

create table my_travel_destination.ticket_schedule (
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

alter table my_travel_destination.ticket_schedule add foreign key (ticket_id) references my_travel_destination.ticket (id);

create table my_travel_destination.ticket_image (
	id bigint primary key auto_increment,
	ticket_id bigint not null,
	image_url varchar(500) not null,
	file_name varchar(255) not null,
	is_main boolean not null default false,
	created_at datetime not null
);

alter table my_travel_destination.ticket_image add foreign key (ticket_id) references my_travel_destination.ticket (id);

create table my_travel_destination.reservation (
	id bigint primary key auto_increment,
	user_id bigint not null,
	ticket_schedule_id bigint not null,
	reservation_number varchar(20) unique not null,
	total_amount int not null,
	reservation_name varchar(50) not null,
	reservation_phone varchar(20) not null,
	status varchar(30) not null,
	cancelled_at datetime,
	created_at datetime not null,
	updated_at datetime not null
);

alter table my_travel_destination.reservation add foreign key (user_id) references my_travel_destination.user (id);
alter table my_travel_destination.reservation add foreign key (ticket_schedule_id) references my_travel_destination.ticket_schedule (id);

create table my_travel_destination.reservation_option (
	id bigint primary key auto_increment,
	reservation_id bigint not null,
	ticket_option_id bigint not null,
	quantity int not null,
	unit_price int not null,
	total_price int not null,
	created_at datetime not null
);

alter table my_travel_destination.reservation_option add foreign key (reservation_id) references my_travel_destination.reservation (id);
alter table my_travel_destination.reservation_option add foreign key (ticket_option_id) references my_travel_destination.ticket_option (id);

create table my_travel_destination.payment (
	id bigint primary key auto_increment,
	reservation_id bigint not null,
	amount int not null,
	payment_method varchar(50) not null,
	payment_gateway varchar(50) not null,
	transaction_id varchar(100) not null,
	status varchar(30) not null,
	paid_at datetime,
	created_at datetime not null,
	updated_at datetime not null
);

alter table my_travel_destination.payment add foreign key (reservation_id) references my_travel_destination.reservation (id);

create table my_travel_destination.blog (
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

alter table my_travel_destination.blog add foreign key (user_id) references my_travel_destination.user (id);

create table my_travel_destination.blog_image (
	id bigint primary key auto_increment,
	blog_id bigint not null,
	image_url varchar(500) not null,
	file_name varchar(255) not null,
	created_at datetime not null
);

alter table my_travel_destination.blog_image add foreign key (blog_id) references my_travel_destination.blog (id);

create table my_travel_destination.blog_ticket_item (
	id bigint primary key auto_increment,
	blog_id bigint not null,
	reservation_id bigint not null,
	created_at datetime not null
);

alter table my_travel_destination.blog_ticket_item add foreign key (blog_id) references my_travel_destination.blog (id);
alter table my_travel_destination.blog_ticket_item add foreign key (reservation_id) references my_travel_destination.reservation (id);

-- 관리자 (admin, admin)
insert into my_travel_destination.user (email, password, display_name, phone, role, is_deleted, created_at, updated_at)
values ('admin', '$2a$10$VPrBFOayEQd.JqkxnLPkGO2KUPnSRD0mjA1mXCVQr/i0kGohCBM5y', '관리자', '01012345678', 'ADMIN', 0, now(), now())