//calculate the quotient and remainder of two given values using the substraction

#include <stdio.h>

int main(){

int a,b,quo=0,rem; //define variables



printf("Enter two values divisor and dividend separated by comma : "); //demand user for input
scanf("%i,%i",&a,&b); //scan values and store them

   rem = a;
   
if(a>0){
   
		while(rem>=b){ //this loop while continue substracting the dividend values until it cannot anymore so the last value will be the remainder.
	
		rem = rem-b;
	
		quo++; //the amount of times it doe sthe loop it will be the quotient of the opertion.
	
	
		}

	
	}else if(a<0){
	
	while(rem<=0){ 
	
	quo++; //the amount of times it doe sthe loop it will be the quotient of the opertion.
	
	rem = rem+b;
	
	
		}

	}
	
	printf("For the operation %i/%i the Quotient is %i and the remainder is %i.\n",a,b,quo,rem);
}
