package Engines;

import Interfaces.ICompression;
import java.io.File;
import java.io.FileInputStream;
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
    private Map<Integer,String> intMap;
    
    private int mapSize;
    
    /**
     * Initializes the prefixMap (needed in encode) and intMap (needed in decode)
     */
    public LZWEngine() {
        prefixMap = new HashMap<>();
        
        //add all characters to the prefixmap
        for(int i = 0; i < 256; i++) 
            prefixMap.put("" + (char)i, i);
        
        // Build the intMap.
        intMap = new HashMap<>();
        for (int i = 0; i < 256; i++)
            intMap.put(i, "" + (char)i);
        
        mapSize = 256;
    }
    
    @Override
    public void decode(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(new File("./misc/outPut.txt"));
            List<Integer> inputs = new ArrayList<>();
            
            //read two inputs at a time = 24 bits = 3 bytes
            byte[] twoInputs = new byte[3];
            while(fis.read(twoInputs) != -1) {
                int first = decodeFirst(twoInputs);
                int second = decodeSecond(twoInputs);
                
                inputs.add(first);
                inputs.add(second);
            }
            
            
            String w = "" + (char)(int)inputs.remove(0);
            fos.write(w.getBytes());
            
            for (int k : inputs) {
                String entry;
                if(intMap.containsKey(k))
                    entry = intMap.get(k);
                else if(k == mapSize)
                    entry = w + w.charAt(0);
                else
                    throw new IllegalArgumentException("Bad compressed k: " + k);
                
                fos.write(entry.getBytes());
                
                // Add w+entry[0] to the dictionary.
                intMap.put(mapSize++, w + entry.charAt(0));
 
                w = entry;
            }
            
            //System.out.println(inputs);
                    
            fis.close();
            fos.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LZWEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LZWEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void encode(File file) {
        String w = "";
        List<Integer> result = new ArrayList<>();
        
        try {
            //max length
            char[] content = new char[624288000];
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
                    prefixMap.put(wc, mapSize++);
                    w = "" + c;
                }
            }
            
            // Output the code for w.
            if (!w.equals(""))
                result.add(prefixMap.get(w));
            
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
                //System.out.println("value: " + value);
                //System.out.println("asBinaryString javas: " + Integer.toBinaryString(value));

                boolean[] asTwelveBits = asBits(value);
                for(int i = 0; i < asTwelveBits.length; i++) {
                    if(asTwelveBits[i])
                        bs.set(++bitIndex);
                    else
                        bs.set(++bitIndex, false);
                }
            }
            
            //add magic integer zero as trailing
            if((bs.length() % 24) != 0)
                bs.set(++bitIndex, bitIndex+11, false);
            
            //System.out.println(bs.toString());
            
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
        
        String asBin = "";
        for (int i = 0; i < toReturn.length; i++) {
            if(toReturn[i])
                asBin += "1";
            else
                asBin += "0";
        }
        //System.out.println("value as binary: " + asBin);
        return toReturn;
    }

    private int decodeFirst(byte[] twoInputs) {
        int secondPart = twoInputs[1] & 0b00000000000000000000000000001111;
        secondPart <<= 8;
        
        int firstPart = twoInputs[0] & 0b00000000000000000000000011111111;
        
        return (firstPart | secondPart);
    }

    private int decodeSecond(byte[] twoInputs) {
        int firstPart = (twoInputs[1] & 0b00000000000000000000000011110000);
        firstPart >>= 4;
        
        int secondPart = (twoInputs[2] & 0b00000000000000000000000011111111);
        secondPart <<= 4;
        
        return (firstPart | secondPart);
    }
    
}
