import sys
import math

minusing = sys.argv[1]

print('********************************')
print('AD-HOC NETWORK SIMULATOR - BEGIN')
print('********************************')

commandsUnread = open("commands" , "r")
commands = commandsUnread.read()

def getCommand(commandsUnread, inputlist):
    commandsList = commands.split("\n")
    for i in commandsList:
        element = i.split("\t")
        inputList.append(element)
    return inputList

def printSimTime(time):
    hour = time// 3600
    time = time % 3600
    min = time // 60
    time = time % 60
    sec = time
    print('SIMULATION TIME: ' + str(hour).zfill(2) + ':' + str(min).zfill(2) + ':' + str(sec).zfill(2))

def createMap():
    pathMap.clear()
    for item in nodeList:
        coordinate = item[1].split(";")
        bounding = item[2].split(";")

        right = int(coordinate[0]) + int(bounding[0])
        left = int(coordinate[0]) - int(bounding[1])
        up = int(coordinate[1]) + int(bounding[2])
        down = int(coordinate[1]) - int(bounding[3])

        for searchItem in nodeList:
            if(item[0] == searchItem[0]):
                continue
            coordinateTarget = searchItem[1].split(";")
            if left <= int(coordinateTarget[0]) <= right and down <= int(coordinateTarget[1]) <= up:
                if item[0] in pathMap:
                    conList = pathMap[item[0]]
                    conList.append(searchItem[0])
                    pathMap[item[0]] = conList
                else:
                    conList = list()
                    conList.append(searchItem[0])
                    pathMap[item[0]] = conList

    resultList = list()
    for node in nodeList:
        nodeName = node[0]
        if nodeName in pathMap:
            res = nodeName + " -> "
            targetList = pathMap[nodeName]
            res += ", ".join(targetList)
            resultList.append(res)
        else:
            resultList.append(nodeName + " -> ")
    print('\tNODES & THEIR NEIGHBORS:', end=" ")
    print(" | ".join(resultList), "|", end = " ")
    print()

def createNode(allList):
    if allList[1] == "CRNODE":
        data = [allList[2], allList[3], allList[4], allList[5]]
        nodeList.append(data)
        print ("\tCOMMAND *CRNODE*: New node " + allList[2] + " is created")
    else:
        return None

def startSending(allList):
    if allList[1] == "SEND":
        print ("\tCOMMAND *SEND*: Data is ready to send from " + starting + " to " + ending)
    else:
        return None

def sendingMyself(largeness, packetid):
    print("\tPACKET " + str(packetid) + " HAS BEEN SENT")
    largeness = float(largeness) - float(minusing)
    if int(largeness) <= 0:
        largeness = 0.0
    print("\tREMAINING DATA SIZE: " + '{0:.6}'.format(str(largeness)) + " BYTE")
    packetid += 1
    return largeness, packetid

def createAlternativePaths():
    paths.clear()
    costs.clear()
    minCostedIndex = list()
    routeList = list()

    totalCost = 0.0
    pathHist = list()
    pathHist.append(starting)

    searchPaths(pathHist, totalCost)

    lenpath = str(len(paths))
    print("\t"+ lenpath + ' ROUTE(S) FOUND:')
    for i in range(len(paths)):
        miniRouteList = list()
        print('\tROUTE ' + str(i+1) +': ' + " -> ".join(paths[i]) + '\t COST: {0:.4f}'.format(costs[i]))
        miniRouteList.append(paths[i])
        miniRouteList.append(i+1)
        routeList.append(miniRouteList)

    if len(paths) != 0:
        for i in range(len(costs)):
            if costs[i] == min(costs):
                minCostedIndex.append(i)

        minimumIndex = minCostedIndex[0]
        if len(minCostedIndex) > 1:
            for index in minCostedIndex:
                if len(paths[minimumIndex]) > len(paths[index]):
                    minimumIndex = index
        print("\tSELECTED ROUTE (ROUTE " + str(minimumIndex + 1) + "): " + " -> ".join(paths[minimumIndex]))
    else:
        continueSim = False


def searchPaths(pathHist, totalCost):
    pathHistory = pathHist[:]
    latestNode = pathHistory[-1]
    #print("pathHistory", pathHistory)
    if latestNode == ending:
        #print("latestNode == ending", latestNode == ending)
        paths.append(pathHistory)
        costs.append(totalCost)
        #print("paths", paths)
        return
    if latestNode not in pathMap:
        return
    for next in pathMap[latestNode]:
        if next in pathHistory:
            continue
        else:
            childList = pathHistory[:]
            childList.append(next)
            cost = calculateCost(latestNode,next)
            searchPaths(childList, totalCost + cost)
            #print("pathHistory", pathHistory, "childList", childList)

def calculateCost(latestNode,next):
    x1,x2,y1,y2 = 0.0, 0.0, 0.0, 0.0
    for node in nodeList:
        if node[0] == latestNode:
            coordinates1 = node[1].split(";")
            x1 = float(coordinates1[0])
            y1 = float(coordinates1[1])
        if node[0] == next:
            coordinates2 = node[1].split(";")
            charge = float(node[3])
            x2 = float(coordinates2[0])
            y2 = float(coordinates2[1])
    cost = (((x1 - x2)**2 + (y1-y2)**2)**(1/2)) / charge
    return cost

def moveNode(order):
    if order[1] == "MOVE":
        for node in nodeList:
            if node[0] == order[2]:
                node[1] = (order[3])
        print("\tCOMMAND *MOVE*: The location of node " + order[2] + " is changed")
    else:
        return None

def changeBattery(order):
    if order[1] == "CHBTTRY":
        for node in nodeList:
            if node[0] == order[2]:
                node[3] = (order[3])
        print("\tCOMMAND *CHBTTRY*: Battery level of node " + order[2] + " is changed to " + str(order[3]))
    else:
        return None

def removeNode(order):
    if order[1] == "RMNODE":
        for node in nodeList:
            if node[0] == order[2]:
                nodeList.remove(node)
        print("\tCOMMAND *RMNODE*: Node " + order[2] + " is removed")
    else:
        return None

def processOrders(time):
    result = False
    for order in orderList:
        if int(order[0]) == time:
            if(order[1] != "SEND"):
                result = True
            createNode(order)
            startSending(order)
            moveNode(order)
            changeBattery(order)
            removeNode(order)
        else:
            continue
    return result

inputList = list()
nodeList = list()
pathMap = dict()
paths = list()
costs = list()
rangeRects = dict()
continueSim = True
time = 0
packetid = 1

orderList = getCommand(commands, inputList)
for i in orderList:
    if i[1] == "SEND":
        keyList = i

starting = keyList[2]
ending = keyList[3]
largeness = keyList[4]

while continueSim == True:
    printSimTime(time)
    result = processOrders(time)
    if(result == True):
        createMap()
        createAlternativePaths()

 #      for node1 in pathMap:
 #          for node2 in pathMap[node1]:
 #              print(node1, node2, calculateCost(node1, node2))

    if len(paths) != 0:
        largeness,packetid = sendingMyself(largeness, packetid)
    else:
        continueSim = False
        print("\tNO ROUTE FROM " + keyList[2] + " TO " + keyList[3] + " FOUND.")


    time += 1


    if largeness == 0.0:
        continueSim = False

else:
    print('******************************')
    print('AD-HOC NETWORK SIMULATOR - END')
    print('******************************')

commandsUnread.close()