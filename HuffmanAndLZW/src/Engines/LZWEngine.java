package Engines;

import Interfaces.ICompression;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LZWEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LZWEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void writeIntoFile(String filename, String[] prefixtable, File file) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
