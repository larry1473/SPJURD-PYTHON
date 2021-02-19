from Column import *
import abc 

class Table(abc.ABC):

    
    def __init__(self, columns = [], name = ""):
        """ # columns is the list of the table's colums, example: Name | Age | Height """
        self.name = name
        self.columns = columns


    def getColumns(self):
        return self.columns

    def createTableInSql(self):
        """ Pour créer la table "originelle" en SQl """
        s = "CREATE TABLE " + self.name + " (\n"
        for i in range (len(self.columns)):
            s += self.columns[i].getColName() + " " + self.columns[i].getColType() 
            if  i != (len(self.columns) - 1):
                s += ","
            s += "\n"
        s += ");"
        return s
        
    def setColumns(self, columns):
        self.columns = columns
    
    def getColumnsNames(self):
        """ This will make us able to see if a Column exists for the Projection per example """
        ColsName = []
        for i in range(len(self.columns)):
            ColsName.append(self.columns[i].getColName())
        return ColsName 
    




        


    


            
            



    