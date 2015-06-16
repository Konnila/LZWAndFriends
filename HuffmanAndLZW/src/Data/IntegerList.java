package Data;

/**
 * Very basic integer array. To replace ArrayList.
 * Only contains methods needed for the compression.
 * @author Stolichnaya
 */
public class IntegerList {
    private int[] array;
    private int size;
    private final int INITIAL_SIZE = 100;
    
    public IntegerList() {
        size = 0;
        array = new int[INITIAL_SIZE];
    }
    
    public void add(int entry) {
        if(size >= array.length)
            doubleSize();
        
        array[size] = entry;
        size++;
    }
    
    public int get(int index) {
        return array[index];
    }

    private void doubleSize() {
        int[] tempArray = new int[size*2];
        System.arraycopy(array, 0, tempArray, 0, size);
        
        array = tempArray;
    }
    
    public int size() {
        return size;
    }
}
