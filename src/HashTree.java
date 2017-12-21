import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class HashTree<K extends Comparable<K>,V> {
    private TreeHashMapNode<K,V> root;

    HashTree(){
        root = null;
    }

    private HashTree(TreeHashMapNode<K,V> root){ //TODO : head tree, tail tree
        this.root = root;
    }

    public void forEach(BiConsumer<? super K, ? super V> action){
        if(action == null) throw new NullPointerException();
        this.iterator().forEachRemaining((node) -> action.accept(node.getKey(), node.getValue()));
    }

    public void insert(K key, V value){
        root = insert(root, key, value);
    }

    public void remove(K key){
        root = remove(root,key);
    }

    public void clear(){
        root = null;
    }

    public TreeHashMapNode<K,V> find(K key){
        return findNode(root,key);
    }

    public boolean containsValue(V value){
        Iterator<TreeHashMapNode<K,V>> it =  this.iterator();
        TreeHashMapNode<K,V> node = null;
        while (it.hasNext()){
            node = it.next();
            if(node.value == value) return true;
        }
        return false;
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    private TreeHashMapNode<K,V> insert(TreeHashMapNode<K,V> root, K key, V value){
        if(root == null) return new TreeHashMapNode<>(key, value);
        if(key.hashCode() < root.hash) root.left = insert(root.left, key, value);
        if(key.hashCode() > root.hash) root.right = insert(root.right, key, value);
        if(key.hashCode() == root.hash){
            int compare = key.compareTo(root.key);
            if(compare < 0) root.left = insert(root.left, key, value);
            if(compare > 0) root.right = insert(root.right, key,value);
            if(compare == 0){
                root.setValue(value);
            }
        }
        return balance(root);
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    private TreeHashMapNode<K,V> remove(TreeHashMapNode<K,V> root, K key){
        if(root == null) return null;
        if(key.hashCode() < root.hash) remove(root.left, key);
        if(key.hashCode() > root.hash) remove(root.right, key);
        if(key.hashCode() == root.hash){
            int compare = key.compareTo(root.key);
            if(compare < 0) remove(root.left, key);
            if(compare > 0) remove(root.right, key);
            if(compare == 0){
                TreeHashMapNode<K,V> left = root.left;
                TreeHashMapNode<K,V> right = root.right;
                root = null;
                if(right == null) return left;
                TreeHashMapNode<K,V> minimal = minimal(right);
                minimal.right = removeMinimalNode(right);
                minimal.left = left;
                return balance(minimal);
            }
        }
        return balance(root);
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    private TreeHashMapNode<K,V> findNode(TreeHashMapNode<K,V> root, K key){
        if(root == null) return null;
        if(key.hashCode() == root.hash){
            int compare = key.compareTo(root.key);
            if(compare == 0) return root;
            if(compare < 0) findNode(root.left, key);
            if(compare > 0) findNode(root.right, key);
        }
        if(key.hashCode() < root.hash) findNode(root.left, key);
        if(key.hashCode() > root.hash) findNode(root.right, key);
        return null;
    }

    private TreeHashMapNode<K,V> minimal(TreeHashMapNode<K,V> root){
        if (root.left != null) return minimal(root.left);
        else return root;
    }

    private TreeHashMapNode<K,V> removeMinimalNode(TreeHashMapNode<K,V> root){
        if(root.left == null) return root.right;
        root.right = removeMinimalNode(root.left);
        return balance(root);
    }

     private int height(TreeHashMapNode<K,V> node){
        if (node != null) return node.height;
        else return 0;
    }

    private int nodeBalance(TreeHashMapNode<K,V> node){
        return height(node.right) - height(node.left);
    }

    private void fixHeight(TreeHashMapNode<K,V> node){
        int heightLeft = height(node.left);
        int heightRight = height(node.right);
        node.height = (heightLeft > heightRight ? heightLeft: heightRight) + 1;
    }

    private TreeHashMapNode<K,V> rotateRight(TreeHashMapNode<K,V> node){
        TreeHashMapNode<K,V> left = node.left;
        node.left = left.right;
        left.right = node;
        fixHeight(node);
        fixHeight(left);
        return left;
    }

    private TreeHashMapNode<K,V> rotateLeft(TreeHashMapNode<K,V> node){
        TreeHashMapNode<K,V> right = node.right;
        node.right = right.left;
        right.left = node;
        fixHeight(node);
        fixHeight(right);
        return right;
    }

    private TreeHashMapNode<K,V> balance(TreeHashMapNode<K,V> node){
        fixHeight(node);
        if(nodeBalance(node) == 2){
            if(nodeBalance(node.right) < 0) node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        if(nodeBalance(node) == -2){
            if(nodeBalance(node.left) > 0) node.left = rotateLeft(node.left);
            return  rotateRight(node);
        }
        return node;
    }

    public Iterator<TreeHashMapNode<K,V>> iterator(){
        return new HashTreeNodeIterator(this.root);
    }

    class HashTreeNodeIterator implements Iterator<TreeHashMapNode<K,V>>{

        private ArrayDeque<TreeHashMapNode<K,V>> deque;
        private  TreeHashMapNode<K,V> current;

        HashTreeNodeIterator(TreeHashMapNode<K,V> root){
            this.deque = new ArrayDeque<>();
            this.current = root;
        }

        @Override
        public boolean hasNext() {
            return !deque.isEmpty() || current != null;
        }

        @Override
        public TreeHashMapNode<K, V> next() {
            while(current != null){
                deque.addLast(current);
                current = current.left;
            }
            current = deque.pollLast();
            final TreeHashMapNode<K,V> next = current;
            current = current.right;
            return next;
        }

        @Override
        public void forEachRemaining(Consumer<? super TreeHashMapNode<K, V>> action) {
            if(action == null) throw new NullPointerException();
            while (this.hasNext()) action.accept(this.next());
        }
    }

    public static class TreeHashMapNode<K extends Comparable<K>,V> implements Map.Entry<K, V> {
        int hash;
        final K key;
        V value;
        int height;

        TreeHashMapNode<K,V> left;
        TreeHashMapNode<K,V> right;

        TreeHashMapNode(K key, V value){
            this.key = key;
            this.value = value;
            this.hash = key.hashCode();
            this.height = 1;
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        @SuppressWarnings({"unchecked"})
        public boolean equals(Object o) {
            if (!this.getClass().equals(o.getClass())) return false;
            HashTree.TreeHashMapNode<K,V> other = (HashTree.TreeHashMapNode<K,V>) o;
            return key.equals(other.key) && value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }
}
