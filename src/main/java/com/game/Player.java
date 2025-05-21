package com.game;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private List<Item> inventory;
    private double maxWeight;

    public Player() {
        this.inventory = new ArrayList<>();
        this.maxWeight = 10.0; // Maximum weight the player can carry
    }

    public boolean addItem(Item item) {
        if (getCurrentWeight() + item.getWeight() <= maxWeight) {
            inventory.add(item);
            return true;
        }
        return false;
    }

    public Item removeItem(String itemName) {
        for (int i = 0; i < inventory.size(); i++) {
            Item item = inventory.get(i);
            if (item.getName().toLowerCase().equals(itemName.toLowerCase())) {
                return inventory.remove(i);
            }
        }
        return null;
    }

    public Item getItem(String itemName) {
        for (Item item : inventory) {
            if (item.getName().toLowerCase().equals(itemName.toLowerCase())) {
                return item;
            }
        }
        return null;
    }

    public double getCurrentWeight() {
        return inventory.stream()
                .mapToDouble(Item::getWeight)
                .sum();
    }

    public String getInventoryDescription() {
        if (inventory.isEmpty()) {
            return "Your inventory is empty.";
        }
        StringBuilder sb = new StringBuilder("Inventory:\n");
        for (Item item : inventory) {
            sb.append("- ").append(item.toString()).append("\n");
        }
        sb.append(String.format("Current weight: %.1f/%.1f", getCurrentWeight(), maxWeight));
        return sb.toString();
    }

    public boolean hasItem(String itemName) {
        return getItem(itemName) != null;
    }
}
