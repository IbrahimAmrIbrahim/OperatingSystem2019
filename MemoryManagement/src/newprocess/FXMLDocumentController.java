/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newprocess;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author ibrah
 */
public class FXMLDocumentController implements Initializable {

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handlecSegmentNumberConfirmation(MouseEvent event) {
    }

    @FXML
    private void handlecSegmentNameConfirmation(MouseEvent event) {
    }

    @FXML
    private void handlecSegmentSizeConfirmation(MouseEvent event) {
    }

    @FXML
    private void handleSegmentConfirmation(MouseEvent event) {
    }

    @FXML
    private void handleCancelButton(MouseEvent event) {
    }
    
}
