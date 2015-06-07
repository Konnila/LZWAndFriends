package Interfaces;

import Data.HuffmanNode;

/**
 * Interface for implementation of priorityQueue
 * @author Toni Könnilä
 */
public interface HuffmanPriorityQueue {
    HuffmanNode peek();
    void offer(HuffmanNode node);
    void mergeNodes();
    int size();
}
