public class Pair<K, T extends Comparable<T>> implements Comparable<Pair<K, T>> {
    private K name;
    private T value;


    public Pair(K name, T value) {
        this.name = name;
        this.value = value;
    }

    public K getName() {
        return name;
    }

    public void setName(K name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public int compareTo(Pair<K, T> o) {
        return getValue().compareTo(o.getValue());
    }

    @Override
    public String toString() {
        return "Pair{" +
                "name=" + name +
                ", value=" + value +
                '}';
    }
}
