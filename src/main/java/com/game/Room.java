package com.game;

import java.util.HashMap;
import java.util.Map;

public class Room {

    private String name;
    private String description;
    private Map<String, Room> exits;
    private Map<String, Item> items;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        this.exits = new HashMap<>();
        this.items = new HashMap<>();
    }

    public void setExit(String direction, Room room) {
        exits.put(direction.toLowerCase(), room);
    }

    public Room getExit(String direction) {
        return exits.get(direction.toLowerCase());
    }

    public void addItem(Item item) {
        items.put(item.getName().toLowerCase(), item);
    }

    public Item removeItem(String itemName) {
        return items.remove(itemName.toLowerCase());
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getExitsDescription() {
        StringBuilder sb = new StringBuilder("Exits: ");
        for (String direction : exits.keySet()) {
            sb.append(direction).append(" ");
        }
        return sb.toString();
    }

    public String getItemsDescription() {
        if (items.isEmpty()) {
            return "There are no items in this room.";
        }
        StringBuilder sb = new StringBuilder("Items in this room: ");
        for (Item item : items.values()) {
            sb.append(item.getName()).append(", ");
        }
        return sb.toString().replaceAll(", $", "");
    }
}
