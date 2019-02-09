package hangman;

import java.io.File;
import java.util.*;

public class HangMain {

    public static void main(String[] args) {

        if (args.length != 3) {
            System.out.println("Usage: java [your main class name] dictionary wordLength guesses");
            System.exit(1);
        }

        File dictionary = new File(args[0]);

        int wordLength = 0;
        int numGuesses = 0;

        try {
            wordLength = Integer.parseInt(args[1]);

            if (wordLength < 2) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.out.println("Word length must be a number greater than 1");
            System.exit(1);
        }

        try {
            numGuesses = Integer.parseInt(args[2]);

            if (numGuesses < 1) {

                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.out.println("Guesses must be a number greater than 0");
            System.exit(1);
        }

        List<String> guesses = new ArrayList<>();

        IEvilHangmanGame game = new EvilHangmanGame();

        try {
            game.startGame(dictionary, wordLength);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        StringBuilder wordBuilder = new StringBuilder();

        for(int i = 0; i < wordLength; i++) {

            wordBuilder.append("-");
        }

        Scanner in = new Scanner(System.in);

        boolean winner = false;

        Set<String> words = null;

        while(numGuesses > 0) {

            Collections.sort(guesses);
            System.out.println("You have " + numGuesses + (numGuesses == 1 ? " guess" : " guesses") + " left");
            System.out.println("Used letters: " + String.join(" ", guesses));
            System.out.println("Word: " + wordBuilder.toString());

            String guess;

            while (true) {
                System.out.print("Enter guess: ");

                guess = in.nextLine().trim().toLowerCase();

                if (!guess.matches("[a-z]")) {
                    System.out.println("Invalid input");
                    continue;
                }

                try {
                    words = game.makeGuess(guess.charAt(0));
                    break;
                } catch (IEvilHangmanGame.GuessAlreadyMadeException e) {
                    System.out.println("You already used that letter");
                }
            }

            char guessChar = guess.charAt(0);
            char[] word = words.iterator().next().toCharArray();
            int count = 0;

            for(int i = 0; i < wordLength; i++) {

                if(word[i] == guessChar) {
                    wordBuilder.setCharAt(i, guessChar);
                    count++;
                }
            }

            if (!wordBuilder.toString().contains("-")) {
                winner = true;
                break;
            }

            guesses.add(guess);

            if (numGuesses > 0) {
                if(count == 0) {
                    numGuesses--;
                    System.out.println("Sorry, there are no " + guess + "'s");
                } else if(count == 1) {

                    System.out.println("Yes, there is 1 " + guess);
                } else {

                    System.out.println("Yes, there are " + count + " " + guess + "'s");
                }
            }
        }

        if (winner) {
            System.out.println("You win!");
        } else {
            System.out.println("You lose!");
        }

        System.out.println("The word was " + words.iterator().next());
    }
}
