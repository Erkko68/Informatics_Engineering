//This program solves 4x4 matrices using the Laplace expansion methodology, it uses sub matrices of the original one to calculate the final determinant.

#include <stdio.h>

//define calc functions

float calculate_4x4(float matrix_4x4[4][4]);
float calculate_3x3(float matrix_3x3[3][3]);
float calculate_2x2(float matrix_2x2[2][2]);
float show_results(float det,float matrix_4x4[][4]);
void scan_store(float matrix_4x4[4][4]);

//scan and store the matrix

void scan_store(float matrix_4x4[][4]){

	printf("Introdueix la matriu 4x4: \n");
	
	for(int i=0;i<4;i++){
		for(int j=0;j<4;j++){
			scanf("%g",&matrix_4x4[i][j]);
		}
	}
}

//calculate the determinant of the matrix 4x4
/*
    | a b c d |       | f g h |   | e g h |   | e f h |   | e f g |
|A|=| e f g h | =   a*| j k l |-b*| i k l |+c*| i j l |-d*| i j k |
    | i j k l |       | n o p |   | m o p |   | m n p |   | m n o |
    | m n o p |

*/

float calculate_4x4(float matrix_4x4[][4]){

	float matrix_3x3[3][3];
	float det_4x4=0;
	int row=0,column=0;
	
	for(int k=0;k<4;k++){
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				if(i!=0 and j!=k){
					//Debug
					//printf("%g ",matrix_4x4[i][j]);
					
					//Create each sub 3x3 matrix
					matrix_3x3[row][column]=matrix_4x4[i][j];
					column++;
				}
			}
			//Debug
			//printf("\n");
			if(column==1){
				row++;
			}
		}
		//Calculate determinant
		if(k==0 or k==2){
			det_4x4 = calculate_3x3(matrix_3x3)*matrix_4x4[0][k]+det_4x4; 
		}else if(k==1 or k==3){
			det_4x4 = -calculate_3x3(matrix_3x3)*matrix_4x4[0][k]+det_4x4;
		}
		row=0;
		column=0;
	}	
	
	return det_4x4;
}


//Calculate the determinant of the 3x3 matrix
/* 

     |a b c|
|A|= |d e f|  =  a*|e f|-b*|d f|+c*|d e|
     |g h i|       |h i|   |g i|   |g h|

*/

float calculate_3x3(float matrix_3x3[][3]){	

	float matrix_2x2[2][2];
	int row=0,column=0;
	float det_3x3=0;
	
	for(int k=0;k<3;k++){
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				if(i!=0 and j!=k){
					//Debug
					//printf("%g ",matrix_3x3[i][j]);	
					matrix_2x2[row][column]=matrix_3x3[i][j];//Create sub 2x2 matrix
					column++;		
				}
			}
			//Debug
			//printf("\n");
		}
		//Calculate determinant
		if(k==0 or k==2){
			det_3x3 = calculate_2x2(matrix_2x2)*matrix_3x3[0][k]+det_3x3;
		}else{
			det_3x3 = -calculate_2x2(matrix_2x2)*matrix_3x3[0][k]+det_3x3;
		}
		row=0;
		column=0;
	}
	//Debug
	//printf("%g \n",det_3x3);
	return det_3x3;
}

//Calculate the determinant of 2x2
/*

|A|=|a b|  =  a*d-c*b
    |c d|

*/

float calculate_2x2(float matrix_2x2[][2]){

	float det_2x2=0;

	det_2x2 = (matrix_2x2[0][0]*matrix_2x2[1][1])-(matrix_2x2[0][1]*matrix_2x2[1][0]);
	
	//Debug
	//printf("%g \n",det_2x2);
	
	return det_2x2;
}

//show final results

float show_results(float det, float matrix_4x4[][4]){

	printf("La matriu introduïda és: \n");
	
	for(int i=0;i<4;i++){
		for(int j=0;j<4;j++){
			printf("%g ",matrix_4x4[i][j]);
		}
		printf("\n");
	}
	printf("El determinant és %g.\n",det);
	
	return 0;
}


int main(){

	float matrix_4x4[4][4];

	scan_store(matrix_4x4);
	
	show_results(calculate_4x4(matrix_4x4),matrix_4x4);

}
