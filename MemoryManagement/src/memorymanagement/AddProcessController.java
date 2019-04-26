/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memorymanagement;

import memorymanagementAlgorithm.Segment;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Almonzer
 */
public class AddProcessController implements Initializable {

    private int numberOfSegments = 0;
    private Segment[] SegmentsArray;
    private int segmentIndex = 0;

    @FXML
    private TableView<Segment> segmentsTable;

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
    private Button confirmName;

    @FXML
    private Label labelsegmentSize;

    @FXML
    private TextField sizeInputed;

    @FXML
    private Button confirmSize;

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

            int tempSize = Integer.valueOf(sizeInputed.getText());
            SegmentsArray[segmentIndex] = new Segment(tempSize, nameInputed.getText(), true);

            segmentsTable.getItems().add(SegmentsArray[segmentIndex]);

            segmentIndex++;

            nameInputed.setText("");
            sizeInputed.setText("");

            labelsegmentName.setText("Segment " + Integer.toString(segmentIndex + 1) + " name");
            labelsegmentSize.setText("Segment " + Integer.toString(segmentIndex + 1) + " size");

            if (segmentIndex == numberOfSegments ) {
                labelsegmentName.setDisable(true);
                nameInputed.setDisable(true);
                confirmName.setDisable(true);
                labelsegmentSize.setDisable(true);
                sizeInputed.setDisable(true);
                confirmSize.setDisable(true);
                confirmSegment.setDisable(true);
                cancel.setText("Done");
            }
        } catch (NumberFormatException | NullPointerException nfe) {
            Alert WrongEntry = new Alert(AlertType.ERROR);
            WrongEntry.setContentText("Input must be Integer");
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
            confirmName.setDisable(false);
            labelsegmentSize.setDisable(false);
            sizeInputed.setDisable(false);
            confirmSize.setDisable(false);
            segmentsTable.setDisable(false);
            confirmSegment.setDisable(false);
            confirmNumber.setDisable(true);
            numberInputed.setDisable(true);

            labelsegmentName.setText("Segment 1 name");
            labelsegmentSize.setText("Segment 1 size");

            SegmentsArray = new Segment[numberOfSegments];
        } catch (NumberFormatException | NullPointerException nfe) {
            Alert WrongEntry = new Alert(AlertType.ERROR);
            WrongEntry.setContentText("Input must be Integer");
            WrongEntry.show();
        }
    }

    @FXML
    void handlecSegmentSizeConfirmation(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        segmentName.setCellValueFactory(new PropertyValueFactory<>("name"));
        segmentSize.setCellValueFactory(new PropertyValueFactory<>("limit"));
    }

}
