package tests;

import Engines.HuffmanEngine;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Stolichnaya
 */
public class HuffmanEngineTests {
    public HuffmanEngine engine;
    
    public final String testFileName = "testFile.txt";
    public final String testInput = "jeesus christ overlord, I am not feeling very well";
    public String prefixTable;
    
    public HuffmanEngineTests() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        engine = new HuffmanEngine();
    }
    
    @After
    public void tearDown() {
        try {
            File file = new File(testFileName);
            file.delete();
        }
        //no file needed to delete
        catch(Exception e) {
            
        }
    }

    @Test
    public void testEncode_SunnyScenaario() {
        try {
            engine.encode(writeIntoFile(testFileName, testInput));
        } catch (IOException ex) {
            Assert.fail();
        }
    }
    
    public File writeIntoFile(String filename, String testData) throws FileNotFoundException, IOException {
        File file = new File(filename);
        FileOutputStream s = new FileOutputStream(file);
        
        s.write(testData.getBytes());
        
        s.close();
        
        return file;
    }
}
