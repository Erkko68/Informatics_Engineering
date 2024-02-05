//Create a program that calculates the area and perimeter of diferent forms.


#include <stdio.h>
#include <math.h> //needed to call the functions square root and the constant PI
int main(){

//show on screen the functionallity of the program and the options the user has.

	printf("Area and perimeter calculator:\nPlease select wich figure you want to calculate:\n");
	printf("-  Square: 1\n-  Rectangle: 2\n-  Triangle: 3\n-  Circle: 4\n");
	
	int opt; //variable options
	int height,width; //variable for the values of the forms
	double area,perim; //outputs variables
	
	printf(">");
	scanf("%i",&opt);
	
	if(opt==1){ 	//square
	
		printf("Insert the lenght of a side of the square: ");
		scanf("%i",&height);
		
		area = height*height;
		perim = height*4;
		
		printf("The area of the square is: %gu²\n",area);
		printf("The perimeter of the square is: %gu\n",perim);
	
	}else if(opt==2){	//rectangle
	
		printf("Insert the height of the rectangle: ");
		scanf("%i",&height);
		printf("Insert the width of the rectangle: ");
		scanf("%i",&width);
		
		area = height*width;
		perim = height*2+width*2;
		
		printf("The area of the rectangle is: %gu²\n",area);
		printf("The perimeter of the rectangle is: %gu\n",perim);
		
		
	}else if(opt==3){	//triangle
	
	
	int tri_type;
	
		printf("Select the type of triangle: 1) Equilateral, 2) Issoceles, 3) Escalene: ");
		scanf("%i",&tri_type);
		
		if(tri_type==1){
		
			printf("Insert the lenght of the side: ");
			scanf("%i",&height);
			
			area = (sqrt(3)*(height*height))/4;
			perim = height*3;
			
			printf("The area of the triangle is: %gu²\n",area);
			printf("The perimeter of the triangle is: %gu\n",perim);
		
		}else if(tri_type==2){
		
			printf("Insert the lenght of the base ");
			scanf("%i",&width);
			printf("Insert the lenght of the side ");
			scanf("%i",&height);
			
			area = 1/2*width*sqrt(height*height-(width*width)/4);
			perim = height*2+width;
			
			printf("The area of the triangle is: %gu²\n",area);
			printf("The perimeter of the triangle is: %gu\n",perim);
		
		
		}else if(tri_type==3){
		
		float side_a,side_b,side_c; //define the variables for each side of the rectangle
		
			printf("Insert the lenght of the first side: ");
			scanf("%f",&side_a);
			printf("Insert the lenght of the second side: ");
			scanf("%f",&side_b);
			printf("Insert the lenght of the third side: ");
			scanf("%f",&side_c);
			
			area = ((sqrt(side_a+side_b+side_c))*(sqrt((side_c+side_b-side_a)*(side_a-side_b+side_c)))*(sqrt(side_a+side_b-side_c)))/4;
			
			perim = side_a+side_b+side_c;
			
			printf("The area of the triangle is: %gu²\n",area);
			printf("The perimeter of the triangle is: %gu\n",perim);
		
		}else {
		
		printf("Invalid value\n");
			
		}
		
	}else if(opt==4){	//circle
	
		printf("Insert the radius of the circle: ");
		scanf("%i",&height);
				
		area = M_PI*height*height;
		perim = height*2*M_PI;
		
		printf("The area of the circle is: %g u²\n",area);
		printf("The perimeter of the circle is: %gu\n",perim);
	
	}else{
	
		printf("Invalid input\n");
	
	}

}
