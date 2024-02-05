//Practica 1: Crear un programa que simuli la funcionalitat d'una ruleta de casino.

//inlcoure llibreries estandard

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

//defiinir variables globals

int capital=1000; 	//definir capital inicial
int mode;		//definir mode de joc
int aposta;		//definir valor de la aposta en euros
int guess;		//numero que tria el jugador
int profit;		//guanys
int ruleta;		//(Nombre generat aleatoriament per la ruleta)
int perd,guanya; 	//variables per registrar les vegades que guanya o perd.


//inicialització generacio de nombres aleatoris.

//funció per mostrar la user interface.

void ui_print(){

	printf("Tens un capital de %i Euros. A quin joc vols jugar?\n",capital);
	printf("-Prem 0 per sortir\n");
	printf("-Prem 1 per jugar a endevinar el numero de l'1 al 36\n");
	printf("-Prem 2 per jugar a endevinar si sera parell o imparell\n");
	printf(">");
	
	scanf("%i",&mode); //determinar quin joc vol jugar el jugador guardant el nombre a la variable "mode".

}

//Mode de joc 1, endevinar el numero de la ruleta.

void mode_1(){

	//mostrar ui
	
	printf("Tens un capital de %i Euros, quina quantitat vols apostar? \n",capital); 
	printf(">");
	scanf("%i",&aposta);
			
		while(aposta>capital || aposta<=0){ //verificar que el jugador té prous diners
				
			printf("No tens prous diners!\n");
			printf(">");
			scanf("%i",&aposta);
				
		}
				
		printf("-Introdueix el numero per la teva aposta (de l'1 al 36):\n"); //mostrar ui
		printf(">");
			
		scanf("%i",&guess);
			
			while(guess<1 || guess>36){ //verificar que el valor introduit esta dins el rang de la ruleta
				
				printf("Valor incorrecte!\n");
				printf(">");
				scanf("%i",&guess);
				
			}
				
		printf("Has apostat pel numero %i i ha sortit %i\n",guess,ruleta); //mostrar resultats
			
			
			if(guess==ruleta){ //si guanya calcular guanys
			
				profit = (aposta*36)-aposta; 
				
				printf("Has guanyat %i Euros!\n",profit);
				
				capital = capital+profit;
				
				guanya++;
				
			}else{ //si perd calcular perdues
				
				printf("Has perdut %i Euros!\n",aposta);
				
				capital = capital-aposta;
				
				perd++;
				
			}
			
//tornar a demanar al jugador quin mode vol jugar en cas que no ho hagi perdut tot. I generar un nou numero aleatori.
			
		if(capital>0){ 
		
			ruleta = rand() % 37;
			ui_print();
			
		}

}

//definir la funcio mode_2, per a executar el segon mode de joc de endevinar si el nombre es parell o imparell

void mode_2(){

//mostrar ui

	printf("Tens un capital de %i Euros, quina quantitat vols apostar? \n",capital);
	printf(">");
	scanf("%i",&aposta);
			
		while(aposta>capital || aposta<=0){ //verificar que el jugador té prous diners
				
			printf("No tens prous diners!\n");
			printf(">");
			scanf("%i",&aposta);
				
		}
		
	printf("Prem 1 per apostar a numero imparell o 2 per apostar a numero parell:\n");
	printf(">");
			
	scanf("%i",&guess);
			
		while(guess!=1 && guess!=2){ //verificar el valor introduit
				
			printf("Valor incorrecte!\n");
			printf(">");
			scanf("%i",&guess);
				
		}
		
		//regla de la ruleta, si surt el numero 0 el jugador sempre perd.
		
		if(ruleta==0){
		
			printf("Ha sortit el 0!\n");
			
			printf("Has perdut %i Euros!\n",aposta);
				
			capital = capital-aposta;
				
			perd++;
			
		}else {
		
				if(guess==1){
			
					printf("Has apostat per imparell i ha sortit %i\n",ruleta); //aposta imparell
			
				}else {
			
					printf("Has apostat per parell i ha sortit %i\n",ruleta); //aposta parell
			
				}
				
//determinar si el nombre de la ruleta es parell mirant si obtenim residu.

				if((ruleta % 2 == 0 && guess==1) || (ruleta % 2 != 0 && guess==2)){ //definir casos en els que es perd dins la condicio del if
		
					printf("Has perdut %i Euros!\n",aposta);
				
					capital = capital-aposta;
				
					perd++;
		
				}else if((ruleta % 2 == 0 && guess==2) || (ruleta % 2 != 0 && guess==1)){ //definir casos en els que es guanya dins la condicio del if
			
					printf("Has guanyat %i Euros!\n",aposta);
				
					capital = capital+aposta;
				
					guanya++;
				}
			
			}
		
//tornar a demanar al jugador quin mode vol jugar en cas que no ho hagi perdut tot. I generar un nou numero aleatori.
	
	if(capital>0){
		
		ruleta = rand() % 37;
		ui_print();
			
	}
}


//funcio principal

int main(){

	srand(time(NULL)); //inicialització generació numero aleatori
	
	printf("Benvingut al joc de la Ruleta!\n");  //Mostrar en pantalla la funcionalitat del programa.
	
	ruleta = rand() % 37; //generar nombre aleatori
	
	ui_print();	//Cridar a la funció ui_print per a mostrar els modes de joc
	
	//mentres el jugador no premi 0 o el seu capital sigui 0 es seguira executant.
	
	while((mode!=0) && (capital>0)){ 
		
		//verificar que s'ha seleccionat un mode de joc valid.
		
		while(mode!=0 && mode!=1 && mode!=2){
		
			printf("Mode incorrecte!\n");
			printf(">");
			scanf("%i",&mode);
		
		}
		
		while(mode==1 && capital>0){

			mode_1(); 		//En cas de seleccionar mode 1 cridar a la funcio mode_1();

		}
		
		while(mode==2 && capital>0){

			mode_2();		//En cas de seleccionar mode 2 cridar a la funcio mode_2();

		}
		

	}
	
	//indicar al jugador resultats finals.
	
	if(capital<=0){
	
		printf("Ho has perdut tot!\n"); //si el capital es igual a 0 mostrar que ho ha perdut tot.
	
	}else{
	
	printf("El teu capital final es de %i Euros.\n",capital); //mostrar capital final
	
	}
	
	//mostrar el nombre de vegades que el jugador ha guanyat i perdut.
	
	printf("Has guanyat %i partides i n'has perdut %i.\n",guanya,perd); 

	
}
