package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {

    private Set<String> words;
    private Set<Character> guesses;

    @Override
    public void startGame(File dictionary, int wordLength) {

        try {
            Scanner scan = new Scanner(dictionary);

            while(scan.hasNextLine()) {

                String line = scan.nextLine();

                if (line.length() == wordLength) {
                    words.add(line);
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + dictionary.getName());
        }
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {

        if (guesses.contains(guess)) {
            throw new GuessAlreadyMadeException();
        }

        guesses.add(guess);

        Map<String, Set<String>> familyMap = new HashMap<>();

        for(String word : words) {
            StringBuilder familyKey = new StringBuilder();

            for(char c : word.toCharArray()) {

                if (c == guess) {

                    familyKey.append(c);
                } else {

                    familyKey.append("-");
                }
            }

            String key = familyKey.toString();

            if(!familyMap.containsKey(key)) {

                Set<String> set = new TreeSet<>();
                familyMap.put(key, set);
            }

            familyMap.get(key).add(word);
        }

        int maxLength = 0;
        Set<String> maxSet = null;
        String maxKey = "";
        for(String key : familyMap.keySet()) {
            Set<String> set = familyMap.get(key);

            if (set.size() > maxLength) {
                maxLength = set.size();
                maxSet = set;
                maxKey = key;
            } else if (set.size() == maxLength) {

                int keyCount = 0;
                int maxKeyCount = 0;

                for(int i = 0; i < key.length(); i++) {

                    keyCount += key.charAt(i) == guess ? 1 : 0;
                    maxKeyCount += maxKey.charAt(i) == guess ? 1 : 0;
                }

                if(keyCount > maxKeyCount) {

                    maxSet = set;
                    maxKey = key;
                } else if (keyCount == maxKeyCount) {

                    for(int i = key.length() - 1; i >= 0; i--) {

                        if(!(key.charAt(i) == maxKey.charAt(i))) {

                            maxSet = key.charAt(i) == guess ? set : maxSet;
                            break;
                        }
                    }
                }
            }
        }

        words = maxSet;

        return words;
    }
}
