/**
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

    public Tensor(int[] shape, int value) {
        this.shape = shape;
        this.values = new int[calculateSize(shape)];
        Arrays.fill(values, value);
    }

    public Tensor(int[] shape, int[] values) {
        this.shape = shape;
        if (values.length != calculateSize(shape)) {
            throw new IllegalArgumentException("Length of values must match the size determined by the shape.");
        }
        this.values = Arrays.copyOf(values, values.length);
    }

    public int value(int[] index) {
        int flatIndex = calculateFlatIndex(index);
        return values[flatIndex];
    }

    public Tensor reshape(int[] newShape) {
        int newSize = calculateSize(newShape);
        if (newSize != values.length) {
            throw new IllegalArgumentException("New shape must have the same number of elements.");
        }
        return new Tensor(newShape, values);
    }

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

@Override
public String toString() {
    int[] indices = new int[shape.length];
    return buildString(this, 0, indices).replaceAll(", ,", ",");
}

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

    public int calculateSize(int[] shape) {
        int size = 1;
        for (int dim : shape) {
            size *= dim;
        }
        return size;
    }

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
    public int[] getShape() {
            return shape;
        }
    
    public int[] getValues() {
    return Arrays.copyOf(values, values.length);
    }

    public Tensor shuffle() {
    int[] shuffledValues = Arrays.copyOf(values, values.length);
    List<Integer> valueList = Arrays.stream(shuffledValues).boxed().collect(Collectors.toList());
    Collections.shuffle(valueList);
    for (int i = 0; i < shuffledValues.length; i++) {
        shuffledValues[i] = valueList.get(i);
    }
    return new Tensor(shape, shuffledValues);
    }

    public int[] getData(){
        return this.values;
        
    }
    public int[] slice(int[] list, int start, int end, int step) {
        int[] result = new int[0];
        for (int i = start; i < end; i += step) {
            result = Arrays.copyOf(result, result.length + 1);
            result[result.length - 1] = list[i];
        }
        return result;
    }
    



    private void calculateIndices(int[] indices, int flatIndex) {
        for (int i = shape.length - 1; i >= 0; i--) {
            indices[i] = flatIndex % shape[i];
            flatIndex /= shape[i];
        }
    }
    
public Tensor mean(int axis) {
    if (axis < 0 || axis >= shape.length) {
        throw new IllegalArgumentException("Invalid axis for mean operation.");
    }

    int[] newShape = new int[shape.length - 1];
    int newSize = 1;

    for (int i = 0, j = 0; i < shape.length; i++) {
        if (i != axis) {
            newShape[j++] = shape[i];
            newSize *= shape[i];
        }
    }

    int[] meanValues = new int[newSize];
    int divisor = shape[axis];

    for (int i = 0; i < newSize; i++) {
        int sum = 0;
        for (int j = 0; j < divisor; j++) {
            int[] indices = new int[shape.length];
            calculateIndices(indices, i);
            indices[axis] = j;
            int flatIndex = calculateFlatIndex(indices);
            sum += values[flatIndex];
        }
        meanValues[i] = sum / divisor;
    }

    return new Tensor(newShape, meanValues);
}

public int find(int value) {
    for (int i = 0; i < values.length; i++) {
        if (values[i] == value) {
            return i;
        }
    }
    return -1; // Si no se encuentra el valor, retorna -1
}


}