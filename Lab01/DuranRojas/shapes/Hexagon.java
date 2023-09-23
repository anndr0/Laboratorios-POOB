import java.awt.*;
/**
 * Write a description of class Hexagon here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Hexagon
{
    // instance variables - replace the example below with your own
    public static int SIDES=6;
    private int sideLength;
    private int xPosition;
    private int yPosition;
    private String color;
    private boolean isVisible;
    private int matrixPosX;
    private int matrixPosY;
    
    /**
     * Constructor for objects of class Hexagon
     */
    public Hexagon(int xPosition, int yPosition, String color){
        sideLength = 12;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.color = color;
        isVisible = false;
        
    }
    
    public Hexagon(int xPosition, int yPosition, String color, int matrixPosX, int matrixPosY){
        sideLength = 12;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.matrixPosX = matrixPosX;
        this.matrixPosY = matrixPosY;
        this.color = color;
        isVisible = false;
        
    }
    
    public Hexagon(){
        sideLength = 30;
        this.xPosition = 30;
        this.yPosition = 40;
        this.color = "green";
        isVisible = false;
        
    }

    
    public int getMatPosX(){
        return this.matrixPosX;
    }
    
    public int getMatPosY(){
        return this.matrixPosY;
    }
    
    public String getColor(){
        return this.color;
    }
    
    public int hexagonValue(){
        if(getColor() == "orange"){
            return 0;
        } else{
            return 1;
        }
    }
    
    /**
     * Make this hexagon visible. If it was already visible, do nothing.
     */
    public void makeVisible(){
        isVisible = true;
        draw();
    }
    
    /**
     * Make this hexagon invisible. If it was already invisible, do nothing.
     */
    public void makeInvisible(){
        erase();
        isVisible = false;
    }
    
    /**
     * Move the hexagon a few pixels to the right.
     */
    public void moveRight(){
        moveHorizontal(20);
    }

    /**
     * Move the hexagon a few pixels to the left.
     */
    public void moveLeft(){
        moveHorizontal(-20);
    }

    /**
     * Move the hexagon a few pixels up.
     */
    public void moveUp(){
        moveVertical(-20);
    }

    /**
     * Move the hexagon a few pixels down.
     */
    public void moveDown(){
        moveVertical(20);
    }

    /**
     * Move the hexagon horizontally.
     * @param distance the desired distance in pixels
     */
    public void moveHorizontal(int distance){
        erase();
        xPosition += distance;
        draw();
    }

    /**
     * Move the hexagon vertically.
     * @param distance the desired distance in pixels
     */
    public void moveVertical(int distance){
        erase();
        yPosition += distance;
        draw();
    }

    /**
     * Slowly move the hexagon horizontally.
     * @param distance the desired distance in pixels
     */
    public void slowMoveHorizontal(int distance){
        int delta;

        if(distance < 0) {
            delta = -1;
            distance = -distance;
        } else {
            delta = 1;
        }

        for(int i = 0; i < distance; i++){
            xPosition += delta;
            draw();
        }
    }

    /**
     * Slowly move the hexagon vertically.
     * @param distance the desired distance in pixels
     */
    public void slowMoveVertical(int distance){
        int delta;

        if(distance < 0) {
            delta = -1;
            distance = -distance;
        } else {
            delta = 1;
        }

        for(int i = 0; i < distance; i++){
            yPosition += delta;
            draw();
        }
    }

    /**
     * Change the size to the new size
     * @param newSideLength the new height in pixels. sideLength must be >=0.
     */
    public void changeSize(int newSideLength) {
        erase();
        sideLength = newSideLength;
        draw();
    }
    
    /**
     * Change the color. 
     * @param color the new color. Valid colors are "red", "yellow", "blue", "green",
     * "magenta" and "black".
     */
    public void changeColor(String newColor){
        color = newColor;
        draw();
    }

    /*
     * Draw the hexagon with current specifications on screen.
     */
    public void draw() {
    if (isVisible) {
        int[] xpoints = new int[6];
        int[] ypoints = new int[6];
        int centerX = xPosition;
        int centerY = yPosition;

        for (int i = 0; i < 6; i++) {
            double angleRad = 2 * Math.PI * (i + 0.5) / 6;
            xpoints[i] = (int) (centerX + sideLength * Math.cos(angleRad));
            ypoints[i] = (int) (centerY + sideLength * Math.sin(angleRad));
        }

        Canvas canvas = Canvas.getCanvas();
        canvas.draw(this, color, new Polygon(xpoints, ypoints, 6));
    }
}


    /*
     * Erase the hexagon on screen.
     */
    private void erase(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
}
