//create a program that shows the sum, dif, div, and res of 2 given values

#include <stdio.h>

int main(){

	int n1,n2,sum,div,dif,res;
	
	printf("Insert two values followed by a comma: \n");
	scanf("%i,%i",&n1,&n2);

	sum=n1+n2;
	dif=n1-n2;
	div=n1/n2;
	res=n1%n2;
		
	

	printf("The sum of the two values is: %i \n",sum);
	printf("The dif of teh two valus is: %i: \n",dif);
	printf("The div of the two values is: %i: \n",div);
	printf("The res of the two values is: %i \n",res);
	
}
