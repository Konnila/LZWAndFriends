package Data;

public class IntToStringMap {
    private int size;
    private final int INITIAL_SIZE = 4096;
    private IntStringKeyValuePair[] table;
    
    public IntToStringMap() {
        size = 0;
        table = new IntStringKeyValuePair[INITIAL_SIZE];
    }
    
    public String get(int key) {
        int hash = key;
        IntStringKeyValuePair kv = table[hash];
        
        return kv.getValue();
    }
    
    public boolean containsKey(int key) {
        if(key > table.length || table[key] == null)
            return false;
        
        return true;
    }
    
    public void put(int key, String value) {
        if(size >= table.length)
            doubleSize();
        
        table[key] = new IntStringKeyValuePair(key, value);
    }

    private void doubleSize() {
        IntStringKeyValuePair[] tempTable = new IntStringKeyValuePair[size*2];
        
        for (int i = 0; i < size; i++) {
            tempTable[i] = table[i];
        }
        
        table = tempTable;
    }
    
    public int size() {
        return size;
    }
    
    
}
