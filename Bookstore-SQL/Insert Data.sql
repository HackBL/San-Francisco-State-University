/* Use bookstore database*/
use bookstore;

/*insect data into author table*/
INSERT INTO `bookstore`.`author` (`author_id`, `author_name`, `author_birth`) VALUES ('001', 'Pete Souza', '2001-01-01');
INSERT INTO `bookstore`.`author` (`author_id`, `author_name`, `author_birth`) VALUES ('002', 'Ray Dalio', '2002-02-02');
INSERT INTO `bookstore`.`author` (`author_id`, `author_name`, `author_birth`) VALUES ('003', 'JK Rowling', '2003-03-03');
INSERT INTO `bookstore`.`author` (`author_id`, `author_name`, `author_birth`) VALUES ('004', 'RJ Palacio', '2004-04-04');
INSERT INTO `bookstore`.`author` (`author_id`, `author_name`, `author_birth`) VALUES ('005', 'Jeff Kinney', '2005-05-05');
INSERT INTO `bookstore`.`author` (`author_id`, `author_name`, `author_birth`) VALUES ('006', 'RH Sin', '2006-06-06');
INSERT INTO `bookstore`.`author` (`author_id`, `author_name`, `author_birth`) VALUES ('007', 'Walter Isaacson', '2007-07-07');
INSERT INTO `bookstore`.`author` (`author_id`, `author_name`, `author_birth`) VALUES ('008', 'Adam Gasiewski', '2008-08-08');
INSERT INTO `bookstore`.`author` (`author_id`, `author_name`, `author_birth`) VALUES ('009', 'Wizards RPG Team', '2009-09-09');
INSERT INTO `bookstore`.`author` (`author_id`, `author_name`, `author_birth`) VALUES ('010', 'Oprah Winfrey', '2010-10-10');

/*insect data into book table*/
INSERT INTO `bookstore`.`book` (`book_id`, `book_name`, `price`, `isbn`) VALUES ('001', 'Obama: An Intimate Portrait', '30.00', '0316512583');
INSERT INTO `bookstore`.`book` (`book_id`, `book_name`, `price`, `isbn`) VALUES ('002', 'Principles: Life and Work', '11.47', '1501124021');
INSERT INTO `bookstore`.`book` (`book_id`, `book_name`, `price`, `isbn`) VALUES ('003', 'Harry Potter', '111.83', '0545044251');
INSERT INTO `bookstore`.`book` (`book_id`, `book_name`, `price`, `isbn`) VALUES ('004', 'Wonder', '6.37', '0375869026');
INSERT INTO `bookstore`.`book` (`book_id`, `book_name`, `price`, `isbn`) VALUES ('005', 'Dairy of a Wimpy', '7.50', '1419725459');
INSERT INTO `bookstore`.`book` (`book_id`, `book_name`, `price`, `isbn`) VALUES ('006', 'I hope this reaches her in time', '2.93', '1979772304');
INSERT INTO `bookstore`.`book` (`book_id`, `book_name`, `price`, `isbn`) VALUES ('007', 'Leonardo da Vinci', '14.88', '1501139150');
INSERT INTO `bookstore`.`book` (`book_id`, `book_name`, `price`, `isbn`) VALUES ('008', 'Milk and Vine', '3.79', '1973124262');
INSERT INTO `bookstore`.`book` (`book_id`, `book_name`, `price`, `isbn`) VALUES ('009', 'Xanathar\'s Guide to Everything', '29.97', '0786966114');
INSERT INTO `bookstore`.`book` (`book_id`, `book_name`, `price`, `isbn`) VALUES ('010', 'The Wisdom of Sundays', '16.79', '125138060');

/*insect data into bookstore table*/
INSERT INTO `bookstore`.`bookstore` (`store_address`, `store_name`) VALUES ('49 Ocean Ave, San Francisco, CA 94113', 'Walliam\'s Bookstore');
INSERT INTO `bookstore`.`bookstore` (`store_address`, `store_name`) VALUES ('2170 33RD AVE, San Francisco, CA 94116', 'Hans\' Bookstore');

/*insect data into customer table*/
INSERT INTO `bookstore`.`customer` (`customer_id`, `customer_name`) VALUES ('001', 'Zhenru Huang');
INSERT INTO `bookstore`.`customer` (`customer_id`, `customer_name`) VALUES ('002', 'Bo Li');
INSERT INTO `bookstore`.`customer` (`customer_id`, `customer_name`) VALUES ('003', 'Jianhao Zhong');
INSERT INTO `bookstore`.`customer` (`customer_id`, `customer_name`) VALUES ('004', 'Shenliang Wang');
INSERT INTO `bookstore`.`customer` (`customer_id`, `customer_name`) VALUES ('005', 'Wentou Ge');

