package memorymanagement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Vector;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import memorymanagementAlgorithm.Best_fit;
import memorymanagementAlgorithm.Blank;
import memorymanagementAlgorithm.Process;
import memorymanagementAlgorithm.Segment;
import memorymanagementAlgorithm.TableData;
import memorymanagementAlgorithm.Worst_fit;
import memorymanagementAlgorithm.first_fit;

public class MemorySimulationController implements Initializable {

    private MemorySimulationController myController;

    @FXML
    private Button allocateProcess_btn;
    @FXML
    private Button MemoryConfig_btn;
    @FXML
    private Button memoryCompaction_btn;
    @FXML
    private Button deallocateAll_btn;
    @FXML
    private Button deleteAllWaitingProcesses_btn;

    @FXML
    private Button LoadFromFile_btn;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AnchorPane canvas_anchorPane;
    @FXML
    private HBox zoomGroup;

    private Pane canvas;

    private int memoryWidth;
    private double byteHeigt;

    private Vector<Segment> free_vector;
    private Vector<Process> allocatedProcess_vector;
    private Vector<Process> waitProcess_vector;

    private first_fit firstFitAlgorithm;
    private Best_fit bestFitAlgorithm;
    private Worst_fit worstFitAlgorithm;

    @FXML
    private TreeTableView<TableData> allocatedProcessTable;
    @FXML
    private TreeTableColumn<TableData, String> allocatedProcessesTable_id_column;
    @FXML
    private TreeTableColumn<TableData, String> allocatedProcessesTable_name_column;
    @FXML
    private TreeTableColumn<TableData, Color> allocatedProcessesTable_color_column;
    @FXML
    private TreeTableColumn<TableData, String> allocatedProcessesTable_base_column;
    @FXML
    private TreeTableColumn<TableData, String> allocatedProcessesTable_limit_column;
    @FXML
    private TreeTableView<TableData> waitingProcessTable;
    @FXML
    private TreeTableColumn<TableData, String> waitingProcessesTable_id_column;
    @FXML
    private TreeTableColumn<TableData, String> waitingProcessesTable_name_column;
    @FXML
    private TreeTableColumn<TableData, Color> waitingProcessesTable_color_column;
    @FXML
    private TreeTableColumn<TableData, String> waitingProcessesTable_limit_column;
    @FXML
    private TableView<TableData> memoryFreeSpaceTable;
    @FXML
    private TableColumn<TableData, String> memoryFreeSpaceTable_base_column;
    @FXML
    private TableColumn<TableData, String> memoryFreeSpaceTable_limit_column;
    @FXML
    private Accordion accordionPane;
    @FXML
    private Label MemoryTotalSize_Label;
    @FXML
    private Label MemoryAlignment_Label;
    @FXML
    private Label AllocationMethid_Label;
    @FXML
    private Label MemoryFreeSize_Label;
    @FXML
    private Label MemoryFreePrcentage_Label;
    @FXML
    private Label MemoryUsed_Label;
    @FXML
    private Label MemoryUsedPercentage_Label;

    public enum memoryAlignmentOptions {
        _8bit, _32bit, _64bit
    };

    public enum allocationMethodOptions {
        FirstFit, BestFit, WorstFit
    };

    private long memoryTotalSize;
    private long osReservedSize;
    private memoryAlignmentOptions memoryAlignment;
    private allocationMethodOptions allocationMethod;

