#include <stdio.h>
#include <stdlib.h>
#include <string.h>


#define TREE_NO_RECORD              -2
#define TREE_INCORRECT_USERNAME     -1
#define TREE_NOT_ENOUGH_USERNAME    1
#define TREE_DELETE_SUCCESSFUL      2
/*
-a username password #add username to the tree with the given password
-s username #search with the given username and return the password if it is
-q username password #send query for the password with the given username
-d username #delete username and its password
-l #list the tree
 */

typedef struct Node{
  char  letter;
  struct Node * nextNode[26];
  char *password;
}Node;


void initilizeNode(Node * node){
    node->letter = NULL;
    int i = 0;
    for(; i < 26; i++){
        node->nextNode[i] = NULL;
    }
    node->password = NULL;
}

int indexCalculator(char letter){
    if(letter <= 90){ //if uppercase
        letter += 32; //make lowercase
    }
    letter -= 'a'; // lowercase a is 0 and index comes from this equation
    return letter;

}

int addToTree(char * name, Node * root, char* password){
    int index = indexCalculator(name[0]);
    if(root->nextNode[index] == NULL && index >= 0){
        Node * currentLettersNode;
        currentLettersNode = (Node*) malloc (sizeof(Node));
        initilizeNode(currentLettersNode);
        currentLettersNode->letter = name[0];
        root->nextNode[index] = currentLettersNode;
    }
    if (name[0] != '\0'){
        addToTree(name+ sizeof(char), root->nextNode[index], password);
    }
    else{
        if(root->password == NULL) {
            root->password = (char *) malloc(sizeof(char)*256);
            strcpy(root->password, password);
            return 1;
        }
        else
            return 0;
    }
}

char * searchName(char * name, Node * root, int isFirst){
    char * password = NULL;
    int index = indexCalculator(name[0]);
    if(root->nextNode[index] == NULL){
        if(isFirst)
            return "0rec";
        else
            return "2rec";
    }
    if (name[1] != '\0'){
        searchName(name+1, root->nextNode[index], 0);
    }
    else{
        if(root->nextNode[index]->password != NULL) {
            return root->nextNode[index]->password;
        }
        else
            return "1rec";
    }
}

int childCount(Node * root){
    int i = 0;
    int child = 0;
    for(; i<26; i++){
        if (root->nextNode[i] != NULL)
            child++;
    }
    return child;
}

//deleteme are return parameters;
int deleteFromTree(char name[256], Node *root, int* deleteme, FILE * output) {
    //string finished
    if (name[0] == '\0') {
        if (root->password == NULL) {
            (*deleteme) = 0;
            return TREE_NOT_ENOUGH_USERNAME;
        } else {
            if (childCount(root) == 0) {
                (*deleteme) = 1;
            } else {
                (*deleteme) = 0;
            }
            return TREE_DELETE_SUCCESSFUL;
        }
    }
    int index = indexCalculator(name[0]);
    if (root->nextNode[index] == NULL) {
        (*deleteme) = 0;
        return TREE_NO_RECORD;
    }
    int retVal = deleteFromTree(name+1, root->nextNode[index], deleteme, output);

    if(retVal == TREE_NO_RECORD || retVal == TREE_INCORRECT_USERNAME)
        return TREE_INCORRECT_USERNAME;

    if(retVal == TREE_NOT_ENOUGH_USERNAME)
        return TREE_NOT_ENOUGH_USERNAME;

    if(retVal == TREE_DELETE_SUCCESSFUL)
    {
        if((*deleteme) == 1)
        {
            Node* child = root->nextNode[index];
            root->nextNode[index] = NULL;
            if(child->password != NULL)
            {
                free(child->password);
            }
            free(child);
            if (childCount(root) == 0) {
                (*deleteme) = 1;
            }
            else {
                (*deleteme) = 0;
            }
        }
    }
    return TREE_DELETE_SUCCESSFUL;

}
void printSpaces(int num, FILE * output)
{
    int i=0;
    for(; i<num;i++)
    {
        fprintf(output, " ");
    }
}

void printName(char name[256], FILE * output)
{
    int i = 0;
    for(; i < 256; i++){
        if(name[i] != 0x00)
            fprintf(output, "%c", name[i]);
        else
            break;
    }
}

