import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;

/**
 * The test class NPTensorTest.
 *
 * @author  (Ana María Durán y Laura Natalia Rojas)
 */
public class NPTensorTest {

    private NPTensor npTensor;
    @Before
    public void setUp() {
        npTensor = new NPTensor();
    }

    /**
     * Prueba la asignación de un tensor con todos sus elementos con el mismo valor.
     */
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

    /**
     * Prueba la asignación de un tensor con valores específicos.
     */
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
    
    /**
     * Prueba que no se pueda asignar un tensor con valores que no coinciden con la forma especificada.
     */
    @Test
    public void shouldNotAssignMismatchedValues() {
        int[] shape = {2, 2};
        int[] values = {1, 2, 3}; // No coinciden los valores
        npTensor.assign("C", shape, values);

        assertNull(npTensor.getValue("C"));
        assertFalse(npTensor.ok());
    }
    
    /**
     * Prueba que no se pueda asignar un tensor con una forma vacía.
     */
    @Test
    public void shouldNotAssignEmptyShape() {
        int[] shape = {}; // vacio
        int[] values = {1, 2, 3};
        npTensor.assign("D", shape, values);

        assertNull(npTensor.getValue("D"));
        assertFalse(npTensor.ok());
    }
    
    /**
     * Prueba la asignación de la forma de un tensor.
     */
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
    
    /**
     * Prueba la operación de reshape en un tensor.
     */
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
    /**
     * Prueba la asignación de un tensor barajado.
     */
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

    /**
     * Prueba que no se pueda realizar una operación unary no válida.
     */
    @Test
    public void shouldNotAssignInvalidUnaryOperation() {
        int[] shapeG = {2, 2};
        int[] valuesG = {1, 2, 3, 4};
        npTensor.assign("G", shapeG, valuesG);

        npTensor.assign("H", "invalidOperation", "G");
        assertNull(npTensor.getValue("H"));
        assertFalse(npTensor.ok());
    }

    /**
     * Prueba la operación de slice en un tensor.
     */
    @Test
    public void shouldSliceTensor() {
        int[] shape = {3, 3};
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        npTensor.assign("A", shape, values);
        int[] parameters = {2,6};
        npTensor.assign("B", "slice", "A", parameters);
        assertEquals("[[3, 4, 5, 6]]", npTensor.getValue("B").toString());
        // La operación debe ser exitosa
        assertTrue(npTensor.ok());
    }

    /**
     * Prueba el cálculo de la media en un tensor.
     */
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

    /**
     * Prueba la búsqueda de un valor en un tensor.
     */
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
    
    /**
     * Prueba que no se pueda realizar una operación de slice con un número incorrecto de parámetros.
     */
    @Test(expected = IllegalArgumentException.class)
    public void shouldNotSliceTensorWithIncorrectNumberOfParameters() {
        int[] shape = {2, 3, 4}; // Un tensor de 3 dimensiones (2x3x4)
        int[] values = new int[shape[0] * shape[1] * shape[2]];
        // Llenar el tensor con valores consecutivos para facilitar las pruebas
        for (int i = 0; i < values.length; i++) {
            values[i] = i;
        }
        npTensor.assign("A", shape, values);
    
        // Definir un número incorrecto de parámetros para la operación de slice
        int[] parameters = {1, 0, 2, 2, 0}; // Número incorrecto de parámetros
    
        // Intentar realizar la operación de slice en el tensor A (debería lanzar una excepción)
        npTensor.assign("B", "slice", "A", parameters);
    }

    /**
     * Prueba la suma de dos tensores.
     */
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
    
        int[] expectedShape = {2, 2};
        assertArrayEquals(expectedShape, tensorC.getShape());
    
        int[] expectedValues = {6, 8, 10, 12};
        assertArrayEquals(expectedValues, tensorC.getValues());
        
