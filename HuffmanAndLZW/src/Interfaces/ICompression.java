package Interfaces;

import java.io.File;

public interface ICompression {
    void decode(File file);
    void encode(File file);
}
