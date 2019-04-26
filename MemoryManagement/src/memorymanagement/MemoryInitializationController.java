package memorymanagement;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import memorymanagementAlgorithm.Segment;

public class MemoryInitializationController implements Initializable {

    @FXML
    private TableView<Segment> memoryHolesTable;
    @FXML
    private TextField baseAddress_txt;
    @FXML
    private TextField limit_txt;
    @FXML
    private ChoiceBox<String> limitUnit_choiceBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void sceneInitialization() {
        setTextFieldValidation();
        initializeChoiceBox();
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
        baseAddress_txt.setTextFormatter(textFormatter1);
        TextFormatter<String> textFormatter2 = new TextFormatter<>(filter);
        limit_txt.setTextFormatter(textFormatter2);
    }

    private void initializeChoiceBox() {
        List<String> UnitChoices = new ArrayList<>();
        UnitChoices.add("Byte");
        UnitChoices.add("KB");
        UnitChoices.add("MB");
        UnitChoices.add("GB");
        UnitChoices.add("TB");

        limitUnit_choiceBox.setItems(FXCollections.observableArrayList(UnitChoices));
        limitUnit_choiceBox.setValue("KB");
    }

    private void tableInitialize() {

    }

    @FXML
    private void addFreeHole_keyboardEvent(KeyEvent event) {
    }

    @FXML
    private void addFreeHole_mouseEvent(MouseEvent event) {
    }

    @FXML
    private void cancelAdditionofFreeHole_keyboardEvent(KeyEvent event) {
    }

    @FXML
    private void cancelAdditionofFreeHole_mouseEvent(MouseEvent event) {
    }

    @FXML
    private void saveFreeHoles_keyboardEvent(KeyEvent event) {
    }

    @FXML
    private void saveFreeHoles_mouseEvent(MouseEvent event) {
    }

    @FXML
    private void cancelFreeHoles_keyboardEvent(KeyEvent event) {
    }

    @FXML
    private void cancelFreeHoles_mouseEvent(MouseEvent event) {
    }

}
