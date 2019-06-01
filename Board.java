/*
 * Name:    Qi Zhao
 * Login:   cs8bms
 * Date:    23/4/2015
 * File:    Board.java
 * Source of help:  Java API
 *
 * This file constructs the grid of game 2048. It updates the board according to
 * the user input. 
 */


/**     Sample Board
 *
 *      0   1   2   3
 *  0   -   -   -   -
 *  1   -   -   -   -
 *  2   -   -   -   -
 *  3   -   -   -   -
 *
 *  The sample board shows the index values for the columns and rows
 *  Remember that you access a 2D array by first specifying the row
 *  and then the column: grid[row][column]
 */

import java.util.*;
import java.io.*;

/* 
 * Name: Board
 * Purpose: Update the board according to the user input
 */
public class Board
{
    public final int NUM_START_TILES = 2;
    public final int TWO_PROBABILITY = 90;
    public final int GRID_SIZE;


    private final Random random;
    private int[][] grid;
    private int score;

    /* 
    *  Name: Board
    *  Purpose: Construct a Board object with parameter boardsize, random
    *  Parameter: int boardsize - The size of the board need to be constructed
    *             Random random - The Random object used to add tiles 
    */
    public Board(int boardSize, Random random)
    {
        this.random = random; 
        // set grid size
        GRID_SIZE = boardSize; 
        // initialise the score
        score = 0;
        // initialise the grid
        grid = new int[GRID_SIZE][GRID_SIZE];       
        // add starter tiles  
        int number = 0;
        while (number != NUM_START_TILES)
        {
            addRandomTile();
            number = number + 1;
        }  
    }

    /*
    *  Name: Board
    *  Purpose: Construct a Board object with parameter inputBoard, random
    *  Parameter: String inputBoard - The existed board to read
    *             Random random - The Random object used to add tiles
    */             
    public Board(String inputBoard, Random random) throws IOException
    {
        this.random = random; 
        Scanner input = new Scanner(new File(inputBoard));
        ArrayList<Integer> list = new ArrayList<Integer>();
                 
        // read the input file store the contents in an ArrayList
        while (input.hasNext())
        {
            int number = input.nextInt(); 
            list.add(new Integer(number));
        }
        // get the size of the grid          
        GRID_SIZE = list.get(0); 
        // get the score 
        score = list.get(1);
        // initialise the grid
        grid = new int[GRID_SIZE][GRID_SIZE];
        int index = 2;
        for (int i = 0; i < GRID_SIZE; i++)
        {
            for (int j = 0; j < GRID_SIZE; j++)
            {
                // set the grid according to the input file
                grid[i][j] = list.get(index);
                index = index + 1;
            }
        }
    }

    /*
    *  Name: saveBoard
    *  Purpose: Save the current board to a file
    *  Parameter: String outputBoard - the name of the file to save
    */
    public void saveBoard(String outputBoard) throws IOException
    {
        PrintWriter output =new PrintWriter(new File(outputBoard)); 
        // save the boardsize
        output.println(GRID_SIZE);
        // save the score  
        output.println(score);
        // save the board
        for (int i = 0; i < GRID_SIZE; i++)
        {
            for (int j = 0; j < GRID_SIZE; j++)
            {
                output.print(grid[i][j] + " ");
            }
            output.println(""); 
        }
        // close the file
        output.close();
        
    }

    /*
    *  Name: addRandomTile
    *  Purpose: Randomly add tile to an ramdomly selected spot
    */
    public void addRandomTile()
    {
        int count = 0;
        // count the number of available tiles
        for (int i = 0; i < GRID_SIZE; i++)
        {
            for (int j = 0; j < GRID_SIZE; j++)
            {
                if (grid[i][j] == 0)
                {
                    count = count + 1;
                }
            }
        }
        // if count is not 0, randomly add the tile to the board
        if (count != 0)
        {
            int location = random.nextInt(count);
            int value = random.nextInt(100);
            int index = 0;
            boolean add = false;
            // find the location  of the empty spot and add the tile
            for (int i = 0; i < GRID_SIZE; i++)
            {
                for (int j = 0; j< GRID_SIZE; j++)
                {
                    // if hit the empty spot required, add the tile
                    // if it is not the location required, keep searching
                    if (grid[i][j] == 0)
                    {   
                        if (index == location)
                        {
                            if (value < TWO_PROBABILITY)
                            {
                                grid[i][j] = 2;
                                add = true; 
                            }
                            else 
                            {
                                grid[i][j] = 4;
                                add = true;
                            }    
                            break;
                        }
                        else 
                        { 
                            index = index + 1;
                        }
                    }
                }
                // condition avoids the problem that the loop breaks while the tile is not
                // added yet
                if (index == location && add)
                {
                    break;
                }
            }    

        }
        
    }

