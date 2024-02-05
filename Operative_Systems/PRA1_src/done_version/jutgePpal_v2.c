/* -----------------------------------------------------------------------
    PRA1: Cronometratge campionats de natació
    Codi font: jutgePpal_v2.c
    Èric Bitriá Ribes
    Nom complet autor2.
---------------------------------------------------------------------- */
/*

----------------------------------------------------------------------
--- L L I B R E R I E S ------------------------------
----------------------------------------------------------------------
*/
#include <stdio.h> /* sprintf*/

// fork, pid_t, wait, ..
#include <sys/types.h>
#include <unistd.h>

#include <stdlib.h> /* exit, EXIT_SUCCESS, ...*/
#include <string.h> /* strlen */

#include <sys/wait.h> /* wait */
#include <errno.h>    /* errno */

/*
----------------------------------------------------------------------
--- C O N S T A N T S------ ------------------------------------------
----------------------------------------------------------------------
*/

#define MIDA_MAX_CADENA 1024
#define NOMBRE_CARRILS 8

#define INVERTIR_COLOR "\e[7m"
#define FI_COLOR "\e[0m"
#define MIDA_MAX_CADENA_COLORS 1024
#define FORMAT_TEXT_ERROR "\e[1;48;5;1;38;5;255m"
#define INT_MAX 2147483647

/*
----------------------------------------------------------------------
--- C A P Ç A L E R E S   D E   F U N C T I O N S --------------------
----------------------------------------------------------------------
*/
void ImprimirInfoJutge(char *text);
void ImprimirError(char *text);

/*
----------------------------------------------------------------------
--- V A R I A B L E S   G L O B A L S --------------------------------
----------------------------------------------------------------------
*/
char capInfoJutge[MIDA_MAX_CADENA];
char capInfoGuanyador[MIDA_MAX_CADENA];

/*
----------------------------------------------------------------------
--- P R O G R A M A   P R I N C I P A L ------------------------------
----------------------------------------------------------------------
*/


