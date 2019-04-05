package scheduler;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import dataStructure.PCB;
import javafx.scene.input.KeyEvent;
import javafx.stage.StageStyle;
import schedulerAlgorithm.FCFS;
import schedulerAlgorithm.Priority_NonPreemptive_FCFS;
import schedulerAlgorithm.Priority_Preemptive_FCFS;
import schedulerAlgorithm.RoundRobin;
import schedulerAlgorithm.SJF_NonPreemptive_FCFS;
import schedulerAlgorithm.SJF_Preemptive_FCFS;

public class SchedulerSimulationController implements Initializable {

    public enum schedulerType {
        None, FCFS, SJF_Preemptive_FCFS, SJF_NonPreemptive_FCFS, RoundRobin, Priority_Preemptive_FCFS, Priority_NonPreemptive_FCFS
    };

    private schedulerType currentScheduler;
    private SchedulerSimulationController myController;

    private int currentTime;
    private int currentXPosition;
    private int currentYPosition;
    private boolean canvasIsEmpty;

    private boolean newProcess;
    private PCB processtoAdd;

    private FCFS FCFS_ProcessQueue;
    private RoundRobin RoundRobin_ProcessQueue;
    private SJF_Preemptive_FCFS SJF_Preemptive_FCFS_ProcessQueue;
    private SJF_NonPreemptive_FCFS SJF_NonPreemptive_FCFS_ProcessQueue;
    private Priority_Preemptive_FCFS Priority_Preemptive_FCFS_ProcessQueue;
    private Priority_NonPreemptive_FCFS Priority_NonPreemptive_FCFS_ProcessQueue;

    @FXML
    private Button startOutputSimulation_btn;
    @FXML
    private Button addProcess_btn;
    @FXML
    private ToggleGroup Simulation_Method;
    @FXML
    private Canvas canvas;

    @FXML
    private void startOutputSimulationButton_KeyboardEvent(KeyEvent event) {
    }

    @FXML
    private void startOutputSimulationButton_MouseEvent(MouseEvent event) {
    }

    @FXML
    private void addNewProcessButton_KeyboardEvent(KeyEvent event) throws IOException {
        if (event.getCode().toString().equals("ENTER")) {
            addProcessDialog();
        }
    }

    @FXML
    private void addNewProcessButton_MouseEvent(MouseEvent event) throws IOException {
        addProcessDialog();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentScheduler = schedulerType.None;
        currentTime = 0;
        currentXPosition = 0;
        currentYPosition = 20;
        canvasIsEmpty = true;
        newProcess = true;
        schedulerSelectDialog();
    }

    /**
     * For Open Error Messages.
     *
     * @param msg Question string
     */
    private void errorDialog(String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }

