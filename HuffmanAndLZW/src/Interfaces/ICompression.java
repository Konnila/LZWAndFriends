package Interfaces;

import java.io.File;
import java.io.IOException;

public interface ICompression {
    void decode(File file);
    void writeIntoFile(String filename, String[] prefixtable, File file);
}
