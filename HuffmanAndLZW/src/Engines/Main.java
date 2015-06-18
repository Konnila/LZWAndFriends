package Engines;

import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    
    public static void main(String[] args) throws Exception {
        final HuffmanEngine hEngine = new HuffmanEngine();
        final LZWEngine lEngine = new LZWEngine();
        
        //remember to fix as more handles come
        if(args.length > 3) {
            throw new Exception("Too many arguments");
        }
        
        //check for file
        File file = new File("./misc/lzw.txt");
        File lzwTest = new File("./misc/lirum.txt");
        File compressedFile = new File("./misc/lzwCompressed");
        File hCompressedFile = new File("./misc/testCompressed");
        
//        if(!file.exists()) {
//            throw new FileNotFoundException("Specified file does not exist");
//        }
        
        
        //lzwOrHuffArgument = which compression to use
        String lzwOrHuffArgument = args[0];
        
        //argument = whether to decode or encode
        String argument = args[1];
        
        String pathToFileArg = args[2];
        
        System.out.println(pathToFileArg);
        
        //specified in arguments. A file to compress, or file to decompress
        File chosenFile = new File(pathToFileArg);
        
        if(!chosenFile.exists())
            throw new FileNotFoundException("Specified file does not exist");
        
        if(argument == null || argument.isEmpty() || lzwOrHuffArgument == null || lzwOrHuffArgument.isEmpty()) {
            throw new Error("not a valid switch");
        }
        long start = System.currentTimeMillis();
        
        switch (lzwOrHuffArgument) {
            case "-h":
                switch (argument) {
                    case "-e":
                        hEngine.encode(chosenFile);
                        break;
                    
                    case "-d":
                        hEngine.decode(chosenFile);
                        break;
                    default:
                        throw new Error("Unknown argument. Use -d (decode) or -e (encode)");
                }
                break;
            case "-l":
                switch(argument) {
                    case "-e":
                        lEngine.encode(chosenFile);
                        break;
                    case "-d":
                        lEngine.decode(chosenFile);
                        break;
                    default:
                        throw new Error("Unknown argument. Use -d (decode) or -e (encode)");
                }
                break;
                
            default:
                throw new Error("Invalid arguments");
        }
        
        long end = System.currentTimeMillis();
        
        System.out.println("Operation took: " + (end-start) + "ms" + " (" + ((end-start)/1000) + " seconds)");
        
    }
    
}
