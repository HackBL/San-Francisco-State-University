/* Use bookstore database*/
use bookstore;

/*　Entity */

create table Author(
	author_id int not null,
	author_name varchar(255), 
	author_birth date,
	primary key (author_id)
);

create table Book(
	book_id int not null,
	book_name varchar(255),
	price Double,
	isbn varchar(255),
	primary key (book_id)
);

create table Bookstore(
	store_address varchar(255) not null,
	store_name varchar(255),
	primary key (store_address)
);

create table Buy (
	book_id int not null,
	customer_id int not null,
	buy_date date,

	primary key (book_id, customer_id),
	foreign key (book_id) references Book(book_id),
	foreign key (customer_id) references Customer(customer_id)
);

create table Customer(
	customer_id int not null,
	customer_name varchar(255), 
	primary key (customer_id)
);

create table Publisher(
	publisher_address varchar(255) not null,
	publisher_name varchar(255),
	primary key (publisher_address)
);

create table Salesman(
	salesman_id int not null,
	salesman_name varchar(255), 
	primary key (salesman_id)
);


/* Relation */

create table Has (
	book_id int not null,
	store_address varchar(255) not null,

	primary key (book_id, store_address),
	foreign key (book_id) references Book(book_id),
	foreign key (store_address) references Bookstore(store_address)
);

create table Buy (
	book_id int not null,
	customer_id int not null,
	buy_date date,

	primary key (book_id, customer_id),
	foreign key (book_id) references Book(book_id),
	foreign key (customer_id) references Customer(customer_id)
);

create table Sell(
	salesman_id int not null,
	book_id int not null,

	primary key (salesman_id, book_id),
	foreign key (salesman_id) references Salesman(salesman_id),
	foreign key (book_id) references Book(book_id)
    
);

create table Work (
	salesman_id int not null,
	store_address varchar(255) not null,
	since date,

	primary key (salesman_id, store_address),
	foreign key (salesman_id) references Salesman(salesman_id),
	foreign key (store_address) references Bookstore(store_address)
);

create table Publish(
	book_id int not null,
	publisher_address varchar(255) not null,
	publish_date DATE,

	primary key (book_id, publisher_address),
	foreign key (book_id) references Book(book_id),
	foreign key (publisher_address) references Publisher(publisher_address)
	on delete cascade
);

create table Writes (
	book_id int not null,
	author_id int not null,	
	writes_year date,
	
	primary key (book_id, author_id),
	foreign key (book_id) references Book(book_id),
	foreign key (author_id) references Author(author_id)
);
