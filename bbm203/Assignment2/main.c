#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>

#include "FrameQueue.h"

#define MAXLINE 1024

//here is the struct of client
struct ClientInfo{
    char name[MAXSIZE];
    char IPAdress[MAXSIZE];
    char macAdress[MAXSIZE];
};

struct ClientLog{
    int index;
    time_t time;
    char message[MAXLINE];
    int numberOfFrames;
    int hops;
    char senderId[MAXSIZE];
    char receiverId[MAXSIZE];
    int activitity; // 0=send 1=recieved 2=transferred
    struct ClientLog* nextLog;
};

struct Client{
    struct ClientInfo clientInfo;
    struct MessageQueue in;
    struct MessageQueue out;
    struct ClientLog* log;
    struct ClientLog* lastLog;
};
//here is for routing map
struct Routing{
    struct ClientInfo middleClient;//NOTE: nearestNeighbour
    struct ClientInfo destinationClient;
};
struct Map{
    struct ClientInfo key;
    struct Routing* routing;
};

struct ClientInfo findClient(struct Client *clientlist, char *name, int numberOfClients){
    int i;
    for (i=0; i<numberOfClients; i++){
        if(strcmp(clientlist[i].clientInfo.name, name) == 0)
            return clientlist[i].clientInfo;
    }
    struct ClientInfo client;
    return client;
}

int findClientIndex(struct Client *clientlist, char *name, int numberOfClients){
    int i;
    for (i=0; i<numberOfClients; i++){
        if(strcmp(clientlist[i].clientInfo.name, name) == 0)
            return i;
    }
    return -1;
}

int findClientIndexFromMacAddress(struct Client *clientlist, char *macAddresss, int numberOfClients){
    int i;
    for (i=0; i<numberOfClients; i++){
        if(strcmp(clientlist[i].clientInfo.macAdress, macAddresss) == 0)
            return i;
    }
    return -1;
}

int findClientIndexFromIPAddress(struct Client *clientlist, char *ipAddresss, int numberOfClients){
    int i;
    for (i=0; i<numberOfClients; i++){
        if(strcmp(clientlist[i].clientInfo.IPAdress, ipAddresss) == 0)
            return i;
    }
    return -1;
}

struct ClientInfo getNearestClient(struct Map* routingMap, char* source, char* dest, int numberOfClients){
    int i,j;
    for (i=0; i<numberOfClients; i++){
        if(strcmp(routingMap[i].key.name, source) == 0) {
            for(j=0; j<(numberOfClients-1); j++){
                if (strcmp(routingMap[i].routing[j].destinationClient.name, dest) == 0)
                    return routingMap[i].routing[j].middleClient;
            }
        }
    }
    struct ClientInfo client;
    return client;
}

//NOTE: destinationID<space>nearestNeighbour
struct Map *routingFileImplementer(FILE *inputFile, struct Client *clientArray, int numberOfClients) {
    int size1 = sizeof(struct Map);
    int size2 = sizeof(struct Routing);

    struct Map *routingMap = (struct Map*) malloc(numberOfClients * sizeof(struct Map));
    int i = 0;
    for(; i<numberOfClients; i++)
    {
        routingMap[i].routing = (struct Routing*) malloc((numberOfClients - 1) * sizeof(struct Routing));
        int j = 0;
        for(; j<(numberOfClients - 1);j++)
        {
            int x = 0;
            for(;x<MAXSIZE;x++) {
                routingMap[i].key.name[x] = 0;
                routingMap[i].key.macAdress[x] = 0;
                routingMap[i].key.IPAdress[x] = 0;
                routingMap[i].routing[j].destinationClient.name[x] = 0;
                routingMap[i].routing[j].destinationClient.macAdress[x] = 0;
                routingMap[i].routing[j].destinationClient.IPAdress[x] = 0;
                routingMap[i].routing[j].middleClient.name[x] = 0;
                routingMap[i].routing[j].middleClient.macAdress[x] = 0;
                routingMap[i].routing[j].middleClient.IPAdress[x] = 0;
            }
        }
    }

    int j=0;
    for(;j<numberOfClients;j++) {
        for (i = 0; i < (numberOfClients - 1); i++) {
            char destination[MAXSIZE], nearest[MAXSIZE];
            fscanf(inputFile, "%s", destination);
            fscanf(inputFile, "%s", nearest);
//            struct Map routing;
            routingMap[j].key = clientArray[j].clientInfo;
            struct ClientInfo destinationClient = findClient(clientArray, destination, numberOfClients);
            struct ClientInfo nearestClient = findClient(clientArray, nearest, numberOfClients);
            routingMap[j].routing[i].destinationClient = destinationClient;
            routingMap[j].routing[i].middleClient = nearestClient;

        }
        char delimeter[MAXSIZE];
        fscanf(inputFile, "%s", delimeter);
    }
    return routingMap;
}

