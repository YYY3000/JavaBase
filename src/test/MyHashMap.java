package test;

/**
 * 实现hashmap
 * @author yinyiyun
 * @date 2019/6/28 16:05
 */
public class MyHashMap {

    private int[] data = new int[1000000];

    /**
     * Initialize your data structure here.
     */
    public MyHashMap() {
    }

    /**
     * value will always be non-negative.
     */
    public void put(int key, int value) {
        data[key] = value;
    }

    /**
     * Returns the value to which the specified key is mapped, or -1 if this map contains no mapping for the key
     */
    public int get(int key) {
        return data[key];
    }

    /**
     * Removes the mapping of the specified value key if this map contains a mapping for the key
     */
    public void remove(int key) {
    }

}
