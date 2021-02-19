class Tables:

    def __init__(self, colsName = [], values = []):
        self.colsName = colsName
        self.values = values
        #Values is a list containing list, each list is a line of the table
        # {"A": 0; "B" : 1} len(dico)

    def __add__(self, line):
        self.values.append(line)

    def __str__(self):
        s = ""
        count = 0
        for i in self.colsName:
            count +=1
            s += i
            if count != len(self.colsName):
                s += " "
        s += "\n"

        for j in self.values:
            d = len(j)
            for i in range(d):
                s += str(j[i])
                s += " "
            s += "\n"
        return s

    def arity(self):
        return len(self.colsName)

    def sorte(self):
        s = "("
        count = 0
        for i in self.colsName:
            count +=1
            s += i
            if count != len(self.colsName):
                s += ", "
        s += ")"
        return s

    def getItemType(self, col):
       a = type(self.values[0][self.colsName.index(col)])
       return a

    def isInCols(self, col):
        return col in self.colsName

    def rename(self, old_col, new_col):
        ind = self.colsName.index(old_col)
        self.colsName[ind] = new_col


t = Tables(["A", "B", "C"], [[1,2,3], [4,5,6]]) #tables.values[0][tables.]
print(t)
# a = type(t.values[0][t.colsName["A"]])
# print(a)
print(t.getItemType("A"))
# print(t.isInCols("A"))
t.rename("A", "a")
print(t)







    # def inTable(self, col):
    #     """ checks if col is a column in the cols """
    #     return col in self.cols
