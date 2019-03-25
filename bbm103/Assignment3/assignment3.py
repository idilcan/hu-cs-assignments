# BBM103 Introduction to Programming Lab 1 - Fall 2017 - Programming Assignment 3 Starter Code
import sys
'''
This program will save the human race if done properly, 
so please do your best to get 100 from this assignment. 
You can do it! :)
'''

# Opening the input files
hurapfile = open(sys.argv[1], "r")
hurap_file = open(sys.argv[1], "r").read()
schucksciifile = open(sys.argv[2], "r")
schuckscii_file = open(sys.argv[2], "r").read()
viruscodesfile = open(sys.argv[3], "r")
virus_codes_file = open(sys.argv[3], "r").read()

# Mission 00: Decrypting the HuRAP

hurapUsable = hurap_file.split("\n")
hurapNew = list()

for i in hurapUsable:
    if i[0] != "1" and i[0] != "0":
        keyList = list(i)
        keyLine = list()
        for element in i:
            if element != "0" and element != "1":
                continue
            else:
                keyLine.append(element)
    else:
        hurapNew.append(i)

#keyLine
keyStr = ""
for i in keyLine:
    keyStr += i

outList = list()
for t in hurapNew:
    inList = [t[i:i + 4] for i in range(0, len(t), 4)]
    outList.append(inList)



print("""*********************
     Mission 00 
*********************""", end="\n\n")

print("""--- hex of encrypted code ---
-----------------------------""", end="\n\n")

# Your code which calculates and prints out the hexadecimal representation
hexList = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"]


tmpList = list()

for k in outList:
    turnedStr = ""
    for binary in k:
        total = 0
        for elementIndex in range(len(binary)):
            t = binary[elementIndex]
            total += int(t) * (2 ** (3 - elementIndex))
        elementHex = hexList[total]
        turnedStr += elementHex
    tmpList.append(turnedStr)

turnedStr = "\n".join(tmpList)
print(turnedStr)

if keyStr[0] == "1":
    newbin = keyStr[1:]
    newestbin = ""
    for indexNo in range(len(newbin)):
        element = newbin[indexNo]
        if element == "0":
            newestbin += "1"
        elif element == "1":
            newestbin += "0"
    power = len(newestbin)
    total = 0
    for i in range(len(newestbin)):
        power -= 1
        total += int(newestbin[i]) * (2 ** power)

    total += 1
    total *= -1

else:
    power = len(keyStr)
    total = 0
    for i in range(len(keyStr)):
        power -= 1
        total += int(keyStr[i]) * (2 ** power)
keyDec = total

# of HuRAP goes here

print("""\n--- encrypted code ----
-----------------------""", end="\n\n")

# Your code which calculates and prints the encrypted character
schDict = {}
schList = list()
splittedSch = schuckscii_file.split("\n")
for k in range(len(splittedSch)):
    i = splittedSch[k]
    element = i.split("\t")
    if(len(element) == 3):
        schDict[element[1]] = k
        schList.append(element[0])

turnedList = turnedStr.split("\n")

bigList = list()
for twohex in turnedList:
    miniList = [twohex[i:i + 2] for i in range(0, len(twohex), 2)]
    bigList.append(miniList)

encrList = list()
for mini in bigList:
    encrStr = ""
    for element in mini:
        index = schDict[element]
        encrStr += (schList[index])
    encrList.append(encrStr)
encrStr = "\n".join(encrList)

print(encrStr)
# representation of HuRAP goes here

print("""\n--- decrypted code ---
----------------------""", end="\n\n")

# Your code which decrypts and prints the
decrList = list()

decrStr = ""
for minil in bigList:
    decrStr = ""
    for element in minil:
        index = (schDict[element] - keyDec) % len(schList)
        decrStr += schList[index]
    decrList.append(decrStr)
decrStr = "\n".join(decrList)

print(decrStr)
# HuRAP goes here

# Mission 01: Coding the virus

print("""\n*********************
     Mission 01 
*********************""", end="\n\n")

# Your code which transforms the original HuRAP and prints
splittedVirus = virus_codes_file.split("\n")
changedStr = decrStr
for k in range(len(splittedVirus)):
    i = splittedVirus[k]
    element = i.split(":")
    changedStr = changedStr.replace(element[0], element[1])

print(changedStr)
# the virus-infected HuRAP goes here
	  
	  
# Mission 10: Encrypting the virus-infected HuRAP

print("""\n*********************
     Mission 10 
*********************""", end="\n\n")


print("""--- encrypted code ---
----------------------""", end="\n\n")

# Your code which encrypts and prints the encrypted character
newSchDict = {}
newSchList = list()
for k in range(len(splittedSch)):
    i = splittedSch[k]
    element = i.split("\t")
    if(len(element) == 3):
        newSchDict[element[0]] = k
        newSchList.append(element[1])

changedList = changedStr.split("\n")
decrList = list()
hexaList= list()

for element in changedList:
    reDecrStr = ""
    reHex = ""
    for letterIndex in range(len(element)):
        letter = element[letterIndex]
        index = newSchDict[letter]
        newIndex = (newSchDict[letter] + keyDec) % len(schList)
        reDecrStr += schList[newIndex]
        reHex += newSchList[newIndex]
    decrList.append(reDecrStr)
    hexaList.append(reHex)
reDecrStr = "\n".join(decrList)
reHex = "\n".join(hexaList)
print(reDecrStr)


# representation of the virus-infected HuRAP goes here

print("""\n--- hex of encrypted code ---
-----------------------------""", end="\n\n")

# Your code which calculates and prints out the hexadecimal representation
print(reHex)
# of virus-infected and encrypted HuRAP goes here

print("""\n--- bin of encrypted code ---
-----------------------------""", end="\n\n")

# Your code which calculates and prints out the binary representation
decList = list()
reHexList = reHex.split("\n")
rereHex = list()
for i in reHexList:
    newestHexList = list(i)
    rereHex.append(newestHexList)
    decInList = list()
    for index in range(len(i)):
        element = i[index]
        if element == "A":
            elementToAppend = "10"
        elif element == "B":
            elementToAppend = "11"
        elif element == "C":
            elementToAppend = "12"
        elif element == "D":
            elementToAppend = "13"
        elif element == "E":
            elementToAppend = "14"
        elif element == "F":
            elementToAppend = "15"
        else:
            elementToAppend = element
        decInList.append(elementToAppend)
    decList.append(decInList)

binRep = ["0000","0001","0010","0011","0100","0101","0110","0111","1000","1001","1010","1011","1100","1101","1110","1111"]
binaryList = list()
for lineList in decList:
    binStr = ""
    for element in lineList:
        element = int(element)
        binStr += binRep[element]
    binaryList.append(binStr)
binStr = "\n".join(binaryList)
print(binStr)

# of virus-infected and encrypted HuRAP goes here

# Closing the input files

hurapfile.close()
schucksciifile.close()
viruscodesfile.close()