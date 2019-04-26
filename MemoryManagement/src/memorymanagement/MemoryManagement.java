package memorymanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MemoryManagement extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MemorySimulation.fxml"));
        Parent root = loader.load();

        MemorySimulationController myController = loader.getController();
        myController.setMyController(myController);

        Scene scene = new Scene(root);
        stage.setTitle("Memory Management");
        stage.setScene(scene);
        stage.show();

        myController.sceneInitialize();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
