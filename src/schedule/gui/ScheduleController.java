package schedule.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ScheduleController {


    @FXML
    private Button btnPrevMonth;

    @FXML
    private Label labelMonth;

    @FXML
    private Button btnNextMonth;

    private String username;

    public void setUsername(String name){
        username = name;
    }

    public void newCustomer(ActionEvent actionEvent) {

    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }
}
