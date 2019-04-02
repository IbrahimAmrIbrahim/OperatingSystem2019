package scheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Scheduler extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SchedulerSimulation.fxml"));
        Parent root = loader.load();
        
        SchedulerSimulationController myController = loader.getController();
        myController.setController(myController);
        
        Scene scene = new Scene(root);
        stage.setTitle("CPU Scheduler");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
