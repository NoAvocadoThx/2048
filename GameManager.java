/*
 *  Name:    Qi Zhao
 *  Login:   cs8bms
 *  Date:    26/4/2015
 *  File:    GameManager.java
 *  Source of help: Java API
 *
 *  This file contains the main loop of the 2048 game. And it also perform
 *  board, print control.
 */
 
import java.util.*;
import java.io.*;

/*
 *  Name: GameManager
 *  Purpose: Manage the 2048 game. Deal with the user input, print board, score
 *  or control
 */
public class GameManager
{
    // Instance variables
    private Board board;    // The actual 2048 board
    private String outputFileName;  // File to save the board to when exiting
    
    /*
    *  Name: GameManager
    *  Purpose: Construct a GameManager object
    *  Parameter: int boardsize - the size of the board used in the game
    *             String outputBoard - the name of the outputfile used to save
    *             board
    *             Random random - the Random object used to add tiles
    */
    GameManager(int boardSize, String outputBoard, Random random)
    {
        board = new Board(boardSize,random);
        outputFileName = outputBoard;
    }

    /*
    *  Name: GameManager
    *  Purpose: Construct a GameManager object
    *  Parameter: String inputBoard - the name of the file which has saved game
    *             String outputBoard - the name of the outputfile used to save
    *             board
    *             Random radom - the Random object used to add tiles
    */
    GameManager(String inputBoard, String outputBoard, Random random)
    throws IOException
    {
        board = new Board(inputBoard,random);
        outputFileName = outputBoard;    
    }

    /* Name: play
    *  Purpose: Takes input command from the user to specify moves to execute
    */
    public void play() throws IOException
    {
        Scanner input = new Scanner(System.in);
        String dir = "";
        Direction mydirection = Direction.UP;
        boolean checkCommand = false;

        // print controls
        printControls();   
        // print current state
        System.out.println(board.toString());

        boolean gameover = board.isGameOver();
       // if game is over at the begining, print "Game Over", and save board
        if (gameover)
        {
            System.out.println("Game Over!");
            board.saveBoard(outputFileName);
        }    
        // prompt the user to insert a command when game is not over at the
        // begining
        else 
        {
            System.out.print("> ");
            while (input.hasNext())
            {
                dir = input.next();
                if (dir.equals("w"))
                {
                    mydirection = Direction.UP;
                    checkCommand = true;
                }
                else if (dir.equals("s"))
                {
                    mydirection = Direction.DOWN;
                    checkCommand = true;
                }
                else if (dir.equals("a"))
                {
                    mydirection = Direction.LEFT;
                    checkCommand = true;
                }
                else if (dir.equals("d"))
                {
                    mydirection = Direction.RIGHT;
                    checkCommand = true;    
                }
                else if (dir.equals("q"))    
                {
                    board.saveBoard(outputFileName);
                    checkCommand = false;
                    break;
                }
                // if the user inserts an invalid command, print control
                else 
                {
                    printControls();   
                    checkCommand = false;
                }        

                // judge if the the move is valid.
                // if it is, update the board and add a random tile  
                if (board.canMove(mydirection) && checkCommand)
                {
                    board.move(mydirection);
                    board.addRandomTile();
                }

                // print the updated board
                System.out.println(board.toString());
                // check if the game is over
                gameover = board.isGameOver();
                // prompt the user to input next command
                // if the game is not over
                if (!gameover)
                { 
                    System.out.print("> ");
                }
                // if the game is over print "Game Over!"
                else
                {
                    System.out.println("Game Over!");
                    break;
                }
            }
        }          

    }

    /* Name:  printControls
    *  Purpose: print controls for the game
    */
    private void printControls()
    {
        System.out.println("  Controls:");
        System.out.println("    w - Move Up");
        System.out.println("    s - Move Down");
        System.out.println("    a - Move Left");
        System.out.println("    d - Move Right");
        System.out.println("    q - Quit and Save Board");
        System.out.println();
    }
    public static void main(String[] args)
    {
        try
        {
            GameManager mygame = new GameManager("input.board","test.board", new Random(2014)); 
            mygame.play(); 
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }    
}
