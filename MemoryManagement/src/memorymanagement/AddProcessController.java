/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memorymanagement;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author Almonzer
 */
public class AddProcessController implements Initializable {

    private int numberOfSegments;

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
    private Label segmentName;

    @FXML
    private TextField nameInputed;

    @FXML
    private Button confirmName;

    @FXML
    private Label segmentSize;

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

    }

    @FXML
    void handlecSegmentNameConfirmation(ActionEvent event) {

    }

    @FXML
    void handlecSegmentNumberConfirmation(ActionEvent event) {

        segmentName.setDisable(false);
        nameInputed.setDisable(false);
        confirmName.setDisable(false);
        segmentSize.setDisable(false);
        sizeInputed.setDisable(false);
        confirmSize.setDisable(false);
        confirmSegment.setDisable(false);
    }

    @FXML
    void handlecSegmentSizeConfirmation(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
