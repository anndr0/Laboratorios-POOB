import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class TensorTest {

    /**
     * Método de configuración que se ejecuta una vez antes de que comiencen todas las pruebas.
     */
    @BeforeClass
    public static void beforeClass(){
        
    }

    /**
     * Método de configuración que se ejecuta antes de cada prueba individual.
     */
    @Before
    public void before(){
        
    }
    
    /**
     * Prueba la creación de un tensor y la obtención de valores.
     */
    @Test
    public void shouldCreateTensor() {
        int [] shape = {1,2,3};
        Tensor t = new Tensor(shape,5);
        int [] one = {0,0,0};
        assertEquals(5,t.value(one));
        int [] two = {0,1,2};
        assertEquals(5,t.value(two));
    }

    /**
     * Prueba la comparación de igualdad entre dos tensores.
     */
    @Test
    public void shouldKnowWhenTwoTensorAreEquals () {
        int [] shapeA= {3,4,5};
        int [] shapeB= {6,7,8};
        int [] shapeC= {3,4,5,6};
        assertTrue (new Tensor(shapeA,5).equals(new Tensor(shapeA,5)));
        assertFalse(new Tensor(shapeA,5).equals(new Tensor(shapeA,6)));
        assertFalse(new Tensor(shapeA,5).equals(new Tensor(shapeB,5)));
        assertFalse(new Tensor(shapeA,5).equals(new Tensor(shapeC,5)));
        assertEquals(new Tensor(shapeA,5), new Tensor(shapeA,5));
    }
    
    /**
     * Prueba la representación de un tensor como una cadena de texto.
     */
    @Test
    public void shouldRepresentATensorAsAString() {
        int [] shapeA = {3, 3} ;
        int [] shapeB = {2, 2, 3};
        assertEquals("[[1, 1, 1], [1, 1, 1], [1, 1, 1]]", new Tensor(shapeA, 1).toString());
        assertEquals("[[[5, 5, 5], [5, 5, 5]], [[5, 5, 5], [5, 5, 5]]]", new Tensor(shapeB, 5).toString());
    }   
    
    /**
     * Prueba la operación de suma entre dos tensores.
     */
    @Test
    public void shouldAdd() {
        int [] shapeA = {3, 3};
        int [] shapeB = {2, 2, 3};
        Tensor tB= new Tensor(shapeB, 0);
        assertEquals(new Tensor(shapeA, 6), new Tensor(shapeA, 1).add(new Tensor(shapeA, 5)));
        assertEquals(tB, tB.add(new Tensor(shapeB, 0)));
    }

}
