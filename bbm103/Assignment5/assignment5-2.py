import sys

def printOhMyMy():
    print("---HUBBM DVD----")
    print("A:\tAdd new\tdvd")
    print("R:\tRemove\tdvd")
    print("S:\tSearch\tdvd")
    print("L:\tList\tdvd")
    print("E:\tEdit\tdvd")
    print("H:\tHire\tdvd")
    print("Q:\tQuit")

try:
    openedFile = open("output.txt", "r+")
    firstSplit = openedFile.read().split("\n")
    openedFile.close()
    fileList = [line.split(',') for line in firstSplit]

except:
    print("error")
    try:
        file = sys.argv[1]
        openedFile = open(file, 'r')
        firstSplit = openedFile.read().split("\n")
        fileList = [line.split(',') for line in firstSplit]

    except IOError or IndexError:
        fileList = list()

f = open('output.txt', 'r+')
f.truncate()
outText = open("output.txt", "a+")

printOhMyMy()
myInput = input("User Commands: ")
myInput = myInput.split(",")

while myInput[0] != "Q":
    assign = myInput[0]
    serialList = list()

    for movie in fileList:
        serialList.append(movie[0])


    if assign == "A":

        check = (myInput[-1] != "Inv" and myInput[-1] != "Hired")
        notcheck = (myInput[-1] == "Inv" or myInput[-1] == "Hired")
        if len(myInput) == 6 and check:
            myInput.append("Inv")
            if myInput[1] in serialList:
                print("WARNING: This serial number is already in use.")
            else:
                fileList.append(myInput[1:])
        elif len(myInput) == 7 and notcheck:
            if myInput[1] in serialList:
                print("WARNING: This serial number is already in use.")
            else:
                fileList.append(myInput[1:])
        elif len(myInput) <= 7:
            print("WARNING: This command's format is wrong.")

    elif assign == "R":
        for i in fileList:
            if i[0] == myInput[1]:
                if i[-1] == "Inv":
                    fileList.remove(i)
                else:
                    print("WARNING: This item is 'Hired'. You cannot remove this item.")

    elif assign == "L":
        if len(fileList) == 0:
            print("WARNING: There is no items to show.")
        elementNo = 0
        while elementNo < len(fileList):
            print(",".join(fileList[elementNo]),end= "")
            x = input()
            if x == "":
                elementNo += 1

    elif assign == "S":
        thisisit = False
        if len(myInput[1]) < 2:
            print("WARNING: The search input is too short. Please give at least 3 characters.")
        else:
            try:
                for movie in fileList:
                    if movie[2][:len(myInput[1])] == myInput[1]:
                        thisisit = True
                        foundMovie = movie
                        break
                if thisisit == True:
                    print(",".join(foundMovie))
                else:
                    print("WARNING: This item does not exist.")
            except IndexError:
                print("WARNING: There is no item.")

    elif assign == "E":
        found = False
        for movie in fileList:
            if movie[0] == myInput[1]:
                found = True
                for edit in myInput[2:]:
                    tmpList = edit.split("=")
                    editing = tmpList[0]
                    changed = tmpList[1][:-1]
                    if editing == "{Name":
                        movie[2]= changed
                    elif editing == "{Price":
                        movie[1] = changed
                    elif editing == "{Genre":
                        movie[3] = changed
                    elif editing == "{Director":
                        movie[4] = changed
                    else:
                        print("WARNING: You cannot change this element or there is no such element.")
                break
        if found == False:
            print("WARNING: The item that you are trying to reach does not exist.")

    elif assign == "H":
        for movie in fileList:
            if movie[0] == myInput[1]:
                if movie[-1] == "Inv":
                    movie[-1] = "Hired"
                    break
                else:
                    print("WARNING: This item canot be hired.")

    else:
        print("WARNING: This command is invalid.")

    printOhMyMy()
    myInput = input("User Commands: ")
    myInput = myInput.split(",")

else:
    for i in fileList:
        t = ",".join(i) + "\n"
        outText.write(t)

    outText.close()