import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;
import java.util.*;

public class Tests {

    private Game letterGame(String phrase, String encrypted, HashMap<Character, Character> solutionMap) throws Exception {
        Player p = new Player("test", "pw");
        Game game = new Game(p, 'l');
        LetterCryptogram crypt = new LetterCryptogram("TESTER");

        setPrivate(game, "phrase", phrase);
        setPrivate(game, "currentCryptogramString", encrypted);
        setPrivate(game, "letterSolutionMap", solutionMap);
        setPrivate(game, "currentCryptogram", crypt);
        setPrivate(game, "letterGuesses", new HashMap<Character, Character>());

        return game;
    }

    private Game numberGame(String phrase, String encrypted, HashMap<Integer, Character> solutionMap) throws Exception {
        Player p = new Player("test", "pw");
        Game game = new Game(p, 'n');
        NumberCryptogram crypt = new NumberCryptogram("TESTER");

        setPrivate(game, "phrase", phrase);
        setPrivate(game, "currentCryptogramString", encrypted);
        setPrivate(game, "numberSolutionMap", solutionMap);
        setPrivate(game, "currentCryptogram", crypt);
        setPrivate(game, "numberGuesses", new HashMap<Integer, Character>());

        return game;
    }

    private void setPrivate(Object object, String field, Object value) throws Exception {
        Field f = object.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(object, value);
    }

    @Test
    void testEnterLetter() throws Exception {
        HashMap<Character, Character> map = new HashMap<>();
        map.put('X', 'A');

        Game game = letterGame("A", "X", map);
        Player p = game.currentPlayer;

        game.enterLetter('X', 'A');

        assertEquals(1, p.getTotalGuesses());
        assertEquals(1, p.getCorrectGuesses());
    }

    @Test
    void testWrongLetter() throws Exception {
        HashMap<Character, Character> map = new HashMap<>();
        map.put('X', 'A');

        Game game = letterGame("A", "X", map);
        Player p = game.currentPlayer;
        game.enterLetter('X', 'F');

        assertEquals(1, p.getTotalGuesses());
        assertEquals(0, p.getCorrectGuesses());
    }

    @Test
    void testEnterNumberCorrect() throws Exception {
        HashMap<Integer, Character> map = new HashMap<>();
        map.put(16, 'E');

        Game game = numberGame("E", "16", map);
        Player p = game.currentPlayer;

        game.enterNumber(16, 'E');

        assertEquals(1, p.getTotalGuesses());
        assertEquals(1, p.getCorrectGuesses());
    }

    @Test
    void testEnterNumberWrong() throws Exception {
        HashMap<Integer, Character> map = new HashMap<>();
        map.put(16, 'E');

        Game game = numberGame("E", "16", map);
        Player p = game.currentPlayer;

        game.enterNumber(16, 'Z');

        assertEquals(1, p.getTotalGuesses());
        assertEquals(0, p.getCorrectGuesses());
    }

    @Test
    void testUndoLetter() throws Exception {
        HashMap<Character, Character> map = new HashMap<>();
        map.put('X', 'A');

        Game game = letterGame("A", "X", map);

        game.enterLetter('X', 'A');
        game.undoLetter('X');

        HashMap<Character, Character> guesses = (HashMap<Character, Character>) game.getClass().getDeclaredField("letterGuesses").get(game);

        assertFalse(guesses.containsKey('X'));
    }

    @Test
    void testUndoNumber() throws Exception {
        HashMap<Integer, Character> map = new HashMap<>();
        map.put(16, 'E');

        Game game = numberGame("E", "16", map);

        game.enterNumber(16, 'E');
        game.undoNumber(16);

        HashMap<Integer, Character> guesses = (HashMap<Integer, Character>) game.getClass().getDeclaredField("numberGuesses").get(game);

        assertFalse(guesses.containsKey(16));
    }

    @Test
    void testIsSolvedLetter() throws Exception {
        HashMap<Character, Character> map = new HashMap<>();
        map.put('X', 'A');
        map.put('Y', 'B');
        map.put('Z', 'C');
        Game game = letterGame("ABC", "XYZ", map);

        game.enterLetter('X', 'A');
        game.enterLetter('Y', 'B');
        game.enterLetter('Z', 'C');
        assertTrue(game.isSolved());
    }

    @Test
    void testIsSolvedNumber() throws Exception {
        HashMap<Integer, Character> map = new HashMap<>();
        map.put(1, 'A');
        map.put(2, 'B');
        map.put(3, 'C');
        Game game = numberGame("ABC", "1 2 3", map);

        game.enterNumber(1, 'A');
        game.enterNumber(2, 'B');
        game.enterNumber(3, 'C');
        assertTrue(game.isSolved());
    }

    @Test
    void testGetHintLetter() throws Exception {
        HashMap<Character, Character> map = new HashMap<>();
        map.put('X', 'A');
        map.put('Y', 'B');

        Game game = letterGame("AAB", "XXY", map);
        String hint = game.getHint();

        assertTrue(hint.contains("X"));
        assertEquals('A', ((HashMap<Character, Character>) game.getClass().getDeclaredField("letterGuesses").get(game)).get('X'));
    }

    @Test
    void testGetHintNumber() throws Exception {
        HashMap<Integer, Character> map = new HashMap<>();
        map.put(16, 'E');
        map.put(4, 'A');

        Game game = numberGame("EEA", "16 16 4", map);

        String hint = game.getHint();

        assertTrue(hint.contains("16"));
    }
    
    @Test
    void testAccuracyTracking() throws Exception {
        HashMap<Character, Character> map = new HashMap<>();
        map.put('X', 'A');

        Game game = letterGame("A", "X", map);
        Player p = game.currentPlayer;

        game.enterLetter('X', 'A');
        game.undoLetter('X');
        game.enterLetter('X', 'Z');

        assertEquals(2, p.getTotalGuesses());
        assertEquals(1, p.getCorrectGuesses());
        assertEquals(50.0, p.getAccuracy());
    }
}
