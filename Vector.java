import java.util.ArrayList;

public class Vector {
    private final ArrayList<Double> data;
    private final int size;

    Vector(int size){
        this.size = size;
        data = new ArrayList<>(size);
    }

    Vector(ArrayList<Double> data){
        this.size = data.size();
        this.data = new ArrayList<>(data);
    }

    Vector(double[] data){
        this.size = data.length;
        this.data = new ArrayList<Double>();
        for (double datum : data) this.data.add(datum);
    }

    int getSize(){
        return size;
    }

    double get(int i){
        if (i >= size)
            throw new RuntimeException();
        return data.get(i);
    }

    double length(){
        double res = 0;
        for (double x : data)
            res += x * x;
        res = Math.sqrt(res);

        return res;
    }

    Vector normalize(){
        double len = this.length();
        if (len != 0.0){
            for (int i = 0; i < size; ++i)
                data.set(i, data.get(i) / len);
        }

        return this;
    }

    double dot(Vector v){
        if (v.size != this.size)
            throw new RuntimeException("Dot: invalid sizes");
        double res = 0.0;
        for (int i = 0; i < this.size; ++i)
            res += data.get(i) * v.get(i);

        return res;
    }

    double dot(ArrayList<Double> v){
        if (v.size() != this.size)
            throw new RuntimeException("Dot: invalid sizes");
        double res = 0.0;
        for (int i = 0; i < this.size; ++i)
            res += data.get(i) * v.get(i);

        return res;
    }

    Vector mul(double k){
        for (int i = 0; i < data.size(); ++i)
            data.set(i, data.get(i) * k);

        return this;
    }
}
