import java.util.ArrayList;

public class Matrix {
    private final ArrayList<ArrayList<Double>> data;
    private final int rowSize;
    private final int colSize;

    Matrix(int rowSize, int colSize){
        this.rowSize = rowSize;
        this.colSize = colSize;

        data = new ArrayList<>();
        for (int i = 0; i < rowSize; ++i) {
            data.add(new ArrayList<Double>());
            for (int j = 0; j < colSize; ++j)
                data.get(i).add(0.0);
        }
    }

    Matrix(int rowSize, int colSize, double[] data){
        this.rowSize = rowSize;
        this.colSize = colSize;

        if (data.length != rowSize * colSize)
            throw new RuntimeException("Invalid sizes");

        this.data = new ArrayList<>();
        for (int i = 0; i < rowSize; ++i){
            this.data.add(new ArrayList<>());
            for (int j = 0; j < colSize; ++j)
                this.data.get(i).add(data[colSize * i + j]);
        }
    }

    public int getRowSize() {
        return rowSize;
    }

    public int getColSize() {
        return colSize;
    }

    public double get(int r, int c){
        if (r >= rowSize || c >= colSize)
            throw new RuntimeException();
        return data.get(r).get(c);
    }

    public Vector mul(Vector v){
        if (colSize != v.getSize())
            throw new RuntimeException("Invalid sizes for matrix and vector multiplication");

        ArrayList<Double> res = new ArrayList<>();
        for (int i = 0; i < rowSize; ++i)
            res.add(v.dot(data.get(i)));

        return new Vector(res);
    }
}
