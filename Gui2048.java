/*
 * Name:    Qi Zhao
 * Login:   cs8bms
 * Date:    5/27/2015
 * Source of help:  Java API
 *
 * This file creats the GUI for the game 2048.
 */

import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;

/*
 * Name:    Gui2048
 * Purpose: Provide GUI application for game 2048
 */
public class Gui2048 extends Application
{
    private String outputBoard; // The filename for where to save the Board
    private Board board; // The 2048 Game Board

    // Fill colors for each of the Tile values
    private static final Color COLOR_EMPTY = Color.rgb(238, 228, 218, 0.35);
    private static final Color COLOR_2 = Color.rgb(238, 228, 218);
    private static final Color COLOR_4 = Color.rgb(237, 224, 200);
    private static final Color COLOR_8 = Color.rgb(242, 177, 121);
    private static final Color COLOR_16 = Color.rgb(245, 149, 99);
    private static final Color COLOR_32 = Color.rgb(246, 124, 95);
    private static final Color COLOR_64 = Color.rgb(246, 94, 59);
    private static final Color COLOR_128 = Color.rgb(237, 207, 114);
    private static final Color COLOR_256 = Color.rgb(237, 204, 97);
    private static final Color COLOR_512 = Color.rgb(237, 200, 80);
    private static final Color COLOR_1024 = Color.rgb(237, 197, 63);
    private static final Color COLOR_2048 = Color.rgb(237, 194, 46);
    private static final Color COLOR_OTHER = Color.BLACK;
    private static final Color COLOR_GAME_OVER = Color.rgb(238, 228, 218, 0.73);
    private static final Color COLOR_TITLE = Color.rgb(119, 110, 101);
    

    private static final Color COLOR_VALUE_LIGHT = Color.rgb(249, 246, 242); // For tiles >= 8
    private static final Color COLOR_VALUE_DARK = Color.rgb(119, 110, 101); // For tiles < 8

    private final int NUMBER_2 = 2;
    private final int NUMBER_12 = 12;
    private final int NUMBER_420 = 420;
    private final int NUMBER_4 = 4;
    private final int NUMBER_8 = 8;
    private final int NUMBER_16 = 16;
    private final int NUMBER_32 = 32;
    private final int NUMBER_64 = 64;
    private final int NUMBER_128 = 128;
    private final int NUMBER_256 = 256;
    private final int NUMBER_512 = 512;
    private final int NUMBER_1024 = 1024;
    private final int NUMBER_2048 = 2048;
    private final int NUMBER_187 = 187;
    private final int NUMBER_173 = 173;
    private final int NUMBER_160 = 160;
    private final int NUMBER_70 = 70;
    private final int NUMBER_60 = 60;
    private final int NUMBER_50 = 50;
    private final int NUMBER_40 = 40;
    private final int NUMBER_30 = 30;
    private int score;
    private int[][] grid;
    private int gridsize;
    private int tilesize;
    private GridPane gridpane;
    private Scene scene;
    private Text text1;
    private Text text2;
    private Text[][] tilevalue;
    private Rectangle[][] tile;
    private Stage mystage;