/*insect data into publisher table*/
INSERT INTO `bookstore`.`publisher` (`publisher_address`, `publisher_name`) VALUES ('256 Orizaba AVE, San Francisco, CA 94115', 'Bo\'s Publisher');
INSERT INTO `bookstore`.`publisher` (`publisher_address`, `publisher_name`) VALUES ('1447 Taraval Street, San Francisco, CA94116', 'Marco\'s Publisher');

/*insect data into salesman table*/
INSERT INTO `bookstore`.`salesman` (`salesman_id`, `salesman_name`) VALUES ('001', 'Jack Ma');
INSERT INTO `bookstore`.`salesman` (`salesman_id`, `salesman_name`) VALUES ('002', 'Leo Wang');
INSERT INTO `bookstore`.`salesman` (`salesman_id`, `salesman_name`) VALUES ('003', 'Hans Huang');
INSERT INTO `bookstore`.`salesman` (`salesman_id`, `salesman_name`) VALUES ('004', 'Allen Zhong');
INSERT INTO `bookstore`.`salesman` (`salesman_id`, `salesman_name`) VALUES ('005', 'Orane Huang');

/*insect data into buy table*/
INSERT INTO `bookstore`.`buy` (`book_id`, `customer_id`, `buy_date`) VALUES ('001', '005', '2011-01-01');
INSERT INTO `bookstore`.`buy` (`book_id`, `customer_id`, `buy_date`) VALUES ('002', '004', '2012-02-02');
INSERT INTO `bookstore`.`buy` (`book_id`, `customer_id`, `buy_date`) VALUES ('003', '003', '2013-03-03');
INSERT INTO `bookstore`.`buy` (`book_id`, `customer_id`, `buy_date`) VALUES ('004', '002', '2014-04-04');
INSERT INTO `bookstore`.`buy` (`book_id`, `customer_id`, `buy_date`) VALUES ('005', '001', '2015-05-05');

/*insect data into has table*/
INSERT INTO `bookstore`.`has` (`book_id`, `store_address`) VALUES ('001', '2170 33RD AVE, San Francisco, CA 94116');
INSERT INTO `bookstore`.`has` (`book_id`, `store_address`) VALUES ('002', '2170 33RD AVE, San Francisco, CA 94116');
INSERT INTO `bookstore`.`has` (`book_id`, `store_address`) VALUES ('003', '2170 33RD AVE, San Francisco, CA 94116');
INSERT INTO `bookstore`.`has` (`book_id`, `store_address`) VALUES ('004', '2170 33RD AVE, San Francisco, CA 94116');
INSERT INTO `bookstore`.`has` (`book_id`, `store_address`) VALUES ('005', '2170 33RD AVE, San Francisco, CA 94116');
INSERT INTO `bookstore`.`has` (`book_id`, `store_address`) VALUES ('006', '49 Ocean Ave, San Francisco, CA 94113');
INSERT INTO `bookstore`.`has` (`book_id`, `store_address`) VALUES ('007', '49 Ocean Ave, San Francisco, CA 94113');
INSERT INTO `bookstore`.`has` (`book_id`, `store_address`) VALUES ('008', '49 Ocean Ave, San Francisco, CA 94113');
INSERT INTO `bookstore`.`has` (`book_id`, `store_address`) VALUES ('009', '49 Ocean Ave, San Francisco, CA 94113');
INSERT INTO `bookstore`.`has` (`book_id`, `store_address`) VALUES ('010', '49 Ocean Ave, San Francisco, CA 94113');

