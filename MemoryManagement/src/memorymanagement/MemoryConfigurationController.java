package memorymanagement;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MemoryConfigurationController implements Initializable {

    @FXML
    private TextField memoryTotalSize_txt;
    @FXML
    private ChoiceBox<String> memoryTotalSizeUnit_choiceBox;
    @FXML
    private TextField osReservedSize_txt;
    @FXML
    private ChoiceBox<String> osReservedSizeUnit_choiceBox;
    @FXML
    private ChoiceBox<String> memoryAlignment_choiceBox;
    @FXML
    private ChoiceBox<String> AllocationMethod_choiceBox;
    @FXML
    private Button cancel_btn;

    private MemorySimulationController parentCtrl;
    boolean isTrueConfiguration;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void sceneInitialization(MemorySimulationController ctrl) {
        parentCtrl = ctrl;
        isTrueConfiguration = false;
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
    private void saveHardwareConfig_keyboardEvent(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            save();
            if (isTrueConfiguration) {
                sceneClose();
            }
        }
    }

    @FXML
    private void saveHardwareConfig_mouseEvent(MouseEvent event) {
        save();
        if (isTrueConfiguration) {
            sceneClose();
        }
    }

    @FXML
    private void cancelHardwareConfig_mouseEvent(MouseEvent event) {
        sceneClose();
    }

    @FXML
    private void cancelHardwareConfig_keyboardEvent(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            sceneClose();
        }
    }

    private void save() {
        //Memory Alignment
        String alignment = memoryAlignment_choiceBox.getValue();
        switch (alignment) {
            case "1 Byte (8bit)":
                parentCtrl.setMemoryAlignment(MemorySimulationController.memoryAlignmentOptions._8bit);
                break;
            case "4 Bytes (32bit)":
                parentCtrl.setMemoryAlignment(MemorySimulationController.memoryAlignmentOptions._32bit);
                break;
            case "8 bytes (64bit)":
                parentCtrl.setMemoryAlignment(MemorySimulationController.memoryAlignmentOptions._64bit);
                break;
        }

        //Memory Total Size
        long totalSize = 0;
        try {
            totalSize = Long.valueOf(memoryTotalSize_txt.getText());
        } catch (Exception e) {
            errorDialog("Total Memory Size is empty or its exceeded the max limit allowed.");
            return;
        }
        if (totalSize == 0) {
            errorDialog("Total Memory Size can't equal to zero.");
            return;
        }
        String totalSizeUnit = memoryTotalSizeUnit_choiceBox.getValue();
        switch (totalSizeUnit) {
            case "Byte":
                switch (parentCtrl.getMemoryAlignment()) {
                    case _8bit:
                        break;
                    case _32bit:
                        if ((totalSize % 4L != 0)) {
                            errorDialog("Total Memory Size must be divisible by 4.");
                            return;
                        }
                        break;
                    case _64bit:
                        if ((totalSize % 8L != 0)) {
                            errorDialog("Total Memory Size must be divisible by 8.");
                            return;
                        }
                        break;
                }
                parentCtrl.setMemoryTotalSize(totalSize);
                break;
            case "KB":
                if (totalSize > (Long.MAX_VALUE / 1024L)) {
                    errorDialog("Total Memory Size is exceeded the max limit allowed.");
                    return;
                } else {
                    parentCtrl.setMemoryTotalSize(totalSize * 1024L);
                }
                break;
            case "MB":
                if (totalSize > (Long.MAX_VALUE / (1024L * 1024L))) {
                    errorDialog("Total Memory Size is exceeded the max limit allowed.");
                    return;
                } else {
                    parentCtrl.setMemoryTotalSize(totalSize * (1024L * 1024L));
                }
                break;
            case "GB":
                if (totalSize > (Long.MAX_VALUE / (1024L * 1024L * 1024L))) {
                    errorDialog("Total Memory Size is exceeded the max limit allowed.");
                    return;
                } else {
                    parentCtrl.setMemoryTotalSize(totalSize * (1024L * 1024L * 1024L));
                }
                break;
            case "TB":
                if (totalSize > (Long.MAX_VALUE / (1024L * 1024L * 1024L * 1024L))) {
                    errorDialog("Total Memory Size is exceeded the max limit allowed.");
                    return;
                } else {
                    parentCtrl.setMemoryTotalSize(totalSize * (1024L * 1024L * 1024L * 1024L));
                }
                break;
        }

        //Os Reserved Size.
        long osSize = 0;
        try {
            osSize = Long.valueOf(osReservedSize_txt.getText());
        } catch (Exception e) {
            errorDialog("OS Reserved Size is empty or its exceeded the max limit allowed.");
            return;
        }

        String osSizeUnit = osReservedSizeUnit_choiceBox.getValue();
        switch (osSizeUnit) {
            case "Byte":
                switch (parentCtrl.getMemoryAlignment()) {
                    case _8bit:
                        break;
                    case _32bit:
                        if ((osSize % 4L != 0)) {
                            osSize += 4 - (osSize % 4L);
                        }
                        break;
                    case _64bit:
                        if ((osSize % 8L != 0)) {
                            osSize += 8 - (osSize % 8L);
                        }
                        break;
                }
                break;
            case "KB":
                if (osSize > (Long.MAX_VALUE / 1024L)) {
                    errorDialog("OS Reserved Size is exceeded the max limit allowed.");
                    return;
                } else {
                    osSize = (osSize * 1024L);
                }
                break;
            case "MB":
                if (osSize > (Long.MAX_VALUE / (1024L * 1024L))) {
                    errorDialog("OS Reserved Size is exceeded the max limit allowed.");
                    return;
                } else {
                    osSize = (osSize * (1024L * 1024L));
                }
                break;
            case "GB":
                if (osSize > (Long.MAX_VALUE / (1024L * 1024L * 1024L))) {
                    errorDialog("OS Reserved Size is exceeded the max limit allowed.");
                    return;
                } else {
                    osSize = (osSize * (1024L * 1024L * 1024L));
                }
                break;
            case "TB":
                if (osSize > (Long.MAX_VALUE / (1024L * 1024L * 1024L * 1024L))) {
                    errorDialog("OS Reserved Size is exceeded the max limit allowed.");
                    return;
                } else {
                    osSize = (osSize * (1024L * 1024L * 1024L * 1024L));
                }
                break;
        }
        if (osSize > parentCtrl.getMemoryTotalSize()) {
            errorDialog("OS Reserved Size has exceeded the max memory size.");
            return;
        }
        parentCtrl.setOsReservedSize(osSize);
        parentCtrl.setMemoryTotalSize(parentCtrl.getMemoryTotalSize() - osSize);

        //Allocaton Method
        String allocation = AllocationMethod_choiceBox.getValue();
        switch (allocation) {
            case "First Fit":
                parentCtrl.setAllocationMethod(MemorySimulationController.allocationMethodOptions.FirstFit);
                break;
            case "Best Fit":
                parentCtrl.setAllocationMethod(MemorySimulationController.allocationMethodOptions.BestFit);
                break;
            case "Worest Fit":
                parentCtrl.setAllocationMethod(MemorySimulationController.allocationMethodOptions.WorstFit);
                break;
        }

        isTrueConfiguration = true;
        parentCtrl.setMemoryConfigurationChange(true);
    }

    private void sceneClose() {
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }

}
