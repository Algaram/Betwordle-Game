import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
//note for matt: I was allowed to import more stuff right?

public class GameLogic{
   
      
   //Name of file containing all the possible "secret words"
   private static final String SECRET_WORDS_FNAME = "secret_words.txt";   
   
   //Name of the dictinoary file (containing all valid guesses)
   private static final String VALID_GUESSES_FNAME = "dictionary.txt";   
   
   
   //Dimensions of the game grid in the game window
   public static final int MAX_ROWS = 5;
   public static final int MAX_COLS = 6;
   
   //Character codes for the enter and backspace key press
   public static final char KEY_ENTER = KeyEvent.VK_ENTER; //(numeric value is: 10)
   public static final char KEY_BACKSPACE = KeyEvent.VK_BACK_SPACE; //(numeric value is: 8)
   
   //The null character value (used to represent an "empty" value for a spot on the game board)
   public static final char NULL_CHAR = 0;
   
   //Various Color Values
   private static final Color CLR_GREEN = new Color(53, 209, 42); //(Green... right letter right place)
   private static final Color CLR_YELLOW = new Color(235, 216, 52); //(Yellow... right letter wrong place)
   private static final Color CLR_DGRAY = Color.DARK_GRAY; //(Dark Gray)
   private static final Color CLR_BLACK = Color.BLACK; //(Black) default cell color
   
   //ints representing different potential arrow directions (drawn at the end of a row on the game board)
   public static final int ARROW_LEFT = -1;
   public static final int ARROW_RIGHT = 1;
   public static final int ARROW_BLANK = 0;
   
   //A preset, hard-coded secret word to be use when the resepective debug is enabled
   private static final char[] HARDCODED_SECRET = {'R', 'A', 'I', 'D', 'E', 'R'};     
   
   // ...Feel free to add more **PRIMITIVE FINAL** variables of your own!

   private static final Random getRandom = new Random();

   
   
   //******************   NON-FINAL GLOBAL VARIABLES   ******************
   //********  YOU CANNOT ADD ANY ADDITIONAL NON-FINAL GLOBALS!  ******** 
   
   
   //Array storing all valid guesses read out of the respective file
   private static String[] valids;
   
   //The active row/col where the user left off typing
   private static int userRow = 0, userCol = 0;
      
   //The indices out of the dictionary where the current "leftmost" and 
   //"rightmost" (ie alphabetically earliest and latest, respetively) occur
   private static int leftWordIdx = 0, rightWordIdx;


   
   //*******************************************************************
   
   private static int arrDirection = ARROW_BLANK;
   
   
   //Short for "initilaize".  This function gets called ONCE when the game is  
   //very first launched before the user has the opportunity to do anything.
   //
   //Should perform any initialization that needs to happen at the start of the game,
   //and return the randomly chosen "secret word" as a char array
   //
   //If either of the valid guess or secret words files cannot be read, or are
   //missing the word count in the first line) this program terminates with System.exit(1)
   public static char[] initGame(){
      
      // implement dictionary here! PLEASE REMEMBER DUE THIS WEEKEND ALREADY EXTENDED

      File secretFile = new File(SECRET_WORDS_FNAME);
      File validWordFile = new File(VALID_GUESSES_FNAME);
      String[] secretWords;
      try{
          Scanner scan = new Scanner(secretFile);
          int num = Integer.parseInt(scan.next());
          secretWords = buildArray(num, scan);

          Scanner scan1 = new Scanner(validWordFile);
          int num1 = Integer.parseInt(scan1.next());
          String[] validWords = buildArray(num1, scan1);
          valids = validWords;
          rightWordIdx = valids.length - 1;
      }
      catch (FileNotFoundException error){
          System.exit(1);
          return null;
      }
      int randNum = getRandom.nextInt(secretWords.length);
      return buildChar(secretWords[randNum]);    
   }
   

   
   //Complete your warmup task (Section 3.1.1 step 2) here by calling the requisite
   //functions out of GameGUI.
   //This function gets called ONCE after the graphics window has been
   //initialized and init has been called.
   public static void warmup(){
     
     //All of your warmup code will go in here except for the
     //"wiggle" task (3.1.1 step 3)... where will that go?

     /*

     GameGUI.setBoardChar(0, 0, 'C');
     GameGUI.setBoardColor(0, 0, CLR_YELLOW);
     GameGUI.setBoardChar(1, 3, 'O');
     GameGUI.setArrow(1, ARROW_LEFT);
     GameGUI.setArrow(2, ARROW_RIGHT);
     GameGUI.setBoardChar(3, 5, 'S');
     GameGUI.setBoardColor(3 , 5, CLR_GREEN);
     GameGUI.setBoardChar(4, 5, 'C');
     GameGUI.setKBColor('C', CLR_GREEN);
     GameGUI.setKBColor('U', CLR_DGRAY);

     */

   }   

   
   //This function gets called everytime the user types a valid key on the
   //keyboard (alphabetic character, enter, or backspace) or clicks one of the
   //keys on the graphical keyboard interface.
   //
   //The key pressed is passed in as a char value.
   public static void reactToKeyPress(char key){
     
      if (key == KEY_BACKSPACE) {
         ifBackspacePressed();
         return;
      }

      if (key == KEY_ENTER) {
         ifEnterPressed();
         return;
      }

      if (Character.isLetter(key) && userRow < MAX_COLS) {
         checkIfLetter(key);
      }
   }