struct Client *clientArrayMakerFromFile(FILE *inputFile, int* numberOfClients){
    fscanf(inputFile, "%d", numberOfClients);

    struct Client* clients = (struct Client*)malloc((*numberOfClients)* sizeof(struct Client));

    int i;
    for(i=0; i<(*numberOfClients); i++){
        messageQueueInit(&clients[i].in);
        messageQueueInit(&clients[i].out);
        clients[i].log = 0;
        int x;
        for(x=0;x<MAXSIZE;x++)
        {
            clients[i].clientInfo.name[x] = 0;
            clients[i].clientInfo.macAdress[x] = 0;
            clients[i].clientInfo.IPAdress[x] = 0;
        }
        fscanf(inputFile, "%s %s %s", clients[i].clientInfo.name, clients[i].clientInfo.IPAdress, clients[i].clientInfo.macAdress);
    }
    return clients;
}

void getMessage(FILE *inFile, char* message, char* line, int* messageSize)
{
    fgets(line, MAXLINE, inFile);

    int i=0;
    int index = 0;
    int state = 0; // 0 = message not started, 1 = message started

    for(; i<MAXLINE;i++)
    {
        char ch = line[i];
        if(state == 0){
            if(ch == '#')
                state = 1;
        } else { //state == 1
            if(ch == '#')
            {
                break;
            }
            message[index] = ch;
            index++;
        }
    }
    message[index] = 0;
    (*messageSize) = index;

}

void pushLog(struct Client* clientList, int senderIndex, struct ClientLog* newLog)
{
    if(clientList[senderIndex].log == 0)
    {
        newLog->index = 1;
        clientList[senderIndex].log = newLog;
        clientList[senderIndex].lastLog = newLog;
    }
    else
    {
        newLog->index = clientList[senderIndex].lastLog->index + 1;
        clientList[senderIndex].lastLog->nextLog = newLog;
        clientList[senderIndex].lastLog = newLog;
    }
}

void sendMessages(struct Client* clientList, int senderIndex, int numberOfClients,
                  int hop, int maxFrameSize, struct Map* routingMap);

void processIncoming(struct Client* clientList, int myIndex, int numberOfClients,
                  int hop, int maxFrameSize, struct Map* routingMap)
{
    struct MessageQueue* inqueue = &clientList[myIndex].in;
    struct MessageQueue* outqueue = &clientList[myIndex].out;
    int queueSize = getMessageQueueSize(inqueue);

    int final = 0;
    int index = 0;
    for(;index < queueSize;index++)
    {
        struct NetworkStack* stack = messageQueuePop(inqueue);
        struct PhysicalFrame *phyFrame = (struct PhysicalFrame *)pop(stack);
        struct NetworkFrame *netFrame = (struct NetworkFrame *)pop(stack);

        int dest  = findClientIndexFromIPAddress(clientList, netFrame->recieverIP, numberOfClients);
        int src   = findClientIndexFromIPAddress(clientList, netFrame->senderIP, numberOfClients);
        if(index == 0)
        {
            if(dest == myIndex) {
                printf("A message received by client %s from client %s after a total of %d hops.\n",
                       clientList[myIndex].clientInfo.name, clientList[src].clientInfo.name, hop);
                printf("Message: ");
            }
            else
                printf("A message received by client %s, but intended for client %s. Forwarding...\n",
                        clientList[myIndex].clientInfo.name, clientList[dest].clientInfo.name);
        }
        if(dest == myIndex)
        {
            struct TransportationFrame *trFrame = (struct TransportationFrame *)pop(stack);
            struct ApplicationFrame *appFrame = (struct ApplicationFrame *)pop(stack);
            printf("%s", appFrame->messagePart);
            final = 1;
        } else{
            push(stack, netFrame);
            strcpy(phyFrame->senderMAC, clientList[myIndex].clientInfo.macAdress);
            struct ClientInfo nearestClient = getNearestClient(routingMap, clientList[myIndex].clientInfo.name,
                                                               clientList[dest].clientInfo.name, numberOfClients);
            int nearestIndex = findClientIndex(clientList, nearestClient.name, numberOfClients);
            strcpy(phyFrame->recieverMAC, clientList[nearestIndex].clientInfo.macAdress);
            push(stack, phyFrame);

            printf("        Frame #%d MAC address change: New sender MAC %s, new receiver MAC %s\n", index+1, phyFrame->senderMAC, phyFrame->recieverMAC);

            messageQueuePush(outqueue, stack);
            final = 0;
        }

    }
    if(final == 1) {
        printf("\n");
    } else {
       sendMessages(clientList, myIndex, numberOfClients, hop, maxFrameSize, routingMap);
    }

}

