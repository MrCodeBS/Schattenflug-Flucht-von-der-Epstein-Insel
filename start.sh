#!/bin/bash

# Compile the game
mvn clean package
 
# Run the game
java -cp target/classes com.game.Game 