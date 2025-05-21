package com.game;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameTest {

    private Game game;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private ByteArrayInputStream inputStream;

    @BeforeEach
    void setUp() {
        game = new Game();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (Exception e) {
                // Ignore close errors
            }
        }
    }

    @Test
    void testGamePlaythrough() {
        // Simulate game commands
        String[] commands = {
            "look", // Look around starting room
            "take stock", // Take the stick
            "inventory", // Check inventory
            "north", // Move to Dschungelpfad
            "east", // Move to Personalquartiere
            "take verkleidungskit", // Take disguise kit
            "north", // Move to Hauptvilla
            "take laptop", // Take laptop
            "take festplatte", // Take hard drive
            "east", // Move to Sicherheitszentrale
            "take sicherheitskarte", // Take security card
            "use sicherheitskarte", // Use security card
            "north", // Move to Helipad-Pfad
            "north", // Move to Helipad
            "inventory", // Check final inventory
            "quit" // End game
        };

        // Create input stream with commands
        String input = String.join("\n", commands);
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        // Run the game
        game.play();

        // Get the output
        String output = outputStream.toString();

        // Verify key game elements
        assertTrue(output.contains("Welcome to Schattenflug"));
        assertTrue(output.contains("You picked up the Stock"));
        assertTrue(output.contains("You picked up the Verkleidungskit"));
        assertTrue(output.contains("You picked up the Laptop"));
        assertTrue(output.contains("You picked up the Festplatte"));
        assertTrue(output.contains("You picked up the Sicherheitskarte"));
        assertTrue(output.contains("You used the Sicherheitskarte"));
        assertTrue(output.contains("Helipad")); // Should reach the final room
        assertTrue(output.contains("Thanks for playing"));
    }

    @Test
    void testInvalidCommands() {
        String[] commands = {
            "invalidcommand",
            "take nonexistentitem",
            "use nonexistentitem",
            "quit"
        };

        String input = String.join("\n", commands);
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        game.play();

        String output = outputStream.toString();
        assertTrue(output.contains("I don't understand that command"));
        assertTrue(output.contains("There is no nonexistentitem here"));
        assertTrue(output.contains("You don't have a nonexistentitem"));
    }

    @Test
    void testWeightLimit() {
        String[] commands = {
            "take stock",
            "north",
            "west",
            "take bolzenschneider", // Should be too heavy
            "inventory",
            "quit"
        };

        String input = String.join("\n", commands);
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        game.play();

        String output = outputStream.toString();
        assertTrue(output.contains("You can't carry that much weight"));
    }
}
