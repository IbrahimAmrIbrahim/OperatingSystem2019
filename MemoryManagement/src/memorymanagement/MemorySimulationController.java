package memorymanagement;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MemorySimulationController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            memoryHardwareConfigDialog();
        } catch (IOException ex) {
            Logger.getLogger(MemorySimulationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void memoryHardwareConfigDialog() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MemoryInitialization.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();

        /*MemoryInitializationController ctrl = fxmlLoader.getController();
        if (newProcess == true) {
            processtoAdd = new PCB(true);
        }
        ctrl.sceneInitialization(myController, processtoAdd, false);
         */
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Memory Hardware");
        stage.initStyle(StageStyle.UTILITY);
        stage.showAndWait();

        /*if (newProcess == true) {
        processTable.getItems().add(processtoAdd);
        insertMethodCall(processtoAdd);
        startOutputSimulation_btn.setDisable(false);
    }*/
    }

}
