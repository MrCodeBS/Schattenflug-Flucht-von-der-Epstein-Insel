package com.schattenflug.game;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private Room currentRoom;
    private List<Item> inventory;
    private int maxInventorySize = 5; // Example inventory limit

    public Player() {
        this.inventory = new ArrayList<>();
        // currentRoom will be set by the Game class after world creation
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public boolean addItem(Item item) {
        if (inventory.size() < maxInventorySize) {
            inventory.add(item);
            return true;
        } else {
            // Optionally print a message here, or let Game class handle it
            // System.out.println("Your inventory is full.");
            return false;
        }
    }

    public Item removeItem(String itemName) {
        Item itemToRemove = null;
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                itemToRemove = item;
                break;
            }
        }
        if (itemToRemove != null) {
            inventory.remove(itemToRemove);
        }
        return itemToRemove;
    }

    public Item getItem(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    public void printInventory() {
        if (inventory.isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.println("You are carrying:");
            for (Item item : inventory) {
                System.out.println("- " + item.getName().toLowerCase() + " (" + item.getDescription() + ")");
            }
        }
    }
}
