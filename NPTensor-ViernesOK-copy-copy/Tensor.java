/**
 * Representa un tensor multidimensional.
 * @Autores Ana María Durán y Laura Natalia Rojas, 2023
 *
 */
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

public class Tensor {

    private int[] shape;
    private int[] values;
/**
 * Crea un nuevo tensor con la misma forma pero todos los elementos con el mismo valor.
 *
 * @param shape  La forma del nuevo tensor.
 * @param value  El valor con el que se llenará el tensor.
 */
    public Tensor(int[] shape, int value) {
        this.shape = shape;
        this.values = new int[calculateSize(shape)];
        Arrays.fill(values, value);
    }
    /**
     * Crea un nuevo tensor con una forma dada y valores proporcionados.
     *
     * @param shape   La forma del nuevo tensor.
     * @param values  Los valores que se asignarán al tensor.
     */
    public Tensor(int[] shape, int[] values) {
        this.shape = shape;
        if (values.length != calculateSize(shape)) {
            throw new IllegalArgumentException("Length of values must match the size determined by the shape.");
        }
        this.values = Arrays.copyOf(values, values.length);
    }
    /**
     * Obtiene el valor en la posición especificada del tensor multidimensional.
     *
     * @param index  El índice de la posición deseada.
     * @return El valor en la posición indicada.
     */
    public int value(int[] index) {
        int flatIndex = calculateFlatIndex(index);
        return values[flatIndex];
    }
    /**
     * Cambia la forma del tensor sin cambiar sus valores.
     *
     * @param newShape  La nueva forma deseada.
     * @return Un nuevo tensor con la forma especificada.
     * @throws IllegalArgumentException Si la nueva forma no tiene el mismo número de elementos.
     */
    public Tensor reshape(int[] newShape) {
        int newSize = calculateSize(newShape);
        if (newSize != values.length) {
            throw new IllegalArgumentException("New shape must have the same number of elements.");
        }
        return new Tensor(newShape, values);
    }
    /**
     * Realiza una operación de suma con otro tensor del mismo tamaño.
     *
     * @param t  El tensor a sumar.
     * @return Un nuevo tensor con el resultado de la suma.
     * @throws IllegalArgumentException Si los tensores no tienen la misma forma.
     */
    public Tensor add(Tensor t) {
        if (!Arrays.equals(this.shape, t.shape)) {
            throw new IllegalArgumentException("Tensors must have the same shape for addition.");
        }

        int[] resultValues = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            resultValues[i] = this.values[i] + t.values[i];
        }

        return new Tensor(this.shape, resultValues);
    }
    /**
     * Compara este tensor con otro para verificar si son iguales.
     *
     * @param other  El tensor con el que se compara.
     * @return true si los tensores son iguales, false en caso contrario.
     */
    public boolean equals(Tensor other) {
        if (!Arrays.equals(this.shape, other.shape)) {
            return false;
        }
        return Arrays.equals(this.values, other.values);
    }

@Override
public boolean equals(Object other) {
    if (this == other) {
        return true;
    }
    if (other == null || getClass() != other.getClass()) {
        return false;
    }
    Tensor tensor = (Tensor) other;
    String thisString = this.toString().replaceAll("\\s+", "");
    String otherString = tensor.toString().replaceAll("\\s+", "");
    return thisString.equals(otherString);
}
    /**
     * Convierte el tensor en una cadena de texto.
     *
     * @return Una representación en cadena del tensor.
     */
@Override
public String toString() {
    int[] indices = new int[shape.length];
    return buildString(this, 0, indices).replaceAll(", ,", ",");
}
    /**
     * Construye una representación en forma de cadena (string) del tensor y sus valores de manera recursiva.
     *
     * @param tensor  El tensor actual que se está construyendo.
     * @param level   El nivel actual de profundidad en la construcción recursiva.
     * @param indices Un arreglo de índices que se utiliza para rastrear la posición actual en el tensor.
     * @return Una representación en forma de cadena del tensor y sus valores.
     */
