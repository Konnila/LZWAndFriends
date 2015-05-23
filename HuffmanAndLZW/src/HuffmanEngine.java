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
    
    //just a helper for now - recursive
    public void printCodes(HuffmanNode node) {
        if(node.isLeafNode()) {
            //do stuff
        }
        else {
            //proceed recursively
        }
    }

    @Override
    public void writeIntoFile(String filename) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }   
}
