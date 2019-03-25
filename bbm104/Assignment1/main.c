//This program is a war simulator between heroes and monsters. The files will be taken
//as command line arguments. First file will be the characters and the second file will
//be contain the commands.

//external libraries:
#include <stdio.h>
#include <string.h>

#define NAME_SIZE 50
#define LINE_SIZE 1024

typedef struct { //structs
    char name[NAME_SIZE];
    int col;
    int row;
    int hp;
    int damage;
    int xp;
}Hero;
typedef struct {
    char name[NAME_SIZE];
    int col;
    int row;
    int hp;
    int damage;
}Monster;
typedef struct {
    int inside; // 0 for blank, 1 for hero and 2 for monster
    char name[NAME_SIZE]; //name for heroes and monsters
}Point;

char *chars[LINE_SIZE];
char *commands[LINE_SIZE];

int heroSize = 0; //defining amount of  heroes
int monsterSize = 0;//defining amount of  monsters
Hero *locHero = NULL; //array of heroes
Monster *locMonst = NULL;//array of monsters
int mapColSize = 0;
int mapRowSize = 0;
Point** map = NULL;

//finding a specific hero
int findHero(char* name){
    int index;
    for(index = 0; index < heroSize; index++){
        if(strcmp(name, locHero[index].name) == 0)
            return index;
    }
    return -1;
}

//finding a specific monster
int findMonster(char* name){
    int index;
    for(index = 0; index < monsterSize; index++){
        if(strcmp(name, locMonst[index].name) == 0)
            return index;
    }
    return -1;
}

//showing status of the map
void showMap(){
    printf("MAP STATUS\n");
    int row, col = 0;
    for (row = 0; row < mapRowSize; row++){
        for (col = 0; col < mapColSize; col++){
            if(map[row][col].inside == 0)
                printf(". ");
            else
                printf("%c ", map[row][col].name[0]);
        }
        printf("\n");
    }
    printf("\n");
}
//showing hero status
void showHeroes(){
    int heroIndex = 0;
    printf("HERO STATUS\n");
    for(heroIndex=0; heroIndex<heroSize; heroIndex++){
        printf("%s HP: %d XP: %d\n",locHero[heroIndex].name, locHero[heroIndex].hp, locHero[heroIndex].xp);
    }
    printf("\n");
}
//showing monster status
void showMonsters(){
    int monsterIndex = 0;
    printf("MONSTER STATUS\n");
    for(monsterIndex=0; monsterIndex<monsterSize; monsterIndex++){
        printf("%s HP: %d\n",locMonst[monsterIndex].name, locMonst[monsterIndex].hp);
    }
    printf("\n");
}

//for attack func finding the enemy
const char* findEnemy(int pos, int row, int col, int isHero){
    int currow = row;
    int curcol = col;
    switch(pos){
    case 1:
        currow -= 1;
        break;
    case 2:
        currow -= 1;
        curcol += 1;
        break;
    case 3:
        curcol += 1;
        break;
    case 4:
        curcol += 1;
        currow += 1;
        break;
    case 5:
        currow += 1;
        break;
    case 6:
        currow += 1;
        curcol -= 1;
        break;
    case 7:
        curcol -= 1;
        break;
    case 8:
        curcol -= 1;
        currow -= 1;
        break;
    }

    //if enemy or not:
    if(curcol < 0 || curcol >= mapColSize || currow < 0 || currow >= mapRowSize)
        return NULL;

    if(map[currow][curcol].inside == 0)
        return NULL;

    if(map[currow][curcol].inside == 1 && isHero)
        return NULL;

    if(map[currow][curcol].inside == 2 && !isHero)
        return NULL;

    return map[currow][curcol].name;
}

