package com.schattenflug.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Random;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Game {

    private final Player player;
    private Room currentRoom;
    private final Parser parser;
    private Map<String, Room> allRooms;
    private Scanner scanner;
    private boolean gameOver;
    private boolean hasSecurityAccess;
    private boolean isDisguised;
    private boolean isDiddyPresent;
    private boolean hasBabyOil;
    private Random random;
    private static final String BABY_OIL_FILE = "assets/baby-oil.txt";

    public Game() {
        initializeRooms();
        scanner = new Scanner(System.in);
        gameOver = false;
        player = new Player();
        hasSecurityAccess = false;
        isDisguised = false;
        isDiddyPresent = false;
        hasBabyOil = false;
        random = new Random();

        this.currentRoom = allRooms.get("BEACH");

        if (this.currentRoom != null) {
            player.setCurrentRoom(this.currentRoom);
        } else {
            System.out.println("Error: Starting room 'BEACH' not found in allRooms. Creating a default temporary room.");
            this.currentRoom = new Room("DEFAULT_START_ROOM", "You are in a non-descript, empty space. Something went wrong with world creation.");
            player.setCurrentRoom(this.currentRoom);
        }
    }

    private void initializeRooms() {
        allRooms = new HashMap<>();
        createWorld();
    }

    private void createWorld() {
        Room beach = new Room("Strand", "You are on a sandy beach. The ocean waves crash against the shore.");
        Room junglePath = new Room("Dschungelpfad", "A dense jungle path with tall trees and thick vegetation.");
        Room staffQuarters = new Room("Personalquartiere", "The staff quarters. Bunk beds line the walls.");
        Room maintenanceShed = new Room("Wartungsschuppen", "A maintenance shed filled with tools and equipment.");
        Room mainVilla = new Room("Hauptvilla", "The main villa. Luxurious furniture and expensive art pieces.");
        Room securityCenter = new Room("Sicherheitszentrale", "The security center with surveillance monitors.");
        Room helipadPath = new Room("Helipad-Pfad", "A path leading to the helipad.");
        Room helipad = new Room("Helipad", "A large helipad with a helicopter waiting.");
        Room whiteParty = new Room("White Party", "A luxurious party room decorated entirely in white. P Diddy's favorite spot.");

        beach.addItem(new Item("Stock", "A sturdy wooden stick", 1.0));
        maintenanceShed.addItem(new Item("Bolzenschneider", "Heavy-duty bolt cutters", 3.0));
        staffQuarters.addItem(new Item("Verkleidungskit", "A disguise kit with various items", 2.0));
        staffQuarters.addItem(new Item("Baby Oil", "A bottle of baby oil. P Diddy's favorite.", 0.5));
        securityCenter.addItem(new Item("Sicherheitskarte", "A security access card", 0.1));
        mainVilla.addItem(new Item("Laptop", "A laptop computer", 2.5));
        mainVilla.addItem(new Item("Festplatte", "An external hard drive", 0.5));
        whiteParty.addItem(new Item("Champagne", "A bottle of expensive champagne", 1.0));

        beach.setExit("north", junglePath);
        junglePath.setExit("south", beach);
        junglePath.setExit("east", staffQuarters);
        junglePath.setExit("west", maintenanceShed);
        staffQuarters.setExit("west", junglePath);
        staffQuarters.setExit("north", mainVilla);
        maintenanceShed.setExit("east", junglePath);
        maintenanceShed.setExit("north", securityCenter);
        mainVilla.setExit("south", staffQuarters);
        mainVilla.setExit("east", securityCenter);
        mainVilla.setExit("north", whiteParty);
        securityCenter.setExit("west", mainVilla);
        securityCenter.setExit("south", maintenanceShed);
        securityCenter.setExit("north", helipadPath);
        helipadPath.setExit("south", securityCenter);
        helipadPath.setExit("north", helipad);
        helipad.setExit("south", helipadPath);
        whiteParty.setExit("south", mainVilla);

        allRooms.put("BEACH", beach);
        allRooms.put("JUNGLE_PATH", junglePath);
        allRooms.put("STAFF_QUARTERS", staffQuarters);
        allRooms.put("MAINTENANCE_SHED", maintenanceShed);
        allRooms.put("MAIN_VILLA", mainVilla);
        allRooms.put("SECURITY_CENTER", securityCenter);
        allRooms.put("HELIPAD_PATH", helipadPath);
        allRooms.put("HELIPAD", helipad);
        allRooms.put("WHITE_PARTY", whiteParty);
    }

    public void play() {
        printWelcome();

        boolean finished = false;
        try (Scanner scanner = new Scanner(System.in)) {
            while (!finished) {
                if (player.getCurrentRoom() == null) {
                    System.out.println("Error: Player is in a null room. Exiting.");
                    break;
                }
                System.out.println("\n" + player.getCurrentRoom().getLongDescription());
                System.out.print("> ");
                String input = scanner.nextLine().toUpperCase();

                String[] commandParts = parser.parse(input);
                String commandWord = commandParts[0];
                String secondWord = commandParts.length > 1 ? commandParts[1] : null;
                String thirdWord = commandParts.length > 2 ? commandParts[2] : null;

                finished = processCommand(commandWord, secondWord, thirdWord);
            }
        }
        System.out.println("Thank you for playing Schattenflug. Goodbye.");
    }

    private void printWelcome() {
        System.out.println("Welcome to Schattenflug: Flucht von der Epstein-Insel!");
        System.out.println("You find yourself on a desolate beach, the night air chilling you to the bone.");
        System.out.println("Type 'HELP' if you need assistance with commands.");
    }

    private boolean processCommand(String commandWord, String subject, String object) {
        boolean wantToQuit = false;

        if (commandWord == null || commandWord.isEmpty()) {
            System.out.println("I don't understand...");
            return false;
        }

        switch (commandWord) {
            case "HELP":
                printHelp();
                break;
            case "GO":
                goRoom(subject);
                break;
            case "QUIT":
                wantToQuit = true;
                break;
            case "LOOK":
                if (subject == null) {
                    System.out.println(player.getCurrentRoom().getLongDescription());
                } else {
                    lookAt(subject);
                }
                break;
            case "TAKE":
                takeItem(subject);
                break;
            case "DROP":
                dropItem(subject);
                break;
            case "INVENTORY":
            case "I":
                player.printInventory();
                break;
            case "USE":
                useItem(subject, object);
                break;
            default:
                System.out.println("I don't know what you mean by '" + commandWord + "'");
                break;
        }
        return wantToQuit;
    }

    private void printHelp() {
        System.out.println("You are trying to escape the island. You are lost. You are alone.");
        System.out.println("Your command words are:");
        System.out.println("   GO [DIRECTION] (e.g., GO NORTH)");
        System.out.println("   LOOK (describes the current room)");
        System.out.println("   LOOK [ITEM/OBJECT] (describes an item or object)");
        System.out.println("   TAKE [ITEM]");
        System.out.println("   DROP [ITEM]");
        System.out.println("   INVENTORY (or I)");
        System.out.println("   USE [ITEM] ON [TARGET] (e.g. USE KEY ON DOOR)");
        System.out.println("   HELP");
        System.out.println("   QUIT");
    }

    private void goRoom(String direction) {
        if (direction == null) {
            System.out.println("Go where?");
            return;
        }
        Room nextRoom = player.getCurrentRoom().getExit(direction.toUpperCase());

        if (nextRoom == null) {
            System.out.println("You can't go that way!");
        } else {
            player.setCurrentRoom(nextRoom);
            System.out.println("You move " + direction.toLowerCase() + ".");
        }
    }

    private void lookAt(String itemName) {
        if (itemName == null) {
            System.out.println("Look at what?");
            return;
        }
        Item itemInInventory = player.getItem(itemName);
        if (itemInInventory != null) {
            System.out.println(itemInInventory.getDescription());
            return;
        }
        Item itemInRoom = player.getCurrentRoom().getItem(itemName);
        if (itemInRoom != null) {
            System.out.println(itemInRoom.getDescription());
            return;
        }
        System.out.println("You don't see any '" + itemName + "' here to look at closely.");
    }

    private void takeItem(String itemName) {
        if (itemName == null) {
            System.out.println("Take what?");
            return;
        }
        Item item = player.getCurrentRoom().removeItem(itemName);
        if (item != null) {
            if (player.addItem(item)) {
                System.out.println("You took the " + itemName.toLowerCase() + ".");
            } else {
                player.getCurrentRoom().addItem(item);
                System.out.println("You can't carry any more items.");
            }
        } else {
            System.out.println("There is no " + itemName.toLowerCase() + " here to take.");
        }
    }

    private void dropItem(String itemName) {
        if (itemName == null) {
            System.out.println("Drop what?");
            return;
        }
        Item item = player.removeItem(itemName);
        if (item != null) {
            player.getCurrentRoom().addItem(item);
            System.out.println("You dropped the " + itemName.toLowerCase() + ".");
        } else {
            System.out.println("You don't have a " + itemName.toLowerCase() + " to drop.");
        }
    }

    private void useItem(String itemName, String targetName) {
        if (itemName == null) {
            System.out.println("Use what?");
            return;
        }
        Item itemToUse = player.getItem(itemName);
        if (itemToUse == null) {
            System.out.println("You don't have a " + itemName.toLowerCase() + ".");
            return;
        }

        System.out.println("You try to use the " + itemName.toLowerCase() + (targetName != null ? " on the " + targetName.toLowerCase() : "") + ".");

        if ("BOLTCUTTERS".equalsIgnoreCase(itemName)) {
            if ("CHAIN".equalsIgnoreCase(targetName)) {
                System.out.println("With a grunt, you position the cutters. *SNAP!* The chain falls away.");
            } else if ("FENCE".equalsIgnoreCase(targetName)) {
                System.out.println("The thick wires of the fence are no match for the cutters. You snip an opening large enough to slip through.");
            } else {
                System.out.println("You can't use the boltcutters on that.");
            }
        } else if ("STOCK".equalsIgnoreCase(itemName)) {
            if ("GUARD".equalsIgnoreCase(targetName)) {
                System.out.println("You swing the stock at the guard. It connects with a dull thud!");
            } else if ("DOOR".equalsIgnoreCase(targetName)) {
                System.out.println("You hit the door with the stock. It shudders but remains closed.");
            } else {
                System.out.println("You wave the stock around menacingly, but nothing happens.");
            }
        } else {
            System.out.println("Nothing interesting happens.");
        }
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
            case "baby oil":
                hasBabyOil = true;
                System.out.println("You take out the baby oil. It might come in handy...");
                break;
            default:
                System.out.println("You can't find a use for the " + item.getName() + " right now.");
                player.addItem(item); // Return the item if it can't be used
                return;
        }
    }

    private void playWhitePartyGame() {
        System.out.println("\nP Diddy challenges you to a drinking game!");

        if (hasBabyOil) {
            try {
                String babyOilArt = Files.readString(Paths.get(BABY_OIL_FILE));
                System.out.println("\nYou present P Diddy with the baby oil...");
                System.out.println(babyOilArt);
                System.out.println("\nP Diddy is impressed! He lets you pass without playing the game.");
                isDiddyPresent = false;
                return;
            } catch (IOException e) {
                System.out.println("You try to show P Diddy the baby oil, but something goes wrong!");
            }
        }

        System.out.println("You need to guess the number of champagne bottles (1-10) to win.");
        System.out.println("If you lose, you'll be caught!");

        int correctNumber = random.nextInt(10) + 1;
        System.out.print("How many bottles do you think there are? (1-10): ");

        try {
            int guess = Integer.parseInt(scanner.nextLine());
            if (guess == correctNumber) {
                System.out.println("You won! P Diddy is impressed and lets you pass.");
                isDiddyPresent = false;
            } else {
                System.out.println("Wrong guess! The correct number was " + correctNumber);
                showJumpscare();
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! P Diddy catches you trying to cheat!");
            showJumpscare();
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }
}
