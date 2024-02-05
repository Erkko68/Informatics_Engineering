//wite a program that reads a string of characters finished with /n

#include <stdio.h>
#include <ctype.h>

int main () {
	char c;
	int dec = 0;

	printf("Introdueix una sequencia de digits acabada en '\\n': ");
	scanf("%c", &c);
	while (isdigit(c)) {
		dec = dec * 10 + c - '0';
		scanf("%c", &c);
	}
	if (c == '\n') {
		printf("El valor decimal introduit es %d.\n", dec);
	} else {
		printf("Caracter incorrecte.\n");
	}
}
