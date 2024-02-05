#!/bin/bash

if [ "$#" -ne 1 ];
    then
    echo "ús: ./prac2_3.sh <nivell_arrancada>"
    exit 1
fi

nivell_arrancada="$1"
directori="/etc/rc${nivell_arrancada}.d"

if [ ! -d "$directori" ];
    then
    echo "El nivell d'arrancada $nivell_arrancada no existeix o no té el directori a /etc/rc${nivell_arrancada}.d"
    exit 1

fi

num_script_inici=0
num_script_aturada=0

for i in "$directori"/*;
    do
    arxiu=$(basename "$i")
	
   if [[ "$arxiu" == S* ]];
       then
       num_script_inici=$((num_script_inici + 1))
   fi
   
   if [[ "$arxiu" == K* ]];
       then
       num_script_aturada=$((num_script_aturada + 1))
   fi

done

echo "El nivell $nivell_arrancada té $num_script_inici serveis per iniciar i $num_script_aturada per aturar"

exit 0

