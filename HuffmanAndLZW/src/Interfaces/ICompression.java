package Interfaces;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface ICompression {
    void decode(File file);
    int[] buildFrequencyTable(File file) throws FileNotFoundException, IOException;
    void writeIntoFile(String filename);
}