    /* Name: isGameOver
    * Purpose: Supervise if the game is over or not
    * Return: true if the game is over; false if it is not
    */
    public boolean isGameOver()
    {
        Direction up = Direction.UP;
        Direction down = Direction.DOWN;
        Direction left = Direction.LEFT;
        Direction right = Direction.RIGHT;
        boolean check = false;

        // check if there is any valid move 
        if (canMove(up))
        {
            check = false;
        }   
        else if (canMove(down))
        {
            check = false;
        }
        else if (canMove(left))
        {
            check = false;
        }
        else if (canMove(right))
        {
            check = false;
        }
        else 
        {
            check = true;
        }   
        return check;
    }

    /*
    *  Name: canMove
    *  Purpose: Check if a move can be operated according to
    *  a user input direction
    *  Parameter: Direction direction - the direction need to move
    *  Return: true if the move is valid
    *           flase if it is not
    */
    public boolean canMove(Direction direction)
    {
        if (direction.equals(Direction.UP))
        {
            return canMoveUp();
        }
        else if (direction.equals(Direction.DOWN))
        {
            return canMoveDown();
        }
        else if (direction.equals(Direction.LEFT))
        {
            return canMoveLeft();
        }
        else
        {
            return canMoveRight();
        } 
    }
    
    /* Name: canMoveLeft
    *  Purpose: Check if we can move left
    *  Return: ture if the move is valid
    *           false if it is not
    */ 
    public boolean canMoveLeft()
    {
        boolean check = false;
        for (int i = 0; i < GRID_SIZE; i++)
        {
            for (int j = 1; j < GRID_SIZE; j++)
            {
                if (grid[i][j] != 0)
                {  
                    // check empty space
                    if (grid[i][j - 1] == 0)
                    {
                        check = true;
                    } 

                    // check same value
                    else
                    {
                        if (grid[i][j - 1] == grid[i][j])
                        {
                            check = true;
                        }
                    }
                }
            }
        }     
        return check;
    }

    /*
    *  Name: CanMoveRight
    *  Purpose: Check if we can move right
    *  Return: ture if the move is valid
    *           false if it is not
    */
    public boolean canMoveRight()
    {
        boolean check = false;
        for (int i = 0; i < GRID_SIZE; i++)
        {
            for (int j = 0; j < GRID_SIZE - 1; j++)
            {
                // check empty space
                if (grid[i][j] != 0)
                {  
                    if (grid[i][j + 1] == 0)
                    {
                        check = true;
                    } 

                    // check same value
                    else
                    {
                        if (grid[i][j + 1] == grid[i][j])
                        {
                            check = true;
                        }
                    }
                }
            }
        }     
        return check;
    }

    /* Name: canMoveUp
    *  Purpose: Check if we can move up
    *  Return: ture if the move is valid
    *           false if it is not
    */
    public boolean canMoveUp()
    {
        boolean check = false;
        for (int i = 1; i < GRID_SIZE; i++)
        {
            for (int j = 0; j < GRID_SIZE; j++)
            {
                // check empty space
                if (grid[i][j] != 0)
                {  
                    if (grid[i - 1][j] == 0)
                    {
                        check = true;
                    } 

                    // check same value
                    else
                    {
                        if (grid[i - 1][j] == grid[i][j])
                        {
                            check = true;
                        }
                    }
                }
            }
        }     
        return check;
    }

    /*
    *  Name: canMoveDown
    *  Purpose: Check if we can move down
    *  Return: true if the move is valid
    *          flase if it is not
    */
    public boolean canMoveDown()
    {
        boolean check = false;
        for (int i = 0; i < GRID_SIZE - 1; i++)
        {
            for (int j = 0; j < GRID_SIZE; j++)
            {
                // check empty space
                if (grid[i][j] != 0)
                {  
                    if (grid[i + 1][j] == 0)
                    {
                        check = true;
                    } 

                    // check same value
                    else
                    {
                        if (grid[i + 1][j] == grid[i][j])
                        {
                            check = true;
                        }
                    }
                }
            }
        }     
        return check;
    } 
    
