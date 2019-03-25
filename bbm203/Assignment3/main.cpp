#include <iostream>
#include <fstream>
#include <sstream>
#include <stdlib.h>
using namespace std;

class AwayNode{
    protected:
        AwayNode * prevPtr;
        AwayNode* nextPtr;
        string teamName;
        int minOfGoal;
        int matchID;
    public:
        AwayNode(string team, int min, int id) {
            teamName = team;
            minOfGoal = min;
            matchID = id;
            prevPtr = NULL;
            nextPtr = NULL;
        }
        int getMatchID(){
            return matchID;
        }
        string getName(){
            return teamName;
        }
        int getMin(){
            return minOfGoal;
        }
        void insertNextNode(AwayNode* nextNode) {
            if(nextPtr != NULL)
                nextPtr->prevPtr = nextNode;
            nextNode->prevPtr = this;

            nextNode->nextPtr = nextPtr;
            nextPtr = nextNode;
        }
        void insertPrevNode(AwayNode* prevNode) {
            prevNode->nextPtr = this;
            if(prevPtr != NULL)
                prevPtr->nextPtr = prevNode;

            prevNode->prevPtr = prevPtr;
            prevPtr= prevNode;
        }
        bool lessThan(AwayNode* other)
        {
            if(this->matchID < other->matchID)
                return true;
            else if(this->matchID > other->matchID)
                return false;
            else
            {
                return (this->minOfGoal < other->minOfGoal);
            }
        }
        bool hasNext()
        {
            return nextPtr != NULL;
        }
        bool hasPrev()
        {
            return prevPtr != NULL;
        }
        AwayNode* gotoNext()
        {
            return nextPtr;
        }
        AwayNode* gotoPrev()
        {
            return prevPtr;
        }
};

class PlayerNode{
    protected:
        std::string footballerName;
        std::string teamName;
        AwayNode * awayPtr;
        PlayerNode * nextPtr;
    public:
        PlayerNode(std::string name, std::string team){
            footballerName = name;
            teamName = team;
            awayPtr = NULL;
            nextPtr = NULL;
        }
        string getPlayerName(){
            return footballerName;
        }
        string getTeamName() {
            return teamName;
        }
        PlayerNode * nextNode(){
            return nextPtr;
        }
        AwayNode * awayList(){
                return awayPtr;
        }
        void insertNextNode(PlayerNode* nextNode) {
            nextNode->nextPtr = nextPtr;
            nextPtr = nextNode;
        }
        void insertPrevNode(PlayerNode* prevNode) {
            prevNode->nextPtr = this;
        }
        bool lessThan(PlayerNode* other)
        {
            int comparison = footballerName.compare(other->footballerName);
            if(comparison < 0)
                return true;
            else
                return false;
        }
        void addAway(AwayNode* away) {
            if (awayPtr == NULL) {
                awayPtr = away;
                return;
            } else {
                AwayNode *tmp = awayPtr;

                if (away->lessThan(tmp)) {
                    tmp->insertPrevNode(away);
                    awayPtr = away;
                    return;
                }

                while (tmp->hasNext()) {
                    tmp = tmp->gotoNext();
                    if (away->lessThan(tmp)) {
                        tmp->insertPrevNode(away);
                        return;
                    }
                }
                tmp->insertNextNode(away);
            }
        }

    bool hasNext() {
        return nextPtr != NULL;
    }

};

class TeamNode{
    protected:
        std::string teamName;
        TeamNode * nextPtr;
    public:
        TeamNode(std::string name){
            teamName = name;
            nextPtr = NULL;
        }
        string getName(){
            return teamName;
        }
        TeamNode * nextNode(){
            return nextPtr;
        }
        void insertNextNode(TeamNode* nextNode) {
            nextNode->nextPtr = nextPtr;
            nextPtr = nextNode;
        }
        bool hasNext(){
            return nextPtr != NULL;
        }
};

class SecondTypePlayerNode{
    protected:
        string footballer;
        SecondTypePlayerNode * nextPtr;
    public:
        SecondTypePlayerNode(string name){
            footballer = name;
            nextPtr = NULL;
        }
        string getName(){
            return footballer;
        }
    SecondTypePlayerNode * nextNode(){
        return nextPtr;
    }

        void insertNextNode(SecondTypePlayerNode * nextNode) {
            nextNode->nextPtr = nextPtr;
            nextPtr = nextNode;
        }
        bool hasNext(){
            return nextPtr != NULL;
        }
};

