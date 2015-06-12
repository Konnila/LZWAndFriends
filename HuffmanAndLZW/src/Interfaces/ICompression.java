package Interfaces;

import java.io.File;
import java.io.IOException;

public interface ICompression {
    void decode(File file);
    void encode(File file);
}