/*insect data into publish table*/
INSERT INTO `bookstore`.`publish` (`book_id`, `publisher_address`, `publish_date`) VALUES ('1', '1447 Taraval Street, San Francisco, CA94116', '1991-01-01');
INSERT INTO `bookstore`.`publish` (`book_id`, `publisher_address`, `publish_date`) VALUES ('2', '1447 Taraval Street, San Francisco, CA94116', '1992-02-02');
INSERT INTO `bookstore`.`publish` (`book_id`, `publisher_address`, `publish_date`) VALUES ('3', '1447 Taraval Street, San Francisco, CA94116', '1993-03-03');
INSERT INTO `bookstore`.`publish` (`book_id`, `publisher_address`, `publish_date`) VALUES ('4', '1447 Taraval Street, San Francisco, CA94116', '1994-04-04');
INSERT INTO `bookstore`.`publish` (`book_id`, `publisher_address`, `publish_date`) VALUES ('5', '1447 Taraval Street, San Francisco, CA94116', '1995-05-05');
INSERT INTO `bookstore`.`publish` (`book_id`, `publisher_address`, `publish_date`) VALUES ('6', '256 Orizaba AVE, San Francisco, CA 94115', '1996-06-06');
INSERT INTO `bookstore`.`publish` (`book_id`, `publisher_address`, `publish_date`) VALUES ('7', '256 Orizaba AVE, San Francisco, CA 94115', '1997-07-07');
INSERT INTO `bookstore`.`publish` (`book_id`, `publisher_address`, `publish_date`) VALUES ('8', '256 Orizaba AVE, San Francisco, CA 94115', '1998-08-08');
INSERT INTO `bookstore`.`publish` (`book_id`, `publisher_address`, `publish_date`) VALUES ('9', '256 Orizaba AVE, San Francisco, CA 94115', '1999-09-09');
INSERT INTO `bookstore`.`publish` (`book_id`, `publisher_address`, `publish_date`) VALUES ('10', '256 Orizaba AVE, San Francisco, CA 94115', '2000-10-10');

/*insect data into sale table*/
INSERT INTO `bookstore`.`sell` (`salesman_id`, `book_id`) VALUES ('5', '1');
INSERT INTO `bookstore`.`sell` (`salesman_id`, `book_id`) VALUES ('4', '2');
INSERT INTO `bookstore`.`sell` (`salesman_id`, `book_id`) VALUES ('3', '3');
INSERT INTO `bookstore`.`sell` (`salesman_id`, `book_id`) VALUES ('2', '4');
INSERT INTO `bookstore`.`sell` (`salesman_id`, `book_id`) VALUES ('1', '5');

/*insert data into work table*/
INSERT INTO `bookstore`.`work` (`salesman_id`, `store_address`, `since`) VALUES ('1', '2170 33RD AVE, San Francisco, CA 94116', '2008-12-01');
INSERT INTO `bookstore`.`work` (`salesman_id`, `store_address`, `since`) VALUES ('2', '2170 33RD AVE, San Francisco, CA 94116', '2008-12-02');
INSERT INTO `bookstore`.`work` (`salesman_id`, `store_address`, `since`) VALUES ('3', '49 Ocean Ave, San Francisco, CA 94113', '2008-12-03');
INSERT INTO `bookstore`.`work` (`salesman_id`, `store_address`, `since`) VALUES ('4', '49 Ocean Ave, San Francisco, CA 94113', '2008-12-04');
INSERT INTO `bookstore`.`work` (`salesman_id`, `store_address`, `since`) VALUES ('5', '49 Ocean Ave, San Francisco, CA 94113', '2008-12-05');

/*insert data into write table*/
INSERT INTO `bookstore`.`writes` (`book_id`, `author_id`, `writes_year`) VALUES ('1', '1', '2017-04-01');
INSERT INTO `bookstore`.`writes` (`book_id`, `author_id`, `writes_year`) VALUES ('2', '2', '2017-01-06');
INSERT INTO `bookstore`.`writes` (`book_id`, `author_id`, `writes_year`) VALUES ('3', '3', '2017-06-06');
INSERT INTO `bookstore`.`writes` (`book_id`, `author_id`, `writes_year`) VALUES ('4', '4', '2017-07-09');
INSERT INTO `bookstore`.`writes` (`book_id`, `author_id`, `writes_year`) VALUES ('5', '5', '2017-01-01');
INSERT INTO `bookstore`.`writes` (`book_id`, `author_id`, `writes_year`) VALUES ('6', '6', '2016-01-01');
INSERT INTO `bookstore`.`writes` (`book_id`, `author_id`, `writes_year`) VALUES ('7', '7', '2017-01-04');
INSERT INTO `bookstore`.`writes` (`book_id`, `author_id`, `writes_year`) VALUES ('8', '8', '2017-01-25');
INSERT INTO `bookstore`.`writes` (`book_id`, `author_id`, `writes_year`) VALUES ('9', '9', '2017-01-09');
INSERT INTO `bookstore`.`writes` (`book_id`, `author_id`, `writes_year`) VALUES ('10', '10', '2017-05-01');