PlayerNode * playerList = NULL;
TeamNode * teamList = NULL;

void playerLL(string pString[5]){
    PlayerNode * currentNode = NULL;
    if (playerList == NULL){
        currentNode = new PlayerNode(pString[0],pString[1]);
        playerList = currentNode;
    }
    else
    {
        //first find player
        PlayerNode * tmp = playerList;

        do{
            if(tmp->getPlayerName().compare(pString[0]) == 0)
            {
                currentNode = tmp;
                break;
            }
            tmp = tmp->nextNode();
        }while(tmp != NULL);

        //no item than create new
        if(currentNode == NULL)
        {
            currentNode = new PlayerNode(pString[0],pString[1]);

            if(currentNode->lessThan(playerList))
            {
                playerList->insertPrevNode(currentNode);
                playerList = currentNode;
            }
            else
            {
                PlayerNode* tmp = playerList->nextNode();
                PlayerNode* tmpOld = playerList;
                bool inserted = false;
                while(tmp!=NULL){
                    if(currentNode->lessThan(tmp))
                    {
                        tmpOld->insertNextNode(currentNode);
                        inserted = true;
                        break;
                    }
                    tmpOld = tmp;
                    tmp = tmp->nextNode();
                }
                if(inserted == false)
                {
                    tmpOld->insertNextNode(currentNode);
                }
            }
        }


    }
    AwayNode * away = new AwayNode(pString[2], stoi(pString[3]), stoi(pString[4]));
    currentNode->addAway(away);

}

void pushTeamsList(string name)
{
    if(teamList == NULL)
    {
        teamList = new TeamNode(name);
        return;
    }
    TeamNode * node = teamList;
    TeamNode * lnode = NULL;
    while(node != NULL)
    {
        if(node->getName().compare(name) == 0)
            return;
        lnode = node;
        node = node->nextNode();
    }

    TeamNode * newNode = new TeamNode(name);
    lnode->insertNextNode(newNode);
}

void printPlayerMatch(ofstream& outputFile, string name) {

    PlayerNode * temp = playerList;
    while (temp != NULL) {
        if (temp->getPlayerName().compare(name) == 0) {
            AwayNode *temp2 = temp->awayList();
            outputFile << "Matches of " << temp->getPlayerName() << "\n";
            while (temp2 != NULL) {
                outputFile << "Footballer Name: " << temp->getPlayerName() << ",Away Team: " << temp2->getName() <<
                           ",Min of Goal: " << temp2->getMin() << ",Match ID: " << temp2->getMatchID() << "\n";
                temp2 = temp2->gotoNext();
            }
            return;
        }

        temp = temp->nextNode();

    }
}

void printPlayerAscending(ofstream& outputFile, string name) {
    PlayerNode* temp = playerList;
    int currentMatchID = -1;
    while (temp != NULL){
        if(temp->getPlayerName().compare(name) == 0) {
            AwayNode * temp2 = temp->awayList();
            while(temp2 != NULL){
                if (temp2->getMatchID() != currentMatchID) {
                    outputFile << "footballer Name: " << temp->getPlayerName() << ",Match ID: " << temp2->getMatchID()
                               << "\n";
                    currentMatchID = temp2->getMatchID();
                }
                temp2 = temp2->gotoNext();
            }
            return;
        }
        temp = temp->nextNode();
    }
}

void printPlayerDescending(ofstream& outputFile, string name) {
    PlayerNode* temp = playerList;
    int currentMatchID = -1;
    while (temp != NULL){
        if(temp->getPlayerName().compare(name) == 0) {
            AwayNode * temp2 = temp->awayList();
            while(temp2->hasNext()){
                temp2 = temp2->gotoNext();
            }
            while(temp2 != NULL){
                if (temp2->getMatchID() != currentMatchID) {
                    outputFile << "footballer Name: " << temp->getPlayerName() << ",Match ID: " << temp2->getMatchID()
                               << "\n";
                    currentMatchID = temp2->getMatchID();
                }
                temp2 = temp2->gotoPrev();
            }
            return;
        }
        temp = temp->nextNode();
    }
}


