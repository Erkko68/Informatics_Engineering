#!/bin/bash
if [ $# -ne 2 ] 
    then
    echo "$0 suma els dos nombres passats com a parametres"
    echo "Ús: $0 <nombre1> <nombre2>"
    exit 1
fi
echo "$1 + $2 = `expr $1 + $2`"

# Comanda LET

let "a=$1" "b=$2"
let c=a+b
echo "$1 + $2 ="  $c

# Expansió

a=$1 b=$2
echo "$1 + $2 = $((a+b))"