int main(int argc, char *argv[])
{
    unsigned short int distancia;
    unsigned char i;
    char cadena[MIDA_MAX_CADENA];

    char *args[] = {"./cronoCarril_v2", "./cronoCarril_v2", NULL};

    if (argc != 2 || atoi(argv[1]) < 50 || atoi(argv[1]) % 50 != 0)
    {
        sprintf(cadena, "Us: %s <distancia (metres múltiples de 50)>\n\nPer exemple: %s 100\n\n", argv[0], argv[0]);

        write(1, cadena, strlen(cadena));
        exit(2);
    }

    sprintf(capInfoJutge, "[%s-pid:%u]> ", argv[0], getpid());

    distancia = atoi(argv[1]);
    ImprimirInfoJutge("* * * * * * * * * *  I N I C I   C U R S A  * * * * * * * * * *\n");

    sprintf(cadena, "Distancia: %u m.     Nombre carrils: %u                         \n\n", distancia, NOMBRE_CARRILS);
    ImprimirInfoJutge(cadena);

    // Definir structure

    typedef struct{
        unsigned char numCarril;
        pid_t pidCrono;
        int temps;
    } t_resultat;

    // Crear structure dadesResultatCrono
    t_resultat dadesResultatCrono;

    // Creem array per la pipe
    int pipeResultats[2];

    // Inicialitzem la pipe
    if(pipe(pipeResultats)==-1){
        // Misatge d'error
        sprintf(cadena, "ERROR creacio pipe");
        ImprimirError(cadena);
    }

    for (i = 0; i < NOMBRE_CARRILS; i++){
        if((dadesResultatCrono.pidCrono = fork()) == -1){ // Error
            sprintf(cadena, "ERROR creacio fill %u.", i + 1);
            ImprimirError(cadena);

        }else if(dadesResultatCrono.pidCrono==0){   // Child
            // Tanquem la lectura del fill
            close(pipeResultats[0]);

            // Redireccionem sortida
            dup2(pipeResultats[1],66);
            
            // Tanquem la escritura del fill duplicada
            close(pipeResultats[1]);

            // Recubriment
            sprintf(cadena, "%u", i + 1);
            execl(args[0], args[1], argv[1], cadena, NULL);

            // Error si el fill arriba aqui
            sprintf(cadena, "Error execl fill %u.", i + 1);
            ImprimirError(cadena);

        }else{ // Father
            sprintf(cadena, "Activacio crono carril %u (pid: %u)\n", i + 1, dadesResultatCrono.pidCrono);
            ImprimirInfoJutge(cadena);
        }
    }

    // Tancar escritura una vegada tots els fills l'han heredat
    close(pipeResultats[1]);

    // Variables per guardar el guanyador
    int menorTemps = INT_MAX;
    int guanyadorPID = INT_MAX;
    int guanyadorCarril = 0;

    // Llegim informació del pipe
    while(read(pipeResultats[0],&dadesResultatCrono,sizeof(t_resultat))>0){
        // Mostrar informació per pantalla
        sprintf(cadena, "Rebudes dades crono carril %u (pid:%u) Temps:%.1f seg.\n",dadesResultatCrono.pidCrono, dadesResultatCrono.numCarril, (double)dadesResultatCrono.temps/10);
        ImprimirInfoJutge(cadena);

        // Agafar guanyador amb menor temps
        if (dadesResultatCrono.temps < menorTemps) {
            guanyadorPID = dadesResultatCrono.pidCrono;
            menorTemps = dadesResultatCrono.temps;
            guanyadorCarril = dadesResultatCrono.numCarril;
        // En cas que hi hagi empat agafarem el que tingui menor pid
        }else if(dadesResultatCrono.temps == menorTemps){
            if(dadesResultatCrono.pidCrono < guanyadorPID){
                guanyadorPID = dadesResultatCrono.pidCrono;
                guanyadorCarril = dadesResultatCrono.numCarril;
            }
        }
    }
    
    // Tanquem lector del pare
    close(pipeResultats[0]);
    
    for (i = 0; i < NOMBRE_CARRILS; i++) {
    
        pid_t child_pid;
        int estatFinal;

        child_pid = wait(&estatFinal);
    
        sprintf(cadena, "Rebuda finalitzacio crono carril (pid-%u) codi finalització: %d\n", child_pid, WEXITSTATUS(estatFinal));
        ImprimirInfoJutge(cadena);
    }
    // Crear cadena
    sprintf(capInfoGuanyador, "Millor temps: %.1f seg. - Carril %u (pid-%u)\n", (double)menorTemps/10, guanyadorCarril, guanyadorPID);
    
    ImprimirInfoJutge("* * * * * * * * * *  F I   C U R S A  * * * * * * * * * *\n");
    ImprimirInfoJutge(capInfoGuanyador);
    exit(EXIT_SUCCESS);
}

void ImprimirInfoJutge(char *text)
{
    char infoColor[strlen(capInfoJutge) + strlen(text) + MIDA_MAX_CADENA_COLORS * 2];

    sprintf(infoColor, "%s%s%s%s", INVERTIR_COLOR, capInfoJutge, text, FI_COLOR);

    if (write(1, infoColor, strlen(infoColor)) == -1)
        ImprimirError("ERROR write ImprimirInfoJutge");
}

void ImprimirError(char *text)
{

    char infoColorError[strlen(capInfoJutge) + strlen(text) + MIDA_MAX_CADENA_COLORS * 2];

    sprintf(infoColorError, "%s%s%s: %s%s\n", FORMAT_TEXT_ERROR, capInfoJutge, text, strerror(errno), FI_COLOR);
    write(2, "\n", 1);
    write(2, infoColorError, strlen(infoColorError));
    write(2, "\n", 1);

    exit(EXIT_FAILURE);
}