void sendMessages(struct Client* clientList, int senderIndex, int numberOfClients,
        int hop, int maxFrameSize, struct Map* routingMap)
{
    struct MessageQueue *queue = &clientList[senderIndex].out;
    int queueSize = getMessageQueueSize(queue);
    int destinationIndex;

    char* message = malloc(queueSize * maxFrameSize);

    struct ClientLog* newLog;
    struct ClientLog* destLog;
    newLog = (struct ClientLog*)malloc(sizeof(struct ClientLog));
    destLog = (struct ClientLog*)malloc(sizeof(struct ClientLog));
    newLog->nextLog = 0;
    newLog->numberOfFrames = queueSize;
    destLog->nextLog = 0;
    destLog->numberOfFrames = queueSize;

    int index = 0;
    for(;index < queueSize; index++)
    {
        struct NetworkStack *stack = messageQueuePop(queue);

        struct PhysicalFrame *phyFrame = pop(stack);

        struct ApplicationFrame* appFrame = (struct ApplicationFrame*)stack->data[0];
        int x=0;
        for(;x <maxFrameSize; x++)
        {
            message[index*maxFrameSize + x] = appFrame->messagePart[x];
        }
        strcpy(newLog->senderId, appFrame->senderId);
        strcpy(newLog->receiverId, appFrame->recieverId);
        strcpy(destLog->senderId, appFrame->senderId);
        strcpy(destLog->receiverId, appFrame->recieverId);

        destinationIndex = findClientIndexFromMacAddress(clientList, phyFrame->recieverMAC, numberOfClients);
        push(stack, phyFrame);

        messageQueuePush(&clientList[destinationIndex].in, stack);
    }
    newLog->time = time(NULL);
    destLog->time = time(NULL);
    strcpy(newLog->message, message);
    strcpy(destLog->message, message);
    newLog->hops = hop;
    if(hop == 0)
        newLog->activitity = 0; //sending
    else
        newLog->activitity = 2; //forwarding
    pushLog(clientList, senderIndex, newLog);

    hop++;

    destLog->hops = hop;
    destLog->activitity = 1;
    pushLog(clientList, destinationIndex, destLog);

    processIncoming(clientList, destinationIndex, numberOfClients, hop, maxFrameSize, routingMap );
}

void printLog(struct Client* clientList, int senderIndex, int numberOfClients)
{
    struct ClientLog* log = clientList[senderIndex].log;
    int i = 1;

    while(log != 0)
    {
        struct tm* time = localtime(&log->time);

        printf("Log Entry #%d:\n", i);
        printf("Timestamp: %d-%d-%d %d:%d:%d\n", 1900+time->tm_year,time->tm_mon+1,
                time->tm_mday, time->tm_hour, time->tm_min, time->tm_sec);
        printf("Message: %s\n", log->message);
        printf("Number of frames: %d\n", log->numberOfFrames);
        printf("Number of hops: %d\n", log->hops);
        printf("Sender ID: %s\n", log->senderId);
        printf("Receiver ID: %s\n", log->receiverId);
        if(log->activitity == 0)
            printf("Activity: Message Sent\n");
        else if(log->activitity == 1)
            printf("Activity: Message Received\n");
        else
            printf("Activity: Message Forwarded\n");
        printf("Success: Yes\n");

        if(log->nextLog != 0)
            printf("--------------\n");
        i++;
        log = log->nextLog;
    }
}

