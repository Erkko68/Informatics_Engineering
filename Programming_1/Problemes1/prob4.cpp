//make a program that shows the user value in diferent formats//

#include <stdio.h>

int main(){

	int val;
	
	printf("Enter your value: \n");
	scanf("%i",&val);
	
	printf("Your value in hex is: %0x \n",val);
	printf("Your value in octal is: %o \n",val);
	printf("Your value in decimal is: %d \n",val);
	
}
