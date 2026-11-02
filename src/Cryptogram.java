import java.util.HashMap;

public abstract class Cryptogram {
    public String phrase;
    public abstract StringBuilder encryptPhrase();
    public HashMap<Character, Integer> getFrequencies(String currentCryptogramString) {
        HashMap<Character, Integer> frequencies = new HashMap<>();
        for (char c : currentCryptogramString.toCharArray()) {
            if (Character.isLetter(c)) {
                char upper = Character.toUpperCase(c);
                if (frequencies.get(upper) == null) frequencies.put(upper, 1); // If not exists, add that char with a 1 value
                else frequencies.put(upper, frequencies.get(upper) + 1); // Otherwise, increment it
            }
        }
        return frequencies;
    }
}