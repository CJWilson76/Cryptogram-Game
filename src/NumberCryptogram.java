import java.util.*;

public class NumberCryptogram extends Cryptogram {
    private final HashMap<Character, Integer> cryptogramAlphabet = new HashMap<>();

    public NumberCryptogram(String phrase) {
        this.phrase = phrase.toUpperCase();
        generateAlphabet();
    }

    public char getPlainLetter(int cryptoValue) {
        for (Map.Entry<Character, Integer> entry : cryptogramAlphabet.entrySet()) { // Finds the entered number in hashmap
            if (entry.getValue() == cryptoValue) { // Returns the key of the value which is the corresponding correct letter
                return entry.getKey();
            }
        }
        return '\0';
    }

    private void generateAlphabet() {
        List<Integer> alphabet = new ArrayList<>();
        for (int i = 1; i <= 26; i++) { // Fills list with numbers 1 - 26 (all corresponding numbers in the alphabet)
            alphabet.add(i);
        }

        // Shuffles the list
        Collections.shuffle(alphabet);

        int i = 0;
        for (char c = 'A'; c <= 'Z'; c++) { // Sets A-Z as the keys in the hashmap with a random number from the shuffled list as the value
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
            } else { // If it is a letter it appends the corresponding random number from the hashmap
                encryptedPhrase.append((int) (cryptogramAlphabet.get(c)));
                // Whitespace for readability
                encryptedPhrase.append(' ');
            }
        }
        return encryptedPhrase;
    }
}