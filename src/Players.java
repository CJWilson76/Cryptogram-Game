import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Players {
    private final File playersFile;
    private final ArrayList<Player> playerList = new ArrayList<>();
    private boolean loggedIn = false;

    public Players() {
        playersFile = new File("Players.txt");
        loadPlayers();
    }

    // Adding and getting a certain player methods

    public boolean addPlayer(Player p) {
        if (getPlayer(p.getUsername()) != null) {
            System.out.println("Username already exists or is taken by another player.");
            return false;
        }
        playerList.add(p);
        savePlayers();
        System.out.println("Player successfully created: " + p.getUsername());
        return true;
    }

    public Player getPlayer(String username) {
        for (Player p : playerList) {
            if (p.getUsername().equals(username)) return p;
        }
        return null;
    }

    // Methods for loading all players and saving all players

    public void loadPlayers() {
        playerList.clear();

        if (!playersFile.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(playersFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("\\|", 11);

                // Information every user should have
                String username = tokens[0].trim();
                String password = tokens[1].trim();
                int guesses = Integer.parseInt(tokens[2].trim());
                int correct = Integer.parseInt(tokens[3].trim());
                int completed = Integer.parseInt(tokens[4].trim());
                int played = Integer.parseInt(tokens[5].trim());

                Player p = new Player(username, password, guesses, correct, completed, played);

                boolean active = Boolean.parseBoolean(tokens[6]);
                // If there's a saved cryptogram
                if (active) { // If active is true, this means the following tokens will have values
                    String currentEncrypted = tokens[7];
                    String currentGuessState = tokens[8];
                    String currentPhrase = tokens[9];
                    Character currentType = tokens[10].charAt(0);
                    p.setActive(currentEncrypted, currentGuessState, currentPhrase, currentType);
                }

                playerList.add(p);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while loading the players.");
        }
    }

    public void savePlayers() {
        try (FileWriter writer = new FileWriter(playersFile, false)) {
            for (Player p : playerList) {
                writer.write(p.toString()); // Write the player information into the txt file
                writer.write("\n"); // Newline character
            }
        } catch (IOException e) {
            System.out.println("An error occurred while saving the players.");
        }
    }

    public void getAllPlayersCompletedCryptos() {
        for (Player p : playerList) {
            System.out.println(p.getUsername() + " has beaten " + p.getNumCryptogramsCompleted() + " cryptograms.");
        }
    }

    public void getTop10Cryptos() {
        int[] top10CryptoScores = new int[10];
        String[] top10CryptoPlayers = new String[10];

        for (Player p : playerList) {
            for (int i = 0; i < top10CryptoScores.length; i++) {
                if (p.getNumCryptogramsCompleted() > top10CryptoScores[i]) {

                    for (int j = top10CryptoScores.length - 1; j > i; j--) {
                        top10CryptoScores[j] = top10CryptoScores[j - 1];
                        top10CryptoPlayers[j] = top10CryptoPlayers[j - 1];
                    }

                    top10CryptoScores[i] = p.getNumCryptogramsCompleted();
                    top10CryptoPlayers[i] = p.getUsername();
                    break;
                }
            }
        }

        for (int i = 0; i < top10CryptoScores.length; i++) {
            if (top10CryptoScores[i] == 0 && top10CryptoPlayers[i] == null && i == 0) {
                System.out.println("There has been zero players to have successfully completed a cryptogram.");
                break;
            } else if (top10CryptoScores[i] == 0 && top10CryptoPlayers[i] == null && i > 0) {
                System.out.println("There are currently only " + i + " players to have successfully completed a cryptogram.");
                break;
            } else {
                System.out.println((i+1) + ". " + top10CryptoPlayers[i] + " has beaten " + top10CryptoScores[i] + (top10CryptoScores[i] == 1 ? " cryptogram" : " cryptograms") + ".");
            }
        }
    }

    // Player login and log out methods as well as getting current login status

    public boolean login(String username, String password) {
        Player p = getPlayer(username);
        if (p == null) return false;
        if (!p.getPassword().equals(password)) return false;
        loggedIn = true;
        return true;
    }

    public boolean logout() {
        loggedIn = false;
        return true;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public int size() {
        return playerList.size();
    }
}