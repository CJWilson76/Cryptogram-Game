public class Main {
    public static void main(String[] args) {
        Terminal printer = new Terminal();
        printer.welcome();

        Players players = new Players();
        players.loadPlayers();

        Player player = printer.menu(players, null);

        while (true) { // Main game loop
            printer.menu(players, player); // Show main menu again when player is logged in
            players.savePlayers();
            Game game;

            if (player.isActive()) {
                System.out.println("Active cryptogram found! Resuming...");
                game = new Game(player);
            } else {
                Character cryptType = printer.getCryptType();
                game = new Game(player, cryptType);
                game.generateCryptogram();
                players.savePlayers();
            }

            printer.play(game, players);
        }
    }
}