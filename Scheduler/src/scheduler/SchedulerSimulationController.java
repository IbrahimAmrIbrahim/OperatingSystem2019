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
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.function.UnaryOperator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import schedulerAlgorithm.FCFS;
import schedulerAlgorithm.Priority_NonPreemptive_FCFS;
import schedulerAlgorithm.Priority_Preemptive_FCFS;
import schedulerAlgorithm.RoundRobin;
import schedulerAlgorithm.SJF_NonPreemptive_FCFS;
import schedulerAlgorithm.SJF_Preemptive_FCFS;

public class SchedulerSimulationController implements Initializable {

    @FXML
    private TableView<PCB> processTable;
    @FXML
    private TableColumn<PCB, Integer> processIDColumn;
    @FXML
    private TableColumn<PCB, Color> processColorColumn;
    @FXML
    private TableColumn<PCB, Integer> arrivalTimeColumn;
    @FXML
    private TableColumn<PCB, Integer> priorityColumn;
    @FXML
    private TableColumn<PCB, Integer> burstTimeColumn;

    public enum schedulerType {
        None, FCFS, SJF_Preemptive_FCFS, SJF_NonPreemptive_FCFS, RoundRobin, Priority_Preemptive_FCFS, Priority_NonPreemptive_FCFS
    };

    private schedulerType currentScheduler;
    private SchedulerSimulationController myController;

    final private int unityTimeWidth = 40;
    private int currentTime;
    private int currentXPosition;
    private int currentYPosition;
    private boolean canvasIsEmpty;
    private int timeSlice;

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
    private Group outputSimulationGroup;
    @FXML
    private Button clear_btn;
    @FXML
    private TextField timeSlice_textField;

