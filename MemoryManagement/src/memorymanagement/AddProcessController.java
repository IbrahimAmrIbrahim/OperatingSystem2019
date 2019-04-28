package memorymanagement;

import memorymanagementAlgorithm.Segment;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import memorymanagementAlgorithm.Process;

/**
 *
 * @author Almonzer
 */
public class AddProcessController implements Initializable {

    private int numberOfSegments = 0;
    private Segment[] SegmentsArray;
    private int segmentIndex = 0;
    private long maxSize;
    private long osSize;
    private MemorySimulationController.memoryAlignmentOptions option;

    private MemorySimulationController parentCtrl;
    private Process newProcess;
    private boolean isEdit;

    @FXML
    private ChoiceBox<String> sizeUnit_choiceBox;

    @FXML
    private TitledPane allocateProcessTitlePane;

    @FXML
    private TableView<Segment> segmentsTable;

    @FXML
    private TableColumn<Segment, Long> segmentIDColumn;

    @FXML
    private TableColumn<Segment, String> segmentName;

    @FXML
    private TableColumn<Segment, Long> segmentSize;

    @FXML
    private Label labelPID;

    @FXML
    private Label currentPID;

    @FXML
    private Label segmentNumber;

    @FXML
    private TextField numberInputed;

    @FXML
    private Button confirmNumber;

    @FXML
    private Label labelsegmentName;

    @FXML
    private TextField nameInputed;

    @FXML
    private Label labelsegmentSize;

    @FXML
    private TextField sizeInputed;

    @FXML
    private Button confirmSegment;

    @FXML
    private Button cancel;

    private void errorDialog(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }

    @FXML
    void handleCancelButton(ActionEvent event) {
        Stage thisStage = (Stage)cancel.getScene().getWindow();
        thisStage.close();
        
    }

    @FXML
    void handleSegmentConfirmation(ActionEvent event) {
        try {
            Double tempSizeD = Double.valueOf(sizeInputed.getText());
            Long tempSize = Math.round(tempSizeD);
            String selectedValue = sizeUnit_choiceBox.getValue();

            switch (selectedValue) {
                case "Byte":
                    if (newProcess.get_total_size() + tempSize > (maxSize)) {
                        errorDialog("Total Memory Size exceeded the maximum limit allowed.");
                        return;
                    } else {
                        tempSize = Math.round(tempSizeD);
                    }
                    break;
                case "KB":
                    if (newProcess.get_total_size() + (tempSize * 1024L)> (maxSize)) {
                        errorDialog("Total Memory Size exceeded the maximum limit allowed.");
                        return;
                    } else {
                        tempSize = Math.round(tempSizeD * 1024.0);
                    }

                    break;
                case "MB":
                    if (newProcess.get_total_size() + (tempSize * 1024L * 1024L)> (maxSize)) {
                        errorDialog("Total Memory Size exceeded the maximum limit allowed.");
                        return;
                    } else {
                        tempSize = Math.round(tempSizeD * 1024.0 * 1024.0);
                    }

                    break;
                case "GB":
                    if (newProcess.get_total_size() + (tempSize * 1024L * 1024L * 1024L)> (maxSize)) {
                        errorDialog("Total Memory Size exceeded the maximum limit allowed.");
                        return;
                    } else {
                        tempSize = Math.round(tempSizeD * 1024.0 * 1024.0 * 1024.0);
                    }

                    break;
                case "TB":
                    if (newProcess.get_total_size() + (tempSize * 1024L * 1024L * 1024L * 1024L)> maxSize) {
                        errorDialog("Total Memory Size exceeded the maximum limit allowed.");
                        return;
                    } else {
                        tempSize = Math.round(tempSizeD * 1024.0 * 1024.0 * 1024.0 * 1024.0);
                    }

                    break;
            }
            switch (option) {
                case _8bit:
                    break;
                case _32bit:
                    if ((tempSize % 4L != 0)) {
                        tempSize += 4 - (tempSize % 4L);
                    }
                    break;
                case _64bit:
                    if ((tempSize % 8L != 0)) {
                        tempSize += 8 - (tempSize % 8L);
                    }
                    break;
            }
            SegmentsArray[segmentIndex] = new Segment(tempSize, nameInputed.getText(), true);
            
            newProcess.add_Segment(SegmentsArray[segmentIndex]);
            segmentsTable.getItems().add(newProcess.get_segemnt_i(segmentIndex));

            segmentIndex++;

            nameInputed.setText("");
            sizeInputed.setText("");

            if (segmentIndex == numberOfSegments) {
                labelsegmentName.setDisable(true);
                nameInputed.setDisable(true);
                labelsegmentSize.setDisable(true);
                sizeInputed.setDisable(true);
                confirmSegment.setDisable(true);
                sizeUnit_choiceBox.setDisable(true);
                cancel.setText("Done");
            } else {
                labelsegmentName.setText("Segment " + Integer.toString(segmentIndex + 1) + " name");
                labelsegmentSize.setText("Segment " + Integer.toString(segmentIndex + 1) + " size");
            }
        } catch (NumberFormatException | NullPointerException nfe) {
            Alert WrongEntry = new Alert(AlertType.ERROR);
            WrongEntry.setContentText("Size input must be an Integer");
            WrongEntry.show();
        }
    }

    @FXML
    void handlecSegmentNameConfirmation(ActionEvent event) {

    }

    void InputRequiredSegments(int NumberOfSegments) {
    }

    @FXML
    void handlecSegmentNumberConfirmation(ActionEvent event) {
        try {
            numberOfSegments = Integer.valueOf(numberInputed.getText());
            labelsegmentName.setDisable(false);
            nameInputed.setDisable(false);
            labelsegmentSize.setDisable(false);
            sizeInputed.setDisable(false);
            segmentsTable.setDisable(false);
            confirmSegment.setDisable(false);
            confirmNumber.setDisable(true);
            numberInputed.setDisable(true);
            sizeUnit_choiceBox.setDisable(false);

            labelsegmentName.setText("Segment 1 name");
            labelsegmentSize.setText("Segment 1 size");

            SegmentsArray = new Segment[numberOfSegments];
        } catch (NumberFormatException | NullPointerException nfe) {
            Alert WrongEntry = new Alert(AlertType.ERROR);
            WrongEntry.setContentText("Input must be an Integer");
            WrongEntry.show();
        }
    }

    @FXML
    void handlecSegmentSizeConfirmation(ActionEvent event) {

    }

    private void initializeChoiceBox() {
        List<String> UnitChoices = new ArrayList<>();
        UnitChoices.add("Byte");
        UnitChoices.add("KB");
        UnitChoices.add("MB");
        UnitChoices.add("GB");
        UnitChoices.add("TB");

        sizeUnit_choiceBox.setItems(FXCollections.observableArrayList(UnitChoices));
        sizeUnit_choiceBox.setValue("KB");
    }

    public void sceneInitialization(MemorySimulationController ctrl, Process process) {
        parentCtrl = ctrl;
        newProcess = process;
        isEdit = false;
        maxSize = parentCtrl.getMemoryTotalSize();
        osSize = parentCtrl.getOsReservedSize();
        option = parentCtrl.getMemoryAlignment();
        Segment.setSEGMENT_ID(0);
        initializeChoiceBox();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        segmentName.setCellValueFactory(new PropertyValueFactory<>("name"));
        segmentSize.setCellValueFactory(new PropertyValueFactory<>("limit"));
        segmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        allocateProcessTitlePane.setText("Process x");
//        initializeChoiceBox();
    }

}
