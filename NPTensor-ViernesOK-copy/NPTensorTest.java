import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;

/**
 * The test class NPTensorTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class NPTensorTest {

    private NPTensor npTensor;
    @Before
    public void setUp() {
        npTensor = new NPTensor();
    }

    @Test
    public void shouldAssignTensorWithSameValue() {
        int[] shape = {3, 3};
        int value = 5;
        npTensor.assign("A", shape, value);
        Tensor tensorA = npTensor.getValue("A");
        assertNotNull(tensorA);
        assertEquals(value, tensorA.value(new int[]{0, 0}));
        assertEquals(value, tensorA.value(new int[]{2,2}));
    }

    @Test
    public void shouldAssignTensorWithGivenValues() {
        int[] shape = {2, 2};
        int[] values = {1, 2, 3, 4};
        npTensor.assign("B", shape, values);
    
        Tensor tensorB = npTensor.getValue("B");
        assertNotNull(tensorB);
        
        int[] retrievedValues = new int[values.length];
        int index = 0;
        for (int i = 0; i < shape[0]; i++) {
            for (int j = 0; j < shape[1]; j++) {
                retrievedValues[index++] = tensorB.value(new int[]{i, j});
            }
        }
    
        assertArrayEquals(values, retrievedValues);
        assertTrue(npTensor.ok());
    }

    @Test
    public void shouldNotAssignMismatchedValues() {
        int[] shape = {2, 2};
        int[] values = {1, 2, 3}; // Mismatched number of values
        npTensor.assign("C", shape, values);

        assertNull(npTensor.getValue("C"));
        assertFalse(npTensor.ok());
    }

    @Test
    public void shouldNotAssignEmptyShape() {
        int[] shape = {}; // Empty shape
        int[] values = {1, 2, 3};
        npTensor.assign("D", shape, values);

        assertNull(npTensor.getValue("D"));
        assertFalse(npTensor.ok());
    }
    
    @Test
    public void testAssignShapeOperation() {
        NPTensor npTensor = new NPTensor();

        int[] shape = {2, 3};
        int[] values = {1, 2, 3, 4, 5, 6};

        npTensor.assign("A", shape, values);

        npTensor.assign("B", "shape", "A");

        assertTrue(npTensor.ok());
        assertEquals(Arrays.toString(shape), npTensor.toString("B"));
    }

    @Test
    public void shouldReshapeTensor() {
        // Crear un tensor con dimensiones originales [2, 3]
        int[] shapeA = {2, 3};
        int[] valuesA = {1, 2, 3, 4, 5, 6};
        npTensor.assign("A", shapeA, valuesA);

        // Realizar la operación de "reshape" en el tensor A
        npTensor.assign("B", "reshape", "A");

        // Obtener el tensor resultante
        Tensor tensorB = npTensor.getValue("B");
        assertNotNull(tensorB);

        // Las dimensiones resultantes deben ser [3, 2]
        int[] expectedShape = {3, 2};
        assertArrayEquals(expectedShape, tensorB.getShape());

        // Verificar que los valores del tensor B sean los mismos que los del tensor A
        assertArrayEquals(valuesA, tensorB.getValues());
        
        // La operación debe ser exitosa
        assertTrue(npTensor.ok());
    }

    @Test
    public void shouldAssignShuffledTensor() {
        int[] shapeE = {2, 3};
        int[] valuesE = {1, 2, 3, 4, 5, 6};
        npTensor.assign("E", shapeE, valuesE);
    
        npTensor.assign("F", "shuffle", "E");
    
        Tensor tensorF = npTensor.getValue("F");
        assertNotNull(tensorF);
        
        int[] valuesF = tensorF.getValues();
        assertFalse(Arrays.equals(valuesE, valuesF)); // Verifica que los valores no sean iguales
        assertTrue(npTensor.ok());
    }

    @Test
    public void shouldNotAssignInvalidUnaryOperation() {
        int[] shapeG = {2, 2};
        int[] valuesG = {1, 2, 3, 4};
        npTensor.assign("G", shapeG, valuesG);

        npTensor.assign("H", "invalidOperation", "G");
        
        assertNull(npTensor.getValue("H"));
        assertFalse(npTensor.ok());
    }
    
    @Test
    public void shouldSliceTensor() {
        int[] shape = {3, 3};
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        npTensor.assign("A", shape, values);
        int[] parameters = {2,6};
        npTensor.assign("B", "slice", "A", parameters);
        assertEquals("[[3, 4, 5, 6]]", npTensor.getValue("B").toString());
    }
    
    @Test
    public void shouldCalculateMean() {
        // Crear un tensor con algunos valores
        int[] shape = {3, 3};
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        npTensor.assign("A", shape, values);
        int axis = 0;
        npTensor.assign("B", "mean", "A", new int[]{axis});
    
        // Obtener el tensor resultante
        Tensor meanTensor = npTensor.getValue("B");
        assertNotNull(meanTensor);
    
        // El resultado esperado es el promedio de los valores en el eje 0, que es 5
        int[] expectedShape = {1};
        int[] expectedValues = {5};
        assertArrayEquals(expectedShape, meanTensor.getShape());
        assertArrayEquals(expectedValues, meanTensor.getValues());
        
        // La operación debe ser exitosa
        assertTrue(npTensor.ok());
    }

    
    @Test
    public void shouldFindValueInTensor() {
        int[] shape = {3, 3};
        int[] values = {
            1, 2, 3,
            4, 5, 6,
            7, 8, 9
        };
        npTensor.assign("H", shape, values);
    
        // Prueba 1: Encontrar el valor 5 en el tensor
        int[] findParams1 = {5};
        npTensor.assign("I", "find", "H", findParams1);
        Tensor result1 = npTensor.getValue("I");
        assertNotNull(result1);
        int[] expectedValues1 = {4}; // Índice del valor 5
        assertArrayEquals(expectedValues1, result1.getValues());
    
        // Prueba 2: Encontrar el valor 10 en el tensor (valor no presente)
        int[] findParams2 = {10};
        npTensor.assign("J", "find", "H", findParams2);
        Tensor result2 = npTensor.getValue("J");
        assertNotNull(result2);
        int[] expectedValues2 = {-1}; // Valor no encontrado
        assertArrayEquals(expectedValues2, result2.getValues());
    }
    
    @Test
    public void shouldAddTwoTensors() {
        int[] shape = {2, 2};
        int[] valuesA = {1, 2, 3, 4};
        int[] valuesB = {5, 6, 7, 8};
        npTensor.assign("A", shape, valuesA);
        npTensor.assign("B", shape, valuesB);
    
        npTensor.assign("C", "A", "add", "B");
    
        Tensor tensorC = npTensor.getValue("C");
        assertNotNull(tensorC);

        System.out.println(tensorC);
    
        int[] expectedShape = {2, 2};
        assertArrayEquals(expectedShape, tensorC.getShape());
    
        int[] expectedValues = {6, 8, 10, 12};
        assertArrayEquals(expectedValues, tensorC.getValues());
        
        assertTrue(npTensor.ok());
    }



}




