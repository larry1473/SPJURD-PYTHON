import sqlite3
class DataBase:
    """plays the role of a database and def certain methods from sqlite"""
    def __init__(self):
        self.tables = []
        self.conn = None;
        self.database = "";

    def exits(self,table):
        """ checks if a relation belongs to the database """

        return table in self.tables

    def execute(self,query):
        """ executes the translated sql query"""
        pass

    def commit(self):
        pass
    def connect(self):
        if len(self.tables == 0):
            raise IOError;

    def connect(self):
        pass
    def close(self):
        pass


    def showTables(self):
        """ show all the tables in the database"""
        for x in self.tables:
            print(x)

