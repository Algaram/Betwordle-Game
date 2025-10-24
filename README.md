# Betwordle Game

A Java-based word-guessing game that puts a unique twist on the popular Wordle. Betwordle combines classic word-guessing mechanics with alphabetic range constraints to create a more strategic gameplay experience.

## Overview

Betwordle is a portmanteau of "between" and "Wordle" - a variant of the classic word game featuring new rules and objectives. This implementation was developed as a COSC 102 course project to practice Java programming and software design principles.

## Key Differences from Standard Wordle

- **6-letter words** instead of 5-letter words
- **5 attempts** instead of 6 attempts
- **Alphabetic range feedback**: After each guess, arrows indicate if the secret word comes alphabetically before (←) or after (→) your guess
- **Range constraints**: Each subsequent guess must fall within the alphabetic range established by previous arrows

## Features

- **GUI-based gameplay** with graphical keyboard interface
- **Color-coded feedback system**:
  - 🟩 **Green**: Letter is correct and in the right position
  - 🟨 **Yellow**: Letter is in the word but in the wrong position
  - ⬜ **Gray**: Letter is not in the word
- **Directional arrows** showing alphabetic positioning
- **Dictionary validation** using custom word lists
- **Duplicate letter handling** following official Wordle logic
- **Debug toggles** for testing and development
- **Smart keyboard coloring** that prioritizes green over yellow

## How to Play

1. The game selects a random 6-letter word from the secret words file
2. You have 5 attempts to guess the word
3. Type your guess using either:
   - Your physical keyboard
   - The on-screen graphical keyboard
4. After each guess:
   - Letters are colored based on their correctness
   - An arrow (← or →) indicates if the target word comes before or after your guess alphabetically
5. Use the alphabetic range hints to narrow down possibilities
6. All guesses must be valid English words within the established alphabetic range
7. Win by guessing correctly, or lose if you run out of attempts

## Example Gameplay

If the secret word is **RAIDER**:

1. **Guess: JOYFUL** → All gray letters, **→** arrow (RAIDER comes after JOYFUL)
2. **Guess: WISDOM** → D is green, I is yellow, **←** arrow (RAIDER comes before WISDOM)
3. **Guess: THIRDS** → I is green, D and R are yellow, **←** arrow
4. Continue narrowing the range until you guess RAIDER!

## Installation & Setup

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Java IDE (Eclipse, IntelliJ IDEA, or similar) recommended

### Running the Game

```bash
# Clone the repository
git clone https://github.com/Algaram/Betwordle-Game.git

# Navigate to the project directory
cd Betwordle-Game

# Compile the Java files
javac BetwordleLauncher.java GameLogic.java GameGUI.java

# Run the game
java BetwordleLauncher
```
## Technical Details

- **Language**: Java
- **GUI Framework**: Custom Swing-based interface
- **Design Pattern**: Separation of game logic from GUI rendering
- **Memory Efficiency**: Uses `char` arrays over `String` objects where possible

## Game Rules Summary

✅ **Valid Guesses**:
- Must be exactly 6 letters long
- Must be a real English word from the dictionary
- Must fall within the alphabetic range established by previous arrows

❌ **Invalid Guesses**:
- Non-existent words
- Words outside the established alphabetic range
- Incomplete words (less than 6 letters)

## Acknowledgments

- Original Wordle game by Josh Wardle
- Course project for COSC 102 - Spring 2025
- GUI framework provided by course instructors

## Contributing

This is an academic project. If you're working on a similar assignment, please use this as reference only and follow your institution's academic integrity policies.

---

**Have fun playing Betwordle!** 🎮
