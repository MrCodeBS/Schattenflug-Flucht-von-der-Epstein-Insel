# Schattenflug: Flucht von der Epstein-Insel

A text-based adventure game where you must escape from Epstein Island by collecting evidence and finding your way to the helipad.

## Game Description

You find yourself stranded on Epstein Island. Your mission is to collect evidence and escape without being caught. Navigate through various locations, collect items, and solve puzzles to make your way to the helipad.

## Features

- Text-based adventure game with a rich narrative
- Multiple interconnected rooms to explore
- Collectible items with different properties
- Inventory system with weight management
- Interactive puzzles and challenges
- Clear winning condition

## Room Layout

The game features the following locations:
- Strand (Starting point)
- Dschungelpfad (Jungle Path)
- Personalquartiere (Staff Quarters)
- Wartungsschuppen (Maintenance Shed)
- Hauptvilla (Main Villa)
- Sicherheitszentrale (Security Center)
- Helipad-Pfad (Path to Helipad)
- Helipad (Escape point)

## Available Items

- Stock (Stick) - A sturdy wooden stick
- Bolzenschneider (Bolt Cutters) - Heavy-duty bolt cutters
- Verkleidungskit (Disguise Kit) - A kit with various disguise items
- Sicherheitskarte (Security Card) - Access card for restricted areas
- Laptop - A laptop computer containing evidence
- Festplatte (Hard Drive) - External hard drive with important data

## How to Play

### Basic Commands
- Movement:
  - `north` or `n` - Move north
  - `south` or `s` - Move south
  - `east` or `e` - Move east
  - `west` or `w` - Move west
- `take <item>` - Pick up an item
- `drop <item>` - Drop an item
- `use <item>` - Use an item
- `inventory` - View your inventory
- `look` - Look around the current room
- `help` - Show available commands
- `quit` - Exit the game

### Game Mechanics
- Each item has a weight value
- Your inventory has a maximum weight limit
- Some items are required to progress
- Using certain items will consume them

## Installation

1. Ensure you have Java 17 or later installed
2. Clone this repository
3. Navigate to the project directory
4. Run the game using the start script:
   ```bash
   ./start.sh
   ```

## Development

This game is built using:
- Java 17
- Maven for dependency management
- JUnit for testing

### Running Tests
```bash
mvn test
```

## Project Structure

```
src/
├── main/java/com/game/
│   ├── Game.java       # Main game logic
│   ├── Room.java       # Room implementation
│   ├── Item.java       # Item implementation
│   └── Player.java     # Player and inventory management
└── test/java/com/game/
    └── GameTest.java   # Game tests
```

## Contributing

Feel free to submit issues and enhancement requests!

## License

This project is licensed under the MIT License - see the LICENSE file for details. 