/* -----------------------------------------------------------------------
    PRA1: Cronometratge campionats de natació
    Codi font: cronoCarril_v1.c
    Èric Bitriá Ribes
    Nom complet autor2.
---------------------------------------------------------------------- */
/*
/*
----------------------------------------------------------------------
--- L L I B R E R I E S ------------------------------
----------------------------------------------------------------------
*/
#include <stdio.h> /* sprintf*/

// pid_t
#include <sys/types.h>
#include <unistd.h>

#include <stdlib.h> /* exit, EXIT_SUCCESS, ...*/
#include <string.h> /* strlen */
#include <unistd.h> /* STDOUT_FILENO */
#include <errno.h>  /* errno */

/*
----------------------------------------------------------------------
--- C O N S T A N T S------ ------------------------------------------
----------------------------------------------------------------------
*/
#define MIDA_MAX_CADENA 1024

#define FI_COLOR "\e[0m"
#define MIDA_MAX_CADENA_COLORS 1024
#define FORMAT_TEXT_ERROR "\e[1;48;5;1;38;5;255m"

/*
----------------------------------------------------------------------
--- C A P Ç A L E R E S   D E   F U N C T I O N S --------------------
----------------------------------------------------------------------
*/
void ImprimirInfoCarril(char *text);
void ImprimirError(char *text);

/*
----------------------------------------------------------------------
--- V A R I A B L E S   G L O B A L S --------------------------------
----------------------------------------------------------------------
*/
unsigned char numCarril;
pid_t pidPropi;
char dadesCarril[MIDA_MAX_CADENA];

char taulaColors[8][MIDA_MAX_CADENA_COLORS] = {
    "\e[01;31m", // Vermell
    "\e[01;32m", // Verd
    "\e[01;33m", // Groc
    "\e[01;34m", // Blau
    "\e[01;35m", // Magenta
    "\e[01;36m", // Cian
    "\e[00;33m", // Taronja
    "\e[1;90m"   // Gris fosc
};

/*
----------------------------------------------------------------------
--- P R O G R A M A   P R I N C I P A L ------------------------------
----------------------------------------------------------------------
*/
int main(int argc, char *argv[])
{
    unsigned short int distancia, voltes, sleepRestant;
    unsigned char i;
    char cadena[MIDA_MAX_CADENA];
    // unsigned int codiResultat, sleepRestant;

    int tempsVolta, tempsTotal = 0;

    if (argc != 3 || atoi(argv[1]) < 50 || atoi(argv[1]) % 50 != 0)
    {
        sprintf(cadena, "Us: %s <distancia (metres múltiples de 50)> <número carril>\n\nPer exemple: %s 100 1\n\n", argv[0], argv[0]);
        write(STDOUT_FILENO, cadena, strlen(cadena));
        exit(1);
    }

    pidPropi = getpid();
    distancia = atoi(argv[1]);
    voltes = distancia / 50;

    numCarril = atoi(argv[2]);

    sprintf(dadesCarril, "[Crono carril %u-pid:%u]> ", numCarril, pidPropi);

    srand(getpid());

    //    sprintf(cadena, "[crono %u-pid:%u]> Activat! Distancia: %u\n", numCarril, pidPropi, distancia);
    sprintf(cadena, "Activat! Distancia: %u\n", distancia);
    ImprimirInfoCarril(cadena);

    for (i = 0; i < voltes; i++)
    {
        tempsVolta = rand() % 56 + 200;
        tempsTotal = tempsTotal + tempsVolta;

        sleepRestant = tempsVolta / 10;
        // Bucle per evitar que l'sleep acabi abans de temps quan es rep un senyal.
        do
        {
            sleepRestant = sleep(sleepRestant);
        } while (sleepRestant != 0);

        sprintf(cadena, "Volta %u: %.1f seg.\n", i + 1, (double)tempsVolta / 10);
        ImprimirInfoCarril(cadena);
    }

    sprintf(cadena, "TEMPS TOTAL: %.1f seg.\n", (double)tempsTotal / 10);
    ImprimirInfoCarril(cadena);

    exit(tempsTotal);
}

void ImprimirInfoCarril(char *text)
{
    unsigned char i;
    char info[numCarril * 3 + strlen(dadesCarril) + strlen(text) + 1];
    char infoColor[numCarril * 3 + strlen(dadesCarril) + strlen(text) + 1 + MIDA_MAX_CADENA_COLORS * 2];

    for (i = 0; i < numCarril * 3; i++)
        info[i] = ' ';

    for (i = 0; i < strlen(dadesCarril); i++)
        info[i + numCarril * 3] = dadesCarril[i];

    for (i = 0; i < strlen(text); i++)
        info[i + numCarril * 3 + strlen(dadesCarril)] = text[i];

    info[numCarril * 3 + strlen(dadesCarril) + strlen(text)] = '\0';

    sprintf(infoColor, "%s%s%s", taulaColors[(numCarril - 1) % 8], info, FI_COLOR);

    if (write(STDOUT_FILENO, infoColor, strlen(infoColor)) == -1)
        ImprimirError("ERROR write ImprimirInfoCarril");
}

void ImprimirError(char *text)
{
    unsigned char i;
    char info[numCarril * 3 + strlen(dadesCarril) + strlen(text) + 1];
    char infoColorError[MIDA_MAX_CADENA];

    for (i = 0; i < numCarril * 3; i++)
        info[i] = ' ';

    for (i = 0; i < strlen(dadesCarril); i++)
        info[i + numCarril * 3] = dadesCarril[i];

    for (i = 0; i < strlen(text); i++)
        info[i + numCarril * 3 + strlen(dadesCarril)] = text[i];

    info[numCarril * 3 + strlen(dadesCarril) + strlen(text)] = '\0';

    sprintf(infoColorError, "%s%s: %s%s\n", FORMAT_TEXT_ERROR, info, strerror(errno), FI_COLOR);
    write(STDERR_FILENO, "\n", 1);
    write(STDERR_FILENO, infoColorError, strlen(infoColorError));
    write(STDERR_FILENO, "\n", 1);

    exit(2);
}