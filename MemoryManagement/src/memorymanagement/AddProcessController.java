package memorymanagement;

import memorymanagementAlgorithm.Segment;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
    private boolean editing = false;
    private Segment selectedSegment = null;
    private MemorySimulationController parentCtrl;
    private Process newProcess;
    private boolean isEdit;

    @FXML
    private MenuItem editOption;

    @FXML
    private MenuItem deleteOption;

    @FXML
    private ContextMenu rightClickOptions = new ContextMenu();

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

    @FXML
    void handleRightMouseClick(ActionEvent event) {
        selectedSegment = segmentsTable.getSelectionModel().getSelectedItem();
        String selectedOption = ((MenuItem) event.getTarget()).getText();
        if (selectedOption.equals("Edit")) {
            editing = true;
            if (labelsegmentName.isDisabled()) {
                labelsegmentName.setDisable(false);
                nameInputed.setDisable(false);
                labelsegmentSize.setDisable(false);
                sizeInputed.setDisable(false);
                confirmSegment.setDisable(false);
                sizeUnit_choiceBox.setDisable(false);

                sizeUnit_choiceBox.setValue("Byte");

                nameInputed.setText(selectedSegment.getName());
                sizeInputed.setText(Long.toString(selectedSegment.getLimit()));

                labelsegmentName.setText("Segment " + Integer.toString((int) selectedSegment.getID()) + " name");
                labelsegmentSize.setText("Segment " + Integer.toString((int) selectedSegment.getID()) + " size");

            } else {

                sizeUnit_choiceBox.setValue("Byte");

                nameInputed.setText(selectedSegment.getName());
                sizeInputed.setText(Long.toString(selectedSegment.getLimit()));

                labelsegmentName.setText("Segment " + Integer.toString((int) selectedSegment.getID()) + " name");
                labelsegmentSize.setText("Segment " + Integer.toString((int) selectedSegment.getID()) + " size");

            }
        } else if (selectedOption.equals("Delete")) {
            if (labelsegmentName.isDisabled()) {
                labelsegmentName.setDisable(false);
                nameInputed.setDisable(false);
                labelsegmentSize.setDisable(false);
                sizeInputed.setDisable(false);
                confirmSegment.setDisable(false);
                sizeUnit_choiceBox.setDisable(false);
            }
            segmentIndex--;
            segmentsTable.getItems().remove(selectedSegment);
            for (int k = 0; k < newProcess.get_number_of_segments(); k++) {
                if (SegmentsArray[k].equals(selectedSegment)) {
                    newProcess.remove_Segment_i(k);
                }
            }
        }
    }

    private void errorDialog(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }

    @FXML
    void handleKeyCancelButton(KeyEvent event) {
        if (cancel.getText().equals("Cancel")) {
            parentCtrl.setValidProcess(false);
        } else if (cancel.getText().equals("Done")) {
            parentCtrl.setValidProcess(true);
        }
        if (event.getCode().equals(KeyCode.ENTER)) {
            Stage thisStage = (Stage) cancel.getScene().getWindow();
            thisStage.close();
        }
    }

    @FXML
    void handleCancelButton(MouseEvent event) {
        if (cancel.getText().equals("Cancel")) {
            parentCtrl.setValidProcess(false);
        } else if (cancel.getText().equals("Done")) {
            parentCtrl.setValidProcess(true);
        }
        Stage thisStage = (Stage) cancel.getScene().getWindow();
        thisStage.close();
    }

    @FXML
    void handleKeySegmentConfirmButton(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            try {
                Double tempSizeD = Double.valueOf(sizeInputed.getText());
                Long tempSize = Math.round(Math.ceil(tempSizeD));
                String selectedValue = sizeUnit_choiceBox.getValue();
                Long forEditing = 0L;
                if (editing) {
                    forEditing = newProcess.get_total_size() - selectedSegment.getLimit();
                } else {
                    forEditing = newProcess.get_total_size();
                }

                if (tempSizeD <= 0) {
                    errorDialog("Size can't be negative or equal to zero");
                    return;
                }
                switch (selectedValue) {
                    case "Byte":
                        if (forEditing + tempSizeD > (maxSize)) {
                            errorDialog("Total Memory Size exceeded the maximum limit allowed.");
                            return;
                        } else {
                            tempSize = Math.round(Math.ceil(tempSizeD));
                        }
                        break;
                    case "KB":
                        if (forEditing + (tempSizeD * 1024L) > (maxSize)) {
                            errorDialog("Total Memory Size exceeded the maximum limit allowed.");
                            return;
                        } else {
                            tempSize = Math.round(Math.ceil(tempSizeD * 1024.0));
                        }

                        break;
                    case "MB":
                        if (forEditing + (tempSizeD * 1024L * 1024L) > (maxSize)) {
                            errorDialog("Total Memory Size exceeded the maximum limit allowed.");
                            return;
                        } else {
                            tempSize = Math.round(Math.ceil(tempSizeD * 1024.0 * 1024.0));
                        }

                        break;
                    case "GB":
                        if (forEditing + (tempSizeD * 1024L * 1024L * 1024L) > (maxSize)) {
                            errorDialog("Total Memory Size exceeded the maximum limit allowed.");
                            return;
                        } else {
                            tempSize = Math.round(Math.ceil(tempSizeD * 1024.0 * 1024.0 * 1024.0));
                        }

                        break;
                    case "TB":
                        if (forEditing + (tempSizeD * 1024L * 1024L * 1024L * 1024L) > maxSize) {
                            errorDialog("Total Memory Size exceeded the maximum limit allowed.");
                            return;
                        } else {
                            tempSize = Math.round(Math.ceil(tempSizeD * 1024.0 * 1024.0 * 1024.0 * 1024.0));
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
                if (editing) {
                    selectedSegment.setLimit(tempSize);
                    selectedSegment.setName(nameInputed.getText());
                    segmentsTable.refresh();

                    nameInputed.setText("");
                    sizeInputed.setText("");

                    editing = false;
                } else {
                    SegmentsArray[segmentIndex] = new Segment(tempSize, nameInputed.getText(), true);

                    newProcess.add_Segment(SegmentsArray[segmentIndex]);
                    segmentsTable.getItems().add(newProcess.get_segemnt_i(segmentIndex));

                    segmentIndex++;

                    nameInputed.setText("");
                    sizeInputed.setText("");
                }
                if (segmentIndex == numberOfSegments) {
                    labelsegmentName.setDisable(true);
                    nameInputed.setDisable(true);
                    labelsegmentSize.setDisable(true);
                    sizeInputed.setDisable(true);
                    confirmSegment.setDisable(true);
                    sizeUnit_choiceBox.setDisable(true);
                    cancel.setText("Done");
                } else {
                    labelsegmentName.setText("Segment " + Integer.toString(segmentIndex) + " name");
                    labelsegmentSize.setText("Segment " + Integer.toString(segmentIndex) + " size");
                }
            } catch (NumberFormatException | NullPointerException nfe) {
                Alert WrongEntry = new Alert(AlertType.ERROR);
                WrongEntry.setContentText("Size input must be an Integer");
                WrongEntry.show();
            }
        }
    }

    @FXML
    void handleSegmentConfirmation(MouseEvent event) {
        try {
            Double tempSizeD = Double.valueOf(sizeInputed.getText());
            Long tempSize = Math.round(Math.ceil(tempSizeD));
            String selectedValue = sizeUnit_choiceBox.getValue();

            Long forEditing = 0L;
            if (editing) {
                forEditing = newProcess.get_total_size() - selectedSegment.getLimit();
            } else {
                forEditing = newProcess.get_total_size();
            }
            if (tempSizeD <= 0) {
                errorDialog("Size can't be negative or equal to zero");
                return;
            }
            switch (selectedValue) {
                case "Byte":
                    if (forEditing + tempSizeD > (maxSize)) {
                        errorDialog("Total Memory Size exceeded the maximum limit allowed.");
                        return;
                    } else {
                        tempSize = Math.round(Math.ceil(tempSizeD));
                    }
                    break;
                case "KB":
                    if (forEditing + (tempSizeD * 1024L) > (maxSize)) {
                        errorDialog("Total Memory Size exceeded the maximum limit allowed.");
                        return;
                    } else {
                        tempSize = Math.round(Math.ceil(tempSizeD * 1024.0));
                    }

                    break;
                case "MB":
                    if (forEditing + (tempSizeD * 1024L * 1024L) > (maxSize)) {
                        errorDialog("Total Memory Size exceeded the maximum limit allowed.");
                        return;
                    } else {
                        tempSize = Math.round(Math.ceil(tempSizeD * 1024.0 * 1024.0));
                    }

                    break;
                case "GB":
                    if (forEditing + (tempSizeD * 1024L * 1024L * 1024L) > (maxSize)) {
                        errorDialog("Total Memory Size exceeded the maximum limit allowed.");
                        return;
                    } else {
                        tempSize = Math.round(Math.ceil(tempSizeD * 1024.0 * 1024.0 * 1024.0));
                    }

                    break;
                case "TB":
                    if (forEditing + (tempSizeD * 1024L * 1024L * 1024L * 1024L) > maxSize) {
                        errorDialog("Total Memory Size exceeded the maximum limit allowed.");
                        return;
                    } else {
                        tempSize = Math.round(Math.ceil(tempSizeD * 1024.0 * 1024.0 * 1024.0 * 1024.0));
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
            if (editing) {

                selectedSegment.setLimit(tempSize);
                selectedSegment.setName(nameInputed.getText());
                segmentsTable.refresh();

                nameInputed.setText("");
                sizeInputed.setText("");

                editing = false;
            } else {
                SegmentsArray[segmentIndex] = new Segment(tempSize, nameInputed.getText(), true);

                newProcess.add_Segment(SegmentsArray[segmentIndex]);
                segmentsTable.getItems().add(newProcess.get_segemnt_i(segmentIndex));

                segmentIndex++;

                nameInputed.setText("");
                sizeInputed.setText("");
            }
            if (segmentIndex == numberOfSegments) {
                labelsegmentName.setDisable(true);
                nameInputed.setDisable(true);
                labelsegmentSize.setDisable(true);
                sizeInputed.setDisable(true);
                confirmSegment.setDisable(true);
                sizeUnit_choiceBox.setDisable(true);
                cancel.setText("Done");
            } else {
                labelsegmentName.setText("Segment " + Integer.toString(segmentIndex) + " name");
                labelsegmentSize.setText("Segment " + Integer.toString(segmentIndex) + " size");
            }
        } catch (NumberFormatException | NullPointerException nfe) {
            Alert WrongEntry = new Alert(AlertType.ERROR);
            WrongEntry.setContentText("Size input must be an Integer");
            WrongEntry.show();
        }
    }

    @FXML
    void handleKeySegmentNumberConfirmTextfeild(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            try {
                numberOfSegments = Integer.valueOf(numberInputed.getText());
                if (numberOfSegments <= 0) {
                    errorDialog("Number can't be negative or equal to zero");
                    return;
                }
                labelsegmentName.setDisable(false);
                nameInputed.setDisable(false);
                labelsegmentSize.setDisable(false);
                sizeInputed.setDisable(false);
                segmentsTable.setDisable(false);
                confirmSegment.setDisable(false);
                confirmNumber.setDisable(true);
                numberInputed.setDisable(true);
                sizeUnit_choiceBox.setDisable(false);

                labelsegmentName.setText("Segment 0 name");
                labelsegmentSize.setText("Segment 0 size");

                SegmentsArray = new Segment[numberOfSegments];
            } catch (NumberFormatException | NullPointerException nfe) {
                Alert WrongEntry = new Alert(AlertType.ERROR);
                WrongEntry.setContentText("Input must be an Integer");
                WrongEntry.show();
            }
        }
    }

    @FXML
    void handleKeySegmentNumberConfirmButton(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            try {
                numberOfSegments = Integer.valueOf(numberInputed.getText());
                if (numberOfSegments <= 0) {
                    errorDialog("Number can't be negative or equal to zero");
                    return;
                }
                labelsegmentName.setDisable(false);
                nameInputed.setDisable(false);
                labelsegmentSize.setDisable(false);
                sizeInputed.setDisable(false);
                segmentsTable.setDisable(false);
                confirmSegment.setDisable(false);
                confirmNumber.setDisable(true);
                numberInputed.setDisable(true);
                sizeUnit_choiceBox.setDisable(false);

                labelsegmentName.setText("Segment 0 name");
                labelsegmentSize.setText("Segment 0 size");

                SegmentsArray = new Segment[numberOfSegments];
            } catch (NumberFormatException | NullPointerException nfe) {
                Alert WrongEntry = new Alert(AlertType.ERROR);
                WrongEntry.setContentText("Input must be an Integer");
                WrongEntry.show();
            }
        }
    }

    @FXML
    void handlecSegmentNumberConfirmation(MouseEvent event) {
        try {
            numberOfSegments = Integer.valueOf(numberInputed.getText());
            if (numberOfSegments <= 0) {
                errorDialog("Number can't be negativeor equal to zero");
                return;
            }
            labelsegmentName.setDisable(false);
            nameInputed.setDisable(false);
            labelsegmentSize.setDisable(false);
            sizeInputed.setDisable(false);
            segmentsTable.setDisable(false);
            confirmSegment.setDisable(false);
            confirmNumber.setDisable(true);
            numberInputed.setDisable(true);
            sizeUnit_choiceBox.setDisable(false);

            labelsegmentName.setText("Segment 0 name");
            labelsegmentSize.setText("Segment 0 size");

            SegmentsArray = new Segment[numberOfSegments];
        } catch (NumberFormatException | NullPointerException nfe) {
            Alert WrongEntry = new Alert(AlertType.ERROR);
            WrongEntry.setContentText("Input must be an Integer");
            WrongEntry.show();
        }
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
        allocateProcessTitlePane.setText("Process " + newProcess.getID());
        initializeChoiceBox();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        segmentName.setCellValueFactory(new PropertyValueFactory<>("name"));
        segmentSize.setCellValueFactory(new PropertyValueFactory<>("limit"));
        segmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
    }

}