private String buildString(Tensor tensor, int level, int[] indices) {
    StringBuilder builder = new StringBuilder();

    if (level == tensor.shape.length - 1) {
        builder.append("[");
        for (int i = 0; i < tensor.shape[level]; i++) {
            indices[level] = i;
            int flatIndex = tensor.calculateFlatIndex(indices);
            builder.append(tensor.values[flatIndex]);
            if (i < tensor.shape[level] - 1) {
                builder.append(", ");
            }
        }
        builder.append("]");
    } else {
        builder.append("[");
        for (int i = 0; i < tensor.shape[level]; i++) {
            indices[level] = i;
            builder.append(buildString(tensor, level + 1, indices));
            if (i < tensor.shape[level] - 1) {
                builder.append(", ");
            }
        }
        builder.append("]");
    }
    return builder.toString();
}
    /**
     * Calcula el tamaño total de un tensor en función de las dimensiones proporcionadas.
     *
     * @param shape Un arreglo de enteros que representa las dimensiones del tensor.
     * @return El tamaño total del tensor, que es el producto de todas las dimensiones.
     */
    public int calculateSize(int[] shape) {
        int size = 1;
        for (int dim : shape) {
            size *= dim;
        }
        return size;
    }
    /**
     * Calcula el índice plano (flat index) correspondiente a un índice multidimensional en el tensor.
     *
     * @param index Un arreglo de enteros que representa el índice multidimensional.
     * @return El índice plano que corresponde a la posición en el arreglo unidimensional de valores del tensor.
     * @throws IllegalArgumentException Si la longitud del índice no coincide con la cantidad de dimensiones del tensor.
     */
    private int calculateFlatIndex(int[] index) {
        if (index.length != shape.length) {
            throw new IllegalArgumentException("Index must have the same length as the shape.");
        }
        int flatIndex = 0;
        int stride = 1;
        for (int i = shape.length - 1; i >= 0; i--) {
            flatIndex += index[i] * stride;
            stride *= shape[i];
        }
        return flatIndex;
    }
    /**
     * Obtiene la forma del tensor.
     *
     * @return Un arreglo de enteros que representa la forma del tensor.
     */
    public int[] getShape() {
            return shape;
        }
    /**
     * Obtiene una copia de los valores del tensor.
     *
     * @return Un arreglo de enteros que contiene los valores del tensor.
     */
    public int[] getValues() {
    return Arrays.copyOf(values, values.length);
    }
    /**
     * Realiza una operación de barajado aleatorio en el tensor.
     *
     * @return Un nuevo tensor con los mismos valores pero en orden aleatorio.
     */
    public Tensor shuffle() {
    int[] shuffledValues = Arrays.copyOf(values, values.length);
    List<Integer> valueList = Arrays.stream(shuffledValues).boxed().collect(Collectors.toList());
    Collections.shuffle(valueList);
    for (int i = 0; i < shuffledValues.length; i++) {
        shuffledValues[i] = valueList.get(i);
    }
    return new Tensor(shape, shuffledValues);
    }
    /**
     * Obtiene los datos del tensor en forma de un arreglo unidimensional de enteros.
     *
     * @return Un arreglo unidimensional que contiene los valores almacenados en el tensor.
     */
    public int[] getData(){
        return this.values;
        
    }
    /**
     * Obtiene una sección específica de un arreglo de enteros.
     *
     * @param list   El arreglo de enteros original.
     * @param start  El índice de inicio de la sección.
     * @param end    El índice de fin de la sección.
     * @param step   El incremento entre elementos de la sección.
     * @return Un nuevo arreglo de enteros que representa la sección especificada.
     */
    public int[] slice(int[] list, int start, int end, int step) {
        int[] result = new int[0];
        for (int i = start; i < end; i += step) {
            result = Arrays.copyOf(result, result.length + 1);
            result[result.length - 1] = list[i];
        }
        return result;
    }

    /**
     * Calcula los índices multidimensionales a partir de un índice plano (flat index).
     *
     * @param indices    Un arreglo que se llenará con los índices multidimensionales calculados.
     * @param flatIndex  El índice plano que se convertirá en índices multidimensionales.
     */
private void calculateIndices(int[] indices, int flatIndex) {
        for (int i = shape.length - 1; i >= 0; i--) {
            indices[i] = flatIndex % shape[i];
            flatIndex /= shape[i];
        }
    }

    /**
     * Busca un valor en el tensor y devuelve su índice.
     *
     * @param value  El valor que se busca en el tensor.
     * @return El índice del valor encontrado o -1 si no se encuentra.
     */
public int find(int value) {
    for (int i = 0; i < values.length; i++) {
        if (values[i] == value) {
            return i;
        }
    }
    return -1; // Si no se encuentra el valor, retorna -1
}
    /**
     * Realiza una operación de resta con otro tensor del mismo tamaño.
     *
     * @param other  El tensor a restar.
     * @return Un nuevo tensor con el resultado de la resta.
     * @throws IllegalArgumentException Si los tensores no tienen la misma forma.
     */
public Tensor subtract(Tensor other) {
    if (!Arrays.equals(this.shape, other.shape)) {
        throw new IllegalArgumentException("Los tensores deben tener la misma forma para la resta.");
    }

    int[] resultValues = new int[values.length];
    for (int i = 0; i < values.length; i++) {
        resultValues[i] = this.values[i] - other.values[i];
    }

    return new Tensor(this.shape, resultValues);
}
    /**
     * Realiza una operación de multiplicación con otro tensor del mismo tamaño.
     *
     * @param other  El tensor a multiplicar.
     * @return Un nuevo tensor con el resultado de la multiplicación.
     * @throws IllegalArgumentException Si los tensores no tienen la misma forma.
     */
public Tensor multiply(Tensor other) {
    if (!Arrays.equals(this.shape, other.shape)) {
        throw new IllegalArgumentException("Los tensores deben tener la misma forma para la multiplicación.");
    }

    int[] resultValues = new int[values.length];
    for (int i = 0; i < values.length; i++) {
        resultValues[i] = this.values[i] * other.values[i];
    }

    return new Tensor(this.shape, resultValues);
}
    /**
     * Calcula la norma (módulo) del tensor como la raíz cuadrada de la suma de los cuadrados de sus elementos.
     *
     * @return La norma del tensor como un número entero.
     */
public int calculateNorm() {
    int[] values = this.getValues();
    int sumOfSquares = 0;

    for (int value : values) {
        sumOfSquares += value * value;
    }

    return (int) Math.sqrt(sumOfSquares);
}
    /**
     * Calcula la traza de una matriz cuadrada, que es la suma de los elementos en la diagonal principal.
     *
     * @return La traza de la matriz cuadrada como un número entero.
     */
public int calculateTrace() {
        if (shape.length != 2 || shape[0] != shape[1]) {
            throw new IllegalArgumentException("La traza solo se puede calcular en matrices cuadradas.");
        }

        int size = shape[0];
        int trace = 0;

        for (int i = 0; i < size; i++) {
            trace += values[i * size + i];
        }

        return trace;
    }



}
