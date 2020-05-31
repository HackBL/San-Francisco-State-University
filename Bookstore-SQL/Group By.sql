use bookstore;

/*how many books sold on 2011-01-01*/
select B.buy_date, count(*)
from buy B
group by B.buy_date
having B.buy_date = '2011-01-01' ;

/*find books under 30 dollars*/
select B.book_name, B.price as cheapestPrice 
from book B 
group by B.book_name 
having B.price < 30; 