    private boolean memoryConfigurationChange;
    private boolean validProcess;
    Process newProcess;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        memoryConfigurationChange = false;
        canvas = new Pane();
        scrollPane.setContent(canvas);
        tableInitialize();
        sceneDisable();
    }

    private void sceneDisable() {
        accordionPane.setDisable(true);
        scrollPane.setDisable(true);
        allocateProcess_btn.setDisable(true);
        LoadFromFile_btn.setDisable(true);
        zoomGroup.setDisable(true);
        memoryCompaction_btn.setDisable(true);
        deallocateAll_btn.setDisable(true);
        deleteAllWaitingProcesses_btn.setDisable(true);
    }

    private void sceneEnable() {
        accordionPane.setDisable(false);
        scrollPane.setDisable(false);
        allocateProcess_btn.setDisable(false);
        LoadFromFile_btn.setDisable(false);
        zoomGroup.setDisable(false);
        memoryCompaction_btn.setDisable(false);
        deallocateAll_btn.setDisable(false);
        deleteAllWaitingProcesses_btn.setDisable(false);
        validProcess = true;
        Process.setPROCESS_ID(0);
    }

    public void sceneInitialize() throws IOException {
        memoryHardwareConfigDialog();
    }

    private void memoryHardwareConfigDialog() throws IOException {
        FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("MemoryConfiguration.fxml"));
        Parent root1 = (Parent) fxmlLoader1.load();

        MemoryConfigurationController ctrl1 = fxmlLoader1.getController();
        ctrl1.sceneInitialization(myController);

        memoryConfigurationChange = false;

        Stage stage1 = new Stage();
        stage1.setScene(new Scene(root1));
        stage1.initModality(Modality.APPLICATION_MODAL);
        stage1.setTitle("Memory Hardware");
        stage1.setResizable(false);
        stage1.initStyle(StageStyle.UTILITY);
        stage1.showAndWait();

        if (memoryConfigurationChange) {
            FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("MemoryInitialization.fxml"));
            Parent root2 = (Parent) fxmlLoader2.load();

            Blank freeHoles = new Blank();
            MemoryInitializationController ctrl2 = fxmlLoader2.getController();
            ctrl2.sceneInitialization(myController, freeHoles);

            Stage stage2 = new Stage();
            stage2.setScene(new Scene(root2));
            stage2.initModality(Modality.APPLICATION_MODAL);
            stage2.setTitle("Memory Initialization");
            stage2.setResizable(false);
            stage2.initStyle(StageStyle.UTILITY);
            stage2.showAndWait();

            sceneEnable();

            for (int i = 0; i < freeHoles.get_number_of_free_segments(); i++) {
                freeHoles.get_segemnt_i(i).setBase((freeHoles.get_segemnt_i(i).getBase() - osReservedSize));
            }

            switch (allocationMethod) {
                case FirstFit:
                    firstFitAlgorithm.insert_holes(freeHoles);
                    break;
                case BestFit:
                    bestFitAlgorithm.insert_holes(freeHoles);
                    break;
                case WorstFit:
                    worstFitAlgorithm.insert_holes(freeHoles);
                    break;
            }
            zoomFit();
        }
    }

    @FXML
    private void allocateProcess_keyboardEvent(KeyEvent event) throws IOException {
        if (event.getCode().toString().equals("ENTER")) {
            allocateProcessDialog();
        }
    }

    @FXML
    private void allocateProcess_mouseEvent(MouseEvent event) throws IOException {
        allocateProcessDialog();
    }

    @FXML
    private void memoryCompaction_KeyboardEvent(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            if (yesNoDialog("Are you sure you want to do memory compaction?")) {
                switch (allocationMethod) {
                    case FirstFit:
                        firstFitAlgorithm.memoryCompaction();
                        break;
                    case BestFit:
                        bestFitAlgorithm.memoryCompaction();
                        break;
                    case WorstFit:
                        worstFitAlgorithm.memoryCompaction();
                        break;
                }
                draw();
            }
        }
    }

    @FXML
    private void memoryCompaction_mouseEvent(MouseEvent event) {
        if (yesNoDialog("Are you sure you want to do memory compaction?")) {
            switch (allocationMethod) {
                case FirstFit:
                    firstFitAlgorithm.memoryCompaction();
                    break;
                case BestFit:
                    bestFitAlgorithm.memoryCompaction();
                    break;
                case WorstFit:
                    worstFitAlgorithm.memoryCompaction();
                    break;
            }
            draw();
        }
    }

    @FXML
    private void deallocateAll_keyboardEvent(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            if (yesNoDialog("Are you sure you want to deallocate all processes?")) {
                switch (allocationMethod) {
                    case FirstFit:
                        firstFitAlgorithm.remove_all_runing();
                        break;
                    case BestFit:
                        bestFitAlgorithm.remove_all_runing();
                        break;
                    case WorstFit:
                        worstFitAlgorithm.remove_all_runing();
                        break;
                }
                draw();
            }
        }
    }

    @FXML
    private void deallocateAll_mouseEvent(MouseEvent event) {
        if (yesNoDialog("Are you sure you want to deallocate all processes?")) {
            switch (allocationMethod) {
                case FirstFit:
                    firstFitAlgorithm.remove_all_runing();
                    break;
                case BestFit:
                    bestFitAlgorithm.remove_all_runing();
                    break;
                case WorstFit:
                    worstFitAlgorithm.remove_all_runing();
                    break;
            }
            draw();
        }
    }

    @FXML
    private void deleteAllWaitingProcesses_keyboardEvent(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            if (yesNoDialog("Are you sure you want to delete all waiting processes?")) {
                switch (allocationMethod) {
                    case FirstFit:
                        firstFitAlgorithm.clear_waiting_process();
                        break;
                    case BestFit:
                        bestFitAlgorithm.clear_waiting_process();
                        break;
                    case WorstFit:
                        worstFitAlgorithm.clear_waiting_process();
                        break;
                }
                tableFill();
            }
        }
    }

    @FXML
    private void deleteAllWaitingProcesses_mouseEvent(MouseEvent event) {
        if (yesNoDialog("Are you sure you want to delete all waiting processes?")) {
            switch (allocationMethod) {
                case FirstFit:
                    firstFitAlgorithm.clear_waiting_process();
                    break;
                case BestFit:
                    bestFitAlgorithm.clear_waiting_process();
                    break;
                case WorstFit:
                    worstFitAlgorithm.clear_waiting_process();
                    break;
            }
            tableFill();
        }
    }

    @FXML
    private void memoryHardwareConfiguration_keyboardEvent(KeyEvent event) throws IOException {
        if (event.getCode().toString().equals("ENTER")) {
            if (yesNoDialog("Are you sure you want to change memory configuration?")) {
                memoryHardwareConfigDialog();
            }
        }
    }

    @FXML
    private void memoryHardwareConfiguration_mouseEvent(MouseEvent event) throws IOException {
        if (yesNoDialog("Are you sure you want to change memory configuration?")) {
            memoryHardwareConfigDialog();
        }
    }

    @FXML
    private void zoomOut_keyboardEvent(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            zoomOut();
        }
    }

    @FXML
    private void zoomOut_mouseEvent(MouseEvent event) {
        zoomOut();
    }

    @FXML
    private void zoomIn_keyboardEvent(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            zoomIn();
        }
    }

    @FXML
    private void zoomIn_mouseEvent(MouseEvent event) {
        zoomIn();
    }

    @FXML
    private void zoomFit_keyboardEvent(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            zoomFit();
        }
    }

    @FXML
    private void zoomFit_mouseEvent(MouseEvent event) {
        zoomFit();
    }

    @FXML
    private void loadProcessesFromFile_keyboardEvent(KeyEvent event) throws FileNotFoundException {
        if (event.getCode().toString().equals("ENTER")) {
            loadFromFile();
        }
    }

    @FXML
    private void loadProcessesFromFile_mouseEvent(MouseEvent event) throws FileNotFoundException {
        loadFromFile();
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

    private void alertDialog(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
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

    private void loadFromFile() throws FileNotFoundException {
        alertDialog("The text file must contain each pocess in a separate line and each pocess must be in the format (Seg0_Name Seg0_Limit Seg0_Limit_Unit Seg1_Name Seg1_Limit Seg1_Limit_Unit ...).\n"
                + "{B for Bytes, KB, MB, GB, TB}");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Source File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text file", "*.txt")
        );
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.dir"))
        );
        File file = fileChooser.showOpenDialog(LoadFromFile_btn.getScene().getWindow());
        if (file != null) {
            Scanner scanner = new Scanner(file);
            for (int k = 0; scanner.hasNextLine(); k++) {
                Scanner line = new Scanner(scanner.nextLine());

                if (validProcess) {
                    newProcess = new Process();
                } else {
                    newProcess.clear_segment_vector();
                }

                validProcess = true;
                Segment.setSEGMENT_ID(0);

                for (int i = 0; line.hasNext(); i++) {

                    String segNameRead = "";
                    String segLimitRead = "";
                    String segLimitUnitRead = "";
                    long limit = -1;

                    segNameRead = line.next();
                    if (line.hasNext()) {
                        segLimitRead = line.next();
                    } else {
                        errorDialog("Error in Process " + Integer.valueOf(k) + " Input");
                        validProcess = false;
                        break;
                    }
                    if (line.hasNext()) {
                        segLimitUnitRead = line.next();
                    } else {
                        errorDialog("Error in Process " + Integer.valueOf(k) + " Input");
                        validProcess = false;
                        break;
                    }

                    double limitDouble = -1;
                    try {
                        limitDouble = Double.valueOf(segLimitRead);
                    } catch (Exception e) {
                        errorDialog("Size is empty, wrong input or it exceeded the max limit allowed.\n"
                                + "Error in Process " + Integer.valueOf(k) + ", Seg " + Integer.valueOf(i));
                        validProcess = false;
                        break;
                    }
                    if (limitDouble == 0) {
                        errorDialog("Size can't equal to zero.\n"
                                + "Error in Process " + Integer.valueOf(k) + ", Seg " + Integer.valueOf(i));
                        validProcess = false;
                        break;
                    }
                    String limitUnit = segLimitUnitRead;
                    switch (limitUnit) {
                        case "B":
                            if (limitDouble > (Long.MAX_VALUE)) {
                                errorDialog("Total Memory Size exceeded the max limit allowed.\n"
                                        + "Error in Process " + Integer.valueOf(k) + ", Seg " + Integer.valueOf(i));
                                validProcess = false;
                            } else {
                                limit = Math.round(limitDouble);
                            }
                            break;
                        case "KB":
                            if (limitDouble > (Long.MAX_VALUE / 1024d)) {
                                errorDialog("Total Memory Size exceeded the max limit allowed.\n"
                                        + "Error in Process " + Integer.valueOf(k) + ", Seg " + Integer.valueOf(i));
                                validProcess = false;
                            } else {
                                limit = Math.round(limitDouble * 1024d);
                            }
                            break;
                        case "MB":
                            if (limitDouble > (Long.MAX_VALUE / (1024d * 1024d))) {
                                errorDialog("Total Memory Size exceeded the max limit allowed.\n"
                                        + "Error in Process " + Integer.valueOf(k) + ", Seg " + Integer.valueOf(i));
                                validProcess = false;
                            } else {
                                limit = Math.round(limitDouble * (1024d * 1024d));
                            }
                            break;
                        case "GB":
                            if (limitDouble > (Long.MAX_VALUE / (1024d * 1024d * 1024d))) {
                                errorDialog("Total Memory Size exceeded the max limit allowed.\n"
                                        + "Error in Process " + Integer.valueOf(k) + ", Seg " + Integer.valueOf(i));
                                validProcess = false;
                            } else {
                                limit = Math.round(limitDouble * (1024d * 1024d * 1024d));
                            }
                            break;
                        case "TB":
                            if (limitDouble > (Long.MAX_VALUE / (1024d * 1024d * 1024d * 1024d))) {
                                errorDialog("Total Memory Size exceeded the max limit allowed.\n"
                                        + "Error in Process " + Integer.valueOf(k) + ", Seg " + Integer.valueOf(i));
                                validProcess = false;
                            } else {
                                limit = Math.round(limitDouble * (1024d * 1024d * 1024d * 1024d));
                            }
                            break;
                        default:
                            errorDialog("Error in limit unit.\n"
                                    + "Error in Process " + Integer.valueOf(k) + ", Seg " + Integer.valueOf(i));
                            validProcess = false;
                            break;
                    }
                    if (validProcess == false) {
                        break;
                    }
                    switch (memoryAlignment) {
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

                    if (limit > memoryTotalSize) {
                        errorDialog("This hole exceeeded total memory size.\n"
                                + "Error in Process " + Integer.valueOf(k) + ", Seg " + Integer.valueOf(i));
                        validProcess = false;
                        break;
                    }

                    Segment newSeg = new Segment(limit, segNameRead, true);
                    newProcess.add_Segment(newSeg);
                    validProcess = true;
                }
                if (validProcess) {
                    switch (allocationMethod) {
                        case FirstFit:
                            if (firstFitAlgorithm.allocate_process(newProcess)) {
                                alertDialog("This process successfully allocated");
                            } else {
                                alertDialog("This process can't be allocated, it will be put in waiting queue");
                            }
                            break;
                        case BestFit:
                            if (bestFitAlgorithm.allocate_process(newProcess)) {
                                alertDialog("This process successfully allocated");
                            } else {
                                alertDialog("This process can't be allocated, it will be put in waiting queue");
                            }
                            break;
                        case WorstFit:
                            if (worstFitAlgorithm.allocate_process(newProcess)) {
                                alertDialog("This process successfully allocated");
                            } else {
                                alertDialog("This process can't be allocated, it will be put in waiting queue");
                            }
                            break;
                    }
                    draw();
                }
            }
            scanner.close();
        }
    }

    private void allocateProcessDialog() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddProcess.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();

        if (validProcess) {
            newProcess = new Process();
        } else {
            newProcess.clear_segment_vector();
        }

        validProcess = false;
        AddProcessController ctrl = fxmlLoader.getController();
        ctrl.sceneInitialization(myController, newProcess);

        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Allocate Process");
        stage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY);
        stage.showAndWait();

        if (validProcess) {
            switch (allocationMethod) {
                case FirstFit:
                    if (firstFitAlgorithm.allocate_process(newProcess)) {
                        alertDialog("This process successfully allocated");
                    } else {
                        alertDialog("This process can't be allocated, it will be put in waiting queue");
                    }
                    break;
                case BestFit:
                    if (bestFitAlgorithm.allocate_process(newProcess)) {
                        alertDialog("This process successfully allocated");
                    } else {
                        alertDialog("This process can't be allocated, it will be put in waiting queue");
                    }
                    break;
                case WorstFit:
                    if (worstFitAlgorithm.allocate_process(newProcess)) {
                        alertDialog("This process successfully allocated");
                    } else {
                        alertDialog("This process can't be allocated, it will be put in waiting queue");
                    }
                    break;
            }
            draw();
        }
    }

    private void tableInitialize() {
        allocatedProcessesTable_id_column.setCellValueFactory(new TreeItemPropertyValueFactory<>("ID"));
        allocatedProcessesTable_name_column.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        allocatedProcessesTable_color_column.setCellValueFactory(new TreeItemPropertyValueFactory<>("color"));
        // Custom rendering of the table cell.
        allocatedProcessesTable_color_column.setCellFactory(column -> {
            return new TreeTableCell<TableData, Color>() {
                @Override
                protected void updateItem(Color item, boolean empty) {
                    super.updateItem(item, empty);
                    if (getTreeTableRow() == null || empty) {
                        setText(null);
                        setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                    } else {
                        setTextFill(Color.WHITE);
                        setText(item.toString());
                        setBackground(new Background(new BackgroundFill(item, CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                }
            };
        });
        allocatedProcessesTable_base_column.setCellValueFactory(new TreeItemPropertyValueFactory<>("base"));
        allocatedProcessesTable_limit_column.setCellValueFactory(new TreeItemPropertyValueFactory<>("limit"));
        allocatedProcessTable.setRowFactory(new Callback<TreeTableView<TableData>, TreeTableRow<TableData>>() {
            @Override
            public TreeTableRow<TableData> call(TreeTableView<TableData> treeTableView) {
                final ContextMenu contextMenu = new ContextMenu();
                final MenuItem deallocatedMenuItem = new MenuItem("Dellocate");
                final TreeTableRow<TableData> row = new TreeTableRow<>();

                deallocatedMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        TableData processtoDeallocate = allocatedProcessTable.getSelectionModel().getSelectedItem().getValue();
                        switch (allocationMethod) {
                            case FirstFit:
                                firstFitAlgorithm.deallocate_process(processtoDeallocate.getProcess());
                                break;
                            case BestFit:
                                bestFitAlgorithm.deallocate_process(processtoDeallocate.getProcess());
                                break;
                            case WorstFit:
                                worstFitAlgorithm.deallocate_process(processtoDeallocate.getProcess());
                                break;
                        }
                        draw();
                    }
                });
                contextMenu.getItems().add(deallocatedMenuItem);

                Tooltip processTooltip = new Tooltip("To deallocate process, please right click then click deallocate");
                row.emptyProperty().addListener((obs, wasEmpty, isNowEmpty)
                        -> row.setContextMenu((isNowEmpty) ? null : (!(row.getItem().getLimit().equals("") && !row.getItem().getID().equals("OS Reserved"))) ? null : contextMenu));
                row.emptyProperty().addListener((obs, wasEmpty, isNowEmpty)
                        -> row.setTooltip((isNowEmpty) ? null : (!(row.getItem().getLimit().equals("") && !row.getItem().getID().equals("OS Reserved"))) ? null : processTooltip));

                return row;
            }
        });

        waitingProcessesTable_id_column.setCellValueFactory(new TreeItemPropertyValueFactory<>("ID"));
        waitingProcessesTable_name_column.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        waitingProcessesTable_color_column.setCellValueFactory(new TreeItemPropertyValueFactory<>("color"));
        // Custom rendering of the table cell.
        waitingProcessesTable_color_column.setCellFactory(column -> {
            return new TreeTableCell<TableData, Color>() {
                @Override
                protected void updateItem(Color item, boolean empty) {
                    super.updateItem(item, empty);
                    if (getTreeTableRow() == null || empty) {
                        setText(null);
                        setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                    } else {
                        setTextFill(Color.WHITE);
                        setText(item.toString());
                        setBackground(new Background(new BackgroundFill(item, CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                }
            };
        });
        waitingProcessesTable_limit_column.setCellValueFactory(new TreeItemPropertyValueFactory<>("limit"));
        waitingProcessTable.setRowFactory(new Callback<TreeTableView<TableData>, TreeTableRow<TableData>>() {
            @Override
            public TreeTableRow<TableData> call(TreeTableView<TableData> treeTableView) {
                final ContextMenu contextMenu = new ContextMenu();
                final MenuItem deleteMenuItem = new MenuItem("Delete");
                final TreeTableRow<TableData> row = new TreeTableRow<>();

                deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        TableData processtoDelete = waitingProcessTable.getSelectionModel().getSelectedItem().getValue();
                        switch (allocationMethod) {
                            case FirstFit:
                                firstFitAlgorithm.remove_waiting_process(processtoDelete.getProcess());
                                break;
                            case BestFit:
                                bestFitAlgorithm.remove_waiting_process(processtoDelete.getProcess());
                                break;
                            case WorstFit:
                                worstFitAlgorithm.remove_waiting_process(processtoDelete.getProcess());
                                break;
                        }
                        tableFill();
                    }
                });
                contextMenu.getItems().add(deleteMenuItem);

                Tooltip processTooltip = new Tooltip("To delete process, please right click then click Delete");
                row.emptyProperty().addListener((obs, wasEmpty, isNowEmpty)
                        -> row.setContextMenu((isNowEmpty) ? null : (!row.getItem().getLimit().equals("")) ? null : contextMenu));
                row.emptyProperty().addListener((obs, wasEmpty, isNowEmpty)
                        -> row.setTooltip((isNowEmpty) ? null : (!row.getItem().getLimit().equals("")) ? null : processTooltip));

                return row;
            }
        });

        memoryFreeSpaceTable_base_column.setCellValueFactory(new PropertyValueFactory<>("base"));
        memoryFreeSpaceTable_limit_column.setCellValueFactory(new PropertyValueFactory<>("limit"));
    }

    private void tableFill() {
        //Allocated Processes table
        allocatedProcessTable.setRoot(null);
        // Create the root node and add children
        TreeItem<TableData> allocatedTableRootNode = new TreeItem<>();
        TreeItem<TableData> OSNode = new TreeItem<>(new TableData("OS Reserved", "", "0", Long.toString(osReservedSize), Color.RED));
        allocatedTableRootNode.getChildren().add(OSNode);
        for (int i = 0; i < allocatedProcess_vector.size(); i++) {
            TreeItem<TableData> Node = new TreeItem<>(new TableData(("P" + Long.toString(allocatedProcess_vector.get(i).getID())),
                    allocatedProcess_vector.get(i).getColor(),
                    allocatedProcess_vector.get(i)));
            for (int j = 0; j < allocatedProcess_vector.get(i).getSegment_vector().size(); j++) {
                Segment seg = allocatedProcess_vector.get(i).get_segemnt_i(j);
                TreeItem<TableData> child = new TreeItem<>(new TableData(("Seg" + Long.toString(seg.getID())),
                        seg.getName(),
                        Long.toString(seg.getBase()),
                        Long.toString(seg.getLimit()),
                        allocatedProcess_vector.get(i).getColor()));

                Node.getChildren().add(child);
            }
            allocatedTableRootNode.getChildren().add(Node);
        }
        allocatedProcessTable.setShowRoot(false);
        allocatedProcessTable.setRoot(allocatedTableRootNode);

        //Waiting Table
        waitingProcessTable.setRoot(null);
        // Create the root node and add children
        TreeItem<TableData> waitingTableRootNode = new TreeItem<>();
        for (int i = 0; i < waitProcess_vector.size(); i++) {
            TreeItem<TableData> Node = new TreeItem<>(new TableData(("P" + Long.toString(waitProcess_vector.get(i).getID())),
                    waitProcess_vector.get(i).getColor(),
                    waitProcess_vector.get(i)));
            for (int j = 0; j < waitProcess_vector.get(i).getSegment_vector().size(); j++) {
                Segment seg = waitProcess_vector.get(i).get_segemnt_i(j);
                TreeItem<TableData> child = new TreeItem<>(new TableData(("Seg" + Long.toString(seg.getID())),
                        seg.getName(),
                        "",
                        Long.toString(seg.getLimit()),
                        waitProcess_vector.get(i).getColor()));

                Node.getChildren().add(child);
            }
            waitingTableRootNode.getChildren().add(Node);
        }
        waitingProcessTable.setShowRoot(false);
        waitingProcessTable.setRoot(waitingTableRootNode);

        //Memory Free Space Table
        memoryFreeSpaceTable.getItems().clear();
        for (int i = 0; i < free_vector.size(); i++) {
            memoryFreeSpaceTable.getItems().add(new TableData(Long.toString(free_vector.get(i).getBase()), Long.toString(free_vector.get(i).getLimit())));
        }

    }

    private void canvasReset() {
        canvas.getChildren().clear();
        canvas.setPrefWidth(0);
        canvas.setPrefHeight(0);
    }

    private void canvasInitialization() {
        Rectangle rectangle;
        Text text;

        //240 Address text max size
        //6   Rect Margin
        canvas.setPrefWidth((memoryWidth + 6) + 240);
        //80 Margin
        //5  Rect Margin
        canvas.setPrefHeight((((memoryTotalSize + osReservedSize) * byteHeigt) + 5) + 80);

        rectangle = new Rectangle((memoryWidth + 6), (((memoryTotalSize + osReservedSize) * byteHeigt) + 5), Color.TRANSPARENT);
        rectangle.setStroke(Color.BLUE);
        rectangle.setStrokeWidth(4);
        rectangle.relocate((200 - 5), (40 - 5));

        DropShadow e = new DropShadow();
        e.setColor(Color.BLUE);
        e.setSpread(0.4);
        e.setWidth(21);
        e.setHeight(21);
        e.setOffsetX(0);
        e.setOffsetY(0);
        e.setRadius(15);
        rectangle.setEffect(e);

        canvas.getChildren().add(rectangle);

        text = new Text();
        text.setFill(Color.WHITE);
        switch (memoryAlignment) {
            case _8bit:
                text.setText("0");
                break;
            case _32bit:
                text.setText("3");
                break;
            case _64bit:
                text.setText("7");
                break;
        }
        text.setX(200 + memoryWidth);
        text.setY(30);
        canvas.getChildren().add(text);

        text = new Text();
        text.setFill(Color.WHITE);
        switch (memoryAlignment) {
            case _8bit:
                text.setText(Long.toString(memoryTotalSize + osReservedSize - 1));
                break;
            case _32bit:
                text.setText(Long.toString(memoryTotalSize + osReservedSize - 4));
                break;
            case _64bit:
                text.setText(Long.toString(memoryTotalSize + osReservedSize - 8));
                break;
        }
        text.setX(180 - text.getLayoutBounds().getWidth());
        text.setY(40 + ((memoryTotalSize + osReservedSize) * byteHeigt) + 5);
        canvas.getChildren().add(text);

        if (osReservedSize > 0) {
            text = new Text();
            text.setFill(Color.WHITE);
            text.setText("0");
            text.setX(180 - text.getLayoutBounds().getWidth());
            text.setY(45);
            canvas.getChildren().add(text);

            rectangle = new Rectangle(memoryWidth, (osReservedSize * byteHeigt), Color.RED);
            rectangle.setArcWidth(30);
            rectangle.setArcHeight(30);
            rectangle.relocate(200, 40);
            canvas.getChildren().add(rectangle);

            text = new Text();
            text.setFill(Color.WHITE);
            text.setText("OS Reserved\n" + "Size: " + Long.toString(osReservedSize));
            text.setTextAlignment(TextAlignment.CENTER);
            text.setX(200 + (memoryWidth / 2d) - (text.getLayoutBounds().getWidth() / 2d));
            text.setY(55 + ((osReservedSize * byteHeigt) / 2d) - (text.getLayoutBounds().getHeight() / 2d));
            canvas.getChildren().add(text);
        }
    }

    private void draw() {
        canvasReset();
        canvasInitialization();

        switch (allocationMethod) {
            case FirstFit:
                free_vector = firstFitAlgorithm.getFreeSpace().get_segment_vector();
                allocatedProcess_vector = firstFitAlgorithm.getAllocatedProcessVector();
                waitProcess_vector = firstFitAlgorithm.getWaitingProcessVector();
                break;
            case BestFit:
                free_vector = bestFitAlgorithm.getFreeSpace().get_segment_vector();
                allocatedProcess_vector = bestFitAlgorithm.getAllocatedProcessVector();
                waitProcess_vector = bestFitAlgorithm.getWaitingProcessVector();
                break;
            case WorstFit:
                free_vector = worstFitAlgorithm.getFreeSpace().get_segment_vector();
                allocatedProcess_vector = worstFitAlgorithm.getAllocatedProcessVector();
                waitProcess_vector = worstFitAlgorithm.getWaitingProcessVector();
                break;
        }

        Rectangle rectangle;
        Text text;

        for (int i = 0; i < free_vector.size(); i++) {
            long segBase = free_vector.get(i).getBase() + osReservedSize;
            text = new Text();
            text.setFill(Color.WHITE);
            text.setText(Long.toString(segBase));
            text.setX(180 - text.getLayoutBounds().getWidth());
            text.setY(40 + (segBase * byteHeigt));
            canvas.getChildren().add(text);

            text = new Text();
            text.setFill(Color.WHITE);
            text.setText("Free Space\n" + "Size: " + Long.toString(free_vector.get(i).getLimit()));
            text.setTextAlignment(TextAlignment.CENTER);
            text.setX(200 + (memoryWidth / 2d) - (text.getLayoutBounds().getWidth() / 2d));
            text.setY(55 + (segBase * byteHeigt) + ((free_vector.get(i).getLimit() * byteHeigt) / 2d) - (text.getLayoutBounds().getHeight() / 2d));
            canvas.getChildren().add(text);
        }

        for (int i = 0; i < allocatedProcess_vector.size(); i++) {
            for (int j = 0; j < allocatedProcess_vector.get(i).getSegment_vector().size(); j++) {
                Segment drawn = allocatedProcess_vector.get(i).getSegment_vector().get(j);
                long segBase = osReservedSize + drawn.getBase();

                text = new Text();
                text.setFill(Color.WHITE);
                text.setText(Long.toString(segBase));
                text.setX(180 - text.getLayoutBounds().getWidth());
                text.setY(40 + (segBase * byteHeigt));
                canvas.getChildren().add(text);

                rectangle = new Rectangle(memoryWidth, (drawn.getLimit() * byteHeigt), allocatedProcess_vector.get(i).getColor());
                rectangle.setArcWidth(30);
                rectangle.setArcHeight(30);
                rectangle.relocate(200, (40 + (segBase * byteHeigt)));
                canvas.getChildren().add(rectangle);

                text = new Text();
                text.setFill(Color.WHITE);
                text.setText("P" + Long.toString(allocatedProcess_vector.get(i).getID()) + " , Seg" + Long.toString(drawn.getID()) + "\n"
                        + drawn.getName() + "\n"
                        + "Size: " + Long.toString(drawn.getLimit()));
                text.setTextAlignment(TextAlignment.CENTER);
                text.setX(200 + (memoryWidth / 2d) - (text.getLayoutBounds().getWidth() / 2d));
                text.setY(55 + (segBase * byteHeigt) + ((drawn.getLimit() * byteHeigt) / 2d) - (text.getLayoutBounds().getHeight() / 2d));
                canvas.getChildren().add(text);

            }
        }

        tableFill();
        MemoryInfo();
    }

    private void MemoryInfo() {
        long totalSize = memoryTotalSize + osReservedSize;
        MemoryTotalSize_Label.setText(Long.toString(totalSize));
        long freeSize = 0;
        long usedSize = osReservedSize;
        switch (allocationMethod) {
            case FirstFit:
                freeSize = firstFitAlgorithm.getFreeSpace().get_total_size();
                usedSize += firstFitAlgorithm.get_total_used_size();
                break;
            case BestFit:
                freeSize = bestFitAlgorithm.getFreeSpace().get_total_size();
                usedSize += bestFitAlgorithm.get_total_used_size();
                break;
            case WorstFit:
                freeSize = worstFitAlgorithm.getFreeSpace().get_total_size();
                usedSize += worstFitAlgorithm.get_total_used_size();
                break;
        }
        MemoryFreeSize_Label.setText(Long.toString(freeSize));
        MemoryFreePrcentage_Label.setText(String.format("%.5g", ((double) freeSize / (double) totalSize) * 100));
        MemoryUsed_Label.setText(Long.toString(usedSize));
        MemoryUsedPercentage_Label.setText(String.format("%.5g", ((double) usedSize / (double) totalSize) * 100));
    }

    private void zoomOut() {
        byteHeigt *= 0.8;
        draw();
    }

    private void zoomIn() {
        byteHeigt *= 1.25;
        draw();
    }

    private void zoomFit() {
        byteHeigt = ((canvas_anchorPane.getHeight() - 120) / (memoryTotalSize + osReservedSize));
        draw();
    }

    /**
     * @return the memoryTotalSize
     */
    public long getMemoryTotalSize() {
        return memoryTotalSize;
    }

    /**
     * @param memoryTotalSize the memoryTotalSize to set
     */
    public void setMemoryTotalSize(long memoryTotalSize) {
        this.memoryTotalSize = memoryTotalSize;
    }

    /**
     * @return the osReservedSize
     */
    public long getOsReservedSize() {
        return osReservedSize;
    }

    /**
     * @param osReservedSize the osReservedSize to set
     */
    public void setOsReservedSize(long osReservedSize) {
        this.osReservedSize = osReservedSize;
    }

    /**
     * @return the memoryAlignment
     */
    public memoryAlignmentOptions getMemoryAlignment() {
        return memoryAlignment;
    }

    /**
     * @param memoryAlignment the memoryAlignment to set
     */
    public void setMemoryAlignment(memoryAlignmentOptions memoryAlignment) {
        this.memoryAlignment = memoryAlignment;
        switch (memoryAlignment) {
            case _8bit:
                memoryWidth = 200;
                MemoryAlignment_Label.setText("1 Byte (8bit)");
                break;
            case _32bit:
                memoryWidth = 400;
                MemoryAlignment_Label.setText("4 Byte (32bit)");
                break;
            case _64bit:
                memoryWidth = 600;
                MemoryAlignment_Label.setText("8 Byte (64bit)");
                break;
        }
    }

    /**
     * @return the allocationMethod
     */
    public allocationMethodOptions getAllocationMethod() {
        return allocationMethod;
    }

    /**
     * @param allocationMethod the allocationMethod to set
     */
    public void setAllocationMethod(allocationMethodOptions allocationMethod) {
        this.allocationMethod = allocationMethod;
        switch (allocationMethod) {
            case FirstFit:
                firstFitAlgorithm = new first_fit(memoryTotalSize);
                bestFitAlgorithm = null;
                worstFitAlgorithm = null;
                AllocationMethid_Label.setText("First Fit");
                break;
            case BestFit:
                firstFitAlgorithm = null;
                bestFitAlgorithm = new Best_fit(memoryTotalSize);
                worstFitAlgorithm = null;
                AllocationMethid_Label.setText("Best Fit");
                break;
            case WorstFit:
                firstFitAlgorithm = null;
                bestFitAlgorithm = null;
                worstFitAlgorithm = new Worst_fit(memoryTotalSize);
                AllocationMethid_Label.setText("Worst Fit");
                break;
        }
    }

    /**
     * @param myController the myController to set
     */
    public void setMyController(MemorySimulationController myController) {
        this.myController = myController;
    }

    /**
     * @param memoryConfigurationChange the memoryConfigurationChange to set
     */
    public void setMemoryConfigurationChange(boolean memoryConfigurationChange) {
        this.memoryConfigurationChange = memoryConfigurationChange;
    }

    /**
     * @param validProcess the validProcess to set
     */
    public void setValidProcess(boolean validProcess) {
        this.validProcess = validProcess;
    }
}
