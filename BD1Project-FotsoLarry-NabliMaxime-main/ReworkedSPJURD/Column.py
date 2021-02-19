class  Column:

    def __init__(self, colName, colType):
        self.setColName(colName)
        self.setColType(colType)

    def  __str__(self):
        return self.colName

    def setColName(self, colName):
        """To give a name the column """
        self.colName = colName

    def getColName(self):
        return self.colName
    
    
    def setColType(self, colType):
        """ We give a type to the column, as an example the column age would be Integer type, types in SQl tables can only be in the sqlTypes list"""
        sqlTypes = ["INTEGER", "TEXT", "REAL", "BLOB"]
        if colType not in sqlTypes:
            raise TypeError("This type is invalid in SQLite")
        else:
            self.colType = colType

    def getColType(self):
        return self.colType  



    

    
