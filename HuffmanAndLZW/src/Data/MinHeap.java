package Data;

import Interfaces.HuffmanPriorityQueue;

public class MinHeap implements HuffmanPriorityQueue{
    private int pointer = 0;
    private HuffmanNode[] heap;
    private HuffmanNode tempNode, tempNode2, comboNode;
    private int heapSize;
    
    
    public MinHeap(int size) {
        this.heap = new HuffmanNode[size+1];
    }
    
    @Override
    public HuffmanNode peek() {
        return heap[1];
    }

    @Override
    public void offer(HuffmanNode node) {
        pointer++;
        this.heap[pointer] = node;
        upheaval(pointer);
    }
    
    protected void upheaval(int index) {
        if(index > 1){
            if(this.heap[index].getFrequency()< this.heap[index/2].getFrequency() ){
                tempNode = this.heap[index/2];
                tempNode2 = new HuffmanNode();
                this.heap[index/2] = this.heap[index];
                this.heap[index] = tempNode;
                this.heap[index].setLeft(tempNode2.getLeftChild());
                this.heap[index].setRight(tempNode2.getRightChild());
                upheaval(index/2);
            }
        }
    }

    @Override
    public int size() {
        return pointer;
    }
    

    @Override
    public void mergeNodes() {
        comboNode = new HuffmanNode();
        HuffmanNode leftC = delMin();
        HuffmanNode rightC = delMin();
        comboNode.setFrequency(leftC.getFrequency() + rightC.getFrequency());
        comboNode.left = leftC;
        comboNode.right = rightC;
        
        this.offer(comboNode);
        
        comboNode = null;
    }
    
    public HuffmanNode delMin(){
        HuffmanNode toDelete = heap[1];  
        this.heap[1] = this.heap[pointer];
        pointer--;
        
        heapify(1);
        
        return toDelete;
    }

    public void heapify(int index){
        if((index*2)+1 <= pointer){
            if ((heap[index*2].getFrequency()) < heap[index].getFrequency() || (heap[(index*2)+1].getFrequency()) < heap[index].getFrequency()){
                int smaller;
                if(heap[index*2].getFrequency() <= heap[(index*2)+1].getFrequency()){
                    smaller = index*2;
                }
                else{
                    smaller = (index*2)+1;
                }

                tempNode = heap[index];
                tempNode2 = heap[smaller];
                heap[index] = heap[smaller];
                heap[smaller] = tempNode;
                tempNode2 = null;
                tempNode = null;
                if(smaller*2+1 <= pointer){
                    heapify(smaller);
                }
            }
        }
        else if(index*2 <= pointer){
            int smaller = (index*2);
            tempNode = heap[index];
            tempNode2 = heap[smaller];
            heap[index] = heap[smaller];
            heap[smaller] = tempNode;
            tempNode2 = null;
            tempNode = null;
            if(smaller*2+1 <= pointer){
                heapify(smaller);
            }
        }
    }
}
