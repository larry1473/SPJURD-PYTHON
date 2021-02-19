Introduction:
During the first semester, in BAB2, we have the "Base de Données 1" class, where we learned about SPJRUD, SQL and many other things.
The goal of this project is to create in Python a translator of the SPJRUD language in the SQL language. The following paragraphs are how we did it.

1) The Columns:
    The first thing we did was to define a Database's columns. Each column has a name and a type, which must be a valid type in SQLite ("INTEGER", "TEXT", "REAL", "BLOB").     
    We chose to do it like this because in some requests like the selection, we needed the type of the values in the columns.

2) The Table:
    Our Table is an array of Columns. We can return its columnns and an array of its columns names. It also has 2 abstract methods. We also have a method that returns a string to create this table in SQL, and also
    the basic translation in SQL of a table that is "Select * from " , this method is abstract and defined din every request

3) The Requests:
    Each request in SPJRUD is a table on its own, so each of our request heirs from our class Table.  Since each request is a Table, we can use recursivity in our different translations.
    For example, the Projection will translate to something like this: Select distinct columns from table.translationInSql(). It was done like this so it would be easier to translate
    requests composed of different subrequests. Each of our request takes the table, the columns (a string for its name) and additional parameters.
    In each request, we use a if condition to see if the type of the table parameter is a base table or a request. Depending of that, in our translation , the tab variable is either
    the name of the Base Table or the translation in SQL of the request.

    A) Selection:
        The Selection is a request that compares a column with either a constant or another column. At  first, we tried to divide the selection in two: one for the constant and
        one for the columns but it wouldn't really be SPJRUD.  We also have an op parameter to say which operation we are doing i.e  ( = <> <= >=).
        We start by checking if the column is in the table. If it's not, we have a NameError.
        A.1) The Constant Class:
            To help us in the selection, we created the Class "Constant" which is an object containing a value. 
        In the Selection, if the compared parameter is an instant of Constant, we simply translate using the function str on it's value to have something like this:
        Select * from table where column op constant 
        If the type of the compared parameter is another column, we check if the column is in the table (If not, it gives us a name error), 
        then we check if the two columns have the same type, then we translate it like above but with the name of the second column instead of the constant.
        So we have Select * from table where column op column2
    
    B) Projection:
        The Projection takes a table and a list of columns as parameters. We start with a for loop checking if each of these columns is in the table. If a column is not, we have
        a name error saying which of them isn't in the table. If the count variable is the equal to the column array's length + 1, it means that the for loop has gone through
        the entire array without spotting a column that is not in the table.
        Then, to translate it, we used the keyword DISTINCT after our Select to prevent us from having duplicates , and a small for loop to have a string of our different columns.
        We have:
        Select DISTINCT cols from table  

    C) Join:
        The Join in SPJRUD is kind of difficult to explain. Let's say that we have a table R containing 3 columns ABC and a table S containing 3 tables BCD, when we join 
        R and S in SPJRUD, we get a table ABCD where the BC are in the R table AND in the S table. If instead of BCD we had DEF, we would get ABCDEF
        In SQL, a simple "Join" gives us the Carthesian Product of the two tables.
        So, we had to use the Natural Join  (https://www.w3resource.com/sqlite/sqlite-natural-join.php) which is equivalent to SPJRUD's join.
        We have:
        Select * from table1 NATURAL JOIN table2

    D) Rename: 
        Rename takes a table and two string as parameters (Old Name and New Name). We start by looking if the string Old Name is a column of a table. If it's not, we have an error.
        Then, we check if the New Name is not already a column of the table. (We don't want two different "Age" columns for example)
        For the translation, we did a for loop to build our string of columns so, when we reached the column with the old name, we add "AS " the new name to rename it in SQL.
        We have:
        Select col1,..., old name AS new name, ... from table
    
    E) Union:
        The union takes two tables as parameters. 
        We start by looking if these two tables have the same number of columnns, then we do a double for loop to check if every column of the first one is a column of the second
        one.
        For the translation, we just use SQLite's Union keyword.
        Select * from table1 UNION Select * from table2

    F) Difference:
        The Difference has the exact same requirements as the union.
        For the translatiton, at first we wanted to do something like "Select * from table1 where * not in table2" but, unsure if it would work, we did a for loop like in the other
        requests above to have a string, so we have "Select columnns from table1 where columnns NOT IN table2 " . We ue the NOT IN here.
        

4) Database:
    The DataBase file just consists of SQLite functions


How to use our project:
    - Create Columns and Tables in a file, don't forget to import DataBase.py, Table.py, Column.py and Request.py
    - Select the .db  file you want to work with the methods of DataBase.py
    - Add the table you've created before using DataBase.py's createTable method with the table's createTableInSQL() method as its query
    - Build your requests with the different classes of the Request.py file
    - Use the RunQuery method of DataBase.py with the translationInSql method of your request to apply the request to the SQL table 


Larry Fotso Guiffo
Maxime Nabli


