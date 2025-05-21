package com.schattenflug.game;

public class Item {

    private String name; // e.g., KEY, LANTERN
    private String description;
    private boolean canBeTaken; // Can the player pick this item up?
    // Add more properties as needed: isUsable, isWeapon, etc.

    public Item(String name, String description, boolean canBeTaken) {
        this.name = name.toUpperCase(); // Store names in uppercase for easier comparison
        this.description = description;
        this.canBeTaken = canBeTaken;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean canBeTaken() {
        return canBeTaken;
    }

    // Overriding equals and hashCode for proper functioning in collections if needed
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return name.equals(item.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
