package Data;

/**
 * Basic node that forms the Huffman Tree
 */
public class HuffmanNode implements Comparable<HuffmanNode>{
    char c;
    int frequency;
    
    //children
    HuffmanNode left,right;
    
    public HuffmanNode(char c, int frequency, HuffmanNode left, HuffmanNode right) {
        this.c = c;
        this.frequency = frequency;
        this.right = right;
        this.left = left;
    }
    
    public HuffmanNode() {
        this.left = null;
        this.right = null;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void setLeft(HuffmanNode left) {
        this.left = left;
    }

    public void setC(char c) {
        this.c = c;
    }

    public void setRight(HuffmanNode right) {
        this.right = right;
    }
    
    public int getFrequency() {
        return frequency;
    }
    
    public char getCharacter() {
        return c;
    }
    
    public HuffmanNode getLeftChild() {
        return left;
    }
    
    public HuffmanNode getRightChild() {
        return right;
    }
    
    public boolean isLeafNode() {
        return '\u0000' != this.c;
    }
    
    @Override
    public int compareTo(HuffmanNode node) {
        return this.frequency - node.frequency;
    }
}