    /*
    *  Name: move
    *  Purpose: Perform a move according to user input
    *  Parameter: Direction direction - the direction need to move 
    */
    public boolean move(Direction direction)
    {
        if (direction.equals(Direction.UP))
        {
            moveUp();
        }
        else if (direction.equals(Direction.DOWN))
        {
            moveDown();
        }
        else if (direction.equals(Direction.LEFT))
        {
            moveLeft();
        }
        else if(direction.equals(Direction.RIGHT))
        {
            moveRight();
        }
        return true;
    }


    /*
    * Name: mergeUp
    * Purpose: to perform merge operation in up direction
    */
    public void mergeUp()
    {
        for (int j = 0; j < GRID_SIZE; j++)
        {
            for (int i = 0; i < GRID_SIZE; i++)
            {
                if (grid[i][j] != 0)
                {
                    // search for the first non-zero tile on the Downside of a 
                    // tile 
                    for (int y = i + 1; y < GRID_SIZE; y++)
                    {
                        // find the non-zero tile 
                        if (grid[y][j] != 0)
                        {
                            // if the non-zero tile is equal to that tile, merge
                            // them, upgrade score
                            if (grid[y][j] == grid[i][j])
                            {
                                score = score + 2 * grid[y][j];
                                grid[i][j] = 2 * grid[y][j];
                                grid[y][j] = 0;
                            } 
                            break;
                        }
                    }
                } 
            }
        }
    }
    
    /*
    * Name: moveUp
    * Purpose: perform the move operation in up direction
    */
    public void moveUp()
    {
        // perform merge operation
        mergeUp();
        ArrayList<Integer> list = new ArrayList<Integer>();
        // get every non-zero tile in a column
        for (int j = 0; j < GRID_SIZE; j++)
        {
            // remove all elements in the list in each new column
            list.clear();
            for (int i = 0; i < GRID_SIZE; i++)
            {
                if (grid[i][j] != 0)
                {
                    list.add(new Integer(grid[i][j]));
                }
            }
            int numzero = GRID_SIZE - list.size();
            // update grid  
            if (numzero != GRID_SIZE)
            {
                for (int i = 0; i < GRID_SIZE; i++)
                {
                    if (i < GRID_SIZE - numzero)
                    { 
                        grid[i][j] = list.get(i);
                    } 
                    else
                    {
                        grid[i][j] = 0;
                    }
                }
            }
        }
    }

    /*
     * Name: mergeDown
     * Purpose: to perform merge operation in down direction
     */
    public void mergeDown()
    {
        for (int j = 0; j < GRID_SIZE; j++)
        {
            // starting from the bottom of the grid
            for (int i = GRID_SIZE - 1; i >= 0; i--)
            {
                if (grid[i][j] != 0)
                {
                    // search for the first non-zero tile on the Upside of a
                    // tile 
                    for (int y = i - 1; y >= 0; y--)
                    {
                        // find the non-zero tile 
                        if (grid[y][j] != 0)
                        {
                            // if the non-zero tile is equal to that tile, merge
                            // them, upgrade score
                            if (grid[y][j] == grid[i][j])
                            {
                                score = score + 2 * grid[y][j];
                                grid[i][j] = 2 * grid[y][j];
                                grid[y][j] = 0;
                            } 
                            break;
                        }
                    }
                } 
            }
        }
    }
     
