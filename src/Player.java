public class Player {
    private String username;
    private String userPassword;
    private double accuracy;
    private int totalGuesses;
    private int correctGuesses;
    private int cryptogramsCompleted;
    private int cryptogramsPlayed;
    private String currentEncrypted;
    private String currentGuessState;
    private String currentPhrase;
    private Character currentType;
    private boolean active;

    public Player(String name, String password) {
        username = name;
        userPassword = password;
        totalGuesses = 0;
        correctGuesses = 0;
        cryptogramsCompleted = 0;
        cryptogramsPlayed = 0;
        active = false;
        accuracy = 0.0;
    }

    public Player(String name, String password, int guesses, int correct, int completed, int played) {
        username = name;
        userPassword = password;
        totalGuesses = guesses;
        correctGuesses = correct;
        cryptogramsCompleted = completed;
        cryptogramsPlayed = played;
        accuracy = calcAccuracy(guesses, correct);
    }

    @Override
    public String toString() {
        return username + "|" + userPassword + "|" + totalGuesses + "|" + correctGuesses + "|" + cryptogramsCompleted + "|" + cryptogramsPlayed + "|" + active + "|" + (currentEncrypted == null ? "" : currentEncrypted) + "|" + (currentGuessState == null ? "" : currentGuessState) + "|" + (currentPhrase == null ? "" : currentPhrase) + "|" + (currentType == null ? "" : currentType);
    }

    // Active puzzles

    public void setActive(String encrypted, String guessState, String phrase, Character type) {
        this.currentEncrypted = encrypted;
        this.currentGuessState = guessState;
        this.currentPhrase = phrase;
        this.currentType = type;
        this.active = true;
    }

    public void clearActive() {
        this.currentEncrypted = null;
        this.currentGuessState = null;
        this.currentPhrase = null;
        this.currentType = null;
        this.active = false;
    }

    public boolean isActive() {
        return active;
    }

    // Statistics getters and setters (for incrementing)

    public void incrementCryptogramsCompleted() {
        cryptogramsCompleted++;
    }

    public void incrementCryptogramsPlayed() {
        cryptogramsPlayed++;
    }

    public int getNumCryptogramsCompleted() {
        return cryptogramsCompleted;
    }

    public int getNumCryptogramsPlayed() {
        return cryptogramsPlayed;
    }

    public int getNumCryptogramsFailed() {
        return cryptogramsPlayed - cryptogramsCompleted;
    }

    public void incrementTotalGuesses() {
        totalGuesses++;
        accuracy = calcAccuracy(totalGuesses, correctGuesses);
    }

    public void incrementCorrectGuesses() {
        correctGuesses++;
        accuracy = calcAccuracy(totalGuesses, correctGuesses);
    }

    public int getTotalGuesses() {
        return totalGuesses;
    }

    public int getCorrectGuesses() {
        return correctGuesses;
    }

    // Current puzzle getters

    public String getCurrentEncrypted() {
        return currentEncrypted;
    }

    public String getCurrentGuessState() {
        return currentGuessState;
    }

    public String getCurrentPhrase() {
        return currentPhrase;
    }

    public Character getCurrentType() {
        return currentType;
    }

    // User account getters

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return userPassword;
    }

    // Accuracy getter and calculator which is run at class instantiation

    public double getAccuracy() {
        return accuracy;
    }

    public double calcAccuracy(int total, int correct) {
        if (total == 0) return 0.0;
        return Math.round((correct * 100.0 / total) * 100.0) / 100.0;
    }
}