import java.awt.*;

/**
 * A triangle that can be manipulated and that draws itself on a canvas.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0  (15 July 2000)
 */

public class Triangle{
    
    public static int VERTICES=3;
    
    private int height;
    private int width;
    private int xPosition;
    private int yPosition;
    private String color;
    private boolean isVisible;

    /**
     * Create a new triangle at default position with default color.
     */
    public Triangle(){
        height = 30;
        width = 40;
        xPosition = 140;
        yPosition = 15;
        color = "green";
        isVisible = false;
    }

    /**
     * Make this triangle visible. If it was already visible, do nothing.
     */
    public void makeVisible(){
        isVisible = true;
        draw();
    }
    
    /**
     * Make this triangle invisible. If it was already invisible, do nothing.
     */
    public void makeInvisible(){
        erase();
        isVisible = false;
    }
    
    /**
     * Move the triangle a few pixels to the right.
     */
    public void moveRight(){
        moveHorizontal(20);
    }

    /**
     * Move the triangle a few pixels to the left.
     */
    public void moveLeft(){
        moveHorizontal(-20);
    }

    /**
     * Move the triangle a few pixels up.
     */
    public void moveUp(){
        moveVertical(-20);
    }

    /**
     * Move the triangle a few pixels down.
     */
    public void moveDown(){
        moveVertical(20);
    }

    /**
     * Move the triangle horizontally.
     * @param distance the desired distance in pixels
     */
    public void moveHorizontal(int distance){
        erase();
        xPosition += distance;
        draw();
    }

    /**
     * Move the triangle vertically.
     * @param distance the desired distance in pixels
     */
    public void moveVertical(int distance){
        erase();
        yPosition += distance;
        draw();
    }

    /**
     * Slowly move the triangle horizontally.
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
     * Slowly move the triangle vertically.
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
     * @param newHeight the new height in pixels. newHeight must be >=0.
     * @param newWidht the new width in pixels. newWidht must be >=0.
     */
    public void changeSize(int newHeight, int newWidth) {
        erase();
        if(newHeight >= 0) {
            height = newHeight;
        }
        width = newWidth;
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
     * Draw the triangle with current specifications on screen.
     */
    private void draw(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            int[] xpoints = { xPosition, xPosition + (width/2), xPosition - (width/2) };
            int[] ypoints = { yPosition, yPosition + height, yPosition + height };
            canvas.draw(this, color, new Polygon(xpoints, ypoints, 3));
            canvas.wait(10);
        }
    }

    /*
     * Erase the triangle on screen.
     */
    private void erase(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
    
    
    /**
     * Calculate the area of a triangle
     * @return The area
     */
    public double area() {
        double area = (this.height*this.width)/2;
        return area;
    }
    
    
    /**
     * Converts the triangle to an equilateral triangle, maintaining the area.
     *
     * @return The side length of the equilateral triangle.
     */
    public void equilateral() {
        int sideLength = (int) Math.round(Math.sqrt(area() * 4 / Math.sqrt(3)));
        int widthE = sideLength;
        int heightE = (int) Math.round((Math.sqrt(3)*widthE) / 2);
        changeSize(heightE, widthE);
    }

 

    /**
     * Calculate the perimeter of a triangle
     * 
     */
    public int perimeter() {
        return width * 2 + height;
    }
    

    
    /**
     * Changes color of the triangle the indicated number of times
     */
    public void blink(int times) {
    if (times > 0) {
        String[] colors = {"red", "yellow", "blue", "green", "magenta", "black"};

        for (int i = 0; i <= times; i++) {
            int colorIndex = (int) (Math.random() * colors.length);
            String newColor = colors[colorIndex];
            changeColor(newColor);
            draw();

            try {
                Thread.sleep(600); // Wait for half a second (500 milliseconds)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            changeColor(color);
            draw();

            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    }
    /**
     * Create a new triangle according to the given area
     * @return triangle
     */
 
    public static Triangle random(double area) {
        // Verifica que el área sea mayor que 0.
        if (area <= 0) {
            throw new IllegalArgumentException("El área debe ser mayor que 0");
        }
    
        // Calcula la longitud de la base.
        double base = Math.sqrt(4 * area / Math.sqrt(3));
    
        // Calcula la altura.
        double height = 2 * area / base;
    
        // Verifica que las dimensiones calculadas sean válidas para un triángulo con el área especificado.
        if (base < 0 || height < 0 || base + height < 0) {
            throw new IllegalArgumentException("Las dimensiones calculadas no corresponden a un triángulo con el área especificado");
        }
    
        // Crea un nuevo triángulo con las dimensiones calculadas.
        Triangle triangle = new Triangle();
        triangle.changeSize((int) height, (int) base);
    
        // Devuelve el triángulo.
        return triangle;
    }   

}
