package com.game;

import java.util.Scanner;

public class Game {

    private Room currentRoom;
    private boolean gameOver;
    private Scanner scanner;
    private Player player;
    private boolean hasSecurityAccess;
    private boolean isDisguised;

    public Game() {
        initializeRooms();
        scanner = new Scanner(System.in);
        gameOver = false;
        player = new Player();
        hasSecurityAccess = false;
        isDisguised = false;
    }

    private void initializeRooms() {
        // Create all rooms
        Room strand = new Room("Strand", "You are on a sandy beach. The ocean waves crash against the shore.");
        Room dschungelpfad = new Room("Dschungelpfad", "A dense jungle path with tall trees and thick vegetation.");
        Room personalquartiere = new Room("Personalquartiere", "The staff quarters. Bunk beds line the walls.");
        Room wartungsschuppen = new Room("Wartungsschuppen", "A maintenance shed filled with tools and equipment.");
        Room hauptvilla = new Room("Hauptvilla", "The main villa. Luxurious furniture and expensive art pieces.");
        Room sicherheitszentrale = new Room("Sicherheitszentrale", "The security center with surveillance monitors.");
        Room helipadPfad = new Room("Helipad-Pfad", "A path leading to the helipad.");
        Room helipad = new Room("Helipad", "A large helipad with a helicopter waiting.");

        // Set up room connections
        strand.setExit("north", dschungelpfad);
        dschungelpfad.setExit("south", strand);
        dschungelpfad.setExit("east", personalquartiere);
        dschungelpfad.setExit("west", wartungsschuppen);
        personalquartiere.setExit("west", dschungelpfad);
        personalquartiere.setExit("north", hauptvilla);
        wartungsschuppen.setExit("east", dschungelpfad);
        wartungsschuppen.setExit("north", sicherheitszentrale);
        hauptvilla.setExit("south", personalquartiere);
        hauptvilla.setExit("east", sicherheitszentrale);
        sicherheitszentrale.setExit("west", hauptvilla);
        sicherheitszentrale.setExit("south", wartungsschuppen);
        sicherheitszentrale.setExit("north", helipadPfad);
        helipadPfad.setExit("south", sicherheitszentrale);
        helipadPfad.setExit("north", helipad);
        helipad.setExit("south", helipadPfad);

        // Add items to rooms
        strand.addItem(new Item("Stock", "A sturdy wooden stick", 1.0));
        wartungsschuppen.addItem(new Item("Bolzenschneider", "Heavy-duty bolt cutters", 3.0));
        personalquartiere.addItem(new Item("Verkleidungskit", "A disguise kit with various items", 2.0));
        sicherheitszentrale.addItem(new Item("Sicherheitskarte", "A security access card", 0.1));
        hauptvilla.addItem(new Item("Laptop", "A laptop computer", 2.5));
        hauptvilla.addItem(new Item("Festplatte", "An external hard drive", 0.5));

        // Set starting room
        currentRoom = strand;
    }

