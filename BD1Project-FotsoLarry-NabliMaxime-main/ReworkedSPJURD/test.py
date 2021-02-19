import unittest
from Request import *
from Table import *
from Column import *
from DataBase import *

class Test(unittest.TestCase):

    def testSelect(self):
        self.assertRaises()

    # # PROJECTION  unit tests
    # def testCheckProject(self):
    #     self.assertRaises(SameColumnProjection(),Projection.checkOpt())
    # def ProjectionTranslation(self):
    #     pass

    # #TABLES  unit tests

    # def TestIsIncols(self):
    #     self.assertAlmostEqual(Tables.isInCols("K"), True)


A = Column("A", "INTEGER")
B = Column("B", "INTEGER")
C = Column("C", "TEXT")
D = Column("D", "TEXT")
table1 =  Table([A, B, C, D], "test1c")

E = Column("E", "INTEGER")
F = Column("F", "INTEGER")
G = Column("G", "INTEGER")
H = Column("H", "INTEGER")
table2 = Table([E, F, G, H], "test2")
table3 = Table([E, F, H], "test3")

""" To test the Selection: first to test if the operator is valid, second to test if both columns have the same type
a = Select(table1, "A", "B", "r") 
"""

""" To test the Projection of a not valid Column
b = Projection(table1, ["C"]) 
"""

"""
Nothing to test with the Join
"""

""" To test the Rename, 1st to test if the column that we want to rename is in the table, second to test if the second column is not a table already
d = Rename(table1, "G", "B")
d = Rename(table1, "A",  "B")
"""

"""To test the Union, 1st a test for the error where the columns names don't match, 2nd to test if the number of columns doesn't match, same goes for the difference
e = Union(table1, table2)
e = Union(table1, table3)
"""

"""
We do 3 "tests" 
testProj = Projection(table1, ["A"])
testRename = Rename(table1,  "A", "Z")
testUnion = Union(table1, table1)

TestDB = DataBase(None, "TestTables.db", None)
TestDB.connect()
TestDB.getDB()
We connect to the database .db file (You can change if you want)

TestDB.createTable(table1.createTableInSql())
TestDB.getCursor()
We create table based on our table1


TestDB.RunQuery(testProj.translationInSQL())
TestDB.RunQuery(testRename.translationInSQL())
TestDB.RunQuery(testUnion.translationInSQL())
We do these 3 small tests

"""




