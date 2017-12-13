import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashTreeTest {
    @Test
    public void hashTreeTest(){
        HashTree<Integer,String> tree = new HashTree<>();
        tree.insert(7,"seven");
        tree.insert(5,"five");
        tree.insert(6,"six");
        tree.insert(1,"one");
        tree.remove(5);
        tree.remove(1);
        assertEquals(tree.find(6).getValue(),"six");
    }
}