        assertTrue(npTensor.ok());
        }

    /**
     * Prueba la resta de dos tensores.
     */
    @Test
    public void shouldSubtractTwoTensors() {
        int[] shape = {3, 3};
        int[] valuesA = {5, 6, 7, 8, 6, 10, 8, 2, 5};
        int[] valuesB = {1, 3, 3, 4, 5, 8, 7, 2, 2};
        npTensor.assign("A", shape, valuesA);
        npTensor.assign("B", shape, valuesB);

        npTensor.assign("C", "A", "subtract", "B");

        Tensor tensorC = npTensor.getValue("C");
        assertNotNull(tensorC);

        int[] expectedShape = {3, 3};
        assertArrayEquals(expectedShape, tensorC.getShape());

        int[] expectedValues = {4, 3, 4, 4, 1, 2, 1, 0, 3}; // Resultado de la resta A - B
        assertArrayEquals(expectedValues, tensorC.getValues());

        assertTrue(npTensor.ok());
    }

    /**
     * Prueba la multiplicación de dos tensores.
     */
    @Test
    public void shouldMultiplyTwoTensors() {
        int[] shape = {2, 2};
        int[] valuesA = {5, 3, 8, 4};
        int[] valuesB = {1, 6, 3, 7};
        npTensor.assign("A", shape, valuesA);
        npTensor.assign("B", shape, valuesB);

        npTensor.assign("C", "A", "multiply", "B");

        Tensor tensorC = npTensor.getValue("C");
        assertNotNull(tensorC);

        int[] expectedShape = {2, 2};
        assertArrayEquals(expectedShape, tensorC.getShape());

        int[] expectedValues = {5, 18, 24, 28}; // Resultado de la resta A - B
        assertArrayEquals(expectedValues, tensorC.getValues());

        assertTrue(npTensor.ok());
    }

    /**
     * Prueba que no se pueda realizar una operación binaria con tensores de formas diferentes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void shouldNotPerformBinaryOperationWithDifferentShapes() {
        int[] shapeA = {2, 2};
        int[] shapeB = {3, 3};
        int[] valuesA = {1, 2, 3, 4};
        int[] valuesB = {5, 6, 7, 8, 9, 10, 11, 12, 13};
    
        npTensor.assign("A", shapeA, valuesA);
        npTensor.assign("B", shapeB, valuesB);
    
        // Intentar realizar una operación binaria ("add") entre A y B con formas diferentes
        npTensor.assign("C", "A", "add", "B");
    }
    
    /**
     * Prueba el cálculo de la norma en un tensor.
     */
    @Test
    public void shouldCalculateNorm() {
        int[] shape = {3, 3};
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9}; // Valores para un tensor 3x3
        npTensor.assign("A", shape, values);
    
        npTensor.calculate("B", "A", "norm"); // Usamos el nuevo método "calculate"
    
        Tensor tensorB = npTensor.getValue("B");
        assertNotNull(tensorB);
    
        int[] expectedShape = {1, 1}; // La norma es un escalar (1x1)
        assertArrayEquals(expectedShape, tensorB.getShape());
    
        int expectedNorm = (int) Math.sqrt(285); // Norma de los valores {1, 2, 3, 4, 5, 6, 7, 8, 9}
        int[] expectedValues = {expectedNorm};
        assertArrayEquals(expectedValues, tensorB.getValues());
    
        assertTrue(npTensor.ok());
    }
    
    /**
     * Prueba el cálculo de la traza en una matriz cuadrada.
     */
    @Test
    public void shouldCalculateTrace() {
        int[] shape = {3, 3};
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9}; // Valores para una matriz cuadrada 3x3
        npTensor.assign("A", shape, values);
    
        npTensor.calculate("B", "A", "trace"); // Usamos el nuevo método "calculate"
    
        Tensor tensorB = npTensor.getValue("B");
        assertNotNull(tensorB);
    
        int[] expectedShape = {1, 1}; // La traza es un escalar (1x1)
        assertArrayEquals(expectedShape, tensorB.getShape());
    
        int expectedTrace = 15; // Traza de la matriz {1, 2, 3, 4, 5, 6, 7, 8, 9}
        int[] expectedValues = {expectedTrace};
        assertArrayEquals(expectedValues, tensorB.getValues());
    
        assertTrue(npTensor.ok());
    }

    /**
     * Prueba la multiplicación de una matriz por un escalar.
     */
    @Test
    public void shouldMultiplyMatrixByScalar() {
        int[] shape = {2, 3};
        int[] values = {1, 2, 3, 4, 5, 6}; // Matriz 2x3
        npTensor.assign("A", shape, values);
    
        npTensor.multiplyScalar("B", "A", 3);
    
        Tensor tensorB = npTensor.getValue("B");
        assertNotNull(tensorB);
    
        int[] expectedShape = {2, 3}; // La forma debe ser la misma que la matriz original
        assertArrayEquals(expectedShape, tensorB.getShape());
    
        int[] expectedValues = {3, 6, 9, 12, 15, 18}; // Resultado de multiplicar la matriz por 3
        assertArrayEquals(expectedValues, tensorB.getValues());
    
        assertTrue(npTensor.ok());
    }




}




