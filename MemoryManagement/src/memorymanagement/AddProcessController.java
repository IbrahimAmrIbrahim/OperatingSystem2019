/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import memorymanagementAlgorithm.Process;

/**
 *
 * @author Almonzer
 */
public class AddProcessController implements Initializable {

    private int numberOfSegments = 0;
    private Segment[] SegmentsArray;
    private int segmentIndex = 0;

    private MemorySimulationController parentCtrl;
    private Process newProcess = new Process();
    private boolean isEdit;

    
    @FXML
    private ChoiceBox<String> sizeUnit_choiceBox;
    
    @FXML
    private TitledPane allocateProcessTitlePane;

    @FXML
    private TableView<Segment> segmentsTable;

    @FXML
    private TableColumn<Segment, Integer> segmentIDColumn;
    
    @FXML
    private TableColumn<Segment, String> segmentName;

    @FXML
    private TableColumn<Segment, Integer> segmentSize;

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

    @FXML
    void handleCancelButton(ActionEvent event) {
    }

    @FXML
    void handleSegmentConfirmation(ActionEvent event) {
        try {

            Long tempSize = Long.valueOf(sizeInputed.getText());
            SegmentsArray[segmentIndex] = new Segment(tempSize, nameInputed.getText(), true);

            Long InputedSize = SegmentsArray[segmentIndex].getLimit(); 
            String selectedValue = sizeUnit_choiceBox.getValue();
            
            switch(selectedValue){ // Still needs maxsize constraint
                case "Byte":
                    break;
                case "KB":
                    SegmentsArray[segmentIndex].setLimit(InputedSize * 1024);
                    break;
                case "MB":
                    SegmentsArray[segmentIndex].setLimit(InputedSize * 2048);
                    break;
                case "GB":
                    SegmentsArray[segmentIndex].setLimit(InputedSize * 4096);
                    break;
                case "TB":
                    SegmentsArray[segmentIndex].setLimit(InputedSize * 8192);
                    break;
                            
            }
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
            }else{
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
        Segment.setSEGMENT_ID(0);   
        initializeChoiceBox();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        segmentName.setCellValueFactory(new PropertyValueFactory<>("name"));
        segmentSize.setCellValueFactory(new PropertyValueFactory<>("limit"));
        segmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        allocateProcessTitlePane.setText("Process x");
        initializeChoiceBox();
    }

}
