package com.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {

    private String name;
    private String description;
    private Map<String, Room> exits;
    private List<Item> items;
    private List<Item> hiddenItems;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        this.exits = new HashMap<>();
        this.items = new ArrayList<>();
        this.hiddenItems = new ArrayList<>();
    }

    public void setExit(String direction, Room room) {
        exits.put(direction, room);
    }

    public Room getExit(String direction) {
        return exits.get(direction);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void addHiddenItem(Item item) {
        hiddenItems.add(item);
    }

    public void revealHiddenItems() {
        items.addAll(hiddenItems);
        hiddenItems.clear();
    }

    public Item removeItem(String itemName) {
        // First check visible items
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item.getName().toLowerCase().equals(itemName.toLowerCase())) {
                return items.remove(i);
            }
        }
        // Then check hidden items
        for (int i = 0; i < hiddenItems.size(); i++) {
            Item item = hiddenItems.get(i);
            if (item.getName().toLowerCase().equals(itemName.toLowerCase())) {
                return hiddenItems.remove(i);
            }
        }
        return null;
    }

    public String getExitsDescription() {
        StringBuilder sb = new StringBuilder("Exits: ");
        if (exits.isEmpty()) {
            return "There are no visible exits.";
        }
        sb.append(String.join(", ", exits.keySet()));
        return sb.toString();
    }

    public String getItemsDescription() {
        if (items.isEmpty()) {
            return "There are no items here.";
        }
        StringBuilder sb = new StringBuilder("Items in this room:\n");
        for (Item item : items) {
            sb.append("- ").append(item.toString()).append("\n");
        }
        return sb.toString();
    }
}
