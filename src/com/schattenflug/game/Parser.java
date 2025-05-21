package com.schattenflug.game;

public class Parser {

    public Parser() {
        // Constructor, can be used to initialize known command words if needed
    }

    /**
     * Parses the player input into a command word and optional arguments. For
     * simplicity, this basic parser just splits by space and takes the first
     * few parts. A more advanced parser would handle more complex sentences,
     * synonyms, etc.
     *
     * @param inputLine The full line of text typed by the player.
     * @return A String array where the first element is the command, and
     * subsequent elements are arguments/subjects/objects. Returns an array with
     * an empty string if input is null or empty, to avoid null pointers.
     */
    public String[] parse(String inputLine) {
        if (inputLine == null || inputLine.trim().isEmpty()) {
            return new String[]{""}; // Return empty command to avoid null pointer in Game
        }
        String[] words = inputLine.trim().toUpperCase().split("\\s+");

        // Basic parsing: command (word1), subject (word2), object (word3/4, e.g. for USE ITEM ON TARGET)
        // For now, let's just return up to 3 parts: COMMAND, SUBJECT, OBJECT/TARGET
        // More sophisticated parsing might be needed for "USE X ON Y"
        if (words.length == 0) {
            return new String[]{""};
        }
        if (words.length == 1) {
            return new String[]{words[0]};
        }
        if (words.length == 2) {
            return new String[]{words[0], words[1]};
        }
        // If 3 or more words, assume COMMAND SUBJECT OBJECT (e.g. USE KEY DOOR)
        // Or for "USE KEY ON DOOR", we need to decide how to handle "ON"
        // For now, let's assume simple three-part: USE KEY DOOR
        // Or let Game.processCommand handle the preposition logic.
        if (words.length >= 3) {
            // If the third word is a common preposition like "ON", "AT", "TO", "WITH"
            // and there's a fourth word, we might want to combine or handle it specially.
            // For now, let's just take the first three significant words if "ON" is present as word 3.
            // Example: "USE KEY ON DOOR" -> words = ["USE", "KEY", "ON", "DOOR"]
            // We want: command="USE", subject="KEY", object="DOOR"
            if (words.length > 3 && (words[2].equals("ON") || words[2].equals("AT") || words[2].equals("TO") || words[2].equals("WITH"))) {
                // Combine words after preposition if needed or handle specific structures
                // For now, let's assume the object is the word after "ON"
                return new String[]{words[0], words[1], words[3]}; // USE, ITEM, TARGET
            } else {
                return new String[]{words[0], words[1], words[2]}; // Default for 3 words
            }
        }
        // Should not be reached given above logic, but as a fallback:
        return words; // Return all words if more than 3 and no specific preposition handling
    }

    // Future: Method to get all known command words (for help text or validation)
    // public List<String> getValidCommands() { ... }
}
