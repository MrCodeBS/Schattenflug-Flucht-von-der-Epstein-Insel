package com.schattenflug.game;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class Room {

    private String name; // e.g., BEACH, JUNGLE_PATH
    private String description; // Full rich description
    private Map<String, Room> exits; // e.g., "NORTH" -> jungleRoom
    private List<Item> items;
    // private List<NPC> npcs; // To be added later
    // private Map<String, String> interactables; // For room-specific features not being items

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        this.exits = new HashMap<>();
        this.items = new ArrayList<>();
        // this.npcs = new ArrayList<>();
        // this.interactables = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public String getShortDescription() {
        // Could return just the name or a brief summary if needed later
        return name;
    }

    public String getLongDescription() {
        StringBuilder fullDesc = new StringBuilder();
        fullDesc.append("Location: ").append(name).append("\n");
        fullDesc.append(description).append("\n");

        // Append available exits
        if (!exits.isEmpty()) {
            fullDesc.append("Exits: ");
            for (String direction : exits.keySet()) {
                fullDesc.append(direction.toLowerCase()).append(" ");
            }
            fullDesc.append("\n");
        }

        // Append items in the room
        if (!items.isEmpty()) {
            fullDesc.append("You see here: ");
            for (Item item : items) {
                fullDesc.append(item.getName().toLowerCase()).append(" ");
            }
            fullDesc.append("\n");
        }
        // Append NPCs in the room (when implemented)
        // if (!npcs.isEmpty()) { ... }

        return fullDesc.toString();
    }

    public void setExit(String direction, Room neighbor) {
        exits.put(direction.toUpperCase(), neighbor);
    }

    public Room getExit(String direction) {
        return exits.get(direction.toUpperCase());
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public Item removeItem(String itemName) {
        Item itemToRemove = null;
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                itemToRemove = item;
                break;
            }
        }
        if (itemToRemove != null) {
            items.remove(itemToRemove);
        }
        return itemToRemove;
    }

    public Item getItem(String itemName) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    // Placeholder for interacting with non-item room features
    // public String getInteractableDescription(String featureName) {
    //     return interactables.getOrDefault(featureName.toUpperCase(), "You see nothing special about that.");
    // }
    // public void addInteractable(String featureName, String featureDescription) {
    //     interactables.put(featureName.toUpperCase(), featureDescription);
    // }
}