int listTree(Node* node, int numSpaces, int deep, char name[256], FILE * output)
{
    fprintf(output, "%c", node->letter);
    name[deep] = node->letter;
    int cCount = childCount(node);

    if(cCount > 1)
    {
        fprintf(output, "\n");
        int i = 0;
        for(; i < 26; i++){
            if(node->nextNode[i] != NULL) {
                printSpaces(numSpaces, output);
                fprintf(output, "-");
                printName(name, output);
                char tmpname[256];
                memcpy(tmpname, name, 256);
                listTree(node->nextNode[i], numSpaces+4, deep+1, tmpname, output);
                fprintf(output, "\n");
            }
        }
        return 1;
    }

    if(cCount == 1)
    {
        int i = 0;
        for(; i < 26; i++){
            if(node->nextNode[i] != NULL) {
                if(node->password != NULL){
                    fprintf(output, ", ");
                    printName(name, output);
                }
                char tmpname[256];
                memcpy(tmpname, name, 256);
                return listTree(node->nextNode[i], numSpaces, deep+1, tmpname, output);
            }
        }
    }

    return 0;
}

void listTreeTopNode(Node* node, FILE * output)
{
    char name[256];
    int deep = 0;
    int i = 0;
    for(; i < 256; i++){
        name[i] = 0x00;
    }
    fprintf(output, "-");
    if(listTree(node, 4, deep, name, output) == 0)
        fprintf(output, "\n");

}

void readAndImplement(FILE * inputFile, Node * root, FILE * output){

    char password[256];
    char command[3];
    char name [256];

    while(!feof(inputFile)) {
        fscanf(inputFile, "%s\n", &command);

        if (strcmp(command, "-l") == 0) { //list
            //no need for anything else just list
            int i = 0;
            for(; i < 26; i++){
                if(root->nextNode[i] != NULL)
                    listTreeTopNode(root->nextNode[i], output);
            }
            continue;
        }

        if (strcmp(command, "-a") == 0) { //add
            fscanf(inputFile, "%s", &name);
            fscanf(inputFile, "%s", &password);

            int hasAdded = addToTree(name, root, (char*)&password);
            if(hasAdded)
                fprintf(output, "\"%s\" was added\n", name);
            else
                fprintf(output, "\"%s\" reserved username\n", name);
            continue;
        }

        if (strcmp(command, "-s") == 0) { // search
            fscanf(inputFile, "%s", &name);

            //take name and search it in the tree
            /*
            "hakan" no record
            "sel" not enough username
            "ozge" password "612"
            "selam" incorrect username
            */

            char* pass = searchName(name, root, 1);
            if(strcmp(pass, "0rec") == 0) // no records not even first element
                fprintf(output, "\"%s\" no record\n",name);
            else if(strcmp(pass, "1rec") == 0) //multiple records
                fprintf(output,"\"%s\" not enough username\n", name);
            else if(strcmp(pass, "2rec") == 0) //wrong username
                fprintf(output, "\"%s\" incorrect username\n", name);
            else
                fprintf(output, "\"%s\" password \"%s\"\n", name, pass);


            continue;
        }

        if (strcmp(command, "-q") == 0) { //control password
            fscanf(inputFile, "%s", &name);
            fscanf(inputFile, "%s", &password);

            //take name and password and look for it
            /*
            "alaeddin" no record
            "seli" not enough username
            "selma" incorrect password
            "cemal" successful login
            */

            char* pass = searchName(name, root, 1);
            if(strcmp(pass, "0rec") == 0 )
                fprintf(output, "\"%s\" no record\n",name);
            else if(strcmp(pass, "2rec") == 0)
                fprintf(output,"\"%s\" incorrect username\n", name);
            else if(strcmp(pass, "1rec") == 0) //multiple records
                fprintf(output, "\"%s\" not enough username\n", name);
            else{
                if(strcmp(pass, password) == 0) {
                    fprintf(output, "\"%s\" successful login\n", name);
                    continue;
                }
                fprintf(output,"\"%s\" incorrect password\n", name);
            }

            continue;

        }

        if (strcmp(command, "-d") == 0) { //delete
            fscanf(inputFile, "%s", &name ,1);

            //take name and delete
            /*
            "yildiz" no record
            "cihan" incorrect username
            "oz" not enough username
            "selim" deletion is successful
            */
            int deleteme = 0;
            int deletionStatus = deleteFromTree(name, root, &deleteme, output);
            if(deletionStatus == TREE_NO_RECORD)
                fprintf(output, "\"%s\" no record\n",name);
            else if(deletionStatus == TREE_INCORRECT_USERNAME)
                fprintf(output, "\"%s\" incorrect username\n", name);
            else if(deletionStatus == TREE_DELETE_SUCCESSFUL)
                fprintf(output, "\"%s\" deletion is successful\n", name);
            else if(deletionStatus == TREE_NOT_ENOUGH_USERNAME)
                fprintf(output, "\"%s\" not enough username\n", name);

            continue;
        }
    }
}

int main() {
    Node *root;
    root = (Node*) malloc (sizeof(Node));
    initilizeNode(root);
    FILE * input = NULL;
    FILE * output = NULL;
    input = fopen("input.txt", "r");
    output = fopen("output.txt", "w");


    readAndImplement(input, root, output);

    return 0;

}
