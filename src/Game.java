import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Game {
    public Player currentPlayer;
    private Cryptogram currentCryptogram;
    private String currentCryptogramString;
    private String phrase;
    private Character cryptType;
    public HashMap<Character, Character> letterGuesses = new HashMap<>();
    public HashMap<Integer, Character> numberGuesses = new HashMap<>();
    private HashMap<Character, Character> letterSolutionMap = new HashMap<>();
    private HashMap<Integer, Character> numberSolutionMap = new HashMap<>();
    private final File phrasesFile;

    public Game(Player p) { // If the player doesn't mind what cryptogram to play
        this.currentPlayer = p;
        this.phrasesFile = new File("Phrases.txt");
        if (p.isActive()) loadGame();
    }

    public Game(Player p, Character cryptType) { // If the player selects a type of cryptogram they want to play
        this.currentPlayer = p;
        this.cryptType = cryptType;
        this.phrasesFile = new File("Phrases.txt");
        currentPlayer.incrementCryptogramsPlayed();
    }

    public void generateCryptogram() {
        phrase = generatePhrase();
        if (Character.toLowerCase(cryptType) == 'l') {
            currentCryptogram = new LetterCryptogram(phrase);
            currentCryptogramString = currentCryptogram.encryptPhrase().toString();
            buildLetterSolutionMap();
        } else if (Character.toLowerCase(cryptType) == 'n') {
            currentCryptogram = new NumberCryptogram(phrase);
            currentCryptogramString = currentCryptogram.encryptPhrase().toString();
            buildNumberSolutionMap();
        }

        currentPlayer.setActive(currentCryptogramString, getPartiallySolvedCryptogram().toString(), phrase, cryptType);
    }

    public String generatePhrase() {
        ArrayList<String> phrases = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(phrasesFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    phrases.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while loading the phrases.");
        }
        // Only return a phrase from indexes 1 to the number of valid lines in the file
        return phrases.get((int) (Math.random() * phrases.size()));
    }

    private void buildLetterSolutionMap() {
        letterSolutionMap.clear();
        for (int i = 0; i < phrase.length(); i++) {
            char encrypted = currentCryptogramString.charAt(i);
            char plain = phrase.charAt(i);
            if (Character.isLetter(encrypted) && Character.isLetter(plain)) {
                letterSolutionMap.put(Character.toUpperCase(encrypted), Character.toUpperCase(plain));
            }
        }
    }

    private void buildNumberSolutionMap() {
        numberSolutionMap.clear();
        String[] tokens = currentCryptogramString.trim().split("\\s+");
        int phraseIndex = 0;
        for (String token : tokens) {
            if (token.matches("\\d+")){
                while (phraseIndex < phrase.length() && !Character.isLetter(phrase.charAt(phraseIndex))){
                    phraseIndex++;
                }

                if (phraseIndex >= phrase.length()) break;
                int value = Integer.parseInt(token);
                char plain = Character.toUpperCase(phrase.charAt(phraseIndex));

                numberSolutionMap.put(value, plain);
                phraseIndex++;
            } else {
                phraseIndex++;
            }
        }
    }


    public void enterLetter(char encrypted, char guess) {
        if (cryptType != 'l') return;
        encrypted = Character.toUpperCase(encrypted);
        guess = Character.toUpperCase(guess);

        currentPlayer.incrementTotalGuesses();

        char correct = letterSolutionMap.getOrDefault(encrypted, '\0');
        if (correct == guess) currentPlayer.incrementCorrectGuesses();


        letterGuesses.put(encrypted, guess);
        saveGame();
        if (isSolved()) finishPuzzle();
    }

    public void enterNumber(int encrypted, char guess) {
        if (cryptType != 'n') return;

        guess = Character.toUpperCase(guess);
        currentPlayer.incrementTotalGuesses();
        char correct = numberSolutionMap.getOrDefault(encrypted, '\0');
        if (correct == guess) currentPlayer.incrementCorrectGuesses();

        numberGuesses.put(encrypted, guess);
        saveGame();

        if (isSolved()) finishPuzzle();
    }


    public void undoLetter(char encrypted) {
        if (cryptType == 'l') {
            letterGuesses.remove(Character.toUpperCase(encrypted));
            saveGame();
        }
    }

    public void undoNumber(int encrypted) {
        if (cryptType == 'n') {
            numberGuesses.remove(encrypted);
            saveGame();
        }
    }


    public void saveGame() {
        currentPlayer.setActive(currentCryptogramString, getPartiallySolvedCryptogram().toString(), phrase, cryptType);
    }

    public void loadGame() {
        currentCryptogramString = currentPlayer.getCurrentEncrypted();
        phrase = currentPlayer.getCurrentPhrase();
        cryptType = currentPlayer.getCurrentType();
        letterGuesses.clear();
        numberGuesses.clear();

        if (Character.toLowerCase(cryptType) == 'l') {
            currentCryptogram = new LetterCryptogram(phrase);
            buildLetterSolutionMap();
            rebuildLetterGuessMap();
        } else if (Character.toLowerCase(cryptType) == 'n') {
            currentCryptogram = new NumberCryptogram(phrase);
            buildNumberSolutionMap();
            rebuildNumberGuessMap();
        }

        System.out.println("Game loaded for player: " + currentPlayer.getUsername());
        System.out.println(getPartiallySolvedCryptogram());
    }

    private void rebuildLetterGuessMap() {
        String encrypted = currentCryptogramString;
        String guessState = currentPlayer.getCurrentGuessState();

        for (int i = 0; i < encrypted.length() && i < guessState.length(); i++) {
            char enc = Character.toUpperCase(encrypted.charAt(i));
            char guess = Character.toUpperCase(guessState.charAt(i));
            if (Character.isLetter(enc) && Character.isLetter(guess)) {
                letterGuesses.put(enc, guess);
            }
        }
    }

    private void rebuildNumberGuessMap() {
        String[] encTokens = currentCryptogramString.trim().split("\\s+");
        String[] guessTokens = currentPlayer.getCurrentGuessState().split("\\s+");

        int len = Math.min(encTokens.length, guessTokens.length);

        for (int i = 0; i < len; i++) {
            if (encTokens[i].matches("\\d+") && guessTokens[i].matches("[A-Za-z]")) {
                numberGuesses.put(Integer.parseInt(encTokens[i]), Character.toUpperCase(guessTokens[i].charAt(0)));
            }
        }
    }

    public StringBuilder getPartiallySolvedCryptogram() { // Returns underscores of the current cryptogram, but where a user has attempted to guess a letter the underscores are replaced with it.
        StringBuilder string = new StringBuilder();

        if(cryptType == 'l') {

            for (int i = 0; i < currentCryptogramString.length(); i++) {
                char c = currentCryptogramString.charAt(i);

                if (!Character.isLetter(c)) { // If it is a space or apostrophe
                    string.append(c);
                } else { // If it is an actual letter
                    Character guess = letterGuesses.get(Character.toUpperCase(c));
                    string.append(guess != null ? guess : '_');
                }
            }
        } else {
            String[] tokens = currentCryptogramString.trim().split("\\s+");
            for (String token : tokens) {
                if(!token.matches("\\d+")){
                    string.append(token);
                } else {
                    int value = Integer.parseInt(token);
                    Character guess = numberGuesses.get(value);
                    string.append(guess != null ? guess : '_');
                }
                string.append(' ');
            }
        }
        return string;
    }

    public String getHint(){
        if(cryptType == 'l'){
            HashMap<Character, Integer> freq = currentCryptogram.getFrequencies(currentCryptogramString);
            char bestEnc = '\0';
            int bestCount = -1;

            for(char enc : letterSolutionMap.keySet()){
                if(!letterGuesses.containsKey(enc)){
                    int count = freq.getOrDefault(enc, 0);
                    if(count > bestCount){
                        bestCount = count;
                        bestEnc = enc;
                    }
                }
            }
            if (bestEnc != '\0') {
                char correct = letterSolutionMap.get(bestEnc);
                letterGuesses.put(bestEnc, correct);
                saveGame();
                return "Hint: " + bestEnc + "=" + correct;
            }
        } else {
            HashMap<Character, Integer> freq = currentCryptogram.getFrequencies(currentCryptogramString);
            int bestEnc = -1;
            int bestCount = -1;

            for (int enc : numberSolutionMap.keySet()) {
                if(!numberGuesses.containsKey(enc)){
                    char encChar = (char) ('A' + (enc % 26));
                    int count = freq.getOrDefault(encChar, 0);
                    if(count > bestCount){
                        bestCount = count;
                        bestEnc = enc;
                    }
                }
            }
            if (bestEnc != '\0') {
                char correct = numberSolutionMap.get(bestEnc);
                numberGuesses.put(bestEnc, correct);
                saveGame();
                return "Hint: " + bestEnc + "=" + correct;
            }
        }
        return "No hints available!";
    }

    public boolean isSolved() {
        if (cryptType == 'l'){
            for(char enc : letterSolutionMap.keySet()){
                Character guess = letterGuesses.get(enc);
                if(guess == null) return false;
                if(!guess.equals(letterSolutionMap.get(enc))) return false;
            }
            return true;
        } else {
            for (int enc: numberSolutionMap.keySet()) {
                Character guess = numberGuesses.get(enc);
                if(guess == null) return false;
                if(!guess.equals(numberSolutionMap.get(enc))) return false;
            }
            return true;
        }
    }

    private void finishPuzzle() {
        currentPlayer.incrementCryptogramsCompleted();
        currentPlayer.clearActive();
    }

    public HashMap<Character, Integer> viewFrequencies(String currentCryptogramString) {
        return currentCryptogram.getFrequencies(currentCryptogramString);
    }

    public String getCurrentCryptogramString() {
        return currentCryptogramString;
    }

    public String showSolution() {
        return phrase;
    }

    public void clearPlayer() {
        currentPlayer.clearActive();
    }

    public Character getCryptType() {
        return cryptType;
    }
}

