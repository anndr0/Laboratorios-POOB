import java.util.HashMap;
import java.util.Arrays;
/** NPTensor.java
 * 
 * @Ana María Durán y Laura Natalia Rojas
 */
    
public class NPTensor {

    private HashMap<String, Tensor> variables; // Mapa de variables donde se almacenan los tensores
    private boolean operationSuccess; // Variable para rastrear si la última operación fue exitosa

    /**
     * Constructor de la clase NPTensor.
     * Inicializa un mapa de variables y establece la operación inicial como exitosa.
     */
    public NPTensor() {
        variables = new HashMap<>();
        operationSuccess = true;
    }

    /**
     * Asigna un tensor a una variable con todos sus elementos con el mismo valor.
     *
     * @param name  Nombre de la variable.
     * @param shape Forma del tensor.
     * @param value Valor con el que se llenará el tensor.
     */
    public void assign(String name, int[] shape, int value) {
        int[] values = new int[calculateSize(shape)];
        Arrays.fill(values, value);
        Tensor tensor = new Tensor(shape, values);
        variables.put(name,tensor);

    }

    /**
     * Asigna un tensor a una variable con los valores proporcionados.
     *
     * @param name   Nombre de la variable.
     * @param shape  Forma del tensor.
     * @param values Valores del tensor.
     */
    
    public void assign(String name, int[] shape, int[] values) {
        if (shape.length == 0 || calculateSize(shape) != values.length) {
            operationSuccess = false;
            return;
        }
        Tensor tensor = new Tensor(shape, values);
        variables.put(name, tensor);
        operationSuccess = true;
    }

     /**
     * Asigna el valor de una operación unaria a una variable.
     *
     * @param a     Nombre de la variable de destino.
     * @param unary Operación unaria a realizar.
     * @param b     Nombre de la variable de origen.
     */
    public void assign(String a, String unary, String b) {
        // Realiza una operación unaria (como "shape", "reshape" o "shuffle") en el tensor y asigna el resultado a la variable de destino.
        Tensor tensorB = variables.get(b);
        if (tensorB == null) {
                operationSuccess = false;
                return;
            }
        if (unary.equals("shape")) {
                int[] dimensions = tensorB.getShape();
                int[] shape = {dimensions.length};
                variables.put(a, new Tensor(shape, dimensions));
            
        } else if (unary.equals("reshape")) {
            // Obtén las dimensiones originales de 'b'
            int[] originalShape = tensorB.getShape();
            
            // Calcula las nuevas dimensiones como la versión invertida de las originales
            int[] newShape = new int[originalShape.length];
            for (int i = 0; i < originalShape.length; i++) {
                newShape[i] = originalShape[originalShape.length - 1 - i];
            }
            // Realiza el reshape y asigna el resultado a 'a'
            Tensor tensorA = tensorB.reshape(newShape);
            variables.put(a, tensorA);
        } else if (unary.equals("shuffle")) {
            //'b' es la variable que se va a barajar
            Tensor tensorA = tensorB.shuffle();
            variables.put(a, tensorA);
        } else {
            operationSuccess = false; // Operación no válida
        }
            
    }

    /**
     * Asigna el valor de una operación unaria con parámetros a una variable.
     *
     * @param a          Nombre de la variable de destino.
     * @param unary      Operación unaria con parámetros a realizar.
     * @param b          Nombre de la variable de origen.
     * @param parameters Parámetros de la operación.
     */
        public void assign(String a, String unary, String b, int[] parameters) {
            // Realiza una operación unaria con parámetros (como "slice", "mean" o "find") en el tensor y asigna el resultado a la variable de destino.
            Tensor tensorB = variables.get(b);
    
            if (unary.equals("slice")) {
                if(parameters.length == 3) {
                    int[] tensorSlice = tensorB.slice(tensorB.getValues(), parameters[0], parameters[1], parameters[2]);
                    int[] tensorShape = {1, tensorSlice.length};
                    variables.put(a, new Tensor(tensorShape, tensorSlice));
                } else if (parameters.length == 2) {
                    int[] tensorSlice = tensorB.slice(tensorB.getValues(), parameters[0], parameters[1], 1);
                    int[] tensorShape = {1, tensorSlice.length};
                    variables.put(a, new Tensor(tensorShape, tensorSlice));
                } else {
                    throw new IllegalArgumentException("Incorret number");
                }
    
    
            } else if (unary.equals("mean")) {
    
            if (parameters.length != 1) {
            operationSuccess = false; // Parámetros insuficientes para la operación "mean"
            return;
            }
                   
            // Obtener los valores del tensor
            int[] values = tensorB.getValues();
            // Calcular la suma de los elementos
            int sum = 0;
            for (int value : values) {
                sum += value;
            }
            // Calcular el promedio
            int size = values.length;
            int meanValue = (size > 0) ? sum / size : 0;
            
            // Crear un tensor con el valor promedio y asignarlo a
            int[] shape = { 1 };
            int[] meanValues = { meanValue };
            Tensor meanTensor = new Tensor(shape, meanValues);
            variables.put(a, meanTensor);
            
        } else if (unary.equals("find")) {
                if (parameters.length != 1) {
                    operationSuccess = false; // Parámetros insuficientes para la operación "find"
                    return;
                }
                int value = parameters[0];
                int foundIndex = tensorB.find(value);
                // Crea un tensor con el índice encontrado y asigna el resultado a 'a'
                int[] shape = { 1 };
                int[] values = { foundIndex };
                Tensor indexTensor = new Tensor(shape, values);
                variables.put(a, indexTensor);
            } else {
                operationSuccess = false; // Operación no válida
            }
    }

