import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Write a description of class Replicate here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Replicate
{
    private int[][] matrixOneZero;
    private String color;
    private ArrayList<Hexagon> matrix;
        /**
         * Constructor for objects of class Replicate.
         *
         * @param xRow The number of rows in the grid.
         * @param yColumn The number of columns in the grid.
         * @param xPosition The x-coordinate of the cell to be colored.
         * @param yPosition The y-coordinate of the cell to be colored.
         * @param color The color of the cell to be colored.
         */
        public Replicate(int xRow, int yColumn, int xPosition, int yPosition, String color) {
        matrix = new ArrayList<>();
        this.color = color;
        matrixOneZero = new int[xRow][yColumn];
        int countX;
        int countY = 30;
        if (color != "orange") {
            for (int i = 0; i < xRow; i++) {
                countX = 30 + (i % 2) * 11; // Ajusta la posición X según la fila
                for (int j = 0; j < yColumn; j++) {
                    matrixOneZero[i][j] = 0;
                    Hexagon hexagons = new Hexagon(countX, countY, "orange", i, j);
                    countX += 23;
                    hexagons.makeVisible();
                    if (i == xPosition && j == yPosition) {
                        hexagons.changeColor(color);
                        matrixOneZero[i][j] = 1;
                    }
                    matrix.add(hexagons);
                }
                countY += 20;
            }
        } else {
            System.out.println("Elige otro cojlor que no sea orange.");
        }

    }

    /**
     * Constructor for objects of class Replicate (RANDOM).
     *
     * @param xRow The number of rows in the grid.
     * @param yColumn The number of columns in the grid.
     * @param color The color of the cell to be colored.
     */
    
    public Replicate(int xRow, int yColumn, String color){
        matrix = new ArrayList<>();
        this.color = color;
        matrixOneZero = new int[xRow][yColumn];
        int countX;
        int countY = 30;
        int dimension = xRow * yColumn;
        Random randI = new Random();
        int rPosition = randI.nextInt(dimension);
        if(color != "orange"){
            for (int i = 0; i < xRow; i++) {
                countX = 30 + (i % 2) * 11; // Ajusta la posición X según la fila
                for (int j = 0; j < yColumn; j++) {
                    matrixOneZero[i][j] = 0;
                    Hexagon hexagons = new Hexagon(countX, countY, "orange", i, j);
                    countX += 23;
                    hexagons.makeVisible();
                    if(rPosition == matrix.size()){
                        hexagons.changeColor(color);
                        matrixOneZero[i][j] = 1;
                    } 
                    matrix.add(hexagons);
                }
                countY +=21; 
            }
        } else{
            System.out.println("Elige otro color que no sea orange.");
        }
    }

    /**
     * Changes the color of the hexagon at the specified coordinates.
     *
     * @param xPos The x-coordinate of the hexagon.
     * @param yPos The y-coordinate of the hexagon.
     * @param check The current state of the hexagon (1 for alive, 0 for dead).
     */
    
    public void changeColor(int xPos, int yPos, int check){
        for (int i = 0; i < matrix.size(); i++){
            if(matrix.get(i).getMatPosX() == xPos && matrix.get(i).getMatPosY() == yPos && check == 1){
                matrix.get(i).changeColor(this.color);
                updateMatrixOneZero(xPos,yPos, 1);
                break;
            }
            if (matrix.get(i).getMatPosX() == xPos && matrix.get(i).getMatPosY() == yPos && check == 0){
                matrix.get(i).changeColor("orange");
                updateMatrixOneZero(xPos,yPos, 0);
                break;
            }
        }
    }

    /**
     * Calculates the number of neighbors of a cell in a matrix.
     *
     * @param matrix The matrix of cells.
     * @param xPos The x-coordinate of the cell.
     * @param yPos The y-coordinate of the cell.
     * @return The number of neighbors of the cell.
     */
    public static int neighbor(int [][] matrix, int xPos, int yPos){
        int sum = 0;
        for(int i = xPos - 1; i<= xPos + 1; i++){
            for(int j = yPos - 1; j <= yPos + 1; j++){
                if(i >= 0 &&  i < matrix.length && j >= 0 && j < matrix[0].length ){
                    sum += matrix[i][j];
                }
            }

        } return sum;
    }

    /**
     * Updates the state of a cell in the state matrix.
     *
     * @param xPos The x-coordinate of the cell.
     * @param yPos The y-coordinate of the cell.
     * @param check The new state of the cell (1 for alive, 0 for dead).
     */
    public void updateMatrixOneZero(int xPos, int yPos, int check){
        if(check == 1){
            matrixOneZero[xPos][yPos] = 1;
        } else{
            matrixOneZero[xPos][yPos] = 0;
        }
    }

    /**
     * Replicates the state of the grid.
     *
     * This method iterates over the grid and changes the color of each cell
     * based on the number of live neighbors it has.
     */
    public void replicate(){
        for( int c = 0; c < matrixOneZero.length; c++){
            for(int j = 0; j < matrixOneZero[c].length; j++){
                if( (neighbor(matrixOneZero, c, j)% 2 == 1) ){ // verifica si la suma de los vecinos es impar
                    changeColor(c, j, 1);
                } else {
                    changeColor(c, j, 0);
                }
            }
        }
    }
    
    /**
     * Inverts the columns of a matrix.
     *
     * This method iterates over the matrix and swaps the values of each column
     * with the values of the corresponding column in the opposite direction.
     *
     * @param matrix The matrix to be inverted.
     */
    public static void invertColumns(int[][] matrix) {
        int temp;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length / 2; j++) {
                temp = matrix[i][j];
                matrix[i][j] = matrix[i][matrix[0].length - j - 1];
                matrix[i][matrix[0].length - j - 1] = temp;
            }
        }
    }
    
    /**
     * invert the rows of a matrix
     * @param matrix to be inverted
     */
    public static void invertRows(int[][] matrix) {
        int temp;
        for (int i = 0; i < matrix.length / 2; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                temp = matrix[i][j];
                matrix[i][j] = matrix[matrix.length - i - 1][j];
                matrix[matrix.length - i - 1][j] = temp;
            }
        }
    }

    /**
     * Updates the board based on the current state of the matrix.
     *
     * This method iterates over the matrix and changes the color of each cell
     * in the grid to match the value in the matrix.
     *
     * @param matrix The matrix that contains the current state of the board.
     */
    public void newBoard(int[][] matrix){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if(matrix[i][j]==1){
                    changeColor(i, j, 1);
                }else{
                    changeColor(i,j, 0);
                }
            }
        }
    }
    
    /**
     * Reflect horizontality the current board.
     */
    public void reflectHorizontal(){
        invertColumns(matrixOneZero);
        newBoard(matrixOneZero);
    }
    
    /**
     * Reflect verticaly the current board.
     */
    public void reflectVertical(){
        invertRows(matrixOneZero);
        newBoard(matrixOneZero);
    }
}

    
    
   