from abc import *
from abc import ABC
from HandelExceptions import *

from Tables import Tables


class Operation(ABC):
    """ takes care of all the relational algebra"""

    def __init__(self):
        self.container = []  # serves as a container for the used operations
        self.tables = Tables()

    def __str__(self):
        return str(self)

    @abstractmethod
    def checkOpt(self):
        """ checks the syntax of the operation  """

    @abstractmethod
    def translateOpt(self):
        """ translates the operation to a SQL query"""


class Projection(Operation):
    """ represents the projection """

    def __init__(self, cols, operation):
        super().__init__()
        self.cols = cols
        self.container.append(operation)

    def checkOpt(self):
        self.container[0].check()
        for i in range(len(self.cols)):  # check if the user wants to project the same columns twice
            for j in range(i+1, len(self.cols)):
                if self.cols[i] == self.cols[j]:
                    raise SameColumnProjection(self.i)

        for x in self.cols:  # check if the col is actually in the table
            if not self.tables.isInCols(self.cols):
                raise ColumnDoestNotExist(self.x)

    def __repr__(self):

        pass

    def __str__(self):
        return "π" + "[" + str(self.cols) + "]" + "(" + str(self.container[0]) + ")"

    def translateOpt(self):
        for x in self.container:
            if type(x) != type(self):
                x.translateOpt()
            else:
                return


class Rename(Operation):
    """ represents the rename operation """

    def __init__(self, oldName, newName, operation):
        super().__init__()
        self.oldName = oldName
        self.newName = newName
        self.container.append(operation)

    def checkOpt(self):
        if self.oldName == self.newName:  # Checks if the old name  and the new name are the same
            raise InvalidRename()
        elif not self.container[0].tables.isInCols(self.oldName):
            raise UnKnownAttribute()

    def translateOpt(self):
        pass


class Subtraction(Operation):
    """ Represents the subtraction operation in relational algebra"""
    def __init__(self, operation1, operation2):
        super().__init__()
        self.container.append(operation1)
        self.container.append(operation2)

    def checkOpt(self):

        self.container[0].check()
        self.container[1].check()

        for x in self.container[0].self.tables.colsName:
            if x not in self.container[1].self.tables.isInCols():
                raise InValidSubtractionOperation(self.container[0].tables, self.container[1].tables)

    def translateOpt(self):
        pass


class Join(Operation):
    """ Represent the join operation in relation algebra """
    def __init__(self, operation1, operation2):
        super().__init__()
        self.container.append(operation1)
        self.container.append(operation2)

    def checkOpt(self):
        self.container[0].check()
        self.conatiner[1].check()


    def translateOpt(self):
        pass


class Union(Operation):
    """ represents the union operation in relation algebra"""

    def __init__(self, operation1, operation2):
        super().__init__()
        self.container.append(operation1)
        self.container.append(operation2)

    def checkOpt(self):
        self.container[0].check()
        self.container[1].check()

        if self.container[0].tables.arity() != self.container[1].tables.arity():
            raise InvalidArityException(self.container[0], self.container[1])
        elif self.container[0].tables.sorte() != self.container[1].tables.sorte():
            raise DifferentAttributesException()

    def translateOpt(self):
        pass


class Selection(Operation):
    """ represent the selection operation in relation algebra """

    def __init__(self, compare, operation):
        super().__init__()
        self.compare = compare
        self.container.append(operation)

    def checkOpt(self):
        self.compare.check()
        self.container[0].check()

    def translateOpt(self):
        pass


class Compare:
    def __init__(self, col1, comparator, col2, colType):
        self.col1 = col1
        self.comparator = comparator
        self.col2 = col2
        self.colType = colType

    def check(self):
        # TODO: checking the types

        pass


    def __str__(self):
        pass

    def __repr__(self):
        pass



