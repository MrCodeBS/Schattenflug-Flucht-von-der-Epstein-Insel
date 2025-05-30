package ch.bbw.zork;

/**
 * Class Game - the main class of the "Zork" game.
 *
 * Author: Michael Kolling, 1.1, March 2000 refactoring: Rinaldo Lanza,
 * September 2020
 */
public class Game {

    private Parser parser;
    private Room currentRoom;
    private Room outside, lab, tavern, gblock, office;

    public Game() {

        parser = new Parser(System.in);

        // create rooms
        outside = new Room("outside G block on Peninsula campus");
        lab = new Room("lab, a lecture theatre in A block");
        tavern = new Room("the Seahorse Tavern (the campus pub)");
        gblock = new Room("the G Block");
        office = new Room("the computing admin office");
        // initialise room exits
        outside.setExits(null, lab, gblock, tavern);
        lab.setExits(null, null, null, outside);
        tavern.setExits(null, outside, null, null);
        gblock.setExits(outside, office, null, null);
        office.setExits(null, null, null, gblock);

        currentRoom = outside; // start game outside
    }

    /**
     * Main play routine. Loops until end of play.
     */
    public void play() {
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
        boolean finished = false;
        while (!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    private void printWelcome() {
        System.out.println();
        System.out.println("Welcome to Zork!");
        System.out.println("Zork is a simple adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.longDescription());
    }

    private boolean processCommand(Command command) {
        if (command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();

        } else if (commandWord.equals("go")) {
            goRoom(command);

        } else if (commandWord.equals("quit")) {
            if (command.hasSecondWord()) {
                System.out.println("Quit what?");
            } else {
                return true; // signal that we want to quit
            }
        }
        return false;
    }

    private void printHelp() {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at Monash Uni, Peninsula Campus.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println(parser.showCommands());
    }

    private void goRoom(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Go where?");
        } else {

            String direction = command.getSecondWord();

            // Try to leave current room.
            Room nextRoom = currentRoom.nextRoom(direction);

            if (nextRoom == null) {
                System.out.println("There is no door!"); 
            }else {
                currentRoom = nextRoom;
                System.out.println(currentRoom.longDescription());
            }
        }
    }
}
