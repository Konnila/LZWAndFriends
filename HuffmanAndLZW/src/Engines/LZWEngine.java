package Engines;

import Interfaces.ICompression;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LZWEngine implements ICompression {
    
    private Map<String,Integer> prefixMap;
    private int prefixMapSize;
    
    public LZWEngine() {
        prefixMap = new HashMap<>();
        
        //add all characters to the map
        for(int i = 0; i < 256; i++) 
            prefixMap.put(String.valueOf((char)i), i);
        
        prefixMapSize = 256;
    }
    
    @Override
    public void decode(File file) {
        throw new UnsupportedOperationException("Have some patience"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void encode(File file) {
        String w = "";
        List<Integer> result = new ArrayList<>();
        
        try {
            //max length = 500mb
            char[] content = new char[524288000];
            FileReader reader = new FileReader(file);
            reader.read(content);
            
            for (char c : content) {
                if(c == '\u0000')
                    break;
                
                String wc = w + c;
                
                if(prefixMap.containsKey(wc))
                    w = wc;
                else {
                    result.add(prefixMap.get(w));
                    prefixMap.put(wc, prefixMapSize++);
                    w = String.valueOf(c);
                }
            }
            
            // Output the code for w.
            if (!w.equals(""))
                result.add(prefixMap.get(w));
            
            System.out.println(result);
            
            reader.close();
            
            writeIntoFile("lzwCompressed", result);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LZWEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LZWEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }


    public void writeIntoFile(String filename, List<Integer> values) throws FileNotFoundException, IOException {
        File file = new File("./misc/" + filename);
        try (FileOutputStream fis = new FileOutputStream(file)) {
            BitSet bs = new BitSet();
            int bitIndex = -1;
            
            for (int value :  values) {
                boolean[] asTwelveBits = asBits(value);
                for (boolean bit : asTwelveBits) {
                    if(bit)
                        bs.set(bitIndex++);
                    else
                        bs.set(bitIndex++, false);
                }
            }
            
            //add magic integer zero as trailing
            if((bs.length() % 8) != 0)
                bs.set(bitIndex++, bitIndex+7, false);
            
            fis.write(bs.toByteArray());
        }
    }

    private boolean[] asBits(int value) {
        boolean[] toReturn = new boolean[12];
        
        int integer = value;
        
        for (int i = 0; i < toReturn.length; i++) {
            toReturn[i] = (integer & 1) == 1;
            
            integer >>= 1;
        }
        
        return toReturn;
    }
    
}
