#include <stdio.h>

int main(){

int a,b,k;
int sum;

	printf("Insert a range of numbers separed by comma: ");
	scanf("%i,%i",&a,&b);
	
	k=a;
	
	while(k<=b){
	
	sum = k+sum;
	
	k++;
	
	printf("%i\n",k);
	
	}

	printf("%i\n",sum);
	
}