    /*
    *   Name:   start
    *   Purpose:    construct the basic gridpane, stage
    *               place GUI controls
    *   Parameter:  Stage primaryStage - the stage used in GUI
    */
    @Override
    public void start(Stage primaryStage)
    {

        // Process Arguments and Initialize the Game Board
        processArgs(getParameters().getRaw().toArray(new String[0]));
        mystage = primaryStage;
        
        gridpane = new GridPane();
        gridpane.setPadding(new Insets(NUMBER_12, NUMBER_12, 
                                       NUMBER_12, NUMBER_12));
        gridpane.setHgap(NUMBER_12);
        gridpane.setVgap(NUMBER_12);
        // set background color
        gridpane.setStyle("-fx-background-color: rgb(187,173,160)");

        // get the grid 
        grid =  board.getGrid(); 
        gridsize = grid.length;
        tilesize = NUMBER_420 / gridsize;
        // use to save number of each tile
        tilevalue = new Text[gridsize][gridsize];
        // use to save rectangle object 
        tile = new Rectangle[gridsize][gridsize];


        text1 = new Text("2048");
        text1.setFont(Font.font("Helvetica", FontWeight.BOLD,
                    FontPosture.ITALIC, NUMBER_40));
        text1.setFill(COLOR_TITLE);
        

        text2 = new Text("Score: " + score);
        text2.setFont(Font.font("Helvetica", FontWeight.BOLD, NUMBER_30));
        text2.setFill(COLOR_TITLE);

        gridpane.add(text1, 0, 0, NUMBER_2,  1);
        gridpane.setHalignment(text1, HPos.CENTER);
        gridpane.add(text2, NUMBER_2, 0, NUMBER_2, 1);
        //gridpane.setGridLinesVisible(true);

        // implement each tile accroding to the value of the tile
        for (int row = 0; row < gridsize; row++)
        {
            for (int col = 0; col < gridsize; col++)
            {
                Rectangle r = new Rectangle(tilesize, tilesize);
                Text number = new Text();
                number.setFont(Font.font("Times New Roman", 
                            FontWeight.BOLD, 60));
                // save value of tile and tile 
                tilevalue[row][col] = number;
                tile[row][col] = r;

                gridpane.add(r, col , row + NUMBER_2);
                GridPane.setHalignment(number, HPos.CENTER);
                gridpane.add(number, col , row + NUMBER_2);

            }
        }

        updateGridpane();
        
        // create a new scene
        scene = new Scene(gridpane);
        // create and register handler
        scene.setOnKeyPressed(new myKeyHandler());
        
        primaryStage.setTitle("Gui2048");
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }

    /*
    *   Name:   updateGridpane
    *   Purpose:    update the gridpane accoding to the board
    */
    private void updateGridpane ()
    {
        // update score
        score = board.getScore();
        text2.setText("Score: " + score);
        
        // update each tile
        for (int row = 0; row < gridsize; row++)
        {
            for (int col = 0; col < gridsize; col++)
            {
                if (grid[row][col] == 0)
                {
                    tilevalue[row][col].setText("");
                    tile[row][col].setFill(COLOR_EMPTY);
                }
                else if (grid[row][col] == NUMBER_2)
                {
                    tilevalue[row][col].setText("2");
                    tilevalue[row][col].setFill(COLOR_VALUE_DARK);
                    
                    tilevalue[row][col].setFont(Font.font("Times New Roman", 
                            FontWeight.BOLD, NUMBER_60));
                    tile[row][col].setFill(COLOR_2);
                }
                else if (grid[row][col] == NUMBER_4)
                {
                    tilevalue[row][col].setText("4");
                    tilevalue[row][col].setFill(COLOR_VALUE_DARK);
                    
                    tilevalue[row][col].setFont(Font.font("Times New Roman", 
                            FontWeight.BOLD, NUMBER_60));
                    tile[row][col].setFill(COLOR_4);
                }
                else if (grid[row][col] == NUMBER_8)
                {
                    tilevalue[row][col].setText("8");
                    tilevalue[row][col].setFill(COLOR_VALUE_LIGHT);
                    
                    tilevalue[row][col].setFont(Font.font("Times New Roman", 
                            FontWeight.BOLD, NUMBER_60));
                    tile[row][col].setFill(COLOR_8);
                }
                else if (grid[row][col] == NUMBER_16)
                {
                    tilevalue[row][col].setText("16");
                      
                    tilevalue[row][col].setFont(Font.font("Times New Roman", 
                            FontWeight.BOLD, NUMBER_50));
                    tilevalue[row][col].setFill(COLOR_VALUE_LIGHT);
                    tile[row][col].setFill(COLOR_16);
                }
                else if (grid[row][col] == NUMBER_32)
                {
                    tilevalue[row][col].setText("32");
                    
                    tilevalue[row][col].setFont(Font.font("Times New Roman", 
                            FontWeight.BOLD, NUMBER_50));
                    tilevalue[row][col].setFill(COLOR_VALUE_LIGHT);
                    tile[row][col].setFill(COLOR_32);
                }  
                else if (grid[row][col] == NUMBER_64)
                {
                    tilevalue[row][col].setText("64");
                    
                    tilevalue[row][col].setFont(Font.font("Times New Roman", 
                            FontWeight.BOLD, NUMBER_50));
                    tilevalue[row][col].setFill(COLOR_VALUE_LIGHT);
                    tile[row][col].setFill(COLOR_64);
                }                    
                else if (grid[row][col] == NUMBER_128)
                {
                    tilevalue[row][col].setText("128");
                    tilevalue[row][col].setFont(Font.font("Times New Roman", 
                            FontWeight.BOLD, NUMBER_40));
                    tilevalue[row][col].setFill(COLOR_VALUE_LIGHT);
                    tile[row][col].setFill(COLOR_128);
                }
                else if (grid[row][col] == NUMBER_256)
                {
                    tilevalue[row][col].setText("256");
                    tilevalue[row][col].setFont(Font.font("Times New Roman", 
                            FontWeight.BOLD, NUMBER_40));
                    tilevalue[row][col].setFill(COLOR_VALUE_LIGHT);
                    tile[row][col].setFill(COLOR_256);
                }
                else if (grid[row][col] == NUMBER_512)
                {
                    tilevalue[row][col].setText("512");
                    
                    tilevalue[row][col].setFont(Font.font("Times New Roman", 
                            FontWeight.BOLD, NUMBER_40));
                    
                    tilevalue[row][col].setFill(COLOR_VALUE_LIGHT);
                    tile[row][col].setFill(COLOR_512);
                }
                else if (grid[row][col] == NUMBER_1024)
                {
                    tilevalue[row][col].setText("1024");
                    
                    tilevalue[row][col].setFont(Font.font("Times New Roman", 
                            FontWeight.BOLD, NUMBER_30));
                    tilevalue[row][col].setFill(COLOR_VALUE_LIGHT);
                    tile[row][col].setFill(COLOR_1024);
                }
                else if (grid[row][col] == NUMBER_2048) 
                {
                    tilevalue[row][col].setText("2048");
                    tilevalue[row][col].setFont(Font.font("Times New Roman", 
                            FontWeight.BOLD, NUMBER_30));
                     
                    tilevalue[row][col].setFill(COLOR_VALUE_LIGHT);
                    tile[row][col].setFill(COLOR_2048);
                }
                else 
                {
                    tilevalue[row][col].setText((
                    new Integer(grid[row][col])).toString());
                    tilevalue[row][col].setFont(Font.font("Times New Roman", 
                            FontWeight.BOLD, NUMBER_30));
                        
                    tilevalue[row][col].setFill(COLOR_VALUE_LIGHT);
                    tile[row][col].setFill(COLOR_OTHER);
                }
            }
        }
    }
    /*
    *   Name:   updateGUI
    *   Purpose:    update the GUI according to user inputs
    *   Parameter:  Direction direction - the direction that user inputs
    */
    private void updateGUI (Direction direction)
    {
        board.move(direction);
        board.addRandomTile();
        // updategridpane
        updateGridpane();
        if (direction == Direction.UP)
        {
            System.out.println("Moving UP");
        }
        else if (direction == Direction.DOWN)
        {
            System.out.println("Moving DOWN");
        }
        else if (direction == Direction.LEFT)
        {
            System.out.println("Moving LEFT");
        }
        else 
        {
            System.out.println("Moving RIGHT");
        }

        // check if the game is over
        if (board.isGameOver())
        {
            // creat overlay
            Rectangle overlay = new Rectangle(mystage.getWidth(),
                                              mystage.getHeight());
            overlay.setFill(COLOR_GAME_OVER);
            Text gameover = new Text("Game Over!");
            gameover.setFont(Font.font("Helvetica", 
                             FontWeight.BOLD, NUMBER_70));
            gameover.setFill(COLOR_VALUE_DARK);
            // place overlay and text of game over
            gridpane.add(overlay, 0, 0, gridsize, gridsize * NUMBER_2);
            gridpane.setHalignment(overlay, HPos.CENTER);
            gridpane.setValignment(overlay, VPos.CENTER);
            gridpane.add(gameover, 0, gridsize / NUMBER_2 - 1, gridsize, gridsize);
        }
    }
                
