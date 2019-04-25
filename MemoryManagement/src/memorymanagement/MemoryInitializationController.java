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
import memorymanagementAlgorithm.Segment;

public class MemoryInitializationController implements Initializable {

    @FXML
    private TextField memoryTotalSize_txt;
    @FXML
    private ChoiceBox<String> memoryTotalSizeUnit_choiceBox;
    @FXML
    private ChoiceBox<String> osReservedSizeUnit_choiceBox;
    @FXML
    private ChoiceBox<String> memoryAlignment_choiceBox;
    @FXML
    private ChoiceBox<String> AllocationMethod_choiceBox;
    @FXML
    private TableView<Segment> memoryHolesTable;
    @FXML
    private TextField osReservedSize_txt;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
        memoryTotalSize_txt.setTextFormatter(textFormatter1);
        TextFormatter<String> textFormatter2 = new TextFormatter<>(filter);
        osReservedSize_txt.setTextFormatter(textFormatter2);
    }

    private void initializeChoiceBox() {
        List<String> UnitChoices = new ArrayList<>();
        UnitChoices.add("Byte");
        UnitChoices.add("KB");
        UnitChoices.add("MB");
        UnitChoices.add("GB");
        UnitChoices.add("TB");

        memoryTotalSizeUnit_choiceBox.setItems(FXCollections.observableArrayList(UnitChoices));
        memoryTotalSizeUnit_choiceBox.setValue("KB");
        osReservedSizeUnit_choiceBox.setItems(FXCollections.observableArrayList(UnitChoices));
        osReservedSizeUnit_choiceBox.setValue("KB");

        List<String> MemoryAllignment = new ArrayList<>();
        MemoryAllignment.add("1 Byte (8bit)");
        MemoryAllignment.add("4 Bytes (32bit)");
        MemoryAllignment.add("8 bytes (64bit)");

        memoryAlignment_choiceBox.setItems(FXCollections.observableArrayList(MemoryAllignment));
        memoryAlignment_choiceBox.setValue("1 Byte (8bit)");

        List<String> AllocationMethod = new ArrayList<>();
        AllocationMethod.add("First Fit");
        AllocationMethod.add("Best Fit");
        AllocationMethod.add("Worest Fit");

        AllocationMethod_choiceBox.setItems(FXCollections.observableArrayList(AllocationMethod));
        AllocationMethod_choiceBox.setValue("First Fit");

    }

}
