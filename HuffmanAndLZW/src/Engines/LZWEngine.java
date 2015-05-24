package Engines;

import Interfaces.ICompression;
import java.io.File;

public class LZWEngine implements ICompression {

    @Override
    public void decode(File file) {
        throw new UnsupportedOperationException("Have some patience"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void encode(File file) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void writeIntoFile(String filename, String[] prefixtable, File file) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
