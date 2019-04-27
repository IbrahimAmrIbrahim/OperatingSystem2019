package memorymanagement;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeTableView;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import memorymanagementAlgorithm.Blank;

public class MemorySimulationController implements Initializable {

    private MemorySimulationController myController;
    @FXML
    private TreeTableView<?> treeTable;
    @FXML
    private Button allocateProcess_btn;
    @FXML
    private Button MemoryConfig_btn;
    @FXML
    private Button clear_btn;
    @FXML
    private ScrollPane scrollPane;

    private Pane canvas;
    private double zoomFactor;

    private int memoryWidth;
    private int byteHeigt;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        memoryTotalSize = 0;
        osReservedSize = 0;
        zoomFactor = 1;
        memoryWidth = 200;
        byteHeigt = 8;
        memoryConfigurationChange = false;
        memoryAlignment = memoryAlignmentOptions._8bit;
        allocationMethod = allocationMethodOptions.FirstFit;
        sceneDisable();
        canvas = new Pane();
        scrollPane.setContent(canvas);
    }

    private void sceneDisable() {
        treeTable.setDisable(true);
        scrollPane.setDisable(true);
        allocateProcess_btn.setDisable(true);
        clear_btn.setDisable(true);
        MemoryConfig_btn.setDisable(true);
    }

    private void sceneEnable() {
        treeTable.setDisable(false);
        scrollPane.setDisable(false);
        allocateProcess_btn.setDisable(false);
        clear_btn.setDisable(false);
        MemoryConfig_btn.setDisable(false);
    }

    public void sceneInitialize() throws IOException {
        //memoryHardwareConfigDialog();
        sceneEnable();
        canvasInitialization();
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
            stage2.initStyle(StageStyle.UTILITY);
            stage2.showAndWait();

            sceneEnable();
            canvasInitialization();
            freeHoles.print();
        }
    }

    @FXML
    private void allocateProcess_mouseEvent(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddProcess.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Allocate Process");
        stage.initStyle(StageStyle.UTILITY);
        stage.showAndWait();
    }

    private void canvasReset() {
        canvas.getChildren().clear();
        canvas.getTransforms().add(Transform.scale((1.0 / zoomFactor), (1.0 / zoomFactor)));
        canvas.setPrefWidth(0);
        canvas.setPrefHeight(0);
    }

    private void canvasInitialization() {
        canvasReset();
        Rectangle rectangle;
        Text text;

        //************************************
        memoryTotalSize = 1024;
        osReservedSize = 50;
        //************************************
        //240 Address text max size
        //6   Rect Margin
        //200 byteWidth
        canvas.setPrefWidth((memoryWidth + 6) + 240);
        //80 Margin
        //5  Rect Margin
        canvas.setPrefHeight(((memoryTotalSize * byteHeigt) + 5) + 80);

        rectangle = new Rectangle((memoryWidth + 6), ((memoryTotalSize * byteHeigt) + 5), Color.TRANSPARENT);
        rectangle.setStroke(Color.BLUE);
        rectangle.setStrokeWidth(4);
        rectangle.relocate((200 - 6), (40 - 5));

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
        text.setText("0");
        text.setX(190 - text.getLayoutBounds().getWidth());
        text.setY(45);
        canvas.getChildren().add(text);

        if (osReservedSize > 0) {
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
            text.setY(40 + ((osReservedSize * byteHeigt) / 2d) - (text.getLayoutBounds().getHeight() / 2d));
            canvas.getChildren().add(text);
        }
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
                byteHeigt = 64;
                break;
            case _32bit:
                memoryWidth = 400;
                byteHeigt = 16;
                break;
            case _64bit:
                memoryWidth = 600;
                byteHeigt = 8;
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
}
