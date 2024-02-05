#!/bin/bash

# Define binari function
binari(){
    num=$1      # Save decimal number in variable "num"
    binary=0    # Define final result binary number
    exp=1       # Define number to represent exponent in the binary calculation

    # Execute while the "num" is greather than 0
    while [ $num -gt 0 ]; do
        remainder=$((num % 2))                  # Use modulus 2 to get reminder as binary
        binary=$((binary + remainder * exp))    # Multiply by the exponent to situate the reminder
        exp=$((exp * 10))                       # Increase power of 10
        num=$((num / 2))                        # Divide num by 2
    done
    
    echo "$1(d = $binary(b"    # Print final result
}

# Check user Input (one positive integer)
if [ $# -ne 1 -o $1 -lt 0 ]; then
    echo "Ús: $0 <Nombre no negatiu>"
    exit 1
fi

# Obtain first range check
range=0
read range
echo "Valor final? $range"

# Execute loop until the number provided is greather than the first parameter
until [ $range -gt $1 ]; do
    echo "ERROR: valor final ha de ser > $1"
    read range
done

conv=$1             # Define number to store each step of the range

while [ $conv -le $range ]; do
    binari $conv        # Binari function call
    conv=$((conv+1))    # INcrease range
done

exit 0