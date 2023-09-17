import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class TensorTest {

    
    @BeforeClass
    public static void beforeClass(){
        
    }
    
    @Before
    public void before(){
        
    }
    
    @Test
    public void shouldCreateTensor() {
        int [] shape = {1,2,3};
        Tensor t = new Tensor(shape,5);
        int [] one = {0,0,0};
        assertEquals(5,t.value(one));
        int [] two = {0,1,2};
        assertEquals(5,t.value(two));
    }


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
    

    @Test
    public void shouldRepresentATensorAsAString() {
        int [] shapeA = {3, 3} ;
        int [] shapeB = {2, 2, 3};
        assertEquals("[[1, 1, 1], [1, 1, 1], [1, 1, 1]]", new Tensor(shapeA, 1).toString());
        assertEquals("[[[5, 5, 5], [5, 5, 5]], [[5, 5, 5], [5, 5, 5]]]", new Tensor(shapeB, 5).toString());
    }   
    
    @Test
    public void shouldAdd() {
        int [] shapeA = {3, 3};
        int [] shapeB = {2, 2, 3};
        Tensor tB= new Tensor(shapeB, 0);
        assertEquals(new Tensor(shapeA, 6), new Tensor(shapeA, 1).add(new Tensor(shapeA, 5)));
        assertEquals(tB, tB.add(new Tensor(shapeB, 0)));
    }
/*   
@Test
    public void shouldPass() {
        int a = 10;
        int b = 10;
        assertEquals(a, b);
    }

    @Test
    public void shouldFail() {
        int a = 10;
        int b = 20;
        assertEquals(a, b);
    }
@Test
public void shouldErr() {
    int result = 10 / 0; // Esto causará una excepción de división por cero
}
*/

}
