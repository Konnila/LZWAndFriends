package Engines;

import Data.*;
import Interfaces.ICompression;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Engine that contains everything needed to LZW encode and decode a file
 */
public class LZWEngine implements ICompression {
    private IntToStringMap intToStrMap;
    private LZWStringArray sarray;
    
    private int mapSize;
    
    /**
     * Initializes the LZW String array (needed in encode) and Integer to String map (needed in decode)
     */
    public LZWEngine() {
        intToStrMap = new IntToStringMap();
        sarray = new LZWStringArray();
        
        // Build the intMap.
        for (int i = 0; i < 256; i++) {
            intToStrMap.put(i, ""+(char)i);
            sarray.put(""+ (char)i, i);
        }
            
        mapSize = 256;
    }
    
    /**
     * LZW-decodes the given file. Creates a new file where all the data is uncompressed
     * @param file 
     */
    @Override
    public void decode(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(new File("./misc/outPut.txt"));
            IntegerList listOfInputs = new IntegerList();
            
            //read two inputs at a time = 24 bits = 3 bytes
            byte[] twoInputs = new byte[3];
            while(fis.read(twoInputs) != -1) {
                int first = decodeFirst(twoInputs);
                int second = decodeSecond(twoInputs);
                
                listOfInputs.add(first);
                listOfInputs.add(second);
            }
            
            
            String w = "" + (char)(int)listOfInputs.get(0);
            fos.write(w.getBytes());
            
            for (int i = 1; i < listOfInputs.size(); i++) {
                String entry;
                if(intToStrMap.containsKey(listOfInputs.get(i)))
                    entry = intToStrMap.get(listOfInputs.get(i));
                else if(listOfInputs.get(i) == intToStrMap.size())
                    entry = w + w.charAt(0);
                else
                    throw new IllegalArgumentException("Bad compressed k: " + listOfInputs.get(i));
                
                fos.write(entry.getBytes());
                
                // Add w+entry[0] to the dictionary.
                intToStrMap.put(mapSize++, w + entry.charAt(0));
 
                w = entry;
            }
                    
            fis.close();
            fos.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LZWEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LZWEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * LZW encodes a given file. Creates a new file that consists of compressed data.
     * @param file 
     */
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
                
                if(sarray.containsKey(wc)) {
                    w = wc;
                }
                else {
                    result.add(sarray.get(w));
                    sarray.put(wc, mapSize++);
                    w = "" + c;
                }
                
            }
            
            if(!w.equals(""))
                result.add(sarray.get(w));
            
            reader.close();

            writeIntoFile("lzwCompressed", result);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LZWEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LZWEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(LZWEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    /**
     * writes the compressed data into a new file
     * @param filename
     * @param values
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void writeIntoFile(String filename, List<Integer> values) throws FileNotFoundException, IOException {
        File file = new File("./misc/" + filename);
        try (FileOutputStream fis = new FileOutputStream(file)) {
            BitSet bs = new BitSet();
            int bitIndex = -1;
            
            for (int value :  values) {
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
            
            fis.write(bs.toByteArray());
        }
    }
    
    /**
     * gives bit representation of the given integer as boolean array
     * @param value
     * @return 
     */
    private boolean[] asBits(int value) {
        boolean[] toReturn = new boolean[12];
        
        int integer = value;
        
        for (int i = 0; i < toReturn.length; i++) {
            toReturn[i] = (integer & 1) == 1;
            
            integer >>= 1;
        }

        return toReturn;
    }

    /**
     * program reads 3 bytes of compressed data at a time.
     * This method extracts the first 12bit integer from those bytes
     * @param twoInputs
     * @return 
     */
    private int decodeFirst(byte[] twoInputs) {
        int secondPart = twoInputs[1] & 0b00000000000000000000000000001111;
        secondPart <<= 8;
        
        int firstPart = twoInputs[0] & 0b00000000000000000000000011111111;
        
        return (firstPart | secondPart);
    }

    /**
     * program reads 3 bytes of compressed data at a time.
     * This method extracts the second (and last) 12bit integer from those bytes
     * @param twoInputs
     * @return 
     */
    private int decodeSecond(byte[] twoInputs) {
        int firstPart = (twoInputs[1] & 0b00000000000000000000000011110000);
        firstPart >>= 4;
        
        int secondPart = (twoInputs[2] & 0b00000000000000000000000011111111);
        secondPart <<= 4;
        
        return (firstPart | secondPart);
    }
    
}
