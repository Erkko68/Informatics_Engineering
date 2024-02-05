#include<stdio.h>

int main(){

int f=1,num;

	//DEmand user an input
	printf("Enter a number: ");
	scanf("%d",&num);
	
	//for the initial value f=1 1*1=1, 1*2=2 2*3=6 changing the value i.
	for(int i=1;i<=num;i++){
	
	f=f*i;
	
	}

	printf("Factorial of %d is: %d\n",num,f);
}