    private void clearScreen() {
        // Clear screen command for both Windows and Unix-like systems
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void play() {
        clearScreen();
        System.out.println("Welcome to Schattenflug: Flucht von der Epstein-Insel!");
        System.out.println("Type 'help' for commands.");

        while (!gameOver) {
            try {
                System.out.println("\n" + currentRoom.getDescription());
                System.out.println(currentRoom.getExitsDescription());
                System.out.println(currentRoom.getItemsDescription());
                System.out.println("\n" + player.getInventoryDescription());
                System.out.println("\nAvailable commands:");
                System.out.println("north, south, east, west - Move in that direction");
                System.out.println("take <item> - Pick up an item");
                System.out.println("drop <item> - Drop an item");
                System.out.println("use <item> - Use an item");
                System.out.println("inventory - View your inventory");
                System.out.println("look - Look around the current room");
                System.out.println("help - Show this help message");
                System.out.println("quit - Exit the game");

                System.out.print("\nWhat would you like to do? ");
                if (!scanner.hasNextLine()) {
                    gameOver = true;
                    System.out.println("\nGame terminated.");
                    break;
                }
                String command = scanner.nextLine().toLowerCase().trim();

                clearScreen();
                processCommand(command);
            } catch (Exception e) {
                System.out.println("\nAn error occurred. Please try again.");
                if (scanner.hasNextLine()) {
                    scanner.nextLine(); // Clear the error input
                }
            }
        }
        scanner.close();
    }

    private void processCommand(String command) {
        String[] parts = command.split("\\s+", 2);
        String action = parts[0];
        String target = parts.length > 1 ? parts[1] : "";

        switch (action) {
            case "north":
            case "n":
            case "south":
            case "s":
            case "east":
            case "e":
            case "west":
            case "w":
                move(action);
                break;
            case "take":
                takeItem(target);
                break;
            case "drop":
                dropItem(target);
                break;
            case "use":
                useItem(target);
                break;
            case "inventory":
                System.out.println(player.getInventoryDescription());
                break;
            case "look":
                // Room description is already shown in the main loop
                break;
            case "help":
                showHelp();
                break;
            case "quit":
                gameOver = true;
                System.out.println("Thanks for playing!");
                break;
            default:
                System.out.println("I don't understand that command.");
        }
    }

    private void move(String direction) {
        // Convert single-letter directions to full words
        String fullDirection = switch (direction) {
            case "n" ->
                "north";
            case "s" ->
                "south";
            case "e" ->
                "east";
            case "w" ->
                "west";
            default ->
                direction;
        };

        Room nextRoom = currentRoom.getExit(fullDirection);
        if (nextRoom == null) {
            System.out.println("You cannot go that way!");
            return;
        }

        // Check for restricted access
        if (nextRoom.getName().equals("Sicherheitszentrale") && !hasSecurityAccess) {
            System.out.println("You need a security card to access this area!");
            return;
        }

        // Check for disguise requirement
        if (nextRoom.getName().equals("Hauptvilla") && !isDisguised) {
            System.out.println("You need a disguise to enter the main villa!");
            return;
        }

        currentRoom = nextRoom;
        System.out.println("You move " + fullDirection + ".");
    }

    private void takeItem(String itemName) {
        if (itemName.isEmpty()) {
            System.out.println("What would you like to take?");
            return;
        }

        Item item = currentRoom.removeItem(itemName);
        if (item == null) {
            System.out.println("There is no " + itemName + " here.");
            return;
        }

        if (player.addItem(item)) {
            System.out.println("You picked up the " + item.getName() + ".");
        } else {
            currentRoom.addItem(item);
            System.out.println("You can't carry that much weight!");
        }
    }

    private void dropItem(String itemName) {
        if (itemName.isEmpty()) {
            System.out.println("What would you like to drop?");
            return;
        }

        Item item = player.removeItem(itemName);
        if (item == null) {
            System.out.println("You don't have a " + itemName + ".");
            return;
        }

        currentRoom.addItem(item);
        System.out.println("You dropped the " + item.getName() + ".");
    }

    private void useItem(String itemName) {
        if (itemName.isEmpty()) {
            System.out.println("What would you like to use?");
            return;
        }

        Item item = player.removeItem(itemName);
        if (item == null) {
            System.out.println("You don't have a " + itemName + ".");
            return;
        }

        switch (item.getName().toLowerCase()) {
            case "sicherheitskarte":
                hasSecurityAccess = true;
                System.out.println("You use the security card. You now have access to restricted areas!");
                break;
            case "verkleidungskit":
                isDisguised = true;
                System.out.println("You use the disguise kit. You now look like staff!");
                break;
            case "laptop":
                System.out.println("You access the laptop and find incriminating evidence!");
                break;
            case "festplatte":
                System.out.println("You connect the hard drive and find more evidence!");
                break;
            case "bolzenschneider":
                System.out.println("You use the bolt cutters to break through a locked gate!");
                break;
            case "stock":
                System.out.println("You use the stick to check for traps ahead.");
                break;
            default:
                System.out.println("You can't find a use for the " + item.getName() + " right now.");
                player.addItem(item); // Return the item if it can't be used
                return;
        }
    }

    private void showHelp() {
        System.out.println("\nAvailable commands:");
        System.out.println("north/n, south/s, east/e, west/w - Move in that direction");
        System.out.println("take <item> - Pick up an item");
        System.out.println("drop <item> - Drop an item");
        System.out.println("use <item> - Use an item");
        System.out.println("inventory - View your inventory");
        System.out.println("look - Look around the current room");
        System.out.println("help - Show this help message");
        System.out.println("quit - Exit the game");
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }
}
