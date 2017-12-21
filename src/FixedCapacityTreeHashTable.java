import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class FixedCapacityTreeHashTable<K extends Comparable<K>,V> extends AbstractMap<K, V> implements Map<K,V> {
    private final int treeCount;
    private int size;
    private final ArrayList<HashTree<K, V>> trees;
    private final int defaultTreeCount = 1024;

    @SuppressWarnings({"unchecked"})
    FixedCapacityTreeHashTable(){
        this.treeCount = defaultTreeCount;
        this.trees = new ArrayList<>(defaultTreeCount);
        for (int i = 0; i < 1024; i++) trees.add(new HashTree<>());
    }

    @Override
    public final int size() {
        return size;
    }

    @Override
    public final boolean isEmpty() {
        return size == 0;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public boolean containsValue(Object value) {
        final V casted = (V) value;
        for(HashTree<K,V> tree: trees){
            if(tree.containsValue(casted)) return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public boolean containsKey(Object key) {
        return trees.get(getTreeNumber((K) key)).find((K) key) != null;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public final V get(Object key) {
        return trees.get(getTreeNumber((K) key)).find((K) key).getValue();
    }

    @Override
    public V put(K key, V value) {
        final V old =  putIfAbsent(key, value);
        if(old != null) trees.get(getTreeNumber(key)).insert(key,value);
        return old;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public V remove(Object key) {
        HashTree<K,V> tree = trees.get(getTreeNumber((K) key));
        HashTree.TreeHashMapNode<K,V> node = tree.find((K) key);
        final V oldValue = (node != null ? node.value : null);
        if(node != null){
            tree.remove((K) key);
            size--;
            return oldValue;
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        super.putAll(m);
    }

    @Override
    public void clear() {
        for(HashTree tree: trees) tree.clear();
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        return super.keySet();
    }

    @Override
    public Collection<V> values() {
        return super.values();
    }

    private int getTreeNumber(K key){
        return (1 << 10) - 1 & key.hashCode();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Map.Entry<K,V>> setForReturn = new HashSet<>();
        (new TableIterator()).forEachRemaining(setForReturn::add);
        return setForReturn;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public V getOrDefault(Object key, V defaultValue) {
        HashTree.TreeHashMapNode<K,V> node = trees.get(getTreeNumber((K) key)).find((K) key);
        return node != null ? node.getValue() : defaultValue;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        (new TableIterator()).forEachRemaining((entry) -> action.accept(entry.getKey(),entry.getValue()));
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V putIfAbsent(K key, V value) {
        HashTree.TreeHashMapNode<K,V> node = trees.get(getTreeNumber(key)).find(key);
        final V old = (node != null ? node.value : null);
        if (old == null) {
            this.size++;
            trees.get(getTreeNumber(key)).insert(key, value);
            return null;
        }
        return old;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public boolean remove(Object key, Object value) {
        HashTree.TreeHashMapNode<K,V> node = trees.get(getTreeNumber((K) key)).find((K) key);
        if(key.equals(node.key) && value.equals(node.value)) {
            trees.get(getTreeNumber((K) key)).remove((K) key);
            this.size--;
            return true;
        }
        else return false;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        HashTree.TreeHashMapNode<K,V> node = trees.get(getTreeNumber(key)).find(key);
        if(node == null) return false;
        if(key.equals(node.key) && oldValue.equals(node.value)) {
            node.setValue(newValue);
            return true;
        }
        return false;
    }

    @Override
    public V replace(K key, V value) {
        HashTree.TreeHashMapNode<K,V> node = trees.get(getTreeNumber(key)).find(key);
        if(node == null) return null;
        final V oldValue = node.value;
        node.setValue(value);
        return oldValue;
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

    class TableIterator implements Iterator<Map.Entry<K,V>>{
        private Iterator<HashTree.TreeHashMapNode<K,V>> treeIterator;
        private Iterator<HashTree<K,V>> ai;

        TableIterator(){
            this.ai = trees.iterator();
            this.treeIterator = trees.get(0).iterator();
        }

        @Override
        public boolean hasNext() {
            while (!treeIterator.hasNext()) {
                if (!ai.hasNext()) return false;
                treeIterator = ai.next().iterator();
            }
            return treeIterator.hasNext();
        }

        @Override
        public Entry<K, V> next() {
            while (!treeIterator.hasNext()) {
                if (!ai.hasNext()) throw new NoSuchElementException();
                treeIterator = ai.next().iterator();
            }
            if(treeIterator.hasNext()) {
                HashTree.TreeHashMapNode<K,V> next = treeIterator.next();
                return new SimpleImmutableEntry<>(next.getKey(),next.getValue());
            }
            throw new NoSuchElementException();
        }

        @Override
        public void forEachRemaining(Consumer<? super Entry<K, V>> action) {
            if(action == null) throw new NullPointerException();
            while (this.hasNext()) action.accept(this.next());
        }

    }
}
