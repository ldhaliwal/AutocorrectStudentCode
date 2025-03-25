**Overview**

This program continuously runs, taking in the user input of a word and running an autocorrect process if the word is not 
found in the given dictionary. This process starts by narrowing down our comparative dictionary by looking at the n-grams, 
of both the typed word and each word in the dictionary it is being compared to. If the two words fail to have a minimum number
of n-grams in common, the program will ignore that word in the dictionary and not carry out any further checks of similarity. 
If the two words do have enough in common, the program then calculates the edit distance between the two words. If that value
passes through another threshold, the word is deemed an acceptable match. All those matches are then sorted by edit distance 
and alphabetized before being returned back to the user. 

**Time/Space Complexity:**

Initializing Data Structures in the Constructor: time O(1), space O(1). 

Loading the Dictionary: time O(n), space O(n). n = the size of the dictionary
* Filling the array is proportional to the number of words
* Same with space

Finding the n-Grams: time O(m), space O(m). m = the length of the word
* Loops through each character in word m
* Stores m - 1 grams

worthRunning Method: time O(m * p), space O(m + p). m = word1 length, p = word2 length
* Loop with O(m) cals .contains max of O(p) times, so O(m * p)
* Stores O(m) grams and O(p) grams, so O(m + p)

editDistance Method: time O(m * p), space O(m * p). m = word1 length, p = word2 length
* Loops through both strings, so O(m * p)
* Stores in 2D array of (m + 1)*(p + 1), so O(m * p)

runTest Method: time O(n) [then calls other methods], space O(w). n = dictionary size, w = number of plausible words (worst case w = n).
* Loops through all the words in the dictionary, so O(n)
* Pairs stores all the possible words, so O(w)

Overall: time O(n * m * p + n), space 0(n + m * p)