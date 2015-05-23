import Data.HuffmanLeaf;
import Interfaces.ICompression;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HuffmanEngine implements ICompression {

    private PriorityQueue<HuffmanLeaf> pqueue;
    
    @Override
    public void decode(File file) {
        try {
            int[] freqTable = buildFrequencyTable(file);
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
    
}
