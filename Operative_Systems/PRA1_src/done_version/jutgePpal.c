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

/*
----------------------------------------------------------------------
--- P R O G R A M A   P R I N C I P A L ------------------------------
----------------------------------------------------------------------
*/
int main(int argc, char *argv[])
{
    unsigned short int distancia;
    unsigned char i;
    pid_t pid;
    int estatWait;
    char cadena[MIDA_MAX_CADENA];

    char *args[] = {"./cronoCarril", "./cronoCarril", NULL};

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

    for (i = 0; i < NOMBRE_CARRILS; i++)
    {
        switch (pid = fork())
        {
        case -1:
            sprintf(cadena, "ERROR creacio fill %u.", i + 1);
            ImprimirError(cadena);

        case 0: /* Fill */
            sprintf(cadena, "%u", i + 1);
            execl(args[0], args[1], argv[1], cadena, NULL);

            sprintf(cadena, "Error execl fill %u.", i + 1);
            ImprimirError(cadena);

        default:
            sprintf(cadena, "Activacio crono carril %u (pid: %u)\n", i + 1, pid);
            ImprimirInfoJutge(cadena);

            pid = wait(&estatWait);

            sprintf(cadena, "Rebuda finalitzacio crono carril (pid-%u) codi:%d \n", pid, WEXITSTATUS(estatWait));
            ImprimirInfoJutge(cadena);
        }
    }

    ImprimirInfoJutge("* * * * * * * * * *  F I   C U R S A  * * * * * * * * * *\n");

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