import java.util.*;

public class LetterCryptogram extends Cryptogram {
    private final HashMap<Character, Character> cryptogramAlphabet = new HashMap<>();

    public LetterCryptogram(String phrase) {
        this.phrase = phrase.toUpperCase();
        generateAlphabet();
    }

    public char getPlainLetter(char cryptoLetter) {
        cryptoLetter = Character.toUpperCase(cryptoLetter);

        for (Map.Entry<Character, Character> entry : cryptogramAlphabet.entrySet()) { // Finds the entered letter in the hashmap
            if (entry.getValue() == cryptoLetter) { // Returns the key of the value which is the corresponding correct letter
                return entry.getKey();
            }
        }

        return '\0';
    }

    private void generateAlphabet() {
        List<Character> alphabet = new ArrayList<>();
        for (char c = 'A'; c <= 'Z'; c++) { // Fills list with the alphabet
            alphabet.add(c);
        }

        // Shuffles the list
        Collections.shuffle(alphabet);

        int i = 0;
        for (char c = 'A'; c <= 'Z'; c++)
        {
            // Sets A-Z as the keys in the hashmap with a random letter from the shuffled list as the value
            cryptogramAlphabet.put(c, alphabet.get(i));
            i++;
        }
    }

    public StringBuilder encryptPhrase() {
        StringBuilder encryptedPhrase = new StringBuilder();
        for (int i = 0; i < phrase.length(); i++) {
            char c = phrase.charAt(i);

            if (!Character.isLetter(c)) { // Appends the character from the phrase if it's not a letter (Used for whitespace and different symbols such as ' or /)
                encryptedPhrase.append(c);
            }
            else { // If it is a letter it appends the corresponding random letter from the hashmap
                encryptedPhrase.append(cryptogramAlphabet.get(c));
            }
        }
        return encryptedPhrase;
    }
}