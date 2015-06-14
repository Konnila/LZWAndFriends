package Engines;

import Data.HuffmanNode;
import Data.MinHeap;
import Interfaces.HuffmanPriorityQueue;
import Interfaces.ICompression;
import java.awt.PointerInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HuffmanEngine implements ICompression {
    private HuffmanPriorityQueue pq;
    private int[] freqTable;
    private String[] prefixTable = new String[256];

    public HuffmanEngine() {
        pq = new MinHeap(10000);
    }
    
    /**
     * Huffman encodes the given file and writes it into new file
     * @param file 
     */
    @Override
    public void encode(File file) {
        try {
            freqTable = buildFrequencyTable(file);
            
            //move into another method
             //insert orphans
             for (int i = 0; i < freqTable.length; i++) {
                 if (freqTable[i] > 0) {
                     pq.offer(new HuffmanNode((char)i, freqTable[i], null, null));
                 }
                    
             }
             
             while(pq.size() > 1)
                 pq.mergeNodes();
             
             //now the tree is in the first and only node of pqueue
             //next populate the prefixtable
             prefixTable = populatePrefixTable(pq.peek(), "");
             
             System.out.println("");
             printTree(pq.peek(), "");
             
             //write the compressed data into file
             writeIntoFile("testCompressed", prefixTable, file);
             
        } 
        catch (IOException ex) {
            Logger.getLogger(HuffmanEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    /**
    * builds frequency table for characters in a file. 
    * Stores into integer array where index is corresponding extended ascii code.
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
            //System.out.println("charINT: " + (int)node.getCharacter() + " = " + prefix);
            //prefixTable soon redundant
            prefixTable[node.getCharacter()] = prefix;
            
            return prefixTable;
        }
        
        else {
            //proceed recursively since this is not a leaf
            if(node.getLeftChild() != null)
                 populatePrefixTable( node.getLeftChild(), prefix + "0");
            if(node.getRightChild() != null)
                 populatePrefixTable( node.getRightChild(), prefix + "1");
        }
        
        return prefixTable;
    }
    
    
    /**
     * First writes header data (Huffman tree)
     * Writes the compressed data from file into a new compressed file
     * Prefixes are added bit by bit, and then trailing bits added if necessary.
     * @param filename
     * @param prefixTable
     * @param file 
     */
    public void writeIntoFile(String outputFilename, String[] prefixTable, File file) {
        try {
            File compressedFile = new File("./misc/" + outputFilename);
            try (FileReader reader = new FileReader(file)) {
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
                            System.out.println("prefixTable[0] = " + prefixTable[0]);
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
                    int remainder = (bitSetIndex-1) % 8;

                    if(remainder != 0) {
                        for(int i = 0; i < 8 - remainder; i++) {
                            bs.set(bitSetIndex, false);
                            bitSetIndex++;
                        }
                    }
                    
                    byte[] bytes = bs.toByteArray();
                    
                    int bytesUsed = writeHeaders(fileOutputStream, 8-remainder);

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
    
    //return the bytes used as int
    protected int writeHeaders(FileOutputStream s, int trailingBits) throws FileNotFoundException, IOException {
        int count = 0;
        String countString = "";
        long bytesUsed = 0;
        
        for (int i = 0; i < freqTable.length; i++) {
            if(freqTable[i] > 0)
                count++;
        }
        countString += count + "\n";
        
        s.write(countString.getBytes("UTF8"));
        bytesUsed += countString.getBytes("UTF8").length;
        
        for (int i = 0; i < freqTable.length; i++) {
            String outputString = "";
            if(freqTable[i] > 0) {
                outputString += i + " " + freqTable[i] + "\n";
                s.write(outputString.getBytes("UTF8"));
                bytesUsed += outputString.getBytes("UTF8").length;
            }
        }
        
        long bytesFromBytesUsedString = ("" + bytesUsed + "\n").getBytes("UTF8").length;
        
        long bytesFromTrailingBitsString =(""+ trailingBits+"\n").getBytes("UTF8").length;
        
        s.write(("" + trailingBits + "\n").getBytes("UTF8"));
        s.write((""+(bytesUsed+bytesFromBytesUsedString+bytesFromTrailingBitsString) + "\n").getBytes("UTF8"));
        
        return (int)bytesUsed;
    }
    
    @Override
    public void decode(File file) {
        freqTable = new int[256];
        FileReader r = null;
        
        try {
            FileInputStream fis = new FileInputStream(file);
            
            System.out.println("file length in bytes: " + file.length());
            r = new FileReader(file);
            BufferedReader reader = new BufferedReader(r);

            File outputFile = new File("./misc/outPut.txt");
            FileWriter writer = new FileWriter(outputFile);
            
            //this many lines after this line forms our prefixTable
            int headerLineCount = Integer.parseInt(reader.readLine());
            
            //so lets form the freqTable!
            for (int i = 0; i < headerLineCount; i++) {
                String[] entry = new String[2];
                String freqTableEntryData = reader.readLine();
                entry = freqTableEntryData.split(" ");
                
                freqTable[Integer.parseInt(entry[0])] = Integer.parseInt(entry[1]);
            }
            
            //and now the prefixTable
            //insert orphans
            for (int i = 0; i < freqTable.length; i++) {
                if (freqTable[i] > 0) {
                    pq.offer(new HuffmanNode((char) i, freqTable[i], null, null));
                }
            }  
            
            //construct the tree
            while(pq.size() > 1)
                pq.mergeNodes();
                
            prefixTable = populatePrefixTable(pq.peek(), "");
            int trailingBits = Integer.parseInt(reader.readLine());
            long compressedDataOffset = Long.parseLong( reader.readLine() );
            System.out.println("compressed data begins at: " + compressedDataOffset);
            
            fis.skip(compressedDataOffset);
            
            System.out.println("compr data = " + (file.length() - compressedDataOffset));
            
            readCompressedData(writer, fis, trailingBits, (int)(file.length() - compressedDataOffset));
            
            reader.close();
            r.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HuffmanEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HuffmanEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NumberFormatException | NullPointerException ex) {
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(HuffmanEngine.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                r.close();
            } catch (IOException ex) {
                Logger.getLogger(HuffmanEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    protected boolean[] decodeByte(char data) {
        boolean[] bits = new boolean[8];

        int masked = data;
        
        for(int i = 0; i < bits.length; i++) {
            bits[i] = (masked & 0b00000001) == 0b00000001;
            
            masked >>= 1;
        }
        
        return bits;
    }
    
    /**
     * For testing what the characters and prefixes associated to them are.
     * @param node
     * @param prefix 
     */
    public void printTree(HuffmanNode node, String prefix) {
        if(node.isLeafNode()) {
            System.out.print(node.getCharacter());
            System.out.println(" " + prefix);
            return;
        }
        
        if(node.getLeftChild() != null)
            printTree(node.getLeftChild(), prefix + "0");
        if(node.getRightChild() != null)
            printTree(node.getRightChild(), prefix + "1");
                
    }
    
    
    /**
     * Everything the reader reads here, should be compressed
     * @param reader 
     */
    private void readCompressedData(OutputStreamWriter writer, FileInputStream fis, int numberOfTrailingBits, int dataLength) throws IOException, Exception {
        //so yeah, this limits compressed file size a bit
        //so dont go all enterprise-y
        boolean[] barray = new boolean[8*dataLength+1];
        int arrayPointer = 0;
        
        long a,b,c,d;
        a = System.currentTimeMillis();
        while (true) {
            int data = fis.read();
            
            if(data == -1)
                break;
            
            boolean[] bits = decodeByte((char)data);
            
            for (int i = 0; i < bits.length; i++) {
                if (bits[i]) {
                    barray[arrayPointer] = true;
                    arrayPointer++;
                } else {
                    arrayPointer++;
                }
            }
        }
        b = System.currentTimeMillis();
        
        System.out.println("Some code 1 took "+(b-a)+"mil to execute. ("+((b-a)/1000)+" seconds)");
        
        for(int i = 0; i < numberOfTrailingBits-1; i++) {
            //array.remove(array.size()-1);
            arrayPointer--;
        }
            
        
        HuffmanNode root = pq.peek();
        HuffmanNode traverser = pq.peek();
        
        c = System.currentTimeMillis();
        
        for (int i = 0; i < arrayPointer; i++) {
            HuffmanNode r = traverser.getRightChild();
            HuffmanNode l = traverser.getLeftChild();
            
             if (barray[i]) {
                if(r.isLeafNode()) {
                    writer.append(r.getCharacter());
                    traverser = root;
                }
                else     
                    traverser = r;

            } else {
                 if(l.isLeafNode()) {
                     writer.append(l.getCharacter());
                     traverser = root;
                 }
                 else
                    traverser = l;
            }

        }
        
        d = System.currentTimeMillis();
        
        System.out.println("Some code 3 took "+(d-c)+"mil to execute. ("+((d-c)/1000)+" seconds)");
        
        writer.close();
        fis.close();
    }
}
