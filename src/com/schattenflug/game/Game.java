package com.schattenflug.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Game {

    private final Player player;
    private Room currentRoom;
    private final Parser parser;
    private Map<String, Room> allRooms;

    public Game() {
        player = new Player();
        parser = new Parser();
        allRooms = new HashMap<>();
        createWorld();

        this.currentRoom = allRooms.get("BEACH");

        if (this.currentRoom != null) {
            player.setCurrentRoom(this.currentRoom);
        } else {
            System.out.println("Error: Starting room 'BEACH' not found in allRooms. Creating a default temporary room.");
            this.currentRoom = new Room("DEFAULT_START_ROOM", "You are in a non-descript, empty space. Something went wrong with world creation.");
            player.setCurrentRoom(this.currentRoom);
        }
    }

    private void createWorld() {
        Room beach = new Room("BEACH",
                "The pale moonlight casts long, dancing shadows from the swaying palm trees. The sand, usually golden, appears a ghostly white, littered with damp seaweed and the occasional broken shell. Far out, the dark, choppy waves of the ocean glitter ominously under the night sky.\nSounds: The rhythmic crash of waves against the shore is a constant, punctuated by the rustling of palm fronds in the salty breeze and the distant, unsettling cry of a night bird.\nSmells: The air is thick with the smell of salt, damp sand, and decaying marine life.");
        Room junglePath = new Room("JUNGLE_PATH",
                "The jungle canopy is so dense that only slivers of moonlight penetrate, leaving the path shrouded in deep, oppressive darkness. Twisted vines hang like snares, and unfamiliar, broad-leafed plants brush against you as you move.\nSounds: A cacophony of unseen creatures – insects, frogs, and the rustle of something in the undergrowth.\nSmells: Damp earth, decaying leaves, and a sweet, cloying floral fragrance.");
        Room villaExterior = new Room("VILLA_EXTERIOR",
                "The villa looms before you, a stark silhouette against the bruised twilight sky. Some windows are dark, others emit a faint, flickering light. The gardens are overgrown.\nSounds: A low hum (generator?), occasional crunch of gravel, muffled voices.\nSmells: Salt, damp jungle, and a faint acrid chemical smell.");

        Item driftwoodStock = new Item("STOCK", "A sturdy piece of driftwood, surprisingly well-balanced. One end is splintered.", true);
        Item boltCutters = new Item("BOLTCUTTERS", "Heavy-duty bolt cutters. The jaws are sharp and powerful.", true);
        Item laptop = new Item("LAPTOP", "A sleek, modern laptop, surprisingly intact. The screen is dark.", true);

        beach.addItem(driftwoodStock);
        junglePath.addItem(boltCutters);
        villaExterior.addItem(laptop);

        allRooms.put(beach.getName().toUpperCase(), beach);
        allRooms.put(junglePath.getName().toUpperCase(), junglePath);
        allRooms.put(villaExterior.getName().toUpperCase(), villaExterior);

        beach.setExit("NORTH", junglePath);
        junglePath.setExit("SOUTH", beach);
        junglePath.setExit("EAST", villaExterior);
        villaExterior.setExit("WEST", junglePath);
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

    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }
}
