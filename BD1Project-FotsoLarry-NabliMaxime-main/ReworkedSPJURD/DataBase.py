from sqlite3 import *


class DataBase:
    """plays the role of a database and def certain methods from sqlite"""
    def __init__(self, conn=None, relationFile="", cur=None):
        self.conn = conn
        self.relationFile = relationFile
        self.cur = cur

    def connect(self):
        """ create the connection for the data base"""
        return connect(self.relationFile)

    def getDB(self):
        """ creating the database """
        self.conn = self.connect()
        return self.conn

    def getCursor(self):
        """ return the cursor object """
        self.cur = self.getDB().cursor()
        return self.cur

    def createTable(self, query):
        """ sql method to create a table"""
        self.getCursor().execute(query)

    def RunQuery(self, query):
        """ method to run the user query using the sql module """
        self.getCursor().execute(query)

    def multipleExecution(self, query, tab):
        self.getCursor().executemany(query, tab)

    def save(self):
        """ saves the modification made by the user"""
        self.getDB().commit()
    def closeConncetion(self):
        """ ends the connection when the user is done using the database """
        self.getDB().close()


