    @FXML
    private void startOutputSimulationButton_KeyboardEvent(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            startOutputSimulation_btn.setDisable(true);
            if (currentScheduler == schedulerType.RoundRobin) {
                String timeSliceText = timeSlice_textField.getText();
                if (timeSliceText.isEmpty()) {
                    errorDialog("Time Slice text field can't be empty.");
                    return;
                } else if (Integer.valueOf(timeSliceText) == 0) {
                    errorDialog("Time Slice can't be 0.");
                    return;
                } else {
                    timeSlice = Integer.valueOf(timeSliceText);

                }
            }
            canvasReset();
            drawMethodCall();
        }
    }

    @FXML
    private void startOutputSimulationButton_MouseEvent(MouseEvent event) {
        startOutputSimulation_btn.setDisable(true);
        if (currentScheduler == schedulerType.RoundRobin) {
            String timeSliceText = timeSlice_textField.getText();
            if (timeSliceText.isEmpty()) {
                errorDialog("Time Slice text field can't be empty.");
                return;
            } else if (Integer.valueOf(timeSliceText) == 0) {
                errorDialog("Time Slice can't be 0.");
                return;
            } else {
                timeSlice = Integer.valueOf(timeSliceText);

            }
        }
        canvasReset();
        drawMethodCall();
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

    @FXML
    private void clearButton_KeyboardEvent(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            if (yesNoDialog("Are you sure you want to clear everything?")) {
                clear();
            }
        }
    }

    @FXML
    private void clearButton_MouseEvent(MouseEvent event) {
        if (yesNoDialog("Are you sure you want to clear everything?")) {
            clear();
        }
    }

    @FXML
    private void changeSchedulerButton_KeyboardEvent(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            if (yesNoDialog("Are you sure you want to change the scheduler, it will clear everything?")) {
                schedulerSelectDialog();;
            }
        }
    }

    @FXML
    private void changeSchedulerButton_MouseEvent(MouseEvent event) {
        if (yesNoDialog("Are you sure you want to change the scheduler, it will clear everything?")) {
            schedulerSelectDialog();;
        }
    }

    @FXML
    private void loadFromFileButton_KeyboardEvent(KeyEvent event) throws FileNotFoundException {
        if (event.getCode().toString().equals("ENTER")) {
            loadProcessesFromFile();
        }
    }

    @FXML
    private void loadFromFileButton_MouseEvent(MouseEvent event) throws FileNotFoundException {
        loadProcessesFromFile();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentScheduler = schedulerType.None;
        timeSlice = 0;
        currentTime = 0;
        currentXPosition = 0;
        currentYPosition = 20;
        canvasIsEmpty = true;
        newProcess = true;
        setTextFieldValidation();

        // Listen for TextField text changes
        timeSlice_textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                startOutputSimulation_btn.setDisable(false);
            }
        });

        schedulerSelectDialog();
        tableInitialization();
        sceneInitialization();
    }

    private void tableInitialization() {
        processIDColumn.setCellValueFactory(new PropertyValueFactory<>("pID"));

        processColorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));

        // Custom rendering of the table cell.
        processColorColumn.setCellFactory(column -> {
            return new TableCell<PCB, Color>() {
                @Override
                protected void updateItem(Color item, boolean empty) {
                    super.updateItem(item, empty);
                    if (getTableRow() == null || empty) {
                        setText(null);
                        setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                    } else {
                        setTextFill(Color.WHITE);
                        setText(item.toString());
                        setBackground(new Background(new BackgroundFill(item, CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                }
            };
        });

        arrivalTimeColumn.setCellValueFactory(
                new PropertyValueFactory<>("arrivalTime"));
        burstTimeColumn.setCellValueFactory(
                new PropertyValueFactory<>("burstTime"));
        priorityColumn.setCellValueFactory(
                new PropertyValueFactory<>("priority"));

    }

    /**
     * This method initializes scene according to the scheduler
     */
    private void sceneInitialization() {
        if (currentScheduler == schedulerType.None) {
            addProcess_btn.setDisable(true);
            timeSlice_textField.setText("");
            outputSimulationGroup.setDisable(true);
            clear_btn.setDisable(true);
            processTable.setDisable(true);
        } else if (currentScheduler == schedulerType.RoundRobin) {
            addProcess_btn.setDisable(false);
            outputSimulationGroup.setDisable(false);
            startOutputSimulation_btn.setDisable(false);
            timeSlice_textField.setDisable(false);
            clear_btn.setDisable(false);
            processTable.setDisable(false);
            priorityColumn.setVisible(false);
        } else if ((currentScheduler == schedulerType.Priority_Preemptive_FCFS) || (currentScheduler == schedulerType.Priority_NonPreemptive_FCFS)) {
            addProcess_btn.setDisable(false);
            outputSimulationGroup.setDisable(false);
            startOutputSimulation_btn.setDisable(false);
            timeSlice_textField.setText("");
            timeSlice_textField.setDisable(true);
            clear_btn.setDisable(false);
            processTable.setDisable(false);
            priorityColumn.setVisible(true);
        } else {
            addProcess_btn.setDisable(false);
            outputSimulationGroup.setDisable(false);
            startOutputSimulation_btn.setDisable(false);
            timeSlice_textField.setText("");
            timeSlice_textField.setDisable(true);
            clear_btn.setDisable(false);
            processTable.setDisable(false);
            priorityColumn.setVisible(false);
        }
    }

    private void canvasReset() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        currentTime = 0;
        currentXPosition = 0;
        currentYPosition = 20;
        canvasIsEmpty = true;
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.setWidth(749);
        canvas.setHeight(265);
    }

    private void clear() {
        timeSlice = 0;
        newProcess = true;
        canvasReset();
        processTable.getItems().clear();
        queueInitialize();
        sceneInitialization();
        PCB.setCurrentPID(0);
        startOutputSimulation_btn.setDisable(false);
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

    private void alertDialog(String msg) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }

    private void setTextFieldValidation() {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();

            if (text.matches("[0-9]*")) {
                return change;
            }

            return null;
        };
        TextFormatter<String> textFormatter1 = new TextFormatter<>(filter);
        timeSlice_textField.setTextFormatter(textFormatter1);
    }

    private void loadProcessesFromFile() throws FileNotFoundException {
        alertDialog("The text file must contain each process in a separate line and each process must be in the format (ArrivalTime BurstTime Priority).\n"
                + "Priority is optional.\n"
                + "Warning : If the priority is not specified and the scheduler is the priority scheduler it will be taken zero.");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Source File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text file", "*.txt")
        );
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.dir"))
        );
        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (file != null) {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                Scanner line = new Scanner(scanner.nextLine());
                if (newProcess == true) {
                    processtoAdd = new PCB(true);
                    newProcess = false;
                }

                for (int i = 0; line.hasNextInt(); i++) {
                    switch (i) {
                        case 0:
                            processtoAdd.setArrivalTime(line.nextInt());
                            newProcess = true;
                            break;
                        case 1:
                            processtoAdd.setBurstTime(line.nextInt());
                            newProcess = true;
                            break;
                        case 2:
                            processtoAdd.setPriority(line.nextInt());
                            newProcess = true;
                            break;
                    }
                }
                if (newProcess == true) {
                    insertMethodCall(processtoAdd);
                    processTable.getItems().add(processtoAdd);
                    startOutputSimulation_btn.setDisable(false);
                }
            }
            scanner.close();
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
        clear();
    }

    private void addProcessDialog() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddProcess.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();

        AddProcessController ctrl = fxmlLoader.getController();
        if (newProcess == true) {
            processtoAdd = new PCB(true);
        }
        ctrl.sceneInitialization(myController, processtoAdd);

        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.showAndWait();
        if (newProcess == true) {
            /* Add process */
            insertMethodCall(processtoAdd);
            processTable.getItems().add(processtoAdd);
            startOutputSimulation_btn.setDisable(false);
        }
    }

    private void insertMethodCall(PCB process) {
        switch (currentScheduler) {
            case None:
                break;
            case FCFS:
                FCFS_ProcessQueue.insert(process);
                FCFS_ProcessQueue.printQueue();
                break;
            case RoundRobin:
                RoundRobin_ProcessQueue.insert(process);
                RoundRobin_ProcessQueue.printQueue();
                break;
            case SJF_Preemptive_FCFS:
                SJF_Preemptive_FCFS_ProcessQueue.insert(process);
                SJF_Preemptive_FCFS_ProcessQueue.printQueue();
                break;
            case SJF_NonPreemptive_FCFS:
                SJF_NonPreemptive_FCFS_ProcessQueue.insert(process);
                SJF_NonPreemptive_FCFS_ProcessQueue.printQueue();
                break;
            case Priority_Preemptive_FCFS:
                Priority_Preemptive_FCFS_ProcessQueue.insert(process);
                Priority_Preemptive_FCFS_ProcessQueue.printQueue();
                break;
            case Priority_NonPreemptive_FCFS:
                Priority_NonPreemptive_FCFS_ProcessQueue.insert(process);
                Priority_NonPreemptive_FCFS_ProcessQueue.printQueue();
                break;
            default:
                break;
        }
    }

    private void drawMethodCall() {
        switch (currentScheduler) {
            case None:
                break;
            case FCFS:
                FCFS_ProcessQueue.DrawGanttChart(myController);
                break;
            case RoundRobin:
                RoundRobin_ProcessQueue.DrawGanttChart(myController);
                break;
            case SJF_Preemptive_FCFS:
                SJF_Preemptive_FCFS_ProcessQueue.DrawGanttChart(myController);
                break;
            case SJF_NonPreemptive_FCFS:
                SJF_NonPreemptive_FCFS_ProcessQueue.DrawGanttChart(myController);
                break;
            case Priority_Preemptive_FCFS:
                Priority_Preemptive_FCFS_ProcessQueue.DrawGanttChart(myController);
                break;
            case Priority_NonPreemptive_FCFS:
                Priority_NonPreemptive_FCFS_ProcessQueue.DrawGanttChart(myController);
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

    private void canvasInitialization() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        if (canvasIsEmpty == true) {
            // Write scheduler method
            gc.setFill(Color.WHITE);
            gc.setTextAlign(TextAlignment.LEFT);
            switch (getCurrentScheduler()) {
                case FCFS:
                    gc.fillText("FCFS Scheduler", 20, 20);
                    break;
                case RoundRobin:
                    gc.fillText("Round Robin Scheduler", 20, 20);
                    break;
                case SJF_Preemptive_FCFS:
                    gc.fillText("SJF Preemptive (FCFS) Scheduler", 20, 20);
                    break;
                case SJF_NonPreemptive_FCFS:
                    gc.fillText("SJF NonPreemptive (FCFS) Scheduler", 20, 20);
                    break;
                case Priority_Preemptive_FCFS:
                    gc.fillText("Priority Preemptive (FCFS) Scheduler", 20, 20);
                    break;
                case Priority_NonPreemptive_FCFS:
                    gc.fillText("Priority NonPreemptive (FCFS) Scheduler", 20, 20);
                    break;
                default:
                    break;
            }

            currentYPosition = 80;
            // draw begin time point
            gc.fillRect(40, currentYPosition, 1, 50);

            // write time
            gc.rotate(45);
            gc.fillText(Integer.toString(getCurrentTime()), rotatedX(40, (currentYPosition + 50)), rotatedY(40, (currentYPosition + 50)));
            gc.rotate(-45);
            currentXPosition = 41;

            canvasIsEmpty = false;
        }
    }

    /**
     *This method draws the gantt chart of the process.
     * @param duration
     * @param processID
     * @param color
     */
    public void draw(int duration, int processID, Color color) {

        GraphicsContext gc = canvas.getGraphicsContext2D();

        canvasInitialization();

        if (duration <= 0) {
            return;
        }

        int nextPosition = (duration * unityTimeWidth) + currentXPosition;
        // Canvas max width 8040
        // initial Canvas widh 749
        if (canvas.getWidth() < (8040)) {
            if (nextPosition >= 749 && nextPosition < 8000) {
                canvas.setWidth(nextPosition + 40);
            } else if (nextPosition > 8000) {
                canvas.setWidth(8040);
            }
        }

        if (nextPosition > 8000) {
            int theRest = duration - ((8000 - currentXPosition) / unityTimeWidth);
            draw(((8000 - currentXPosition) / unityTimeWidth), processID, color);
            currentXPosition = 40;
            currentYPosition += 100;
            canvas.setHeight(currentYPosition + 100);
            if (theRest >= 0) {
                draw(theRest, processID, color);
            }
            return;
        }
        currentTime += duration;

        // draw process time
        gc.setFill(color);
        gc.fillRoundRect(currentXPosition, (currentYPosition + 10), (duration * unityTimeWidth), 30, 20, 20);

        // write process id
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.CENTER);
        String processText;
        if (processID == -1) {
            processText = "Idle";
        } else {
            processText = ("P" + Integer.toString(processID));
        }
        gc.fillText(processText, (((duration * unityTimeWidth) / 2) + currentXPosition), (currentYPosition + 30));

        // draw end time point
        gc.fillRect(nextPosition, currentYPosition, 1, 50);

        // write time
        gc.rotate(45);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText(Integer.toString(getCurrentTime()), rotatedX(nextPosition, (currentYPosition + 50)), rotatedY(nextPosition, (currentYPosition + 50)));
        gc.rotate(-45);

        currentXPosition = nextPosition + 1;
    }

    /**
     * This method draws the idle duration of the processor.
     *
     * @param duration
     */
    public void drawIdleProcess(int duration) {
        draw(duration, -1, Color.BLACK);
    }

    /**
     * This method writes average waiting time.
     *
     * @param avgWaitingTime
     */
    public void writeAvgWaitingTime(double avgWaitingTime) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("Average waitting time : " + String.format("%.5g%n", avgWaitingTime), 40, 40);
    }

    /**
     * This method writes average turnaround time.
     *
     * @param avgTurnarroundTime
     */
    public void writeAvgTurnarroundTime(double avgTurnarroundTime) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("Average turnaround time : " + String.format("%.5g%n", avgTurnarroundTime), 40, 60);
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

    /**
     * @return the currentTime
     */
    public int getCurrentTime() {
        return currentTime;
    }

    /**
     * @return the timeSlice
     */
    public int getTimeSlice() {
        return timeSlice;
    }
}
