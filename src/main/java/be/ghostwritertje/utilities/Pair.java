package be.ghostwritertje.utilities;

/**
 * Created by Jorandeboever
 * Date: 27-Dec-16.
 */
public class Pair<K, V> {
    private final K k;
    private final V v;

    public Pair(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public K getK() {
        return k;
    }

    public V getV() {
        return v;
    }
}
