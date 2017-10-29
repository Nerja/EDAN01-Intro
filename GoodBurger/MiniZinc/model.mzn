% Declaring inputs
array[1..8] of int: sodium_levels;
array[1..8] of int: fat_levels;
array[1..8] of int: calories;
array[1..8] of int: costs;

% FDV:s
array [1..8] of var 1..5: item_counts;
var 0..10000: cost;

constraint
    item_counts[6] = item_counts[7] % servings of lettuce == servings of ketchup
    /\
    item_counts[5] = item_counts[8] % servings of pickles == servings of tomatoes
    /\
    sum([item_counts[i] * sodium_levels[i] | i in 1..8]) < 3000 % sodium req
    /\
    sum([item_counts[i] * fat_levels[i] | i in 1..8]) < 150 % fat req
    /\
    sum([item_counts[i] * calories[i] | i in 1..8]) < 3000 % calories req
    /\
    sum([item_counts[i] * costs[i] | i in 1..8]) = cost;

solve maximize cost;
output [show(cost)] ++ [show(item_counts)]
