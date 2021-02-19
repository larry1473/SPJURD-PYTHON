
class NewExceptions(Exception):
    pass


class SameColumnProjection(NewExceptions):
    """ generate when the user wants to project the same columns twice"""

    def __init__(self, col):
        self.col = col

    def __str__(self):
        return "column" + self.col + "can't be projected twice"


class ColumnDoestNotExist(NewExceptions):
    """ Raised when the user wants to project a column or columns that are not in  table"""

    def __init__(self, col):
        self.col = col

    def __str__(self):
        return "column" + self.col + " not found in the relation "


class InValidSubtractionOperation(NewExceptions):
    """ Raised when the user wants to apply subtraction between to tables that do not have the same attributes  """

    def __init__(self, table1, table2):
        self.table1 = table1
        self.table2 = table2

    def __str__(self):
        if self.table1.arity() != self.table2.arity():  # checks the arity of each tables before applying the subtraction
            return "the tables do not have the same number of attributes"
        elif self.table1.arity() == self.table2.arity():
            res = "("
            for x in self.table1.colsName:  # checks if the attributes of table1 are in table2
                if not self.table2.isInCols(x):
                    res += "," + str(x)
            res += ")"
            return "the columns" + res + " are not in " + self.table2.sorte()
        else:
            res = "("
            for x in self.table2.colsName:  # checks if the attributes of table2 are in table1
                if not self.table1.isInCols(x):
                    res += "," + str(x)
            res += ")"
            return "the columns" + res + " are not in " + self.table1.sorte()


class InvalidRename(NewExceptions):
    """" Raised the name chosen name is invalid """


class UnKnownAttribute(NewExceptions):
    """ Raised when the old attribute name is not found in the table"""


class InvalidArityException(NewExceptions):
    """ Generated when the user to apply a union operation on two tables that have different arity """

    def __init__(self, operation1, operation2):
        self.operation1 = operation1
        self.operation2 = operation2

    def __str__(self):
        return "the table1 one has an arity of " + self.operation1.tables.arity() + "while table2 has an arity of " + self.operation2.tables.arity()


class DifferentAttributesException(NewExceptions):
    """ Generated when the tables have different attributes """
    def __init__(self, operation1, operation2):
        self.operation1 = operation1
        self.operation2 = operation2

    def __str__(self):
        return "the table1 has a sort  " + self.operation1.tables.sorte() + "while table2 has a sort  " + self.operation2.tables.sorte()






