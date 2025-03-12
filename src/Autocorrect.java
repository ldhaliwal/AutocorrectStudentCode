import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Autocorrect
 * <p>
 * A command-line tool to suggest similar words when given one not in the dictionary.
 * </p>
 * @author Zach Blick
 * @author Liliana Dhaliwal
 */
public class Autocorrect {

    public static String[] dict;

    /**
     * Constucts an instance of the Autocorrect class.
     * @param words The dictionary of acceptable words.
     * @param threshold The maximum number of edits a suggestion can have.
     */
    public Autocorrect(String[] words, int threshold) {

        dict = words;

        // dictionary words
            // LCS to narrow down
        // candidates
            // edit distance
            // +1 for add, subtract or swap
        // ranked suggestions
            // sort by lowest edit distance



    }

    public int editDistance(String word1, String word2){
        int[][] distances = new int[word1.length() + 1][word2.length() + 1];

        for(int i = 0; i < (word1.length() + 1); i++){
            distances[i][0] = i;
        }

        for(int i = 0; i < (word2.length() + 1); i++){
            distances[0][i] = i;
        }

        for(int i = 1; i < (word1.length() + 1); i++){
            for(int j = 1; j < (word2.length() + 1); j++){
                if(word1.charAt(i) == word2.charAt(j)){
                    distances[i][j] = Math.min(Math.min(distances[i][j-1], distances[i-1][j]), distances[i-1][j-1]);
                }
                else{
                    distances[i][j] = 1 + Math.min(Math.min(distances[i][j-1], distances[i-1][j]), distances[i-1][j-1]);
                }
            }
        }

        return distances[word1.length() + 1][word2.length() + 1];
    }



    /**
     * Runs a test from the tester file, AutocorrectTester.
     * @param typed The (potentially) misspelled word, provided by the user.
     * @return An array of all dictionary words with an edit distance less than or equal
     * to threshold, sorted by edit distance, then sorted alphabetically.
     */
    public String[] runTest(String typed) {
        for(String word : dict){

        }

        return new String[0];
    }


    /**
     * Loads a dictionary of words from the provided textfiles in the dictionaries directory.
     * @param dictionary The name of the textfile, [dictionary].txt, in the dictionaries directory.
     * @return An array of Strings containing all words in alphabetical order.
     */
    private static String[] loadDictionary(String dictionary)  {
        try {
            String line;
            BufferedReader dictReader = new BufferedReader(new FileReader("dictionaries/" + dictionary + ".txt"));
            line = dictReader.readLine();

            // Update instance variables with test data
            int n = Integer.parseInt(line);
            String[] words = new String[n];

            for (int i = 0; i < n; i++) {
                line = dictReader.readLine();
                words[i] = line;
            }
            return words;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}