    /**
     * For Open Yes and No Question Messages.
     *
     * @param msg Question string
     * @return return True if Yes ; return False if No
     */
    private boolean yesNoDialog(String msg) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonTypeYes) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * For Open a scheduler picker.
     */
    private void schedulerSelectDialog() {
        List<String> choices = new ArrayList<>();
        //  None, FCFS, SJF_Preemptive_FCFS, SJF_NonPreemptive_FCFS, RoundRobin, Priority_Preemptive_FCFS, Priority_NonPreemptive_FCFS
        choices.add("None");
        choices.add("FCFS");
        choices.add("Round Robin");
        choices.add("SJF Preemptive (FCFS)");
        choices.add("SJF NonPreemptive (FCFS)");
        choices.add("Priority Preemptive (FCFS)");
        choices.add("Priority NonPreemptive (FCFS)");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Choose a scheduler", choices);
        dialog.setTitle("Choice Dialog");
        dialog.setHeaderText(null);
        dialog.setContentText("Choose a scheduler:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String ans = result.get();
            if (ans.equals("FCFS")) {
                currentScheduler = schedulerType.FCFS;
            } else if (ans.equals("Round Robin")) {
                currentScheduler = schedulerType.RoundRobin;
            } else if (ans.equals("SJF Preemptive (FCFS)")) {
                currentScheduler = schedulerType.SJF_Preemptive_FCFS;
            } else if (ans.equals("SJF NonPreemptive (FCFS)")) {
                currentScheduler = schedulerType.SJF_NonPreemptive_FCFS;
            } else if (ans.equals("Priority Preemptive (FCFS)")) {
                currentScheduler = schedulerType.Priority_Preemptive_FCFS;
            } else if (ans.equals("Priority NonPreemptive (FCFS)")) {
                currentScheduler = schedulerType.Priority_NonPreemptive_FCFS;
            } else {
                currentScheduler = schedulerType.None;
            }
        } else {
            currentScheduler = schedulerType.None;
        }
        queueInitialize();
    }

    private void addProcessDialog() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddProcess.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();

        AddProcessController ctrl = fxmlLoader.getController();
        if (newProcess == true) {
            processtoAdd = new PCB();
        }
        ctrl.sceneInitilize(myController, processtoAdd);

        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.showAndWait();
        if (newProcess == true) {
            /* Add process */
            insertMethodCall(processtoAdd);
            FCFS_ProcessQueue.printQueue();
        }
    }

    private void insertMethodCall(PCB process) {
        switch (currentScheduler) {
            case None:
                break;
            case FCFS:
                FCFS_ProcessQueue.insert(process);
                break;
            case RoundRobin:
                RoundRobin_ProcessQueue.insert(process);
                break;
            case SJF_Preemptive_FCFS:
                SJF_Preemptive_FCFS_ProcessQueue.insert(process);
                break;
            case SJF_NonPreemptive_FCFS:
                SJF_NonPreemptive_FCFS_ProcessQueue.insert(process);
                break;
            case Priority_Preemptive_FCFS:
                Priority_Preemptive_FCFS_ProcessQueue.insert(process);
                break;
            case Priority_NonPreemptive_FCFS:
                Priority_NonPreemptive_FCFS_ProcessQueue.insert(process);
                break;
            default:
                break;
        }
    }

    private void queueInitialize() {
        switch (currentScheduler) {
            case None:
                FCFS_ProcessQueue = null;
                RoundRobin_ProcessQueue = null;
                SJF_Preemptive_FCFS_ProcessQueue = null;
                SJF_NonPreemptive_FCFS_ProcessQueue = null;
                Priority_Preemptive_FCFS_ProcessQueue = null;
                Priority_NonPreemptive_FCFS_ProcessQueue = null;
                break;
            case FCFS:
                FCFS_ProcessQueue = new FCFS();
                RoundRobin_ProcessQueue = null;
                SJF_Preemptive_FCFS_ProcessQueue = null;
                SJF_NonPreemptive_FCFS_ProcessQueue = null;
                Priority_Preemptive_FCFS_ProcessQueue = null;
                Priority_NonPreemptive_FCFS_ProcessQueue = null;
                break;
            case RoundRobin:
                FCFS_ProcessQueue = null;
                RoundRobin_ProcessQueue = new RoundRobin();
                SJF_Preemptive_FCFS_ProcessQueue = null;
                SJF_NonPreemptive_FCFS_ProcessQueue = null;
                Priority_Preemptive_FCFS_ProcessQueue = null;
                Priority_NonPreemptive_FCFS_ProcessQueue = null;
                break;
            case SJF_Preemptive_FCFS:
                FCFS_ProcessQueue = null;
                RoundRobin_ProcessQueue = null;
                SJF_Preemptive_FCFS_ProcessQueue = new SJF_Preemptive_FCFS();
                SJF_NonPreemptive_FCFS_ProcessQueue = null;
                Priority_Preemptive_FCFS_ProcessQueue = null;
                Priority_NonPreemptive_FCFS_ProcessQueue = null;
                break;
            case SJF_NonPreemptive_FCFS:
                FCFS_ProcessQueue = null;
                RoundRobin_ProcessQueue = null;
                SJF_Preemptive_FCFS_ProcessQueue = null;
                SJF_NonPreemptive_FCFS_ProcessQueue = new SJF_NonPreemptive_FCFS();
                Priority_Preemptive_FCFS_ProcessQueue = null;
                Priority_NonPreemptive_FCFS_ProcessQueue = null;
                break;
            case Priority_Preemptive_FCFS:
                FCFS_ProcessQueue = null;
                RoundRobin_ProcessQueue = null;
                SJF_Preemptive_FCFS_ProcessQueue = null;
                SJF_NonPreemptive_FCFS_ProcessQueue = null;
                Priority_Preemptive_FCFS_ProcessQueue = new Priority_Preemptive_FCFS();
                Priority_NonPreemptive_FCFS_ProcessQueue = null;
                break;
            case Priority_NonPreemptive_FCFS:
                FCFS_ProcessQueue = null;
                RoundRobin_ProcessQueue = null;
                SJF_Preemptive_FCFS_ProcessQueue = null;
                SJF_NonPreemptive_FCFS_ProcessQueue = null;
                Priority_Preemptive_FCFS_ProcessQueue = null;
                Priority_NonPreemptive_FCFS_ProcessQueue = new Priority_NonPreemptive_FCFS();
                break;
            default:
                break;
        }
    }

    public void draw(int duration, int processID, Color color) {
        PCB newOne = new PCB();

        final int unityTimeWidth = 40;
        GraphicsContext gc = canvas.getGraphicsContext2D();

        if (duration <= 0) {
            return;
        }

        if (canvasIsEmpty == true) {
            // Write scheduler method
            gc.setFill(Color.WHITE);
            gc.setTextAlign(TextAlignment.LEFT);
            if (getCurrentScheduler() == schedulerType.None) {
                gc.fillText("No Scheduler", 20, currentYPosition);
            } else if (getCurrentScheduler() == schedulerType.FCFS) {
                gc.fillText("FCFS Scheduler", 20, currentYPosition);
            } else if (getCurrentScheduler() == schedulerType.RoundRobin) {
                gc.fillText("Round Robin Scheduler", 20, currentYPosition);
            } else if (getCurrentScheduler() == schedulerType.SJF_Preemptive_FCFS) {
                gc.fillText("SJF Preemptive (FCFS) Scheduler", 20, currentYPosition);
            } else if (getCurrentScheduler() == schedulerType.SJF_NonPreemptive_FCFS) {
                gc.fillText("SJF NonPreemptive (FCFS) Scheduler", 20, currentYPosition);
            } else if (getCurrentScheduler() == schedulerType.Priority_Preemptive_FCFS) {
                gc.fillText("Priority Preemptive (FCFS) Scheduler", 20, currentYPosition);
            } else if (getCurrentScheduler() == schedulerType.Priority_NonPreemptive_FCFS) {
                gc.fillText("Priority NonPreemptive (FCFS) Scheduler", 20, currentYPosition);
            }

            currentYPosition += 20;
            // draw begin time point
            gc.fillRect(40, currentYPosition, 1, 50);

            // write time
            gc.rotate(45);
            gc.fillText(Integer.toString(currentTime), rotatedX(40, (currentYPosition + 50)), rotatedY(40, (currentYPosition + 50)));
            gc.rotate(-45);
            currentXPosition = 41;

            canvasIsEmpty = false;
        }

        int nextPosition = (duration * unityTimeWidth) + currentXPosition;
        if (currentYPosition == 40 && nextPosition < 8000) {
            canvas.setWidth(nextPosition + 40);
        } else {
            canvas.setWidth(8000 + 40);
        }

        if (nextPosition > 8000) {
            int theRest = duration - ((8000 - currentXPosition) / unityTimeWidth);
            draw(((8000 - currentXPosition) / unityTimeWidth), 0, Color.WHITE);
            currentXPosition = 40;
            currentYPosition += 100;
            canvas.setHeight(currentYPosition + 100);
            if (theRest > 0) {
                draw(theRest, 0, Color.WHITE);

            }
            return;
        }

        currentTime += duration;

        //*********************************
        //color need to change 
        //*************************
        // draw process time
        gc.setFill(newOne.getColor());
        //gc.fillRect(currentXPosition, (currentYPosition + 10), (duration * unityTimeWidth), 30);
        gc.fillRoundRect(currentXPosition, (currentYPosition + 10), (duration * unityTimeWidth), 30, 20, 20);

        // write process id
        gc.setFill(Color.WHITE);
        //*********************************
        //pid need to be addad 
        //*************************
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(("P" + Integer.toString(newOne.getpID())), (((duration * unityTimeWidth) / 2) + currentXPosition), (currentYPosition + 30));

        // draw end time point
        gc.fillRect(nextPosition, currentYPosition, 1, 50);

        // write time
        gc.rotate(45);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText(Integer.toString(currentTime), rotatedX(nextPosition, (currentYPosition + 50)), rotatedY(nextPosition, (currentYPosition + 50)));
        gc.rotate(-45);

        currentXPosition = nextPosition + 1;
    }

    /**
     * This method writes average waiting time.
     *
     * @param avgWaitingTime
     */
    public void writeAvgWaitingTime(double avgWaitingTime) {

    }

    /**
     * This method writes average turnaround time.
     *
     * @param avgTurnarroundTime
     */
    public void writeAvgTurnarroundTime(double avgTurnarroundTime) {

    }

    private double rotatedX(double x, double y) {
        // to center the text
        x -= 5;
        y += 10;
        double r = Math.sqrt((x * x) + (y * y));
        double seta = Math.atan2(y, x);
        return (r * Math.cos(seta - (0.25 * Math.PI)));
    }

    private double rotatedY(double x, double y) {
        // to center the text
        x -= 5;
        y += 10;
        double r = Math.sqrt((x * x) + (y * y));
        double seta = Math.atan2(y, x);
        return (r * Math.sin(seta - (0.25 * Math.PI)));
    }

    /**
     * @param myController the myController to set
     */
    public void setMyController(SchedulerSimulationController myController) {
        this.myController = myController;
    }

    /**
     * @return the currentScheduler
     */
    public schedulerType getCurrentScheduler() {
        return currentScheduler;
    }

    /**
     * @param newProcess the newProcess to set
     */
    public void setNewProcess(boolean newProcess) {
        this.newProcess = newProcess;
    }
}