   /*
    * Name: moveDown
    * Purpose: perform the move operation in down direction
    */ 
    public void moveDown()
    {
        mergeDown();
        ArrayList<Integer> list = new ArrayList<Integer>();
        // get every non-zero tile
        for (int j = 0; j < GRID_SIZE; j++)
        {
            // remove all elements in the list in each new row
            list.clear();
            for (int i = 0; i < GRID_SIZE; i++)
            {
                if (grid[i][j] != 0)
                {
                    list.add(new Integer(grid[i][j]));
                }
            }
            int numzero = GRID_SIZE - list.size();
            // update grid 
            if (numzero != GRID_SIZE)
            {
                for (int i = 0; i < GRID_SIZE; i++)
                {
                    if (i < numzero)
                    { 
                        grid[i][j] = 0 ;
                    } 
                    else
                    {
                        grid[i][j] = list.get(i - numzero);
                    }
                }
            }
        }
    }
    
    
    /*
     * Name: mergeLeft
     * Purpose: to perform merge operation in left direction
     */
    public void mergeLeft()
    {
        for (int i = 0; i < GRID_SIZE; i++)
        {
            for (int j = 0; j < GRID_SIZE; j++)
            {
                if (grid[i][j] != 0)
                {
                    // search for the first non-zero tile on the Right of a
                    // tile 
                    for (int x = j + 1; x < GRID_SIZE; x++)
                    {
                        // find the non-zero tile 
                        if (grid[i][x] != 0)
                        {
                            // if the non-zero tile is equal to that tile, merge
                            // them, upgrade score
                            if (grid[i][x] == grid[i][j])
                            {
                                score = score + 2 * grid[i][x];
                                grid[i][j] = 2 * grid[i][x];
                                grid[i][x] = 0;
                            } 
                            break;
                        }
                    }
                } 
            }
        }
    }
    
    
    /*
    * Name: moveLeft
    * Purpose: perform the move operation in left direction
    */
     public void moveLeft()
    {
        mergeLeft();
        ArrayList<Integer> list = new ArrayList<Integer>();
        // get every non-zero tile
        for (int i = 0; i < GRID_SIZE; i++)
        {
            // remove all elements in the list in each new row
            list.clear();
            for (int j = 0; j < GRID_SIZE; j++)
            {
                if (grid[i][j] != 0)
                {
                    list.add(new Integer(grid[i][j]));
                }
            }
            int numzero = GRID_SIZE - list.size();
            // update grid
            if (numzero != GRID_SIZE)
            {
                for (int j = 0; j < GRID_SIZE; j++)
                {
                    if (j < GRID_SIZE - numzero)
                    { 
                        grid[i][j] = list.get(j);
                    } 
                    else
                    {
                        grid[i][j] = 0;
                    }
                }
            }
        }
    }
    
     /*
     * Name: mergeRight
     * Purpose: to perform merge operation in right direction
     */
    public void mergeRight()
    {
        for (int i = 0; i < GRID_SIZE; i++)
        {
            // starting from the right of the grid
            for (int j = GRID_SIZE - 1; j >= 0; j--)
            {
                if (grid[i][j] != 0)
                {
                    // search for the first non-zero tile on the left of a
                    // tile 
                    for (int x = j - 1; x >= 0; x--)
                    {
                        // find the non-zero tile 
                        if (grid[i][x] != 0)
                        {
                            // if the non-zero tile is equal to that tile, merge
                            // them, upgrade score
                            if (grid[i][x] == grid[i][j])
                            {
                                score = score + 2 * grid[i][x];
                                grid[i][j] = 2 * grid[i][x];
                                grid[i][x] = 0;
                            } 
                            break;
                        }
                    }
                } 
            }
        }
    }
    
    /*
    * Name: moveRight
    * Purpose: perform the move operation in right direction
    */ 
    public void moveRight()
    {
        mergeRight();
        ArrayList<Integer> list = new ArrayList<Integer>();
        // get every non-zero tile
        for (int i = 0; i < GRID_SIZE; i++)
        {
            // remove all elements in the list in each new row
            list.clear();
            for (int j = 0; j < GRID_SIZE; j++)
            {
                if (grid[i][j] != 0)
                {
                    list.add(new Integer(grid[i][j]));
                }
            }
            int numzero = GRID_SIZE - list.size();
            // update grid 
            if (numzero != GRID_SIZE)
            {
                for (int j = 0; j < GRID_SIZE; j++)
                {
                    if (j < numzero)
                    { 
                        grid[i][j] = 0;
                    } 
                    else
                    {
                        grid[i][j] = list.get(j - numzero);
                    }
                }
            }
        }
    }
                
                 
        
        
        

    /*
    *  Name: getGrid
    *  Purpose: get the reference of the grid
    *  Return: grid - the reference of the grid 
    */
    public int[][] getGrid()
    {
        return grid;
    }

    /*
    *  Name: getScore
    *  Purpose: get the score
    *  Return: score
    */
    public int getScore()
    {
        return score;
    }

    /*
    *  Name: toString
    *  Purpose: get the String version of the grid
    *  Return: the String version of the grid
    */ 
    @Override
    public String toString()
    {
        StringBuilder outputString = new StringBuilder();
        outputString.append(String.format("Score: %d\n", score));
        for (int row = 0; row < GRID_SIZE; row++)
        {
            for (int column = 0; column < GRID_SIZE; column++)
                outputString.append(grid[row][column] == 0 ? "    -" :
                String.format("%5d", grid[row][column]));
            outputString.append("\n");
        }
        return outputString.toString();
    }
}