int main(int argc, char* argv[]) {

    ifstream inputFile;
    inputFile.open(argv[1]);

    ifstream operationsFile;
    operationsFile.open(argv[2]);

    ofstream outputFile;
    outputFile.open(argv[3]);

    if(!outputFile){
        cout << "unable to open the file...";
        exit(1);
    }
    string str;
    int countFirstPeriod = 0;
    int countSecondPeriod = 0;
    while (getline(inputFile, str)){
        string delimiter = ",";
        string line[5];
        for(int i = 0; i < 5; i++){
            line[i] = str.substr(0, str.find(delimiter));
            str.erase(0,  str.find(delimiter) + delimiter.length());
        }
        if(stoi(line[3]) < 46) countFirstPeriod++;
        else countSecondPeriod++;
        playerLL(line);
    }

    //calculations starts here:
    outputFile << "1)THE MOST SCORED HALF\n" << (countSecondPeriod > countFirstPeriod) << "\n";

    SecondTypePlayerNode * goalScorerLL = NULL;
    SecondTypePlayerNode * hattrickLL = NULL;

    int maxGoal = 0;
    PlayerNode * player = playerList;

    while(player != NULL){
        int matchID = -1;
        AwayNode * awayPtr = player->awayList();
        int currentGoal = 0;
        int hattrick = 0;
        bool hasHatTrick = false;

        pushTeamsList(player->getTeamName());
        while(awayPtr != NULL){
            if(awayPtr->getMatchID() == matchID){
                hattrick++;
                currentGoal++;
                if(hattrick == 3) hasHatTrick = true;
            }else{
                matchID = awayPtr->getMatchID();
                hattrick = 1;
                currentGoal++;
            }
            pushTeamsList(awayPtr->getName());
            awayPtr = awayPtr->gotoNext();

        }
        if (currentGoal > maxGoal){
            goalScorerLL = new SecondTypePlayerNode(player->getPlayerName());
            maxGoal = currentGoal;
        }else if(currentGoal == maxGoal){
            SecondTypePlayerNode * node = goalScorerLL;
            while(node->hasNext()){
                node = node->nextNode();
            }
            node->insertNextNode(new SecondTypePlayerNode(player->getPlayerName()));
        }

        if (hasHatTrick) {
            if(hattrickLL == NULL)
            {
                hattrickLL = new SecondTypePlayerNode(player->getPlayerName());
            }
            else {
                SecondTypePlayerNode *node = hattrickLL;
                while (node->hasNext()) {
                    node = node->nextNode();
                }
                node->insertNextNode(new SecondTypePlayerNode(player->getPlayerName()));
            }
        }
        player = player->nextNode();
    }

    outputFile << "2)GOAL SCORER\n";
    SecondTypePlayerNode * node = goalScorerLL;
    while(node != NULL){
        outputFile << node->getName() << "\n";
        node = node->nextNode();
    }
    outputFile << "3)THE NAMES OF FOOTBALLERS WHO SCORED HAT-TRICK\n";
    node = hattrickLL;
    while(node != NULL){
        outputFile << node->getName() << "\n";
        node = node->nextNode();
    }
    outputFile << "4)LIST OF TEAMS\n";
    TeamNode * team = teamList;
    while(team != NULL){
        outputFile << team->getName() << "\n";
        team = team->nextNode();
    }
    outputFile << "5)LIST OF FOOTBALLERS\n";
    PlayerNode * Currentplayer = playerList;
    while(Currentplayer != NULL){
        outputFile << Currentplayer->getPlayerName() << "\n";
        Currentplayer = Currentplayer->nextNode();
    }
    string operation[3][2];
    for (int j = 0; j <3; j++){
        getline(operationsFile, str);
        string delimiter = ",";
        for(int i = 0; i < 2; i++){
            operation[j][i] = str.substr(0, str.find(delimiter));
            str.erase(0,  str.find(delimiter) + delimiter.length());
        }
    }

    outputFile << "6)MATCHES OF GIVEN FOOTBALLER\n";

    printPlayerMatch(outputFile, operation[0][0]);
    printPlayerMatch(outputFile, operation[0][1]);


    outputFile << "7)ASCENDING ORDER ACCORDING TO MATCH ID\n";

    printPlayerAscending(outputFile, operation[1][0]);
    printPlayerAscending(outputFile, operation[1][1]);

    outputFile << "8)DESCENDING ORDER ACCORDING TO MATCH ID\n";

    printPlayerDescending(outputFile, operation[2][0]);
    printPlayerDescending(outputFile, operation[2][1]);

    return 0;
}
