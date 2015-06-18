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

public class HuffmanEngineTests {
    public HuffmanEngine engine;
    
    public final String testFileName = "testFile.txt";
    public final String testInput = "jeesus christ overlord 123 abcdefsjgh12%";
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
            if(file.exists())
                file.delete();
            
            File file2 = new File("COMPRESSED" + file.getName());
            if(file2.exists())
                file2.delete();
            
            File file3 = new File("uncompressed_" + file2.getName());
            if(file3.exists())
                file3.delete();
        }
        //no file needed to delete
        catch(Exception e) {
            
        }
    }

    @Test
    public void testEncodeAndDecode_SunnyScenaario() {
        try {
            File testFile = writeIntoFile(testFileName, testInput);
            
            System.out.println(testFile.length());
            
            engine.encode(testFile);
            
            
            File compressed = new File("COMPRESSED" + testFile.getName());
            
            System.out.println(compressed.length());
            
            engine = new HuffmanEngine();
            
            engine.decode(compressed);
            
            File uncompressed = new File("uncompressed_"+compressed.getName());
            
            System.out.println(uncompressed.length());
            
            Assert.assertEquals(uncompressed.length(), testFile.length());
            
            
        } catch (IOException e) {
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
