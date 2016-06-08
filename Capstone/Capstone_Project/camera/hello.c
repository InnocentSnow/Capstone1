#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

int main(void){
        char command[50];
        sleep(15);
	system("cd /home/pi/cap; java SendClient");


        return 0;
}
