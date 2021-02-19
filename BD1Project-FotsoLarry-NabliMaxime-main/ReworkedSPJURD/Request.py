""" In this file, we wrote every request's class plus a method to check if it's valid, plus one to translate """
from Table import *
from Column  import *

class Constant:
    """ Class made specifically  for the Selection """
    def __init__(self, value):
        self.value  = value

    def getValue(self):
        return self.value

    

class Select(Table):
    """
    The S of SPJRUD: Selection
    """
    
    def __init__(self, table, col, comp, op):
        """  Col must be a str, compt must be a  Constant or a str too  """
        check = self.checkRequest(table, col, comp, op)
        if check == 1:
            raise NameError("Column 1 not in the table")
            
        elif check == 2:
            raise SyntaxError("Operator not valid")
        elif check == 3:
            raise NameError("Column 2 not in the table")
        else:
            self.table = table
            self.col = col
            self.comp = comp
            self.op = op
        if isinstance(comp, Constant):
            self.compared = str(comp.getValue())
        elif isinstance(comp, str):
            self.compared = comp



    def checkRequest(self, table, col, comp, op):
        opList = ["=", "<>",  "<", ">", ">=", "<="]
        ColsNames = table.getColumnsNames()
        print(ColsNames)
        print(col)
        print(col in ColsNames)
        if col not in ColsNames:
            return 1
        if op not in opList:
            return 2
        if isinstance(comp, str):
            if comp not in ColsNames:
                return 3

        else:
            return 4
        
        
    def translationInSQL(self):
        if str(type(self.table) == "<class 'Table.Table'>"):
            tab = self.table.name
        else:
            tab = self.table.translationInSQL()
        s = "SELECT * FROM " + tab + " WHERE " + self.col + + " " + self.op + " " + self.compared + ";"
        return s
    

class Projection(Table):
    """
    The P of SPJRUD
    """
    def __init__(self, table, columns = []):
        """ columns must be a list of str that are the name of the columns """
        check = self.checkRequest(table, columns)
        if check != (len(columns) + 1):
            raise NameError("Column " + str(check) + " not in the table")
        else:
            self.table = table
            self.columns = columns


    def checkRequest(self, table, columns):
        ColsNames = table.getColumnsNames()
        count = 0
        for col in columns:
            count += 1
            if col not in ColsNames:
                return count
        count += 1
        return count 
    
    def translationInSQL(self):
        c = len(self.columns)
        co = ""
        for i in range(c):
            co += self.columns[i]
            if i != (c-1):
                co += ", "
        if str(type(self.table) == "<class 'Table.Table'>"):
            tab = self.table.name
        else:
            tab = self.table.translationInSQL()
        """ We used DISTINCT to prevent us from having duplicates """
        s = "SELECT DISTINCT " + co + " FROM " + tab + ";" 
        return s 

class Join(Table):
    """
    The J of SPJRUD 
    """
    def __init__(self, table1, table2):
        self.table1 = table1
        self.table2 = table2

    def translationInSQL(self):
        """ We used Natural Join because it's the join that looks the most like SPJRUD's join """
        if str(type(self.table1) == "<class 'Table.Table'>"):
            tab1 = self.table1.name
        else:
            tab1 = self.table1.translationInSQL()
        
        if str(type(self.table2) == "<class 'Table.Table'>"):
            tab2 = self.table2.name
        else:
            tab2 = self.table2.translationInSQL()
        s = "SELECT * FROM " + tab1 + " NATURAL JOIN " + tab2 + ";" 
        return s

class Rename(Table):
    """
    The R of SPJRUD
    """
    def __init__(self, table, old_name = "", new_name = ""):
        check = self.checkRequest(table, old_name, new_name)
        if check == 1:
            raise NameError(old_name + " isn't a column")
        elif check == 2:
            raise NameError("A column is already named like this")
        else:
            self.table = table
            self.old_name = old_name
            self.new_name = new_name


    def checkRequest(self, table, old_name, new_name):
        if old_name not in table.getColumnsNames():
            return 1
        elif new_name in table.getColumnsNames():
            return 2
        else: 
            return 3

    def translationInSQL(self):
        cols = self.table.getColumnsNames()
        s = "SELECT "
        count  = 0
        for i in range (len(cols)):
            count += 1
            if cols[i] == self.old_name:
                s += self.old_name + " AS " + self.new_name
                if count != len(cols):
                    s += ", "
            else:
                s += cols[i]
                if count != len(cols):
                    s += ", "
        
        if str(type(self.table) == "<class 'Table.Table'>"):
            tab = self.table.name
        else:
            tab = self.table.translationInSQL()
        s += " FROM " + tab + ";"
        return s



class Union(Table):
    """
    The U of SPJRUD
    """
    def __init__(self, table1, table2):
        check = self.checkRequest(table1, table2)
        if check == 1:
            raise ValueError("The two tables don't have the same number of columns")
        elif check == 2:
            raise NameError("The two tables have different columns names")
        else:
            self.table1 = table1
            self.table2 = table2

    def checkRequest(self, table1, table2):
        Cols1 = table1.getColumnsNames()
        Cols2 = table2.getColumnsNames()
        if len(Cols1) != len(Cols2):
            return 1
        else:
            for i in range(len(Cols1)):
                    if Cols1[i] not in Cols2:
                        return 2
            return 3 

    
    def translationInSQL(self):
        if str(type(self.table1) == "<class 'Table.Table'>"):
            tab1 = self.table1.name
        else:
            tab1 = self.table1.translationInSQL()
        
        if str(type(self.table2) == "<class 'Table.Table'>"):
            tab2 = self.table2.name
        else:
            tab2 = self.table2.translationInSQL()
        s = "SELECT * FROM " + tab1 + " UNION SELECT * FROM " + tab2 + ";" 
        return s

class Difference(Table):
    """
    The D of SPJRUD,  has the same constraint as the Union
    """
    def __init__(self, table1, table2):
        check = self.checkRequest(table1, table2)
        if check == 1:
            raise ValueError("The two tables don't have the same number of columns")
        elif check == 2:
            raise NameError("The two tables have different columns names")
        else:
            self.table1 = table1
            self.table2 = table2

    def checkRequest(self, table1, table2):
        Cols1 = table1.getColumnsNames()
        Cols2 = table2.getColumnsNames()
        if len(Cols1) != len(Cols2):
            return 1
        else:
            for i in range(len(Cols1)):
                if Cols1[i] != Cols2[i]:
                    return 2
            return 3 

    def translationInSQL(self):
        """ At first, I was thinking of doing something  like  SELECT * FROM table 1 WHERE * NOT IN table2 but since I wasn't sure  """
        cols = self.table.getColumnsNames()
        count  = 0
        colsName = ""
        for i in range (len(cols)):
            count += 1
            colsName += cols[i]
            if count != len(cols):
                colsName += ", "
        if str(type(self.table1) == "<class 'Table.Table'>"):
            tab1 = self.table1.name
        else:
            tab1 = self.table1.translationInSQL()
        
        if str(type(self.table2) == "<class 'Table.Table'>"):
            tab2 = self.table2.name
        else:
            tab2 = self.table2.translationInSQL()
        s = "SELECT " + colsName + " FROM " + tab1 + " WHERE " + colsName + " NOT IN " +  tab2 + ";" 
        return s 

        