    /**
     * Asigna el valor de una operación binaria a una variable.
     *
     * @param a       Nombre de la variable de destino.
     * @param b       Nombre de la primera variable de origen.
     * @param sBinary Operación binaria a realizar (por ejemplo, "add", "subtract", "multiply").
     * @param c       Nombre de la segunda variable de origen.
     */
    public void assign(String a, String b, String sBinary, String c) {
        // Realiza una operación binaria entre dos tensores y asigna el resultado a la variable de destino.
        Tensor tensorB = variables.get(b);
        Tensor tensorC = variables.get(c);

        if (tensorB == null || tensorC == null) {
            throw new IllegalArgumentException("Los tensores " + b + " y " + c + " deben existir en el mapa de variables.");
        }

        if (!Arrays.equals(tensorB.getShape(), tensorC.getShape())) {
            throw new IllegalArgumentException("Los tensores " + b + " y " + c + " deben tener la misma forma para la operación " + sBinary + ".");
        }

        Tensor result;
        if (sBinary.equals("add")) {
            result = tensorB.add(tensorC);
        } else if (sBinary.equals("subtract")) {
            result = tensorB.subtract(tensorC);
        } else if (sBinary.equals("multiply")) {
            result = tensorB.multiply(tensorC);
        } else {
            throw new IllegalArgumentException("Operación binaria no válida: " + sBinary);
        }

        variables.put(a, result);
        operationSuccess = true;
    }
    /**
     * Obtiene la representación en cadena de un tensor.
     *
     * @param variable Nombre de la variable que contiene el tensor.
     * @return Representación en cadena del tensor asociado a la variable, o null si la variable no existe.
     */
    
    public String toString(String variable) {
        // Returns the string representation of a tensor
        Tensor tensor = variables.get(variable);
        return (tensor != null) ? tensor.toString() : null;
    }

    /**
     * Comprueba si la última operación fue exitosa.
     *
     * @return true si la última operación fue exitosa, false en caso contrario.
     */
    public boolean ok() {
        // Verifica si la última operación fue exitosa.
        return operationSuccess;
    }

    // Calculate the size of a tensor based on its shape
    private int calculateSize(int[] shape) {
        int size = 1;
        for (int dim : shape) {
            size *= dim;
        }
        return size;
    }

    /**
     * Obtiene el valor de una variable en forma de tensor.
     *
     * @param name Nombre de la variable.
     * @return Tensor asociado a la variable, o null si la variable no existe.
     */
    public Tensor getValue(String name) {
        // Obtiene el tensor asociado a una variable.
        return variables.get(name);
    }

    /**
     * Calcula una función en un tensor y asigna el resultado a una variable.
     *
     * @param a       Nombre de la variable de destino.
     * @param b       Nombre de la variable de origen.
     * @param function Función a calcular (por ejemplo, "norm" o "trace").
     */
    public void calculate(String a, String b, String function) {
        // Calcula una función en un tensor (como "norm" o "trace") y asigna el resultado a la variable de destino.
        Tensor tensorB = variables.get(b);

        if (tensorB == null) {
            throw new IllegalArgumentException("El tensor " + b + " debe existir en el mapa de variables.");
        }

        int[] shape = {1, 1}; // El resultado es un escalar (1x1)
        int[] values;

        if (function.equals("norm")) {
            values = new int[]{tensorB.calculateNorm()}; // Calcula la norma
        } else if (function.equals("trace")) {
            values = new int[]{tensorB.calculateTrace()}; // Calcula la traza
        } else {
            throw new IllegalArgumentException("Operación unary no válida: " + function);
        }

        Tensor result = new Tensor(shape, values);
        variables.put(a, result);

        operationSuccess = true;
    }
    /**
     * Multiplica un tensor por un escalar y asigna el resultado a una variable.
     *
     * @param a      Nombre de la variable de destino.
     * @param b      Nombre de la variable de origen.
     * @param scalar Escalar por el que se multiplicará el tensor.
     */
    public void multiplyScalar(String a, String b, int scalar) {
        // Multiplica un tensor por un escalar y asigna el resultado a la variable de destino.
        Tensor tensorB = variables.get(b);
    
        if (tensorB == null) {
            throw new IllegalArgumentException("El tensor " + b + " debe existir en el mapa de variables.");
        }
    
        int[] shape = tensorB.getShape();
        int[] values = tensorB.getValues();
    
        // Multiplicar cada elemento de la matriz por el escalar
        for (int i = 0; i < values.length; i++) {
            values[i] *= scalar;
        }
    
        Tensor result = new Tensor(shape, values);
        variables.put(a, result);
    
        operationSuccess = true;
    }

}



    