//this is where main function starts:
int main(int argc, char *argv[]) {
    //opening files:
    FILE *filechar;
    FILE *filecommand;
    filechar = fopen(argv[1], "r");
    filecommand = fopen(argv[2], "r");

    //the loop that reads the chars file and assign them to struct arrays
    do{
        fgets(chars, LINE_SIZE, filechar);
        char *scp;//splitted char piece of file
        scp = strtok(chars,",\n");
        int element = 0;
        int isHero;
        Hero hero;
        Monster monster;
        while(scp != NULL){
            switch(element){
            case 0://hero or not
                if (strcmp ("HERO", scp) == 0){
                    isHero = 1;
                    heroSize++;
                    locHero = (Hero*)realloc(locHero, heroSize*sizeof(Hero));
                }
                else{
                    isHero = 0;
                    monsterSize++ ;
                    locMonst = (Monster*)realloc(locMonst, monsterSize*sizeof(Monster));
                }
                break;
            case 1://name of hero or monster
                if(isHero){
                    strcpy(locHero[heroSize-1].name, scp);
                }
                else{
                    strcpy(locMonst[monsterSize-1].name, scp);
                }

                break;
            case 2: //hp of the hero or monster
                if(isHero){
                    locHero[heroSize-1].hp = atoi( scp );
                }
                else{
                    locMonst[monsterSize-1].hp = atoi( scp );
                }

                break;
            case 3: //damage of hero or monster
                if(isHero){
                    locHero[heroSize-1].damage = atoi( scp );
                }
                else{
                    locMonst[monsterSize-1].damage = atoi( scp );
                }

                break;
            }

            locHero[heroSize-1].xp = 0; // if hero set the xp to zero
            element++;
            scp = strtok (NULL, ",\n");
        }
    }while(!feof(filechar));

    //the loop for commands file and main loop of program
    do{
        fgets(commands, LINE_SIZE, filecommand);
        char *scp;//splitted char piece of file
        scp = strtok(commands," \n");
        int arrSize = 0;

        if(scp == NULL)
            break;

        if( strcmp ("LOADMAP", scp) == 0){ //the loadmap command
            scp = strtok (NULL, " \n");
            mapRowSize = atoi( scp );//row size
            scp = strtok (NULL, " \n");
            mapColSize = atoi( scp );//column size

            //making the map
            map = (Point**)malloc(sizeof(Point*) * mapRowSize);
            int row, col = 0; // assigning base values for row and column
            for (row = 0; row < mapRowSize; row++){
                map[row] = malloc(sizeof(Point) * mapColSize); // allocating a memory area for rows
            }
            for (row = 0; row < mapRowSize; row++){
                for (col = 0; col < mapColSize; col++){ // allocating a memory area for columns
                    map[row][col].inside = 0; //setting the map empty
                }
            }
        }
        else if( strcmp ("PUT", scp) == 0){ //the put function
            scp = strtok (NULL, " \n");
            char type[10];
            strcpy(type, scp);

            scp = strtok (NULL, " \n");
            while(scp != NULL){
                char name[NAME_SIZE];
                strcpy(name, scp);

                scp = strtok (NULL, " \n"); //taking the row value
                int row;
                row = atoi( scp );

                scp = strtok (NULL, " \n"); // taking the column value
                int col;
                col = atoi( scp );

                if(strcmp(type, "HERO") == 0){ //heroes
                    int index = findHero(name);
                    locHero[index].col = col;//writing it in the struct
                    locHero[index].row = row;

                    strcpy(map[row][col].name, name);//adding it to the array
                    map[row][col].inside = 1;
                }
                else{ // monsters
                    int index = findMonster(name);
                    locMonst[index].col = col;//writing it in the struct
                    locMonst[index].row = row;

                    strcpy(map[row][col].name, name);//adding it to the array
                    map[row][col].inside = 2;
                }

                scp = strtok (NULL, " \n"); //next arg
            }
        }
        else if( strcmp ("SHOW", scp) == 0){ // the show function
            scp = strtok (NULL, " \n");
            if( strcmp ("MAP", scp) == 0) // show map
                showMap();
            else if( strcmp ("HERO", scp) == 0) //show hero
                showHeroes();
            else if( strcmp ("MONSTER", scp) == 0) // show monster
                showMonsters();
        }
        else if( strcmp("ATTACK", scp) == 0){ //attack function
            scp = strtok (NULL, " \n");
            if( strcmp("MONSTER", scp) == 0){ //attack monster
                int monsterIndex;
                for(monsterIndex = 0; monsterIndex< monsterSize; monsterIndex++){ //taking monsters in the creating order
                    Monster* monst = &locMonst[monsterIndex];
                    if(monst->hp == 0)
                        continue;
                    int mainrow = monst->row; //now
                    int maincol = monst->col; //column
                    int i = 1;
                    char enemyName[NAME_SIZE];
                    while(i<9){
                        const char* tmp = findEnemy(i, mainrow, maincol, 0); //looking for an enemy in the clockwise
                        if(tmp != NULL){
                            strcpy(enemyName, tmp);
                            int hIndex = findHero(enemyName);

                            Hero* hero = &locHero[hIndex];
                            hero->hp -= monst->damage;
                            if (hero->hp < 1){ //if hero dies:
                                map[hero->row][hero->col].inside = 0; // remove it from the map
                                hero->hp = 0;
                            }
                        }
                        i++;
                    }
                }
                printf("MONSTERS ATTACKED\n\n");
            }
            else if( strcmp("HERO", scp) == 0){ //attack hero
                int heroIndex;
                for(heroIndex = 0; heroIndex< heroSize; heroIndex++){ // taking heroes in the creating order
                    Hero* hero = &locHero[heroIndex];
                    if(hero->hp == 0)
                        continue;
                    int mainrow = hero->row;
                    int maincol = hero->col;
                    int i = 1;
                    char enemyName[NAME_SIZE];
                    while(i<9){
                        const char* tmp = findEnemy(i, mainrow, maincol, 1);//looking for an enemy in the clockwise
                        if(tmp != NULL)
                        {
                            strcpy(enemyName, tmp);
                            //printf("attacker:%s pos:%d enemy name:%s\n",hero->name, i, enemyName);
                            int mIndex = findMonster(enemyName);

                            Monster* monst = &locMonst[mIndex];
                            monst->hp -= hero->damage;
                            if (monst->hp < 1){ //if a monster dies:
                                map[monst->row][monst->col].inside = 0;// remove it from the map
                                monst->hp = 0;
                                hero->xp ++; // hero's xp increased
                            }
                        }
                        i++;
                    }
                }
                printf("HEROES ATTACKED\n\n");
            }

        }
        else if( strcmp("MOVE", scp) == 0){ // move function
            scp = strtok(NULL, " \n"); //reads hero or monster
            if(strcmp("HERO",scp)==0){ //if hero
                printf("HEROES MOVED\n");
                scp = strtok(NULL," \n");
                while(scp != NULL){
                    int heroIndex = findHero(scp); //finds hero's index
                    Hero *hero = &locHero[heroIndex];
                    if(hero->hp == 0){ //isDead
                        printf("%s can't move. Dead.\n", hero->name);
                        scp = strtok(NULL, " \n");
                        scp = strtok(NULL, " \n");//passing this hero
                    }
                    else{
                        int newrow = atoi(strtok(NULL, " \n"));
                        int newcol = atoi(strtok(NULL, " \n"));
                        if(newrow < 0 || newrow >= mapRowSize || newcol <0 || newcol >= mapColSize){
                            printf("%s can't move. There is a wall.\n", hero->name);
                        }
                        else if(map[newrow][newcol].inside != 0){
                            printf("%s can't move. Place is occupied.\n", hero->name);
                        }
                        else
                        {
                            map[hero->row][hero->col].inside = 0;
                            strcpy(map[newrow][newcol].name, hero->name);
                            map[newrow][newcol].inside = 1;
                            hero->row = newrow;
                            hero->col = newcol;
                        }
                    }
                    scp = strtok(NULL," \n");
                }
                printf("\n");
            }
            else if(strcmp("MONSTER",scp)==0){ //if monster
                printf("MONSTERS MOVED\n");
                scp = strtok(NULL," \n");
                while(scp != NULL){
                    int monsterIndex = findMonster(scp); //finds monster's index
                    Monster *monst = &locMonst[monsterIndex];
                    if(monst->hp == 0){ //isDead
                        printf("%s cannot move. Dead.\n", monst->name);
                        scp = strtok(NULL, " \n");
                        scp = strtok(NULL, " \n");//passing this monster
                    }
                    else{
                        int newrow = atoi(strtok(NULL, " \n"));
                        int newcol = atoi(strtok(NULL, " \n"));
                        if(newrow < 0 || newrow >= mapRowSize || newcol <0 || newcol >= mapColSize){
                            printf("%s cannot move. There is a wall.\n", monst->name);
                        }
                        else if(map[newrow][newcol].inside != 0){
                            printf("%s cannot move. Place is occupied.\n", monst->name);
                        }
                        else{
                            map[monst->row][monst->col].inside = 0;
                            strcpy(map[newrow][newcol].name, monst->name);
                            map[newrow][newcol].inside = 1;
                            monst->row = newrow;
                            monst->col = newcol;
                        }
                    }
                    scp = strtok(NULL," \n");
                }
                printf("\n");
            }
        }
        int heroIndex;
        int monstIndex;
        int heroAlive = 0;
        int monstAlive = 0;

        for(heroIndex = 0; heroIndex < heroSize; heroIndex++){
            if(locHero[heroIndex].hp > 0){
                heroAlive = 1;
                break;
            }
        }
        if(heroAlive < 1){
            printf("ALL HEROES ARE DEAD !");
            return 0;
        }
        for(monstIndex = 0; monstIndex < monsterSize; monstIndex++){
            if(locMonst[monstIndex].hp > 0){
                monstAlive = 1;
                break;
            }
        }
        if(monstAlive < 1){
            printf("ALL MONSTERS ARE DEAD !");
            return 0;
        }
    }
    while(!feof(filecommand));

    fclose(filechar);
    fclose(filecommand);

    return 0;
}
