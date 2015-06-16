package Data;

/**
 * Very straightforward array implementation for compression.
 * Contains strings that are substrings of to-be-lzw-compressed file.
 * Heavily suited only for this programs compression needs.
 */
public class LZWStringArray {
    public final int SIZE = 4096;
    String[] array;
    
    public LZWStringArray() {
        array = new String[SIZE];
    }
    
    public boolean containsKey(String k) {
        if(k.length() > 1) {
            for (int i = 255; i < SIZE; i++) {
                if(array[i] != null && array[i].equals(k))
                    return true;
            }
            return false;
        }
        else {
            for (int i = 0; i < 256; i++) {
                if(array[i].equals(k))
                    return true;
                
            }
            return false;
        }
    }
    
    public void put(String str, int index) {
        array[index] = str;
    }
    
    public int get(String str) throws Exception {
        if (str.length() > 1) {
            for (int i = 255; i < SIZE; i++) {
                if (array[i] != null && array[i].equals(str)) {
                    return i;
                }
            }
            throw new Exception("help");
        } else {
            for (int i = 0; i < 256; i++) {
                if (array[i].equals(str)) {
                    return i;
                }

            }
            throw new Exception("help");
        }
    }
    
    @Override
    public String toString() {
        String toStr = "";
        for(int i = 0; i < SIZE; i++) {
            if(array[i] != null) {
                toStr += "index: " + i + " value: " + array[i];
            }
        }
        
        return toStr;
    }
}
