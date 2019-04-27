package memorymanagement;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import memorymanagementAlgorithm.Blank;

public class MemorySimulationController implements Initializable {

    private MemorySimulationController myController;

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
        memoryConfigurationChange = false;
        memoryAlignment = memoryAlignmentOptions._8bit;
        allocationMethod = allocationMethodOptions.FirstFit;
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