   //helper functions below

   //backspace key chekcer
   private static void ifBackspacePressed() {
      if (userRow > 0) {
         userRow--;
         GameGUI.setBoardChar(userCol, userRow, NULL_CHAR);
      }
   }

   //enter key checker
   private static void ifEnterPressed() {
      if (userRow < MAX_COLS) { // if row is not full wiggle
         GameGUI.wiggleRow(userCol);
         return;
      }

      String guess = buildGuessString();
      char[] secret = GameGUI.getSecretAsArr();

   
      int guessIndex = getWordIndex(guess);
      
      // check if valid word
      if (guessIndex == -1) {
         GameGUI.wiggleRow(userCol);
         return;
      }
      
      // checks if satisfy direction hint
      if (userCol > 0) { // ignores the first row of guessing
         // right
         if (GameGUI.getArrowDirection(userCol-1) == ARROW_RIGHT && guessIndex <= leftWordIdx) {
            GameGUI.wiggleRow(userCol);
            return;
         }
         // left
         if (GameGUI.getArrowDirection(userCol-1) == ARROW_LEFT && guessIndex >= rightWordIdx) {
            GameGUI.wiggleRow(userCol);
            return;
         }
      }
      
      // if all is valid set new arrow 
      setArrow(guessIndex);

      if (checkGuessandColor(guess, secret)) {
         GameGUI.triggerGameOver(true);
         return;
      }

      if (userCol == MAX_ROWS - 1) {
         GameGUI.triggerGameOver(false);
         return;
      }

      userCol++;
      userRow = 0;
   }

   public static String[] buildArray(int num, Scanner scan){
      String[] arr = new String[num];
      for(int i = 0; i < num; i++){
          arr[i] = scan.next();
      }
      return arr;
   }

   public static char[] buildChar(String word){
      int len = word.length();
      char[] arr = new char[len];
      for(int i = 0; i < len; i++){
          arr[i] = Character.toUpperCase(word.charAt(i));
      }
      return arr;
   }

   //string guess to check
   private static String buildGuessString() {
      String guess = "";
      for (int i = 0; i < MAX_COLS; i++) {
         guess += GameGUI.getBoardChar(userCol, i);
      }
      return guess;
   }

   //marking cloors
   private static boolean checkGuessandColor(String guess, char[] secret) {
      boolean isCorrect = true;

      int[] secretLetterCount = new int[26];
      for (int i = 0; i < secret.length; i++) {
         secretLetterCount[secret[i] - 'A']++;
      }

      //correct letters
      for (int i = 0; i < MAX_COLS; i++) {
         if (guess.charAt(i) == secret[i]) {
               GameGUI.setBoardColor(userCol, i, CLR_GREEN); // color on board
               GameGUI.setKBColor(guess.charAt(i), CLR_GREEN); // color on kyboard
               secretLetterCount[guess.charAt(i) - 'A']--;
         } 
         else {
               isCorrect = false;
         }
      }

      //incorrect letters or wrong spots
      for (int i = 0; i < MAX_COLS; i++) {
         if (GameGUI.getBoardColor(userCol, i) != CLR_GREEN) {
               char c = guess.charAt(i);
               if (secretLetterCount[c - 'A'] > 0) {
                  GameGUI.setBoardColor(userCol, i, CLR_YELLOW);
                  if (GameGUI.getKBColor(c) != CLR_GREEN) {
                     GameGUI.setKBColor(c, CLR_YELLOW);
                  }
                  secretLetterCount[c - 'A']--;
               } 
               else {
                  GameGUI.setBoardColor(userCol, i, CLR_DGRAY);
                  if (GameGUI.getKBColor(c) != CLR_GREEN && GameGUI.getKBColor(c) != CLR_YELLOW) {
                     GameGUI.setKBColor(c, CLR_DGRAY);
                  }
               }
         }
      }
      
      
      GameGUI.setArrow(userCol, arrDirection);
      return isCorrect;
   }

   private static void checkIfLetter(char key) {
      GameGUI.setBoardChar(userCol, userRow, Character.toUpperCase(key));
      userRow++;
   }

   
   private static void setArrow(int guessNum) {
      int secretNum = getWordIndex(GameGUI.getSecretAsStr());
      
      // checks if is word if it is no arrow
      if (guessNum == secretNum) {
          arrDirection = ARROW_BLANK;
          return;
      }
      
      // checks if the guess is alphabeticaly before the word
      if (guessNum < secretNum) {
          // update index if closer to word
          if (guessNum > leftWordIdx) {
              leftWordIdx = guessNum;
          }
          arrDirection = ARROW_RIGHT;
      }
      // checks if the guess is alphabetically after the word
      else if (guessNum > secretNum) {
          // update index if closer to word
          if (guessNum < rightWordIdx) {
              rightWordIdx = guessNum;
          }
          arrDirection = ARROW_LEFT;
      }
   }

   private static int getWordIndex(String word) {
        word = word.toLowerCase();
        for (int i = 0; i < valids.length; i++) {
            if (word.equalsIgnoreCase(valids[i])) {
                return i;
            }
        }
        return -1;
   }

}