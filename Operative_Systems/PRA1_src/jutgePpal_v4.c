/* -----------------------------------------------------------------------
    PRA1: Cronometratge campionats de natació
    Codi font: jutgePpal_v4.c
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

#include <signal.h>

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
int GenerarCodi(int n);
void EnviarCodis();
void RebreDades();
void sigquit(int sig);
void sigint(int sig);

/*
----------------------------------------------------------------------
--- V A R I A B L E S   G L O B A L S --------------------------------
----------------------------------------------------------------------
*/
char capInfoJutge[MIDA_MAX_CADENA];
char capInfoGuanyador[MIDA_MAX_CADENA];

// Definir structure
typedef struct{

    unsigned char numCarril;
    pid_t pidCrono;
    int temps;
    int codi;

} t_resultat;

// Crear structure dadesResultatCrono
t_resultat dadesResultatCrono[NOMBRE_CARRILS];

// Creem array per les Pipes
int pipeResultats[2];
int pipeCodi[NOMBRE_CARRILS][2];

// Array per la cadena
char cadena[MIDA_MAX_CADENA];

int dades;

/*
----------------------------------------------------------------------
--- P R O G R A M A   P R I N C I P A L ------------------------------
----------------------------------------------------------------------
*/


int main(int argc, char *argv[]){

    unsigned short int distancia;
    unsigned char i;

    char *args[] = {"./cronoCarril_v4", "./cronoCarril_v4", NULL};

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

    // Definir senyals

    if(signal(SIGQUIT,sigquit)==SIG_ERR){
        ImprimirError("Error Senyal");
    };
    if(signal(SIGINT,sigint)==SIG_ERR){
        ImprimirError("Error Senyal");
    };

    // Inicialitzem la pipe resultats
    if(pipe(pipeResultats)==-1){
        // Misatge d'error
        sprintf(cadena, "ERROR creacio pipe Resultats");
        ImprimirError(cadena);
    }

    for(int i = 0; i < NOMBRE_CARRILS; i++){
        // Inicialitzem la pipe codis
        if(pipe(pipeCodi[i])==-1){
            // Misatge d'error
            sprintf(cadena, "ERROR creacio Pipe Codis");
            ImprimirError(cadena);
        }
    }
    
    for (i = 0; i < NOMBRE_CARRILS; i++){
        // Forks
        if((dadesResultatCrono[i].pidCrono = fork()) == -1){ // Error
            sprintf(cadena, "ERROR creacio fill %u.", i + 1);
            ImprimirError(cadena);

        }else if(dadesResultatCrono[i].pidCrono==0){   // Child

            // Tancar escriptura i lectura de les pipes que no necessitem
            for (int j = 0; j < NOMBRE_CARRILS; j++) {
                if (j != i) {
                    close(pipeCodi[j][0]); 
                    close(pipeCodi[j][1]); 
                }
            }

            // Pipes Resultats
                // Tanquem la lectura del fill
                close(pipeResultats[0]);
                // Redireccionem sortida
                dup2(pipeResultats[1],66);
                // Tanquem la escritura del fill duplicada
                close(pipeResultats[1]);

            // Pipes Codis
                //Tanquem la escriptura del fill
                close(pipeCodi[i][1]);
                // Redireccionem sortida
                dup2(pipeCodi[i][0],99);
                // Tanquem lectura duplicada
                close(pipeCodi[i][0]);
                

            // Recubriment
            sprintf(cadena, "%u", i + 1);
            execl(args[0], args[1], argv[1], cadena, NULL);

            // Error si el fill arriba aqui
            sprintf(cadena, "Error execl fill %u.", i + 1);
            ImprimirError(cadena);

        }else{ // Father

            // Informació
            sprintf(cadena, "Activacio crono carril %u (pid: %u)\n", i + 1, dadesResultatCrono[i].pidCrono);
            ImprimirInfoJutge(cadena);
            // Tancar lectura del pipeCodi
            close(pipeCodi[i][0]);
        }
    }
    
    // Tancar escritura del pare
    close(pipeResultats[1]);

    while(2+2!=5){
        sleep(1);
    }
        
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

int GenerarCodi(int i){
    // Regenerar seed
    srand(dadesResultatCrono[i].pidCrono);
    // Generar nombre
    dadesResultatCrono[i].codi = rand() % 10000 + 250000;
    // Retornar codi
    return dadesResultatCrono[i].codi;
}

void EnviarCodis(){
    // Generar i enviar codis
    for(int i = 0; i < NOMBRE_CARRILS; i++){
        // Generem codi únic
        int codi = GenerarCodi(i);
        // Enviem codi per el pipe
        if(write(pipeCodi[i][1],&codi,sizeof(int))<0){
            ImprimirError("ERROR read pipe code");
        };
    }
}

void RebreDades(){

    dades = 1;

    int carrils = 0;

    t_resultat info[2];

    // Definim t_struct pel guanyador
    info[1].codi = 0;
    info[1].numCarril = 0;
    info[1].temps = INT_MAX;
    info[1].pidCrono = INT_MAX;

    // Llegim informació del pipe
    while(read(pipeResultats[0],&info[0],sizeof(t_resultat))>0){
        // Mostrar informació per pantalla
        sprintf(cadena, "Rebudes dades crono carril %u (pid:%u) Temps:%.1f seg. (Codi Resultat: %u)\n",info[0].numCarril, info[0].pidCrono, (double)info[0].temps/10, info[0].codi);
        ImprimirInfoJutge(cadena);

        // Agafar guanyador amb menor temps

        if (info[0].temps < info[1].temps) {
            info[1].pidCrono = info[0].pidCrono;
            info[1].temps = info[0].temps;
            info[1].numCarril = info[0].numCarril;
            info[1].codi = info[0].codi;

        // En cas que hi hagi empat agafarem el que tingui menor pid
        }else if(info[0].temps == info[1].temps){
            if(info[0].pidCrono < info[1].pidCrono){
                info[1].pidCrono = info[0].pidCrono;
                info[1].numCarril = info[0].numCarril;
                info[1].codi = info[0].codi;
            }
        }

        carrils++;

        // Quan hem rebut totes les dades de cada carril sortim del read
        if(carrils >= NOMBRE_CARRILS){
            break;
        }
    }

    // Mostrar Missatge del Guanyador
    sprintf(capInfoGuanyador, "Millor temps: %.1f seg. - Carril %u (pid-%u) (Codi Resultat: %u)\n", (double)info[1].temps/10, info[1].numCarril, info[1].pidCrono, info[1].codi);

    ImprimirInfoJutge("* * * * * * * * * *  F I   S E R I E  * * * * * * * * * *\n\n");
    ImprimirInfoJutge(capInfoGuanyador);

    dades = 0;

}

void finalitzarCampionat(){

    // Realitzar waits
    for (int i = 0; i < NOMBRE_CARRILS; i++) {

        pid_t child_pid;
        int estatFinal;

        // Use waitpid to wait for the child process to terminate
        close(pipeCodi[i][1]);
        child_pid = wait(&estatFinal);

        if (child_pid == -1) {
            ImprimirError("Error en CHILD PID");
        } else {
            sprintf(cadena, "Rebuda finalitzacio crono carril %d (pid-%u) \n", WEXITSTATUS(estatFinal), child_pid);
            ImprimirInfoJutge(cadena);
        }
    }
    // Tanquem pipe Codi
    for(int i = 0; i < NOMBRE_CARRILS; i++){
        close(pipeCodi[i][1]);
    }

    // Tanquem lector del pare
    close(pipeResultats[0]);

    ImprimirInfoJutge("* * * * * * * * * *  F I   C A M P I O N A T  * * * * * * * * * *\n");
    exit(EXIT_SUCCESS);
}

// Signal per SIGQUIT
void sigquit(int sig) {
    sprintf(cadena, "S'ha rebut SIGQUIT (S'ha premut Ctrl+\\ al teclat)\n\n");
    ImprimirInfoJutge(cadena);

    ImprimirInfoJutge("* * * * * * * * * *   I N I C I  N O V A  S E R I E  * * * * * * * * * *\n\n");

    // Enviem codis
    EnviarCodis();

    // Rebem dades i mostrem resultat del guanyador
    RebreDades();
}

// Signal per SIGINT
void sigint(int sig) {
    
    sprintf(cadena, "S'ha rebut SIGINT (S'ha premut Ctrl+C al teclat)\n\n");
    ImprimirInfoJutge(cadena);

    // Enviem SIGTERM a tots els fills
    for(int i = 0; i < NOMBRE_CARRILS; i++){
        kill(dadesResultatCrono[i].pidCrono,SIGTERM);
    }
    if(dades == 1){
        // Rebre dades en cas que encara s'estigui executant el campionat
        RebreDades();
    }

    // FInalitzar campionat
    finalitzarCampionat();
}
