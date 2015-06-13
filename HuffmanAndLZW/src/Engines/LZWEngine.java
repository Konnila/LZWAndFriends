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
    private int prefixMapSize;
    
    /**
     * Initializes the prefixMap which is needed in both encode and decode
     */
    public LZWEngine() {
        prefixMap = new HashMap<>();
        
        //add all characters to the map
        for(int i = 0; i < 256; i++) 
            prefixMap.put(String.valueOf((char)i), i);
        
        prefixMapSize = 256;
    }
    
    @Override
    public void decode(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            
            byte[] twoInputs = new byte[3];
            fis.read(twoInputs);
            
            System.out.println("first8bits as bin string java: " + Integer.toBinaryString(twoInputs[0] &
                    0b00000000000000000000000011111111));
            System.out.println("first8bits: " + (twoInputs[0] & 0b00000000000000000000000011111111));
            System.out.println("second8bits: " + Integer.toBinaryString(twoInputs[1]));
            
            int first = decodeFirst(twoInputs);
            
            System.out.println("first: " + first);
            int second = decodeSecond(twoInputs);
            
            
            fis.close();
            
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
                System.out.println("value: " + value);
                System.out.println("asBinaryString javas: " + Integer.toBinaryString(value));

                boolean[] asTwelveBits = asBits(value);
                for(int i = 0; i < asTwelveBits.length; i++) {
                    if(asTwelveBits[i])
                        bs.set(++bitIndex);
                    else
                        bs.set(++bitIndex, false);
                }
            }
            
            //add magic integer zero as trailing
            if((bs.length() % 8) != 0)
                bs.set(++bitIndex, bitIndex+7, false);
            
            System.out.println(bs.toString());
            
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
        System.out.println("value as binary: " + asBin);
        return toReturn;
    }

    private int decodeFirst(byte[] twoInputs) {
        int secondPart = twoInputs[1] & 0b00000000000000000000000000001111;
        secondPart <<= 8;
        
        System.out.println("secondPart: " + Integer.toBinaryString(secondPart));
        
        int firstPart = twoInputs[0] & 0b00000000000000000000000011111111;
        
        System.out.println("a " + Integer.toBinaryString(firstPart | secondPart));
        return (firstPart | secondPart);
    }

    private int decodeSecond(byte[] twoInputs) {
        int firstPart = (twoInputs[1] & 0b00001111);
        firstPart <<= 8;
        
        return (firstPart | twoInputs[2]);
    }
    
}
