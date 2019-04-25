package schedulerAlgorithm;

import dataStructure.PCB;
import dataStructure.Queue;
import dataStructure.Node;

import scheduler.SchedulerSimulationController;

public class RoundRobin extends Queue implements ReadyQueue {

    Queue RRQueue;
    int timeSlice = 0;
    int currentTime = 0;
    private int totalBurstTime = 0;
    private int totalwaitingTime = 0;
    private int totalTurnaroundtime = 0;
    private int noofProcesses = 0;
    private int noofFinishedProcesses = 0;

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
        timeSlice = ctrl.getTimeSlice();

        Node traverse_node = head;
        PCB dequeued = null;

        while (true) {

            while (traverse_node != null) {
                if ((traverse_node.getPcb().getArrivalTime() <= currentTime)) {
                    if (traverse_node.getPcb().getEndofExec() == (-1)) {
                        PCB temp = new PCB(false);
                        temp.copy(traverse_node.getPcb());
                        temp.setStartofExec(traverse_node.getPcb().getBurstTime());
                        RRQueue.enqueue(temp);
                        traverse_node.getPcb().setEndofExec(1);
                    }
                    traverse_node = traverse_node.getNext();
                } else {
                    if (RRQueue.getHead() == null && dequeued == null) {
                        int durtion = (traverse_node.getPcb().getArrivalTime() - currentTime);
                        ctrl.drawIdleProcess(durtion);
                        currentTime += durtion;
                    } else {
                        break;
                    }
                }
            }

            if (dequeued != null) {
                RRQueue.enqueue(dequeued);
                dequeued = null;
            }

            if (RRQueue.getHead() != null) {
                if (RRQueue.getHead().getPcb().getStartofExec() >= timeSlice) {
                    ctrl.draw(timeSlice, RRQueue.getHead().getPcb().getPID(), RRQueue.getHead().getPcb().getColor());
                    RRQueue.getHead().getPcb().setStartofExec(RRQueue.getHead().getPcb().getStartofExec() - timeSlice);
                    currentTime += timeSlice;
                } else {
                    int duration = RRQueue.getHead().getPcb().getStartofExec();
                    ctrl.draw(duration, RRQueue.getHead().getPcb().getPID(), RRQueue.getHead().getPcb().getColor());
                    RRQueue.getHead().getPcb().setStartofExec(0);
                    currentTime += duration;
                }
                if (RRQueue.getHead().getPcb().getStartofExec() == 0) {
                    totalTurnaroundtime += (currentTime - RRQueue.getHead().getPcb().getArrivalTime());
                    RRQueue.dequeue();
                    noofFinishedProcesses++;
                } else {
                    dequeued = RRQueue.dequeue();
                }
            }

            if (noofFinishedProcesses == noofProcesses) {
                break;
            }
        }

        totalwaitingTime = totalTurnaroundtime - totalBurstTime;
        ctrl.writeAvgWaitingTime((totalwaitingTime / (double) noofProcesses));
        ctrl.writeAvgTurnarroundTime((totalTurnaroundtime / (double) noofProcesses));
    }

    private void prepare() {
        RRQueue = new Queue();
        totalwaitingTime = 0;
        totalTurnaroundtime = 0;
        currentTime = 0;
        noofFinishedProcesses = 0;
        Node traverse_node = head;
        while (traverse_node != null) {
            traverse_node.getPcb().setEndofExec((-1));
            traverse_node = traverse_node.getNext();
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
