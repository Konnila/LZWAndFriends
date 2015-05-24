package Engines;

import Data.HuffmanNode;
import Interfaces.ICompression;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.BitSet;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HuffmanEngine implements ICompression {

    private PriorityQueue<HuffmanNode> pqueue;
    private int[] freqTable;
    private String[] prefixTable = new String[256];
    
    /**
     * Huffman encodes the given file and writes it into new file
     * @param file 
     */
    @Override
    public void encode(File file) {
        try {
            freqTable = buildFrequencyTable(file);
            pqueue = new PriorityQueue<>();
            
             //insert orphans
             for (int i = 0; i < freqTable.length; i++) {
                 if (freqTable[i] > 0)
                    pqueue.offer(new HuffmanNode((char)i, freqTable[i], null, null));
             }
             
             //construct the tree
             while(pqueue.size() > 1) {
                 //two least significant nodes TODO: check what to do if one is null
                 HuffmanNode first = pqueue.poll();
                 HuffmanNode second = pqueue.poll();
                 
                 pqueue.offer(new HuffmanNode('\u0000', first.getFrequency() + second.getFrequency(), first, second));
             }
             
             //now the tree is in the first and only node of pqueue
             //next populate the prefixtable
             prefixTable = populatePrefixTable(pqueue.poll(), "");
             
             int amountOfNulls = 0;
             for (String prefix : prefixTable) {
                 if(prefix == null) {
                     amountOfNulls++;
                     System.out.println("NULL " + amountOfNulls);
                     continue;
                 }
                     
                 if(!prefix.isEmpty())
                    System.out.println("prefix: " + prefix);
            }
             
             //write the compressed data into file
             writeIntoFile("testCompressed", prefixTable, file);
        } 
        catch (IOException ex) {
            Logger.getLogger(HuffmanEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    /**
    * builds frequency table for characters in a file. 
    * Stores into integerarray where index is corresponding extended ascii code.
    *  Value in that index is the amount of that ascii code in file
    * @param file
    */
    protected int[] buildFrequencyTable(File file) throws FileNotFoundException, IOException {
        int[] charFreqs;
        try (FileReader reader = new FileReader(file)) {
            charFreqs = new int[256];
            
            while (true) {
                int c = reader.read();
                
                //End of file
                if(c < 0) {
                    break;
                }
                
                charFreqs[c]++;
            }
        }
        
        return charFreqs;
    }
    /**
     * Fills the prefix table into String array. The index is the character in 
     * alphabet and the value in that index is the prefix for that character.
     * @param node
     * @param prefix
     * @return 
     */
    protected String[] populatePrefixTable(HuffmanNode node, String prefix) {
        if(node.isLeafNode()) {
            prefixTable[(int)node.getCharacter()] = prefix;
            return prefixTable;
        }
        else {
            //proceed recursively since this is not a leaf
            if(node.getLeftChild() != null)
                populatePrefixTable( node.getLeftChild(), prefix + "0");
            if(node.getRightChild() != null)
                populatePrefixTable( node.getRightChild(), prefix + "1");
        }
        
        //should never get here, but this java(1.7.0_01) or netbeans(8.0.2) version handles recursion poorly
        //hence the ugly redundant return 
        return prefixTable;
    }
    
    /**
     * Writes the compressed data from file into a new compressed file
     * Prefixes are added bit by bit, and then trailing bits added if necessary.
     * @param filename
     * @param prefixTable
     * @param file 
     */
    @Override
    public void writeIntoFile(String filename, String[] prefixTable, File file) {
        try {
            File compressedFile;
            try (FileReader reader = new FileReader(file)) {
                compressedFile = new File("../misc/" + filename);
                try (FileOutputStream fileOutputStream = new FileOutputStream(compressedFile)) {
                    BitSet bs = new BitSet();
                    int bitSetIndex = 0;
                    
                    while(true) {
                        int c = reader.read();
                        
                        if(c < 0) {
                            break;
                        }
                        
                        String prefix = prefixTable[c];
                        
                        //something is wrong, fix this
                        if(prefix == null) {
                            //this is probably faulty behavior. Check the code
                            System.out.println("Prefix is null. Read: " + (char)c + "which has charcode: " + c);
                            continue;
                        }
                        
                        //add the characters in prefix string one by one
                        //and add them as bits
                        for(int i = 0; i < prefix.length(); i++) {
                            if (prefix.charAt(i) == '0') {
                                bs.set(bitSetIndex, false);
                                bitSetIndex++;
                            }
                            else {
                                bs.set(bitSetIndex, true);
                                bitSetIndex++;
                            }
                        }
                    }
                    
                    // add trailing bits if needed
                    int remainder = bitSetIndex % 8;
                    if(remainder != 0) {
                        for(int i = 0; i < 8 - remainder; i++) {
                            bs.set(bitSetIndex, false);
                        }
                        
                        bs.set(bitSetIndex);
                    }
                    
                    byte[] bytes = bs.toByteArray();
                    fileOutputStream.write(bytes);
                }
            }
            
            //remove these, just for testing
            long percentage = (file.length() - compressedFile.length());
            double comparingPercentage = (percentage*100.0 /  file.length());
            
            System.out.println("Original file length (bytes): " + file.length());
            System.out.println("Compressed file length (bytes): " + compressedFile.length());
            System.out.println("Compressed file is " + (int)comparingPercentage + "% smaller than original");
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HuffmanEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HuffmanEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void decode(File file) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public int[] getFreqTable() {
        return freqTable;
    }
    
    public PriorityQueue<HuffmanNode> getPqueue() {
        return pqueue;
    }
}
