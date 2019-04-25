package schedulerAlgorithm;

import dataStructure.Node;
import dataStructure.PCB;
import dataStructure.Queue;
import scheduler.SchedulerSimulationController;

public class SJF_Preemptive_FCFS extends Queue implements ReadyQueue {

    Queue sortedQueue;
    int currentTime = 0;
    private int totalBurstTime = 0;
    private int totalwaitingTime = 0;
    private int totalTurnaroundtime = 0;
    private int noofProcesses = 0;

    @Override
    public void insert(PCB newPCB) {
        Node newNode = new Node(newPCB);
        totalBurstTime += newPCB.getBurstTime();
        noofProcesses++;
        if (head == null) {
            enqueue(newPCB);
        } else {
            Node traverse_node = head;

            if (newNode.getPcb().getArrivalTime() < traverse_node.getPcb().getArrivalTime()) {
                newNode.setNext(head);
                head = newNode;
            } else {
                while (traverse_node.getNext() != null) {
                    if (newNode.getPcb().getArrivalTime() < traverse_node.getNext().getPcb().getArrivalTime()) {
                        newNode.setNext(traverse_node.getNext());
                        traverse_node.setNext(newNode);
                        break;
                    }
                    traverse_node = traverse_node.getNext();
                }
                if (traverse_node.getNext() == null) {
                    enqueue(newPCB);
                }
            }
        }
    }

    @Override
    public void DrawGanttChart(SchedulerSimulationController ctrl) {
        prepare();

        Node traverse_node = head;
        while (traverse_node != null) {
            if (traverse_node.getPcb().getEndofExec() == (-1)) {
                if (traverse_node.getPcb().getArrivalTime() > currentTime) {
                    currentTime = traverse_node.getPcb().getArrivalTime();
                }
                sort(traverse_node);
            }
            traverse_node = traverse_node.getNext();
        }

        traverse_node = sortedQueue.getHead();
        while (traverse_node != null) {
            // Print the data at current node
            if (ctrl.getCurrentTime() < traverse_node.getPcb().getArrivalTime()) {
                ctrl.drawIdleProcess((traverse_node.getPcb().getArrivalTime() - ctrl.getCurrentTime()));
            }
            ctrl.draw(traverse_node.getPcb().getBurstTime(), traverse_node.getPcb().getPID(), traverse_node.getPcb().getColor());
            // Go to next node 
            traverse_node = traverse_node.getNext();
        }

        traverse_node = head;
        while (traverse_node != null) {
            totalTurnaroundtime += (traverse_node.getPcb().getEndofExec() - traverse_node.getPcb().getArrivalTime());
            traverse_node = traverse_node.getNext();
        }

        totalwaitingTime = totalTurnaroundtime - totalBurstTime;
        ctrl.writeAvgWaitingTime((totalwaitingTime / (double) noofProcesses));
        ctrl.writeAvgTurnarroundTime((totalTurnaroundtime / (double) noofProcesses));
    }

    private void prepare() {
        sortedQueue = new Queue();
        totalwaitingTime = 0;
        totalTurnaroundtime = 0;
        currentTime = 0;
        Node traverse_node = head;
        while (traverse_node != null) {
            traverse_node.getPcb().setEndofExec((-1));
            traverse_node = traverse_node.getNext();
        }
    }

    private void sort(Node curr) {
        int remainingTime = curr.getPcb().getBurstTime();
        int runningTime = 0;
        Node traverse_node = curr.getNext();
        PCB newPCB;
        while (traverse_node != null) {
            if ((traverse_node.getPcb().getArrivalTime() <= currentTime)) {
                if (traverse_node.getPcb().getEndofExec() == (-1)) {
                    if (traverse_node.getPcb().getBurstTime() < remainingTime) {
                        sort(traverse_node);
                    }
                }
                traverse_node = traverse_node.getNext();
            } else {
                runningTime = traverse_node.getPcb().getArrivalTime() - currentTime;
                if (runningTime > remainingTime) {
                    runningTime = remainingTime;
                }
                if ((sortedQueue.getTail() != null) && (sortedQueue.getTail().getPcb().getPID() == curr.getPcb().getPID())) {
                    sortedQueue.getTail().getPcb().setBurstTime(sortedQueue.getTail().getPcb().getBurstTime() + runningTime);
                } else {
                    newPCB = new PCB(false);
                    newPCB.copy(curr.getPcb());
                    newPCB.setBurstTime(runningTime);
                    sortedQueue.enqueue(newPCB);
                }
                remainingTime -= runningTime;
                currentTime = currentTime + runningTime;
                if (remainingTime == 0) {
                    curr.getPcb().setEndofExec(currentTime);
                    break;
                }
            }
        }
        if (remainingTime > 0) {
            if ((sortedQueue.getTail() != null) && (sortedQueue.getTail().getPcb().getPID() == curr.getPcb().getPID())) {
                sortedQueue.getTail().getPcb().setBurstTime(sortedQueue.getTail().getPcb().getBurstTime() + remainingTime);
            } else {
                newPCB = new PCB(false);
                newPCB.copy(curr.getPcb());
                newPCB.setBurstTime(remainingTime);
                sortedQueue.enqueue(newPCB);
            }
            currentTime = currentTime + remainingTime;
            curr.getPcb().setEndofExec(currentTime);
        }
    }

    @Override
    public void edit(PCB pcb) {
        delete(pcb);
        insert(pcb);
    }

    @Override
    public void delete(PCB pcb) {
        totalBurstTime -= pcb.getBurstTime();
        noofProcesses--;
        if (head.getPcb().equals(pcb)) {
            head = head.getNext();
            if (head == null) {
                tail = null;
            }
        } else {
            Node traverseNode = head;
            while (traverseNode.getNext() != null) {
                if (traverseNode.getNext().getPcb().equals(pcb)) {
                    if (traverseNode.getNext().getNext() == null) {
                        traverseNode.setNext(null);
                        tail = traverseNode;
                    } else {
                        traverseNode.setNext(traverseNode.getNext().getNext());
                    }
                    break;
                }
                traverseNode = traverseNode.getNext();
            }
        }
    }
}
