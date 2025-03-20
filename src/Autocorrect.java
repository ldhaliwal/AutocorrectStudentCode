import com.google.common.collect.Multiset;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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
    public static int threshold;
    public static ArrayList<KeyVal> pairs;
    public static HashMap<String, Integer> matchedWords;

    public static String firstDoc;
    public static String secondDoc;

    /**
     * Constucts an instance of the Autocorrect class.
     * @param words The dictionary of acceptable words.
     * @param threshold The maximum number of edits a suggestion can have.
     */
    public Autocorrect(String[] words, int threshold) {

        dict = words;
        Autocorrect.threshold = threshold;
        matchedWords = new HashMap<>();
        pairs = new ArrayList<>();


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

        for(int i = 1; i < (word1.length()+1); i++){
            for(int j = 1; j < (word2.length()+1); j++){
                if(word1.charAt(i-1) == word2.charAt(j-1)){
                    distances[i][j] = distances[i-1][j-1];
                }
                else{
                    distances[i][j] = 1 + Math.min(Math.min(distances[i][j-1], distances[i-1][j]), distances[i-1][j-1]);
                }
            }
        }

        return distances[word1.length()][word2.length()];
    }


    /**
     * Runs a test from the tester file, AutocorrectTester.
     * @param typed The (potentially) misspelled word, provided by the user.
     * @return An array of all dictionary words with an edit distance less than or equal
     * to threshold, sorted by edit distance, then sorted alphabetically.
     */
    public String[] runTest(String typed) {

        ArrayList<String> dictionary = new ArrayList<>();

//        for(String word : dict){
//            int LCS = longestSharedSubstring(word, typed);
//
//            if(LCS >= 2){
//                dictionary.add(word);
//            }
//        }


        for(String word : dict){
            int distance = editDistance(typed, word);

            if (distance <= threshold){
                KeyVal pair = new KeyVal(word, distance);
                pairs.add(pair);
            }
        }

        String[] words = new String[pairs.size()];

        pairs.sort(Comparator.comparing(KeyVal::getDistance).thenComparing(KeyVal::getWord));

        for(int i = 0; i < pairs.size(); i++){
            words[i] = (pairs.get(i)).getWord();
        }

        System.out.println(Arrays.toString(words));
        return words;
    }


    public static ArrayList<String> nGram(String word){
        ArrayList<String> nGrams = new ArrayList<>();

        for(int i = 0; i < word.length()-1; i++){
            String pair = ""+word.charAt(i) + word.charAt(i + 1);
            nGrams.add(pair);
        }
        return nGrams;
    }


    // LCS stuff

    public static int longestSharedSubstring(String doc1, String doc2) {
        int[][] longest = new int[doc1.length() + 1][doc2.length() + 1];

        firstDoc = doc1;
        secondDoc = doc2;

        int left;
        int up;

        for(int i = 1; i <= doc1.length(); i++){
            for(int j = 1; j <= doc2.length(); j++){
                // Checks if the current characters are equal
                if(doc1.charAt(i - 1) == doc2.charAt(j - 1)){
                    longest[i][j] = longest[i - 1][j - 1] + 1;
                }
                else{
                    up = longest[i - 1][j];
                    left = longest[i][j - 1];

                    longest[i][j] = Math.max(up, left);
                }
            }
        }

        String substring = String.valueOf(findSubstring(longest, doc1.length(), doc2.length(), longest[doc1.length()][doc2.length()], new StringBuilder()));

        System.out.println(substring);

        // Returns the longest count
        return longest[doc1.length()][doc2.length()];
    }

    public static StringBuilder findSubstring(int[][] longest, int i, int j, int current, StringBuilder substring){
        if(longest[i][j] == 0){
            return substring;
        }

        if(firstDoc.charAt(i - 1) == secondDoc.charAt(j - 1)){
            substring.insert(0, secondDoc.charAt(j - 1));
            return findSubstring(longest, i - 1, j - 1, longest[i - 1][j - 1], substring);
        }

        int up = longest[i - 1][j];
        int left = longest[i][j - 1];

        if(up == current){
            return findSubstring(longest, i - 1, j, up, substring);
        }
        if(left == current){
            return findSubstring(longest, i, j - 1, left, substring);
        }

        return substring;
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