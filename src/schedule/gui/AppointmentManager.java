package schedule.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class AppointmentManager {
    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    private TextField tbTitle;

    @FXML
    private TextField tbLocation;

    @FXML
    private TextField tbContact;

    @FXML
    private TextField tbUrl;

    @FXML
    private DatePicker dpStart;

    @FXML
    private DatePicker dpEnd;

    @FXML
    private ChoiceBox<String> cbType;

    @FXML
    private ChoiceBox<String> cbCustomer;

    @FXML
    private ChoiceBox<String> cbStartTime;

    @FXML
    private ChoiceBox<String> cbEndTime;

    @FXML
    void Cancel(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void Save(ActionEvent event) {
        if (tbContact.getText() != null && tbLocation.getText() != null && tbTitle != null && tbUrl.getText() != null && !cbStartTime.getSelectionModel().isEmpty() && !cbEndTime.getSelectionModel().isEmpty() && dpStart.getValue() != null && dpEnd.getValue() != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, DateTimeFormatter.ofPattern("MM/dd/yyy - HH:mm").format(parseStart()) + " " + DateTimeFormatter.ofPattern("MM/dd/yyy - HH:mm").format(parseEnd()));
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label) node).setMinHeight(Region.USE_PREF_SIZE));
            alert.showAndWait();
        }


    }

    @FXML
    public void initialize() {
        cbType.getItems().addAll("Initial", "Followup", "Closing");
        cbStartTime.getItems().addAll("12:00 AM", "12:15 AM", "12:30 AM", "12:45 AM", "01:00 AM", "01:15 AM", "01:30 AM", "01:45 AM", "02:00 AM", "02:15 AM", "02:30 AM", "02:45 AM", "03:00 AM", "03:15 AM", "03:30 AM", "03:45 AM", "04:00 AM", "04:15 AM", "04:30 AM", "04:45 AM", "05:00 AM", "05:15 AM", "05:30 AM", "05:45 AM", "06:00 AM", "06:15 AM", "06:30 AM", "06:45 AM", "07:00 AM", "07:15 AM", "07:30 AM", "07:45 AM", "08:00 AM", "08:15 AM", "08:30 AM", "08:45 AM", "09:00 AM", "09:15 AM", "09:30 AM", "09:45 AM", "10:00 AM", "10:15 AM", "10:30 AM", "10:45 AM", "11:00 AM", "11:15 AM", "11:30 AM", "11:45 AM", "12:00 PM", "12:15 PM", "12:30 PM", "12:45 PM", "01:00 PM", "01:15 PM", "01:30 PM", "01:45 PM", "02:00 PM", "02:15 PM", "02:30 PM", "02:45 PM", "03:00 PM", "03:15 PM", "03:30 PM", "03:45 PM", "04:00 PM", "04:15 PM", "04:30 PM", "04:45 PM", "05:00 PM", "05:15 PM", "05:30 PM", "05:45 PM", "06:00 PM", "06:15 PM", "06:30 PM", "06:45 PM", "07:00 PM", "07:15 PM", "07:30 PM", "07:45 PM", "08:00 PM", "08:15 PM", "08:30 PM", "08:45 PM", "09:00 PM", "09:15 PM", "09:30 PM", "09:45 PM", "10:00 PM", "10:15 PM", "10:30 PM", "10:45 PM", "11:00 PM", "11:15 PM", "11:30 PM", "11:45 PM");
        cbEndTime.getItems().addAll("12:00 AM", "12:15 AM", "12:30 AM", "12:45 AM", "01:00 AM", "01:15 AM", "01:30 AM", "01:45 AM", "02:00 AM", "02:15 AM", "02:30 AM", "02:45 AM", "03:00 AM", "03:15 AM", "03:30 AM", "03:45 AM", "04:00 AM", "04:15 AM", "04:30 AM", "04:45 AM", "05:00 AM", "05:15 AM", "05:30 AM", "05:45 AM", "06:00 AM", "06:15 AM", "06:30 AM", "06:45 AM", "07:00 AM", "07:15 AM", "07:30 AM", "07:45 AM", "08:00 AM", "08:15 AM", "08:30 AM", "08:45 AM", "09:00 AM", "09:15 AM", "09:30 AM", "09:45 AM", "10:00 AM", "10:15 AM", "10:30 AM", "10:45 AM", "11:00 AM", "11:15 AM", "11:30 AM", "11:45 AM", "12:00 PM", "12:15 PM", "12:30 PM", "12:45 PM", "01:00 PM", "01:15 PM", "01:30 PM", "01:45 PM", "02:00 PM", "02:15 PM", "02:30 PM", "02:45 PM", "03:00 PM", "03:15 PM", "03:30 PM", "03:45 PM", "04:00 PM", "04:15 PM", "04:30 PM", "04:45 PM", "05:00 PM", "05:15 PM", "05:30 PM", "05:45 PM", "06:00 PM", "06:15 PM", "06:30 PM", "06:45 PM", "07:00 PM", "07:15 PM", "07:30 PM", "07:45 PM", "08:00 PM", "08:15 PM", "08:30 PM", "08:45 PM", "09:00 PM", "09:15 PM", "09:30 PM", "09:45 PM", "10:00 PM", "10:15 PM", "10:30 PM", "10:45 PM", "11:00 PM", "11:15 PM", "11:30 PM", "11:45 PM");
        dpStart.setValue(LocalDate.now());
        dpEnd.setValue(LocalDate.now());
        cbCustomer.getItems().addAll();
    }

    private ZonedDateTime parseStart(){
        int hours = Integer.parseInt(cbStartTime.getSelectionModel().getSelectedItem().substring(0,2));
        if(cbStartTime.getSelectionModel().getSelectedItem().contains("PM")) {
            hours = hours + 12;
        }
        int minutes = Integer.parseInt(cbStartTime.getSelectionModel().getSelectedItem().substring(3,5));
        LocalDate localDate = dpStart.getValue();
        return localDate.atTime(hours, minutes).atZone(ZoneId.systemDefault());

    }

    private ZonedDateTime parseEnd(){
        int hours = Integer.parseInt(cbEndTime.getSelectionModel().getSelectedItem().substring(0,2));
        if(cbEndTime.getSelectionModel().getSelectedItem().contains("PM")) {
            hours = hours + 12;
        }
        int minutes = Integer.parseInt(cbEndTime.getSelectionModel().getSelectedItem().substring(3,5));
        LocalDate localDate = dpEnd.getValue();
        return localDate.atTime(hours, minutes).atZone(ZoneId.systemDefault());
    }

    private boolean validate(){
        boolean valid = true;
        if(tbTitle.getText() == null) {
            valid = false;
        }
        if(tbUrl.getText() == null){
            valid = false;
        }
        if(tbLocation.getText() == null) {
            valid = false;
        }
        if(tbContact.getText() == null) {
            valid = false;
        }
        if(cbType.getSelectionModel().isEmpty()) {
            valid = false;
        }
        if(cbCustomer.getSelectionModel().isEmpty()){
            valid = false;
        }
        if(!cbStartTime.getSelectionModel().isEmpty() && !cbEndTime.getSelectionModel().isEmpty()){
            if(parseStart().compareTo(parseEnd()) < 0) {
                valid = false;
            }
        } else {
            valid = false;
        }
        return valid;
    }

}
