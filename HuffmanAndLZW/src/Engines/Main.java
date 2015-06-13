package Engines;

import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    
    public static void main(String[] args) throws Exception {
        final HuffmanEngine hEngine = new HuffmanEngine();
        final LZWEngine lEngine = new LZWEngine();
        
        //remember to fix as more handles come
        if(args.length > 2) {
            throw new Exception("Too many arguments");
        }
        
        //check for file
        File file = new File("./misc/lzw.txt");
        File compressedFile = new File("./misc/lzwCompressed");
        
        if(!file.exists()) {
            throw new FileNotFoundException("Specified file does not exist");
        }
        
        
        //lzwOrHuffArgument = which compression to use
        String lzwOrHuffArgument = args[0];
        
        //argument = whether to decode or encode
        String argument = args[1];
        
        if(argument == null || argument.isEmpty() || lzwOrHuffArgument == null || lzwOrHuffArgument.isEmpty()) {
            throw new Error("not a valid switch");
        }
        
        switch (lzwOrHuffArgument) {
            case "-h":
                switch (argument) {
                    case "-e":
                        hEngine.encode(file);
                        break;
                    
                    case "-d":
                        hEngine.decode(compressedFile);
                        break;
                    default:
                        throw new Error("Unknown argument. Use -d (decode) or -e (encode)");
                }
                break;
            case "-l":
                switch(argument) {
                    case "-e":
                        lEngine.encode(file);
                        break;
                    case "-d":
                        lEngine.decode(compressedFile);
                        break;
                    default:
                        throw new Error("Unknown argument. Use -d (decode) or -e (encode)");
                }
                break;
                
            default:
                throw new Error("Invalid arguments");
        }
        
    }
    
}
