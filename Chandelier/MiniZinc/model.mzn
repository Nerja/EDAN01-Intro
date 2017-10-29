include "globals.mzn";

array [1..9] of var 1..9: weights;
array [1..9] of int: balance_coeffs = [8, 5, 4, -2, -3, -5, -4, -5, -9];

constraint
    all_different(weights)
    /\
    sum([weights[i]*balance_coeffs[i] | i in 1..9]) = 0;

solve satisfy;
output [show(weights)]
