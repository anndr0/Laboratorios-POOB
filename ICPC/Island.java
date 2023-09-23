import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.*;
/**
 * Write a description of class Island here.
 * 
 * @author (Laura Natalia Rojas and Ana Maria Duran) 
 * @version (a version number or a date)
 */

public class Island {
    private Map<String, Polygon> islands;
    private boolean isVisible;
    private int[][] vertexArray;
    private String color;

    public Island(int[][] vertexArray, String color) {
        islands = new HashMap<>();
        Polygon polygon = new Polygon(vertexArray, color); // Crea un Polygon con el color especificado
        islands.put(color, polygon); // Usa el color como clave en el mapa
        polygon.makeVisible();
        polygon.draw();
    }
    public void makeIslandVisible(String color) {
        Polygon polygon = islands.get(color);
        if (polygon != null) {
            polygon.makeVisible(); // Hace visible el polígono de la isla especificada
        }
    }

    public void makeIslandInvisible(String color) {
        Polygon polygon = islands.get(color);
        if (polygon != null) {
            polygon.makeInvisible(); // Hace invisible el polígono de la isla especificada
        }
    }
    
    public void delIsland(String color) {
    Polygon polygon = islands.get(color);
    if (polygon != null) {
        polygon.makeInvisible();
        islands.remove(color); // Elimina la isla del mapa
    }
    }
}


