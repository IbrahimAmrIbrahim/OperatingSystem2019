package schedulerAlgorithm;

import dataStructure.Node;
import dataStructure.PCB;
import dataStructure.Queue;

import scheduler.SchedulerSimulationController;

public class Priority_NonPreemptive_FCFS extends Queue implements ReadyQueue {

    @Override
    public void insert(PCB newPCB) {

        Node newNode = new Node(newPCB);

        if (head == null) {

            head = newNode;
            tail = newNode;

        } else if (head == tail) {
            if (newNode.getPcb().getPriority() < head.getPcb().getPriority()) {
                newNode.setNext(head);
                head = newNode;
            } else {
                enqueue(newPCB);
            }
        } else {
            Node traverse_node = head;

            if (newNode.getPcb().getPriority() < traverse_node.getPcb().getPriority()) {
                newNode.setNext(head);
                head = newNode;
            } else {
                while (traverse_node.getNext() != null) {
                    if (newNode.getPcb().getPriority() < traverse_node.getNext().getPcb().getPriority()) {
                        newNode.setNext(traverse_node.getNext());
                        traverse_node.setNext(newNode);
                        break;
                    }
                }
                if (traverse_node.getNext() == null) {
                    enqueue(newPCB);
                }
            }
        }
    }

    @Override
    public void DrawGanttChart(SchedulerSimulationController ctrl) {
    }

    @Override
    public double getAvgWaitingTime() {
        return 0;
    }

    @Override
    public double getAvgTurnarroundTime() {
        return 0;
    }

}
