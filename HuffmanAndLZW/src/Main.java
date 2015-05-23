import java.io.File;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws Exception {
        //check for file
        File file = new File("../misc/test.txt");
        
        if(!file.exists()) {
            throw new FileNotFoundException("Specified file does not exist");
        }
        
        if(args.length > 1) {
            throw new Exception("Too many arguments");
        }
        
        //argument = whether to decode or encode
        String argument = args[0];
        
        if(argument == null || argument.isEmpty()) {
            throw new Error("not a valid switch");
        }
        
        switch (argument) {
            case "-d": 
                System.out.println("File found and switch valid");
                System.out.println("Filename: " + file.getName());
                
                HuffmanEngine engine = new HuffmanEngine();
                
                engine.decode(file);
                
                break;
            case "-e":
                break;
        }
        
    }
    
}
