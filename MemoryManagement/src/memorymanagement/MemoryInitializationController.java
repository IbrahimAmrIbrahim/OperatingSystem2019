package memorymanagement;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.function.UnaryOperator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import memorymanagementAlgorithm.Blank;
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
    @FXML
    private TableColumn<Segment, Long> baseAddressColumn;
    @FXML
    private TableColumn<Segment, Long> limitColumn;
    @FXML
    private Button cancel_btn;

    private MemorySimulationController parentCtrl;
    private Blank freeSpace;
    private Vector<Segment> free_vector;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void sceneInitialization(MemorySimulationController ctrl, Blank free) {
        parentCtrl = ctrl;
        freeSpace = free;
        free_vector = new Vector<Segment>();
        setTextFieldValidation();
        initializeChoiceBox();
        tableInitialize();
    }

    private void setTextFieldValidation() {
        UnaryOperator<TextFormatter.Change> filter_Integer = change -> {
            String text = change.getText();

            if (text.matches("[0-9]*")) {
                return change;
            }

            return null;
        };
        TextFormatter<String> textFormatter1 = new TextFormatter<>(filter_Integer);
        baseAddress_txt.setTextFormatter(textFormatter1);
        UnaryOperator<TextFormatter.Change> filter_Float = change -> {
            String text = change.getText();

            if (text.matches("[0-9]*\\.?[0-9]*")) {
                return change;
            }

            return null;
        };
        TextFormatter<String> textFormatter2 = new TextFormatter<>(filter_Float);
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
        baseAddressColumn.setCellValueFactory(new PropertyValueFactory<>("base"));
        limitColumn.setCellValueFactory(new PropertyValueFactory<>("limit"));
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

    /**
     * For Open Yes and No Question Messages.
     *
     * @param msg Question string
     * @return return True if Yes ; return False if No
     */
    private boolean yesNoDialog(String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonTypeYes) {
            return true;
        } else {
            return false;
        }
    }

    @FXML
    private void addFreeHole_keyboardEvent(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            addHole();
        }
    }

    @FXML
    private void addFreeHole_mouseEvent(MouseEvent event) {
        addHole();
    }

    @FXML
    private void cancelAdditionofFreeHole_keyboardEvent(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            baseAddress_txt.clear();
            limit_txt.clear();
        }
    }

    @FXML
    private void cancelAdditionofFreeHole_mouseEvent(MouseEvent event) {
        baseAddress_txt.clear();
        limit_txt.clear();
    }

    @FXML
    private void saveFreeHoles_keyboardEvent(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            freeSpace.add_free_segment_vector(free_vector);
            sceneClose();
        }
    }

    @FXML
    private void saveFreeHoles_mouseEvent(MouseEvent event) {
        freeSpace.add_free_segment_vector(free_vector);
        sceneClose();
    }

    @FXML
    private void cancelFreeHoles_keyboardEvent(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            if (yesNoDialog("The memory will be consedered full with old process")) {
                sceneClose();
            }
        }
    }

    @FXML
    private void cancelFreeHoles_mouseEvent(MouseEvent event) {
        if (yesNoDialog("The memory will be consedered full with old process")) {
            sceneClose();
        }
    }

    private void addHole() {
        long baseAddress = -1;
        try {
            baseAddress = Long.valueOf(baseAddress_txt.getText());
        } catch (Exception e) {
            errorDialog("Base Address is empty or its exceeded the max limit allowed.");
            return;
        }
        if (baseAddress < parentCtrl.getOsReservedSize()) {
            errorDialog("Base Address is overlapped with the OS memory size.");
            return;
        }
        switch (parentCtrl.getMemoryAlignment()) {
            case _8bit:
                break;
            case _32bit:
                if ((baseAddress % 4L != 0)) {
                    errorDialog("Base Address must be divisible by 4.");
                    return;
                }
                break;
            case _64bit:
                if ((baseAddress % 8L != 0)) {
                    errorDialog("Base Address must be divisible by 8.");
                    return;
                }
                break;
        }

        long limit = -1;
        double limitDouble = -1;
        try {
            limitDouble = Double.valueOf(limit_txt.getText());
        } catch (Exception e) {
            errorDialog("limit is empty, wrong input or its exceeded the max limit allowed.");
            return;
        }
        String limitUnit = limitUnit_choiceBox.getValue();
        switch (limitUnit) {
            case "Byte":
                if (limitDouble > (Long.MAX_VALUE)) {
                    errorDialog("Total Memory Size is exceeded the max limit allowed.");
                    return;
                } else {
                    limit = Math.round(limitDouble);
                }
                break;
            case "KB":
                if (limitDouble > (Long.MAX_VALUE / 1024d)) {
                    errorDialog("Total Memory Size is exceeded the max limit allowed.");
                    return;
                } else {
                    limit = Math.round(limitDouble * 1024d);
                }
                break;
            case "MB":
                if (limitDouble > (Long.MAX_VALUE / (1024d * 1024d))) {
                    errorDialog("Total Memory Size is exceeded the max limit allowed.");
                    return;
                } else {
                    limit = Math.round(limitDouble * (1024d * 1024d));
                }
                break;
            case "GB":
                if (limitDouble > (Long.MAX_VALUE / (1024d * 1024d * 1024d))) {
                    errorDialog("Total Memory Size is exceeded the max limit allowed.");
                    return;
                } else {
                    limit = Math.round(limitDouble * (1024d * 1024d * 1024d));
                }
                break;
            case "TB":
                if (limitDouble > (Long.MAX_VALUE / (1024d * 1024d * 1024d * 1024d))) {
                    errorDialog("Total Memory Size is exceeded the max limit allowed.");
                    return;
                } else {
                    limit = Math.round(limitDouble * (1024d * 1024d * 1024d * 1024d));
                }
                break;
        }
        switch (parentCtrl.getMemoryAlignment()) {
            case _8bit:
                break;
            case _32bit:
                if ((limit % 4L != 0)) {
                    limit += 4 - (limit % 4L);
                }
                break;
            case _64bit:
                if ((limit % 8L != 0)) {
                    limit += 8 - (limit % 8L);
                }
                break;
        }

        if ((baseAddress + limit) > (parentCtrl.getOsReservedSize() + parentCtrl.getMemoryTotalSize())) {
            errorDialog("This hole is exceeeded total memory size.");
            return;
        }

        for (int i = 0; i < free_vector.size(); i++) {
            if ((free_vector.get(i).getBase() >= baseAddress) && (free_vector.get(i).getBase() < (baseAddress + limit))) {
                errorDialog("This hole is overlapped with onther hole.");
                return;
            }
            if ((free_vector.get(i).getBase() <= baseAddress) && ((free_vector.get(i).getBase() + free_vector.get(i).getLimit()) > baseAddress)) {
                errorDialog("This hole is overlapped with onther hole.");
                return;
            }
        }

        Segment newHole = new Segment(baseAddress, limit, "Free", false);
        free_vector.add(newHole);
        memoryHolesTable.getItems().add(newHole);

        baseAddress_txt.clear();
        limit_txt.clear();
    }

    private void sceneClose() {
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }
}
