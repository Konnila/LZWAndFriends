import Data.HuffmanNode;
import Interfaces.ICompression;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HuffmanEngine implements ICompression {

    private PriorityQueue<HuffmanNode> pqueue;
    
    @Override
    public void decode(File file) {
        try {
            int[] freqTable = buildFrequencyTable(file);
            String[] prefixTable;
            pqueue = new PriorityQueue<>();
            
             //insert orphans
             for (int i = 0; i < freqTable.length; i++) {
                 if (freqTable[i] > 0)
                    pqueue.offer(new HuffmanNode((char)i, freqTable[i], null, null));
             }
             
             //construct the tree
             while(pqueue.size() > 1) {
                 //two least significant nodes
                 HuffmanNode first = pqueue.poll();
                 HuffmanNode second = pqueue.poll();
                 
                 pqueue.offer(new HuffmanNode('\u0000', first.getFrequency() + second.getFrequency(), first, second));
             }
             
             //now the tree is in the first and only node of pqueue
             //next populate the prefixtable
             prefixTable = populatePrefixTable(new String[256], pqueue.poll(), "");
             
             //remove, just for test
             System.out.println("" + prefixTable.length);
             for(int i = 0; i < 256; i++) {
                 if(prefixTable[i] != null && !prefixTable[i].isEmpty()) {
                     System.out.println("char: " + 
                             (char)i + "\n" + "PREFIX: " + prefixTable[i] + "\n");
                 }
             }
        } 
        catch (IOException ex) {
            Logger.getLogger(HuffmanEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.exit(0);
    }
    
    @Override
    public int[] buildFrequencyTable(File file) throws FileNotFoundException, IOException {
        FileReader reader = new FileReader(file);
        
        int[] charFreqs = new int[256];
            while (true) {
                int c = reader.read();
                
                //End of file
                if(c < 0)
                    break;
                
                charFreqs[c]++;
            }
        return charFreqs;
    }
    
    protected String[] populatePrefixTable(String[] prefixTable, HuffmanNode node, String prefix) {
        if(node.isLeafNode()) {
            prefixTable[(int)node.getCharacter()] = prefix + "FREQ" + node.getFrequency();
            return prefixTable;
        }
        else {
            //proceed recursively since this is not a leaf
            populatePrefixTable(prefixTable, node.getLeftChild(), prefix + "0");
            populatePrefixTable(prefixTable, node.getRightChild(), prefix + "1");
        }
        
        //should never get here
        return prefixTable;
    }

    @Override
    public void writeIntoFile(String filename) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }   
}
