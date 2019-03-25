//
// Created by idyll on 25.11.2018.
//

#ifndef FRAMEQUEUE_H
#define FRAMEQUEUE_H

#define MAXSIZE 20

struct ApplicationFrame
{
    int size;
    char* messagePart;
    char senderId[MAXSIZE];
    char recieverId[MAXSIZE];
};

struct TransportationFrame
{
    char senderPort[MAXSIZE];
    char recieverPort[MAXSIZE];
};

struct NetworkFrame
{
    char senderIP[MAXSIZE];
    char recieverIP[MAXSIZE];
};

struct PhysicalFrame
{
    char senderMAC[MAXSIZE];
    char recieverMAC[MAXSIZE];
};

struct NetworkStack
{
    void* data[4];
    int top;
    struct NetworkStack* nextNode;
};

void push(struct NetworkStack *stack, void* data)
{
    stack->data[stack->top] = data;
    stack->top++;
}
void* pop(struct NetworkStack *stack)
{
    void* data;
    stack->top--;
    data = stack->data[stack->top];
    return data;
}
void init(struct NetworkStack *stack)
{
    stack->top = 0;
}

struct MessageQueue
{
    struct NetworkStack* first;
    struct NetworkStack* last;
};

void messageQueueInit(struct MessageQueue *queue)
{
    queue->first = 0;
    queue->last = 0;
}
void messageQueuePush(struct MessageQueue *queue, struct NetworkStack* node)
{
    node->nextNode = 0;
    if(queue->last == 0) {
        queue->first = node;
        queue->last = node;
    }
    else {
        queue->last->nextNode = node;
        queue->last = node;
    }
}
struct NetworkStack* messageQueuePop(struct MessageQueue *queue)
{
    struct NetworkStack* node = queue->first;
    if(queue->first != 0)
        queue->first = queue->first->nextNode;

    if(queue->first == 0)
        queue->last = 0;

    return node;
}
int getMessageQueueSize(struct MessageQueue *queue)
{
    if(queue->first == 0)
        return 0;

    struct NetworkStack* tmp = queue->first;
    int size = 1;
    while(tmp->nextNode != 0)
    {
        tmp = tmp->nextNode;
        size++;
    }
    return size;
}
#endif //FRAMEQUEUE_H