void execute(FILE *inFile, struct Client* clientList, int numberOfClients, struct Map* routingMap,
        int maxFrameSize, char* senderPort, char* receiverPort)
{
    //read the fist line and assign it to number of loops.
    int commandSize;
    fscanf(inFile, "%d", &commandSize);

    int commandIndex = 0;
    for(;commandIndex < commandSize; commandIndex++) {
        char command[MAXSIZE];
        fscanf(inFile, "%s", &command);

        if (strcmp(command, "MESSAGE") == 0) {

            char sender[MAXSIZE], receiver[MAXSIZE];
            fscanf(inFile, "%s %s", &sender, &receiver);

            int senderIndex = findClientIndex(clientList, sender, numberOfClients);
            int receiverIndex = findClientIndex(clientList, receiver, numberOfClients);
            struct ClientInfo nearestClient = getNearestClient(routingMap, sender, receiver, numberOfClients);
            int nearestIndex = findClientIndex(clientList, nearestClient.name, numberOfClients);

            char message[MAXLINE];
            char line[MAXLINE];
            int messageSize = 0;
            getMessage(inFile, message, line, &messageSize);

            if (messageSize == 0)
                continue;

            int i = 0;
            int index = 0;
            for (; i < messageSize; i++) {
                struct NetworkStack *stack;
                struct ApplicationFrame *appFrame;
                struct TransportationFrame *transFrame;
                struct NetworkFrame *netFrame;
                struct PhysicalFrame *phyFrame;
                if (index == 0) {
                    stack = (struct NetworkStack *) malloc(sizeof(struct NetworkStack));
                    stack->nextNode = 0;
                    init(stack);

                    appFrame = (struct ApplicationFrame *) malloc(sizeof(struct ApplicationFrame));
                    appFrame->messagePart = (char *) malloc(maxFrameSize + 1);
                    int x = 0;
                    for (; x < (maxFrameSize + 1); x++)
                        appFrame->messagePart[x] = 0;

                    transFrame = (struct TransportationFrame *) malloc(sizeof(struct TransportationFrame));
                    netFrame = (struct NetworkFrame *) malloc(sizeof(struct NetworkFrame));
                    phyFrame = (struct PhysicalFrame *) malloc(sizeof(struct PhysicalFrame));
                }
                appFrame->messagePart[index] = message[i];
                index++;
                if (index >= maxFrameSize || i == (messageSize - 1)) {
                    appFrame->messagePart[index] = 0;
                    strcpy(appFrame->senderId, sender);
                    strcpy(appFrame->recieverId, receiver);

                    appFrame->size = index;
                    push(stack, appFrame);

                    strcpy(transFrame->senderPort, senderPort);
                    strcpy(transFrame->recieverPort, receiverPort);
                    push(stack, transFrame);

                    strcpy(netFrame->senderIP, clientList[senderIndex].clientInfo.IPAdress);
                    strcpy(netFrame->recieverIP, clientList[receiverIndex].clientInfo.IPAdress);
                    push(stack, netFrame);

                    strcpy(phyFrame->senderMAC, clientList[senderIndex].clientInfo.macAdress);
                    strcpy(phyFrame->recieverMAC, clientList[nearestIndex].clientInfo.macAdress);
                    push(stack, phyFrame);

                    //push stack to clients outqueue
                    messageQueuePush(&clientList[senderIndex].out, stack);


                    index = 0;
                }
            }

            printf("---------------------------------------------------------------------------------------\n");
            printf("Command: MESSAGE %s %s%s", sender, receiver, line);
            printf("---------------------------------------------------------------------------------------\n");
            printf("Message to be sent: %s\n\n", message);

            struct MessageQueue *queue = &clientList[senderIndex].out;
            int totalFrames = getMessageQueueSize(queue);
            struct NetworkStack *stack = queue->first;
            int frameIndex = 0;
            for (; frameIndex < totalFrames; frameIndex++) {
                printf("Frame #%d\n", frameIndex + 1);
                struct PhysicalFrame *phyFrame = (struct PhysicalFrame *) stack->data[3];
                printf("Sender MAC address: %s, Receiver MAC address: %s\n", phyFrame->senderMAC,
                       phyFrame->recieverMAC);
                struct NetworkFrame *netFrame = (struct NetworkFrame *) stack->data[2];
                printf("Sender IP address: %s, Receiver IP address: %s\n", netFrame->senderIP, netFrame->recieverIP);
                struct TransportationFrame *transFrame = (struct TransportationFrame *) stack->data[1];
                printf("Sender port number: %s, Receiver port number: %s\n", transFrame->senderPort,
                       transFrame->recieverPort);
                struct ApplicationFrame *appFrame = (struct ApplicationFrame *) stack->data[0];
                printf("Sender ID: %s, Receiver ID: %s\n", appFrame->senderId, appFrame->recieverId);
                printf("Message chunk carried: %s\n", appFrame->messagePart);
                printf("--------\n");

                stack = stack->nextNode;
            }
        }
        else if (strcmp(command, "SHOW_FRAME_INFO") == 0) {
//SHOW_FRAME_INFO C out 3
            char sender[MAXSIZE], inOut[MAXSIZE];
            int frameId = 0;
            fscanf(inFile, "%s %s %d", &sender, &inOut, &frameId);

            printf("---------------------------------\n");
            printf("Command: SHOW_FRAME_INFO %s %s %d\n", sender, inOut, frameId);
            printf("---------------------------------\n");

            int senderIndex = findClientIndex(clientList, sender, numberOfClients);
            struct MessageQueue *queue;
            if(strcmp(inOut, "out") == 0)
                queue = &clientList[senderIndex].out;
            else
                queue = &clientList[senderIndex].in;

            int totalFrames = getMessageQueueSize(queue);
            if(frameId > totalFrames)
            {
                printf("No such frame.\n");
                continue;
            }
            struct NetworkStack *stack = queue->first;

            int next = frameId - 2;
            for(;next>=0;next--)
                stack = stack->nextNode;

            struct PhysicalFrame *phyFrame = (struct PhysicalFrame *) stack->data[3];
            struct NetworkFrame *netFrame = (struct NetworkFrame *) stack->data[2];
            struct TransportationFrame *transFrame = (struct TransportationFrame *) stack->data[1];
            struct ApplicationFrame *appFrame = (struct ApplicationFrame *) stack->data[0];

            printf("Current Frame #%d on the outgoing queue of client %s\n", frameId, sender);
            printf("Carried Message: \"%s\"\n", appFrame->messagePart);
            printf("Layer 0 info: Sender ID: %s, Receiver ID: %s\n", appFrame->senderId, appFrame->recieverId);
            printf("Layer 1 info: Sender port number: %s, Receiver port number: %s\n", transFrame->senderPort, transFrame->recieverPort);
            printf("Layer 2 info: Sender IP address: %s, Receiver IP address: %s\n", netFrame->senderIP, netFrame->recieverIP);
            printf("Layer 3 info: Sender MAC address: %s, Receiver MAC address: %s\n", phyFrame->senderMAC, phyFrame->recieverMAC);
            printf("Number of hops so far: 0\n");//FIXME: number of hops
        }
        else if (strcmp(command, "SHOW_Q_INFO") == 0){
            char sender[MAXSIZE], inOut[MAXSIZE];
            fscanf(inFile, "%s %s", &sender, &inOut);

            printf("---------------------------\n");
            printf("Command: SHOW_Q_INFO %s %s\n", sender, inOut);
            printf("---------------------------\n");

            int senderIndex = findClientIndex(clientList, sender, numberOfClients);
            struct MessageQueue *queue;
            if(strcmp(inOut, "out") == 0)
                queue = &clientList[senderIndex].out;
            else
                queue = &clientList[senderIndex].in;

            int totalFrames = getMessageQueueSize(queue);
            if(strcmp(inOut, "in")==0)
                printf("ClientInfo %s Incoming Queue Status\n", sender);
            else
                printf("ClientInfo %s Outgoing Queue Status\n", sender);
            printf("Current total number of frames: %d\n", totalFrames);
        }
        else if (strcmp(command, "SEND") == 0) {
            char sender[MAXSIZE];
            fscanf(inFile, "%s", &sender);

            printf("----------------\n");
            printf("Command: SEND %s\n", sender);
            printf("----------------\n");

            int senderIndex = findClientIndex(clientList, sender, numberOfClients);

            sendMessages(clientList, senderIndex, numberOfClients, 0, maxFrameSize, routingMap);
        }
        else if (strcmp(command, "PRINT_LOG") == 0) {
            char sender[MAXSIZE];
            fscanf(inFile, "%s", &sender);

            printf("---------------------\n");
            printf("Command: PRINT_LOG %s\n", sender);
            printf("---------------------\n");

            printf("ClientInfo %s Logs:\n--------------\n", sender);


            int senderIndex = findClientIndex(clientList, sender, numberOfClients);
            printLog(clientList, senderIndex, numberOfClients);
        }
        else{
            char line[MAXLINE];
            fgets(line, MAXLINE, inFile);
            printf("---------------------\n");
            printf("Command: %s%s",command, line);
            printf("---------------------\n");
            printf("Invalid command.\n");

        }
    }



}

// NOTE:.\HUBBMNET (1)clients.dat (2)routing.dat (3)commands.dat (4)max_msg_size (5)outgoing_port (6)incoming_port
int main(int argc, char* argv[]) {
    FILE *clientsFile;
    clientsFile = fopen(argv[1], "r");
    FILE *routingFile;
    routingFile = fopen(argv[2], "r");
    FILE *commands;
    commands = fopen(argv[3],"r");

    int maximumFrameSize = atoi(argv[4]);

    int numberOfClients;

    struct Client *clients = clientArrayMakerFromFile(clientsFile, &numberOfClients);

    struct Map *routingMap = routingFileImplementer(routingFile, clients, numberOfClients);

    //A function that executes the commands

    execute(commands, clients, numberOfClients, routingMap,
            maximumFrameSize, argv[5], argv[6]);

}
