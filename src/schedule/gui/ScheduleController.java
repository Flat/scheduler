package schedule.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class ScheduleController {

    @FXML
    private GridPane gridCalendar;

    public void newCustomer(ActionEvent actionEvent) {

    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }
}
