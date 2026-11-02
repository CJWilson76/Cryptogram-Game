import java.util.HashMap;
import java.util.Scanner;

public class Terminal {
    private final Scanner scanner;
    private boolean isValid;

    public void welcome() {
        String[] L = {
                "█      ",
                "█      ",
                "█      ",
                "█      ",
                "███████"
        };

        String[] E = {
                "███████",
                "█      ",
                "████   ",
                "█      ",
                "███████"
        };

        String[] T = {
                "███████",
                "   █   ",
                "   █   ",
                "   █   ",
                "   █   "
        };

        String[] ap = {
                " █   ",
                "█    ",
                "     ",
                "     ",
                "     "
        };

        String[] S = {
                "███████",
                "█      ",
                "███████",
                "      █",
                "███████"
        };

        String[] C = {
                "███████",
                "█      ",
                "█      ",
                "█      ",
                "███████"
        };

        String[] R = {
                "██████ ",
                "█     █",
                "██████ ",
                "█   █  ",
                "█     █"
        };

        String[] Y = {
                "█     █",
                " █   █ ",
                "  ███  ",
                "   █   ",
                "   █   "
        };

        String[] P = {
                "██████ ",
                "█     █",
                "██████ ",
                "█      ",
                "█      "
        };

        String[] ex = {
                "   █   ",
                "   █   ",
                "   █   ",
                "       ",
                "   █   "
        };


        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");

        for (int row = 0; row < L.length; row++) {
            System.out.println("   " + L[row] + "   " + E[row] + "   " + T[row] + "  " + ap[row] + S[row] + "      " + C[row] + "   " + R[row] + "   " + Y[row] + "   " + P[row] + "   " + T[row] + ex[row] + "   ");
        }

        System.out.println();
        System.out.println("             A terminal-based cryptograms game brought to you by Blair Campbell, Adin Hamid,");
        System.out.println("             Jack Storrier, Reece Thomson and Callum Wilson for CS207: Advanced Programming.");

        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
        System.out.println();
    }

