import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class TreeHashTable<K extends Comparable<K>,V> extends AbstractMap<K,V> implements Map<K,V> {
    private int size;
    private final int capacity;
    private final float loadFactor;
    private HashTree<K,V>[] trees;

    @SuppressWarnings("unchecked")
    TreeHashTable(){
        this.size = 0;
        this.capacity = 2;
        this.loadFactor = 0.75f;
        this.trees = (HashTree<K,V>[]) new Object[2];
    }

    @SuppressWarnings("unchecked")
    TreeHashTable(int capacity){
        this.size = 0;
        this.capacity = capacity;
        this.loadFactor = 0.75f;
        this.trees = (HashTree<K,V>[]) new Object[capacity];
    }

    @SuppressWarnings("unchecked")
    TreeHashTable(int capacity, float loadFactor){
        this.size = 0;
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        this.trees = (HashTree<K,V>[]) new Object[capacity];
    }

    @Override
    public int size(){
        return this.size;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return null;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {

    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {

    }

    @Override
    public V putIfAbsent(K key, V value) {
        return null;
    }

    @Override
    public boolean remove(Object key, Object value) {
        return false;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        return false;
    }

    @Override
    public V replace(K key, V value) {
        return null;
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return null;
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return null;
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return null;
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return null;
    }

    public int capacity(){
        return this.capacity;
    }

    public float loadFactor(){
        return this.loadFactor;
    }

}