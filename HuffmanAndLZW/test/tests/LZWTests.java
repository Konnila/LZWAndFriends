/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import Engines.LZWEngine;
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
import static org.junit.Assert.*;

/**
 *
 * @author Stolichnaya
 */
public class LZWTests {
    public LZWEngine lengine;
    public final String testFileName = "testFile.txt";
    public final String testInput = "jeesus christ overlord 123 abcdefsjgh12";
    
    public LZWTests() {
        lengine = new LZWEngine();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        
    }
    
    @After
    public void tearDown() {
        try {
            File file = new File(testFileName);
            if(file.exists())
                file.delete();
            
            File file2 = new File("LZWCOMPRESSED" + file.getName());
            if(file2.exists())
                file2.delete();
            
            File file3 = new File("lzwuncompressed_" + file2.getName());
            if(file3.exists())
                file3.delete();
        }
        //no file needed to delete
        catch(Exception e) {
            
        }
    }
    
    @Test
    public void testEncodeAndDecodeSunnyScenario() throws IOException {
        File testFile = writeIntoFile(testFileName, testInput);
        
        lengine.encode(testFile);
        
        File compressed = new File("LZWCOMPRESSED" + testFile.getName());
        
        lengine = new LZWEngine();
            
        lengine.decode(compressed);
        
        File uncompressed = new File("lzwuncompressed_"+compressed.getName());
        
        Assert.assertEquals(uncompressed.length(), testFile.length());
        
    }
    
    public File writeIntoFile(String filename, String testData) throws FileNotFoundException, IOException {
        File file = new File(filename);
        FileOutputStream s = new FileOutputStream(file);
        
        s.write(testData.getBytes());
        
        s.close();
        
        return file;
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
