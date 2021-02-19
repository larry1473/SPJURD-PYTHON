import unittest
from Operations import *
from Tables import *

class Test(unittest.TestCase):

    # PROJECTION  unit tests
    def testCheckProject(self):
        self.assertRaises(SameColumnProjection(),Projection.checkOpt())
    def ProjectionTranslation(self):
        pass

    #TABLES  unit tests

    def TestIsIncols(self):
        self.assertAlmostEqual(Tables.isInCols("K"), True)

