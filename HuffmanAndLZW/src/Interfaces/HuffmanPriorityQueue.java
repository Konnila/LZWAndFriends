package Interfaces;

import Data.HuffmanNode;

/**
 * Interface for implementation of priorityQueue
 * @author Toni Könnilä
 */
public interface HuffmanPriorityQueue {
    void offer(HuffmanNode node);
    void mergeNodes();
    HuffmanNode peek();
    HuffmanNode poll();
    int size();
}
