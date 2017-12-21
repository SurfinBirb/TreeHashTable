
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class FixedCapacityTreeHashTableTest {

    static FixedCapacityTreeHashTable<String,String> testMap;
    static TreeMap<String, String> controlMap;

    @BeforeAll
    public static void before(){
        testMap = new FixedCapacityTreeHashTable<>();
        controlMap = new TreeMap<>();
        controlMap.put("key1","value1");
        controlMap.put("key2","value2");
        controlMap.put("key3","value3");
        controlMap.put("key4","value4");
        controlMap.put("key5","value5");
        controlMap.put("key6","value6");
        controlMap.put("key7","value7");
        controlMap.put("key8","value8");
        controlMap.put("key9","value9");
        testMap.putAll(controlMap);
    }

    @Test
    public void test(){
        controlMap.forEach((k,v) -> assertTrue(testMap.containsKey(k))); //contains
        assertEquals(testMap.size(),controlMap.size()); //size
        controlMap.forEach((k,v) -> assertEquals(testMap.get(k),v));//get
        testMap.clear();//clear
        assertTrue(testMap.isEmpty());//isEmpty
        controlMap.forEach((k,v) -> testMap.put(k,v));//put
        testMap.forEach((k,v) -> assertEquals(controlMap.get(k),v));//forEach
        testMap.entrySet().forEach(entry -> { //entrySet
            assertTrue(controlMap.containsValue(entry.getValue()));
            assertTrue(controlMap.containsKey(entry.getKey()));
        });
        controlMap.forEach((k,v) -> assertTrue(testMap.containsKey(k))); //containsKey
        System.out.println(testMap.get("key1"));
        System.out.println(testMap.get("key2"));
        System.out.println(testMap.get("key3"));
        System.out.println(testMap.get("key4"));
        System.out.println(testMap.get("key5"));
        System.out.println(testMap.get("key6"));
        System.out.println(testMap.get("key7"));
        System.out.println(testMap.get("key8"));
        System.out.println(testMap.get("key9"));
        testMap.forEach((k,v) -> System.out.println(k + " -> " + v));
        controlMap.forEach((k,v) -> assertTrue(testMap.containsValue(v))); //containsValue
        assertEquals(testMap.remove("key1"),"value1"); //remove
        assertEquals(testMap.size(), controlMap.size() - 1);
        controlMap.remove("key1");
        assertEquals(testMap.keySet(),controlMap.keySet());  //keySet
        assertTrue(testMap.values().containsAll(controlMap.values())); //values
        assertTrue(controlMap.values().containsAll(testMap.values()));
        assertEquals(testMap.replace("key9","nine"),"value9"); //replace
        assertEquals(testMap.get("key9"),"nine");
        assertFalse(testMap.replace("key9","noSuchValue","burp"));
        assertTrue(testMap.replace("key9","nine","newValue"));
        assertEquals(testMap.get("key9"),"newValue");
        assertEquals(testMap.putIfAbsent("key3","newValueFor3"),"value3"); //putIfAbsent
        assertEquals(testMap.get("key3"), "value3");
        assertNull(testMap.putIfAbsent("newKey","value"));
        assertEquals(testMap.get("newKey"),"value");
    }

}