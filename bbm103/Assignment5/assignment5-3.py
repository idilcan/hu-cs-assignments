import sys
f = open("output.txt","a+")

def quicksort(liste):
    mainElement = liste[-1]
    littleList = list()
    bigList = list()
    eqList = list()
    for element in liste[:len(liste)]:
        if element > mainElement:
            bigList.append(element)
        elif element < mainElement:
            littleList.append(element)
        else:
            eqList.append(element)

    if len(littleList) > 1:
        quicksort(littleList)
    elif len(littleList) == 1:
        finalList.append(littleList[-1])

    for i in eqList:
        finalList.append(i)


    if len(bigList) > 0:
        quicksort(bigList)
    elif len(bigList) == 1:
        finalList.append(bigList[-1])

    return finalList

def fullBinarySearch(liste,element):
    indexValue = 0
    print(*liste)
    print(*liste, file= f)
    def ifElement(liste, newList, indexValue):
        if len(liste) > 2:
            return binarySearch(newList, element, indexValue)
    def binarySearch(liste, element, indexValue):
        #print("---", len(liste))
        if len(liste) % 2 == 1:
            nowIndex = len(liste) // 2 #+ 1
            #print("nowIndex", nowIndex, "-", liste[nowIndex])
            listElement = liste[nowIndex]
            if element == listElement:
                print(listElement)
                print(listElement, file= f)
                indexValue += nowIndex
                finalIndex = indexValue
                return finalIndex
            elif element < listElement:
                newList = liste[:(nowIndex)]
                print(*newList)
                print(*newList, file= f)
                indexValue += nowIndex -1
                indexValue -= len(newList)
                return ifElement(liste, newList, indexValue)
            elif element > listElement:
                newList = liste[(nowIndex+1):]
                print(*newList)
                print(*newList, file= f)
                indexValue += nowIndex +1
                return ifElement(liste, newList, indexValue)
        else:
            nowIndex = len(liste) // 2 - 1
            #print("nowIndex", nowIndex, "-", liste[nowIndex])
            listElement = liste[nowIndex]
            if element == listElement:
                print(listElement)
                print(listElement, file=f)
                indexValue += nowIndex
                finalIndex = indexValue
                return finalIndex
            elif element < listElement:
                newList = liste[:(nowIndex)]
                print(*newList)
                print(*newList, file=f)
                indexValue += nowIndex - 1
                indexValue -= len(newList)
                return ifElement(liste, newList, indexValue)
            elif element > listElement:
                newList = liste[(nowIndex + 1):]
                print(*newList)
                print(*newList, file=f)
                indexValue += nowIndex + 1
            return ifElement(liste, newList, indexValue)
            #nowIndex1 = len(liste)//2
            #nowIndex2 = nowIndex1 - 1
            #listElement1 = liste[nowIndex1]
            #listElement2 = liste[nowIndex2]
            #if element == listElement1:
            #    print(listElement1)
            #    print(listElement1, file= f)
            #    indexValue += nowIndex1
            #    finalIndex = indexValue
            #    return finalIndex
            #elif element == listElement2:
            #    indexValue += nowIndex2
            #    finalIndex = indexValue
            #    print(listElement2)
            #    print(listElement2, file= f)
            #    return finalIndex
            #elif element < listElement2:
            #    newList = liste[:(nowIndex2-1)]
            #    print(*newList)
            #    print(*newList, file= f)
            #    indexValue += nowIndex2 -1
            #    indexValue -= len(newList)
            #    return ifElement(liste, newList, indexValue)
            #elif element > listElement1:
            #    newList = liste[(nowIndex1):]
            #    print(*newList)
            #    print(*newList, file= f)
            #    indexValue += nowIndex1
            #    return ifElement(liste, newList, indexValue)

    return binarySearch(liste, element, indexValue)

inputFile = open(sys.argv[1], "r+")
name = sys.argv[2]
print("Searching value is:", name, end="\n\n")
print("Searching value is:", name, end="\n\n", file= f)
readedFile = inputFile.read()
file = readedFile.split("\n")
finalList = list()

quicksort(file)
fileList = list()
searchList = list()

for i in finalList:
    element = i.split(":")
    fileList.append(element)
for i in fileList:
    searchList.append(i[0])

meow = list()
printing = list()
for i in file:
    element = i.split(":")
    meow.append(element)
for i in meow:
    printing.append(i[0])

print(" ".join(printing))
print(" ".join(printing), file= f)

if name in searchList:
    indexValue = fullBinarySearch(searchList, name)
    foundElement = fileList[indexValue]
    name = foundElement[0]
    city = foundElement[1]
    print("", end = "\n")
    print("The search string is", name,"and the city is", city)
    print("", end = "\n", file= f)
    print("The search string is", name,"and the city is", city, file= f)
else:
    indexValue = fullBinarySearch(searchList, name)
    print("\nThe search string was not found in file")
    print("\nThe search string was not found in file", file= f)

f.close()