    /*
    *   Name:   myKeyHandler
    *   Purpose:    innerclass to handle the key press event
    */
    private class myKeyHandler implements EventHandler<KeyEvent>
    {
        /*
        *   Name:   handle
        *   PUrpose:    Process the key press event
        */
        @Override
        public void handle(KeyEvent e)
        {
            boolean gameover = board.isGameOver();
            // update board and GUI according to user input
            if (e.getCode() == KeyCode.UP)
            {
                if (board.canMove(Direction.UP))
                {
                    updateGUI(Direction.UP);
                }
            }
            else if (e.getCode() == KeyCode.DOWN)
            {
                if (board.canMove(Direction.DOWN))
                {
                    updateGUI(Direction.DOWN);
                }
            }
            else if (e.getCode() == KeyCode.LEFT)
            {
                if (board.canMove(Direction.LEFT))
                {
                    updateGUI(Direction.LEFT);
                }
            }
            else if (e.getCode() == KeyCode.RIGHT)
            {
                if (board.canMove(Direction.RIGHT))
                {
                    updateGUI(Direction.RIGHT);
                }
            }
            // save board
            else if (e.getCode() == KeyCode.S)
            {
                try 
                {
                    board.saveBoard(outputBoard);
                    System.out.println("Save Board to " + outputBoard);
                } 
                catch (IOException ex) 
                {
                    System.out.println("saveBoard threw an Exception");
                }
            }
        }
    }
                    
                    
                
            


        /** Add your own Instance Methods Here */








    /** DO NOT EDIT BELOW */

    // The method used to process the command line arguments
    private void processArgs(String[] args)
    {
        String inputBoard = null;   // The filename for where to load the Board
        int boardSize = 0;          // The Size of the Board

        // Arguments must come in pairs
        if((args.length % 2) != 0)
        {
            printUsage();
            System.exit(-1);
        }

        // Process all the arguments 
        for(int i = 0; i < args.length; i += 2)
        {
            if(args[i].equals("-i"))
            {   // We are processing the argument that specifies
                // the input file to be used to set the board
                inputBoard = args[i + 1];
            }
            else if(args[i].equals("-o"))
            {   // We are processing the argument that specifies
                // the output file to be used to save the board
                outputBoard = args[i + 1];
            }
            else if(args[i].equals("-s"))
            {   // We are processing the argument that specifies
                // the size of the Board
                boardSize = Integer.parseInt(args[i + 1]);
            }
            else
            {   // Incorrect Argument 
                printUsage();
                System.exit(-1);
            }
        }

        // Set the default output file if none specified
        if(outputBoard == null)
            outputBoard = "2048.board";
        // Set the default Board size if none specified or less than 2
        if(boardSize < 2)
            boardSize = 4;

        // Initialize the Game Board
        try{
            if(inputBoard != null)
                board = new Board(inputBoard, new Random());
            else
                board = new Board(boardSize, new Random());
        }
        catch (Exception e)
        {
            System.out.println(e.getClass().getName() + " was thrown while creating a " +
                    "Board from file " + inputBoard);
            System.out.println("Either your Board(String, Random) " +
                    "Constructor is broken or the file isn't " +
                    "formated correctly");
            System.exit(-1);
        }
    }

    // Print the Usage Message 
    private static void printUsage()
    {
        System.out.println("Gui2048");
        System.out.println("Usage:  Gui2048 [-i|o file ...]");
        System.out.println();
        System.out.println("  Command line arguments come in pairs of the form: <command> <argument>");
        System.out.println();
        System.out.println("  -i [file]  -> Specifies a 2048 board that should be loaded");
        System.out.println();
        System.out.println("  -o [file]  -> Specifies a file that should be used to save the 2048 board");
        System.out.println("                If none specified then the default \"2048.board\" file will be used");
        System.out.println("  -s [size]  -> Specifies the size of the 2048 board if an input file hasn't been");
        System.out.println("                specified.  If both -s and -i are used, then the size of the board");
        System.out.println("                will be determined by the input file. The default size is 4.");
    }
}
