//this is a program that prints some diamond shape depends on the input.
#include <stdio.h>
//take the input from the user as an integer

void printDiamond(int size){
    int i = 1;
    for(; i<=size; i++){
        //print spaces
        int x = size-i;
        for(; x>0; x--){
            printf(" ");
        }
        //print /
        printf("/");

        x = 0;
        for(; x<(i-1)*2; x++){
            printf(" ");
        }
        printf("\\\n");
    }
    i = 1;
    for(; i<=size; i++){
        //print spaces
        int x = 0;
        for(; x<i-1; x++){
            printf(" ");
        }
        //print /
        printf("\\");

        x = (size-i)*2;
        for(; x>0; x--){
            printf(" ");
        }
        printf("/\n");
    }
}

int main(int argc, char *argv[]){
    if(argc < 2)
        return -1;

    printf("Input: %s \n", argv[1]);
    printf("Output:\n");

    int lenofBorder = strtol(argv[1], NULL, 10);
    printDiamond(lenofBorder);

    return 0;
}
