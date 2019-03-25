#include <stdio.h>
#include <string.h>

//declarations together below:
int** ReadAndImplement(FILE *intxt,int sizex,int sizey);
int GetMultiplicationTotal(int** map, int matrixX, int matrixY, int** key, int keysize);
void TreasureHunt(int** mapMatrix, int** keyMatrix, int keysize, FILE *output , int x, int y, int* mapsize);

int main(int argc, char* argv[]) {
    char *size = argv[1];
    char *sizeList = strtok(size, "x");//splitting
    int sizeY = atoi(sizeList);
    sizeList = strtok(NULL, "x");
    int sizeX = atoi(sizeList);
    int mapsize[2]= {sizeX,sizeY};//assigning the char values and making them a list(x,y)
    int sizekey = atoi(argv[2]);//key map's size, since it has to be a square there is only one value:)
    //file assign down here:
    FILE *maptxt, *keytxt, *out;
    maptxt = fopen(argv[3], "r");
    keytxt = fopen(argv[4], "r");
    out = fopen(argv[5],"w");

    //read and implement the files
    int** mapMatrix = ReadAndImplement(maptxt, sizeX, sizeY);
    int** keyMatrix = ReadAndImplement(keytxt, sizekey, sizekey);

    //close the files that been used in order to save from memory :)
    fclose(maptxt);
    fclose(keytxt);
    //treasure hunt
    TreasureHunt(mapMatrix,keyMatrix,sizekey, out, 0, 0, mapsize);

    //free all these spaces:
    fclose(out);
    free(keyMatrix);
    free(mapMatrix);
}

//functions:

//read ind implement the txt into a 3-d array
int** ReadAndImplement(FILE *intxt, int sizex, int sizey) {
    int** array = 0;
    array = malloc(sizey*sizeof(int*));

    int i;
    for(i=0; i<sizey; i++){
        array[i] = malloc(sizex* sizeof(int));
    }
    int max = 1;
    int j;
    for(i=0; i<sizey; i++){
        for(j = 0; j<sizex; j++){
            fscanf(intxt, "%d", &array[i][j]);
        }
    }
    return array;
}

//real game's here
void TreasureHunt(int** mapMatrix, int** keyMatrix, int keysize, FILE *output , int x, int y, int* mapsize) {
    //matrix multiplicaiton in order to get the total
    int total, preTotal;
    preTotal = GetMultiplicationTotal(mapMatrix, x, y, keyMatrix, keysize);
    total = preTotal%5;

    //prints:
    int midSize = keysize / 2;
    fprintf(output,"%i,%i:%i\n",y+midSize,x+midSize,preTotal);//appending the stream into the output file
    //exit condition:
    if(total == 0) {//treasure found
        return;
    }else if(total == 1){//go up
        int temp = y - keysize;
        if(temp < 0)
            y += keysize;
        else
            y = temp;
    }else if(total == 2){//go down
        int temp = y + keysize;
        if(temp >= mapsize[1])
            y -= keysize;
        else
            y = temp;
    }else if(total == 3){//go right
        int temp = x + keysize;
        if(temp >= mapsize[0])
            x -= keysize;
        else
            x = temp;
    }else if(total == 4){//go left
        int temp = x - keysize;
        if(temp < 0)
            x += keysize;
        else
            x = temp;
   }
   TreasureHunt(mapMatrix, keyMatrix, keysize, output, x,y, mapsize);
}

//gets multiplication:(matrixX and matrixY are the coordinates of the left top corner)
int GetMultiplicationTotal(int** map, int matrixX, int matrixY, int** key, int keysize) {
    int total,i,j;//return value
    total = 0;
    for(i = 0; i < keysize ;i++){
        for(j =  0; j< keysize;j++){
            total+= key[j][i] * map[j+matrixY][i+matrixX];
        }
    }
    return total;
}
