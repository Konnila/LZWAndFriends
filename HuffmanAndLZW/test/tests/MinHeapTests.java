/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import Data.HuffmanNode;
import Data.MinHeap;
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
public class MinHeapTests {
    private HuffmanNode firstnode, secondnode, thirdnode, comb;
    private MinHeap heap;
    
    public MinHeapTests() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        firstnode = new HuffmanNode((char)97, 200, null, null);
        secondnode = new HuffmanNode((char)10, 100 , null, null);
        thirdnode = new HuffmanNode((char)20, 150, null, null);
        comb = new HuffmanNode();
        heap = new MinHeap(257);
        heap.offer(firstnode);
        heap.offer(secondnode);
        heap.offer(thirdnode);
    }
    
    @Test
    public void testInsertLowest() {
        System.out.println("insert");
        HuffmanNode node = new HuffmanNode((char)80, 40, null, null);
        MinHeap instance = heap;
        instance.offer(node);
        
        System.out.println(heap.peek().getCharacter());
        Assert.assertEquals(heap.size(), 4);
        Assert.assertEquals(heap.peek().getCharacter(), node.getCharacter());
    }
    
        public void testInsertNotLowest() {
        System.out.println("insert");
        HuffmanNode node = new HuffmanNode((char)80, 120, null, null);
        heap.offer(node);
        
        System.out.println(heap.peek().getCharacter());
        Assert.assertEquals(heap.size(), 4);
        Assert.assertEquals(heap.peek().getCharacter(), secondnode.getCharacter());
    }
        
    @Test
    public void testMergeNodes() {
        System.out.println("mergeNodes");
        heap.mergeNodes();
        Assert.assertEquals(2, heap.size());
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
