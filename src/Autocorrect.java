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
        for(String word : dict){
            if(worthRunning(word, typed)){
                int distance = editDistance(typed, word);

                if (distance <= threshold){
                    KeyVal pair = new KeyVal(word, distance);
                    pairs.add(pair);
                }
            }
        }

        String[] words = new String[pairs.size()];

        pairs.sort(Comparator.comparing(KeyVal::getDistance).thenComparing(KeyVal::getWord));

        for(int i = 0; i < pairs.size(); i++){
            words[i] = (pairs.get(i)).getWord();
        }

        return words;
    }

    public static boolean worthRunning(String word, String typed){
        ArrayList<String> wordNGram = nGram(word);
        ArrayList<String> typedNGram = nGram(typed);

        int overlap = 0;

        for(String nGram : wordNGram){
            if(typedNGram.contains(nGram)){
                overlap++;
            }
        }

        int similarityScore = (20 * (overlap+1))/(wordNGram.size() + typedNGram.size());

        int nGramThreshold = (Math.min(wordNGram.size(), typedNGram.size()) / 2);

        return similarityScore >= nGramThreshold;
    }


    public static ArrayList<String> nGram(String word){
        ArrayList<String> nGrams = new ArrayList<>();

        for(int i = 0; i < word.length()-1; i++){
            String pair = ""+word.charAt(i) + word.charAt(i + 1);
            nGrams.add(pair);
        }
        return nGrams;
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

    public static void main(String[] args){
        String[] dict = loadDictionary("large");
        ArrayList<String> fastDict = new ArrayList<>(Arrays.asList(dict));

        Autocorrect program = new Autocorrect(dict, 2);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Type a word or type 'exit' to quit:");

        ArrayList<String> suggestions = new ArrayList<>();

        while (true) {
            suggestions.clear();

            System.out.println("---------------------------------------");
            System.out.print("Enter a word: ");
            String userWord = scanner.nextLine().trim().toLowerCase();

            if (userWord.equals("exit")) {
                break;
            }

            if (fastDict.contains(userWord)) {
                System.out.println("Correct spelling.");
            }
            else {
                suggestions = new ArrayList<>(Arrays.asList(program.runTest(userWord)));
                if (suggestions.isEmpty()) {
                    System.out.println("No matches found.");
                }
                else {
                    System.out.println("Did you mean: " + String.join(", ", suggestions) + "?");
                }
            }
        }
        scanner.close();
    }
}