    public Player menu(Players players, Player player) {
        Player currentPlayer = player;

        System.out.println("┏━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("  ━━━ Main Menu ━━━ ");
        if (players.isLoggedIn()) {
            while (true) {
                System.out.println("You are currently logged in.");
                System.out.println("Option 1: View your statistics.");
                System.out.println("Option 2: Play cryptogram in progress or start a new cryptogram.");
                System.out.println("Option 3: View the top 10 scores.");
                System.out.println("Option 4: Log out and exit.");

                String input = "";

                while (!isValid) {
                    input = scanner.nextLine();
                    validateOption(input, 4);
                }

                isValid = false;

                switch (input) {
                    case "1": // View statistics
                        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
                        System.out.println("    Your Statistics!");
                        System.out.println("    ━━━━━ Username: " + player.getUsername() + " ━━━━━    ");
                        System.out.println("    ━━━ Game Statistics ━━━");
                        System.out.println("    Overall Games: " + player.getNumCryptogramsPlayed() + " games    ");
                        System.out.println("    Completed Games: " + player.getNumCryptogramsCompleted() + " games    ");
                        System.out.println("    Failed Games: " + player.getNumCryptogramsFailed() + " games    ");
                        System.out.println("    ━━━ Guessing Statistics ━━━");
                        System.out.println("    Total Guesses: " + player.getTotalGuesses() + " guesses    ");
                        System.out.println("    Correct Guesses: " + player.getCorrectGuesses() + " guesses    ");
                        System.out.println("    Accuracy: " + player.getAccuracy() + "%    ");
                        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
                        break;
                    case "2": // Play cryptogram
                        return null;
                   case "3":
                       System.out.println("━━━━ Let's Crypt Top 10 Scores ━━━━");
                       players.getTop10Cryptos();
                       System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                       break;

                    case "4": // Log out and exit
                        System.out.println("You have been logged out, thanks for playing Let's Crypt!");
                        players.logout();
                        System.exit(0);
                        break;
                    default: // Should never happen
                        break;
                }
            }
        } else {
            System.out.println("You are not logged in.");
            System.out.println("Option 1: Log in.");
            System.out.println("Option 2: Create an account.");
            System.out.println("Option 3: Exit.");

            String input = "";

            while (!isValid) {
                input = scanner.nextLine();
                validateOption(input, 3);
            }

            isValid = false;

            switch (input) {
                case "1": // Login
                    System.out.println("Logging in to an existing user account...");
                    System.out.println("Please enter your username and password, separated with a comma.");
                    System.out.println("Example: `ILoveCryptograms, Cats12`");
                    while (!isValid) {
                        input = scanner.nextLine();
                        try {
                            String[] splits = input.split(",");
                            String username = splits[0].trim(); // Remove whitespace
                            String password = splits[1]; // Don't remove whitespace in case someone actually wants to use spaces in their password?? Throw the hackers off I guess
                            boolean result = players.login(username, password);
                            if (!result) {
                                System.out.println("Unable to log in, those credentials were wrong. Please try again.");
                                continue;
                            }
                            currentPlayer = players.getPlayer(username);
                            isValid = true;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("You did not enter the user credentials in the right format.");
                            System.out.println("Please enter your username and password, separated with a comma.");
                            System.out.println("Example: `ILoveCryptograms, Cats12`");
                            isValid = false;
                        }
                    }
                    isValid = false;
                    break;
                case "2": // Create an account
                    System.out.println("Creating a brand new user account...");
                    System.out.println("Please enter your preferred username and password, separated with a comma.");
                    System.out.println("Example: `ILoveCryptograms, Cats12`");
                    while (!isValid) {
                        input = scanner.nextLine();
                        try {
                            String[] splits = input.split(",");
                            String username = splits[0].trim(); // Remove whitespace
                            String password = splits[1]; // Don't remove whitespace in case someone actually wants to use spaces in their password?? Throw the hackers off I guess
                            Player newPlayer = new Player(username, password);
                            if (!players.addPlayer((newPlayer))) {
                                throw new Exception();
                            }
                            players.savePlayers();
                            currentPlayer = newPlayer;
                            players.login(username, password);
                            isValid = true;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("You did not enter the user credentials in the right format.");
                            System.out.println("Please enter a username and password, separated with a comma.");
                            System.out.println("Example: `ILoveCryptograms, Cats12`");
                            isValid = false;
                        } catch (Exception e) {
                            System.out.println("Please enter new credentials, including a username that is not the one you have just attempted.");
                            isValid = false;
                        }
                    }
                    isValid = false;
                    break;
                case "3": // Exit the program
                    System.out.println("Thanks for playing Let's Crypt!");
                    System.exit(0);
                default: // Should never happen as we validate user input
                    break;
            }
            System.out.println("You are now successfully logged in!");
        } // Below, we return a player if login or account creation was successful, otherwise we repeat the account login flow
        if (currentPlayer == null) {
            System.out.println("Something went wrong. Please try again.");
            return menu(players, null);
        }
        return currentPlayer;
    }

    public Character getCryptType() {
        System.out.println("You don't currently have a cryptogram puzzle in progress.");
        System.out.println("Please select the type of cryptogram you wish to play.");
        System.out.println("Options: 'l' for letter cryptogram, 'n' for number cryptogram");

        char cryptType = 0;
        String input;

        while (!isValid) {
            input = scanner.nextLine();
            try {
                if (input.length() == 1) {
                    cryptType = Character.toLowerCase(input.charAt(0));
                    if (cryptType != 'l' && cryptType != 'n') {
                        throw new IllegalArgumentException();
                    } else isValid = true;
                } else throw new IllegalArgumentException();
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("You did not enter the user credentials in the right format.");
                System.out.println("Please enter your username and password, separated with a comma.");
                System.out.println("Example: `ILoveCryptograms, Cats12`");
                isValid = false;
            } catch (IllegalArgumentException e) {
                System.out.println("You either did not enter only a single character or did not enter an accepted option. Please try again.");
                System.out.println("Accepted options: 'l' for letter cryptogram, 'n' for number cryptogram.");
            }
        }
        isValid = false;
        return cryptType;
    }

    public void play(Game game, Players players) {
        String input = "";

        while (true) {
            System.out.println("━━━━━ Encrypted phrase ━━━━━");
            System.out.println(game.getCurrentCryptogramString());
            System.out.println();
            System.out.println("━━━━━ Current puzzle ━━━━━");
            System.out.println(game.getPartiallySolvedCryptogram());
            System.out.println();
            System.out.println("Option 1: Enter a guess.");
            System.out.println("Option 2: Undo a guess.");
            System.out.println("Option 3: Get frequencies.");
            System.out.println("Option 4: Get a hint.");
            System.out.println("Option 5: Reveal the cryptogram solution.");
            System.out.println("Option 6: Save and quit the cryptogram.");

            while (!isValid) {
                input = scanner.nextLine();
                validateOption(input, 6);
            }

            isValid = false;

            switch (input) {
                case "1": // Enter a guess
                    while (!isValid) {
                        try {
                            if (game.getCryptType() == 'l') System.out.println("Enter the encrypted letter:");
                            else System.out.println("Enter the encrypted number:");

                            String encInput = scanner.nextLine().trim();

                            if (game.getCryptType() == 'l') {
                                if (!encInput.matches("[A-Za-z]")){
                                    System.out.println("Invalid input, please enter a single letter A-Z.");
                                    continue;
                                }
                                char enc = Character.toUpperCase(encInput.charAt(0));

                                System.out.println("Enter your guess for that letter:");
                                String guessInput = scanner.nextLine().trim();

                                if (!guessInput.matches("[A-Za-z]")){
                                    System.out.println("Invalid guess, must be a letter A-Z.");
                                    continue;
                                }
                                char guess = Character.toUpperCase(guessInput.charAt(0));
                                game.enterLetter(enc, guess);
                                isValid = true;

                            } else {
                                if (game.getCryptType() == 'n') {
                                    if (!encInput.matches("\\d+")) {
                                        System.out.println("Invalid input, please enter a single number i.e. 16");
                                        continue;
                                    }
                                    int enc = Integer.parseInt(encInput);

                                    System.out.println("Enter your guess for that number:");
                                    String guessInput = scanner.nextLine().trim();

                                    if (!guessInput.matches("[A-Za-z]")) {
                                        System.out.println("Invalid guess, must be a letter A-Z.");
                                        continue;
                                    }
                                    char guess = Character.toUpperCase(guessInput.charAt(0));
                                    game.enterNumber(enc, guess);
                                    isValid = true;
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("You most likely didn't enter anything here.");
                        }
                    }

                    isValid = false;
                    players.savePlayers();

                    if (game.isSolved()) {
                        System.out.println("You solved it!");
                        players.savePlayers();
                        return;
                    }
                    break;

                case "2": // Undo a guess
                    while (!isValid) {
                        try {
                            if (game.getCryptType() == 'l') System.out.println("Enter the encrypted letter to undo:");
                            else System.out.println("Enter the encrypted number to undo:");

                            String encInput = scanner.nextLine().trim();
                            if (game.getCryptType() == 'l'){
                                if(!encInput.matches("[A-Za-z]")){
                                    System.out.println("Invalid input, please enter a single letter A-Z.");
                                    continue;
                                }
                                char enc = Character.toUpperCase(encInput.charAt(0));
                                game.undoLetter(enc);
                                isValid = true;
                            } else {
                                if (game.getCryptType() == 'n') {
                                    if (!encInput.matches("\\d+")) {
                                        System.out.println("Invalid input, must be a valid number.");
                                        continue;
                                    }
                                    int enc = Integer.parseInt(encInput);
                                    game.undoNumber(enc);
                                    isValid = true;
                                }
                            }

                        } catch (Exception e) {
                            System.out.println("You most likely didn't enter anything here.");
                        }
                    }

                    isValid = false;
                    players.savePlayers();
                    break;

                case "3": // Get the frequencies of the encrypted symbols in the cryptogram
                    System.out.println("━━━━━ Frequencies ━━━━━");
                    StringBuilder display = new StringBuilder();
                    for (HashMap.Entry<Character, Integer> m : game.viewFrequencies(game.getCurrentCryptogramString()).entrySet()) {
                        String element = m.getKey() + ": " + m.getValue() + " | ";
                        display.append(element);
                    }
                    System.out.println(display);
                    System.out.println();

                    break;

                case "4": // Get a hint
                    String hint = game.getHint();
                    System.out.println(hint);
                    players.savePlayers();
                    if (game.isSolved()) {
                        System.out.println("You solved it! (with some help :P )");
                        return;
                    }
                    break;

                case "5": // Reveal the solution for the cryptogram, they would not have any more stats incremented
                    System.out.println("━━━━━ Puzzle solution ━━━━━");
                    System.out.println(game.showSolution());
                    game.clearPlayer();
                    players.savePlayers();
                    return;

                case "6": // Save and exit
                    System.out.println("Your current cryptogram has been saved, and you will now be taken to the main menu.");
                    game.saveGame();
                    players.savePlayers();
                    return;
                default:
                    break;
            }
        }
    }

    private void validateOption(String input, int allowedOptionCount) {
        int value;
        try {
            value = Integer.parseInt(input);
            if (value >= 1 && value <= allowedOptionCount) isValid = true;
            else System.out.println("You did not provide a valid option.");
        } catch (NumberFormatException e) {
            System.out.println("You did not provide a valid option.");
        }
    }


    public Terminal() {
        scanner = new Scanner(System.in);
        isValid = false;
    }
}
