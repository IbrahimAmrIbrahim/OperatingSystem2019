package scheduler;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import dataStructure.PCB;
import java.util.function.UnaryOperator;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.stage.Stage;

public class AddProcessController implements Initializable {

    @FXML
    private Label pID_Label;
    @FXML
    private Label pColor_Label;
    @FXML
    private TextField arrivalTime_Text;
    @FXML
    private TextField burstTime_Text;
    @FXML
    private TextField priority_Text;
    @FXML
    private Button addProcee_btn;
    @FXML
    private Button cancel_btn;
    @FXML
    private Label PriorityIndication_Label;

    private PCB newProcess;
    private SchedulerSimulationController parentController;
    private SchedulerSimulationController.schedulerType currentScheduler;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void sceneInitialization(SchedulerSimulationController ctrl, PCB process) {
        parentController = ctrl;
        newProcess = process;
        pID_Label.setText(Integer.toString(newProcess.getPID()));
        pColor_Label.setBackground(new Background(new BackgroundFill(newProcess.getColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        setTextFieldValidation();
        currentScheduler = parentController.getCurrentScheduler();
        parentController.setNewProcess(false);
        if ((currentScheduler == SchedulerSimulationController.schedulerType.FCFS)
                || (currentScheduler == SchedulerSimulationController.schedulerType.SJF_Preemptive_FCFS)
                || (currentScheduler == SchedulerSimulationController.schedulerType.SJF_NonPreemptive_FCFS)
                || (currentScheduler == SchedulerSimulationController.schedulerType.RoundRobin)) {
            PriorityIndication_Label.setVisible(false);
            priority_Text.setVisible(false);
        }
    }

    private void setTextFieldValidation() {
        UnaryOperator<Change> filter = change -> {
            String text = change.getText();

            if (text.matches("[0-9]*")) {
                return change;
            }

            return null;
        };
        TextFormatter<String> textFormatter1 = new TextFormatter<>(filter);
        arrivalTime_Text.setTextFormatter(textFormatter1);
        TextFormatter<String> textFormatter2 = new TextFormatter<>(filter);
        burstTime_Text.setTextFormatter(textFormatter2);
        TextFormatter<String> textFormatter3 = new TextFormatter<>(filter);
        priority_Text.setTextFormatter(textFormatter3);
    }

    /**
     * For Open Error Messages.
     *
     * @param msg Question string
     */
    private void errorDialog(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }

    @FXML
    private void addProcessButton_MouseEvent(MouseEvent event) {
        setProcess();
    }

    @FXML
    private void addProcessButton_KeyboardEvent(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            setProcess();
        }
    }

    private void setProcess() {
        if (arrivalTime_Text.getText().isEmpty()) {
            errorDialog("Arrival Time text field can't be empty.");
            return;
        }
        if (burstTime_Text.getText().isEmpty()) {
            errorDialog("Burst Time text field can't be empty.");
            return;
        }

        if ((currentScheduler == SchedulerSimulationController.schedulerType.FCFS)
                || (currentScheduler == SchedulerSimulationController.schedulerType.SJF_Preemptive_FCFS)
                || (currentScheduler == SchedulerSimulationController.schedulerType.SJF_NonPreemptive_FCFS)
                || (currentScheduler == SchedulerSimulationController.schedulerType.RoundRobin)) {

        } else {
            if (priority_Text.getText().isEmpty()) {
                errorDialog("Priority text field can't be empty.");
                return;
            }
            newProcess.setPriority(Integer.valueOf(priority_Text.getText()));
        }

        newProcess.setArrivalTime(Integer.valueOf(arrivalTime_Text.getText()));
        newProcess.setBurstTime(Integer.valueOf(burstTime_Text.getText()));
        parentController.setNewProcess(true);
        sceneClose();
    }

    @FXML
    private void cancelBuotton_MouseEvent(MouseEvent event) {
        parentController.setNewProcess(false);
        sceneClose();
    }

    @FXML
    private void cancelBuotton_KeyboardEvent(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            parentController.setNewProcess(false);
            sceneClose();
        }
    }

    private void sceneClose() {
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }

}
