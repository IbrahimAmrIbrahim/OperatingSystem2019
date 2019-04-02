package scheduler;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class SchedulerSimulationController implements Initializable {

    private SchedulerSimulationController controller;

    int currentTime = 0;
    int currentXPosition = 0;
    int currentYPosition = 20;
    boolean canvasIsEmpty = true;

    enum schedulerType {
        Null, FCFS
    };

    schedulerType currentScheduler;

    @FXML
    private ToggleGroup Simulation_Method;

    @FXML
    private Button startOutputSimulation;

    @FXML
    private void startOutputSimulationButton(MouseEvent event) {
        /*for (int i = 1; i <= 100; i++) {
            draw(i);
        }*/
    }

    @FXML
    private Canvas canvas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        schedulerSelectDialog();
    }

    /**
     * For Open Error Messages.
     *
     * @param msg Question string
     */
    public void errorDialog(String msg) {
        Alert alert = new Alert(AlertType.ERROR);
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
    public boolean yesNoDialog(String msg) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
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

    /**
     * For Open a scheduler picker.
     */
    public void schedulerSelectDialog() {
        List<String> choices = new ArrayList<>();
        choices.add("FCFS");
        choices.add("b");
        choices.add("c");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Choose a scheduler", choices);
        dialog.setTitle("Choice Dialog");
        dialog.setHeaderText(null);
        dialog.setContentText("Choose a scheduler:");

        Optional<String> result = dialog.showAndWait();

        /*
        *missing Return handeler
         */
        if (result.isPresent()) {
            System.out.println("Your choice: " + result.get());
        }
    }

    public void addProcessDialog() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddProcess.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.showAndWait();
    }

    public void draw(int duration) {
        final int unityTimeWidth = 40;
        GraphicsContext gc = canvas.getGraphicsContext2D();

        if (duration <= 0) {
            return;
        }
        //**************************
        currentScheduler = schedulerType.FCFS;
        //**************************

        if (canvasIsEmpty == true) {
            // Write scheduler method
            gc.setFill(Color.WHITE);
            gc.setTextAlign(TextAlignment.LEFT);
            if (currentScheduler == schedulerType.FCFS) {
                gc.fillText("FCFS Scheduler", 20, currentYPosition);
            }

            currentYPosition += 20;
            // draw begin time point
            gc.fillRect(40, currentYPosition, 1, 50);

            // write time
            gc.rotate(45);
            gc.fillText(Integer.toString(currentTime), rotatedX(40, (currentYPosition + 50)), rotatedY(40, (currentYPosition + 50)));
            gc.rotate(-45);
            currentXPosition = 41;

            canvasIsEmpty = false;
        }

        int nextPosition = (duration * unityTimeWidth) + currentXPosition;
        if (currentYPosition == 40 && nextPosition < 8000) {
            canvas.setWidth(nextPosition + 40);
        } else {
            canvas.setWidth(8000 + 40);
        }

        if (nextPosition > 8000) {
            int theRest = duration - ((8000 - currentXPosition) / unityTimeWidth);
            draw(((8000 - currentXPosition) / unityTimeWidth));
            currentXPosition = 40;
            currentYPosition += 100;
            canvas.setHeight(currentYPosition + 100);
            if (theRest > 0) {
                draw(theRest);

            }
            return;
        }

        currentTime += duration;

        //*********************************
        //color need to change 
        //*************************
        // draw process time
        gc.setFill(Color.hsb(25, 1, 1));
        //gc.fillRect(currentXPosition, (currentYPosition + 10), (duration * unityTimeWidth), 30);
        gc.fillRoundRect(currentXPosition, (currentYPosition + 10), (duration * unityTimeWidth), 30, 20, 20);

        // write process id
        gc.setFill(Color.WHITE);
        //*********************************
        //pid need to be addad 
        //*************************
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(Integer.toString(duration), (((duration * unityTimeWidth) / 2) + currentXPosition), (currentYPosition + 30));

        // draw end time point
        gc.fillRect(nextPosition, currentYPosition, 1, 50);

        // write time
        gc.rotate(45);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText(Integer.toString(currentTime), rotatedX(nextPosition, (currentYPosition + 50)), rotatedY(nextPosition, (currentYPosition + 50)));
        gc.rotate(-45);

        currentXPosition = nextPosition + 1;
    }

    private double rotatedX(double x, double y) {
        // to center the text
        x -= 5;
        y += 10;
        double r = Math.sqrt((x * x) + (y * y));
        double seta = Math.atan2(y, x);
        return (r * Math.cos(seta - (0.25 * Math.PI)));
    }

    private double rotatedY(double x, double y) {
        // to center the text
        x -= 5;
        y += 10;
        double r = Math.sqrt((x * x) + (y * y));
        double seta = Math.atan2(y, x);
        return (r * Math.sin(seta - (0.25 * Math.PI)));
    }

    /**
     * @return the controller
     */
    public SchedulerSimulationController getController() {
        return controller;
    }

    /**
     * @param controller the controller to set
     */
    public void setController(SchedulerSimulationController controller) {
        this.controller = controller;
    }

}
