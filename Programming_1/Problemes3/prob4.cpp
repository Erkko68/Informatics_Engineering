//create a program that does the power of a number using multiplication

#include <stdio.h>

int main(){

	int a,b,res=1,i=1; //define the a,b input variables, res for the result and i for the loop
	
	printf("Enter two values followed by comma: ");
	
	scanf("%i,%i",&a,&b); //scan and store variables
	
	if(a>=0 && b>=0){  //detect if the values are natural
	
		while(i<=b){ //if the i value is smaller than the b the loop will continue executing
	
			if(i==b){ //for decoration purposes, it simply removes the last "*" when shoing in screen the result
			
			printf("%i",a);
			
			}else{
			
			printf("%i*",a);
			
			}
		
		
		res = a*res; //adds the previous value to the new one
		
		i++; //adds a new value to i
		
		}
		
	printf("=%i\n",res); //prints result

	}else{
	
	printf("Invalid values.\n");
	
	}
}
