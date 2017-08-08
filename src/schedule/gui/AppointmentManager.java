package schedule.gui;

import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Callback;
import schedule.Appointment;
import schedule.Customer;
import schedule.Database;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class AppointmentManager {

    private String username;
    
    private ObservableList<Appointment> appointments;

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
    private ComboBox<Customer> cbCustomer;

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
        if(validate()){
            Appointment appointment = new Appointment(0, cbCustomer.getSelectionModel().getSelectedItem(), tbTitle.getText(), cbType.getSelectionModel().getSelectedItem(), tbLocation.getText(), tbContact.getText(), tbUrl.getText(), parseStart().toInstant(), parseEnd().toInstant(), Instant.now(), username, Instant.now(), username);
            //db code
        }

    }

    @FXML
    public void initialize() {
        cbType.getItems().addAll("Initial", "Followup", "Closing");
        cbStartTime.getItems().addAll("12:00 AM", "12:15 AM", "12:30 AM", "12:45 AM", "01:00 AM", "01:15 AM", "01:30 AM", "01:45 AM", "02:00 AM", "02:15 AM", "02:30 AM", "02:45 AM", "03:00 AM", "03:15 AM", "03:30 AM", "03:45 AM", "04:00 AM", "04:15 AM", "04:30 AM", "04:45 AM", "05:00 AM", "05:15 AM", "05:30 AM", "05:45 AM", "06:00 AM", "06:15 AM", "06:30 AM", "06:45 AM", "07:00 AM", "07:15 AM", "07:30 AM", "07:45 AM", "08:00 AM", "08:15 AM", "08:30 AM", "08:45 AM", "09:00 AM", "09:15 AM", "09:30 AM", "09:45 AM", "10:00 AM", "10:15 AM", "10:30 AM", "10:45 AM", "11:00 AM", "11:15 AM", "11:30 AM", "11:45 AM", "12:00 PM", "12:15 PM", "12:30 PM", "12:45 PM", "01:00 PM", "01:15 PM", "01:30 PM", "01:45 PM", "02:00 PM", "02:15 PM", "02:30 PM", "02:45 PM", "03:00 PM", "03:15 PM", "03:30 PM", "03:45 PM", "04:00 PM", "04:15 PM", "04:30 PM", "04:45 PM", "05:00 PM", "05:15 PM", "05:30 PM", "05:45 PM", "06:00 PM", "06:15 PM", "06:30 PM", "06:45 PM", "07:00 PM", "07:15 PM", "07:30 PM", "07:45 PM", "08:00 PM", "08:15 PM", "08:30 PM", "08:45 PM", "09:00 PM", "09:15 PM", "09:30 PM", "09:45 PM", "10:00 PM", "10:15 PM", "10:30 PM", "10:45 PM", "11:00 PM", "11:15 PM", "11:30 PM", "11:45 PM");
        cbEndTime.getItems().addAll("12:00 AM", "12:15 AM", "12:30 AM", "12:45 AM", "01:00 AM", "01:15 AM", "01:30 AM", "01:45 AM", "02:00 AM", "02:15 AM", "02:30 AM", "02:45 AM", "03:00 AM", "03:15 AM", "03:30 AM", "03:45 AM", "04:00 AM", "04:15 AM", "04:30 AM", "04:45 AM", "05:00 AM", "05:15 AM", "05:30 AM", "05:45 AM", "06:00 AM", "06:15 AM", "06:30 AM", "06:45 AM", "07:00 AM", "07:15 AM", "07:30 AM", "07:45 AM", "08:00 AM", "08:15 AM", "08:30 AM", "08:45 AM", "09:00 AM", "09:15 AM", "09:30 AM", "09:45 AM", "10:00 AM", "10:15 AM", "10:30 AM", "10:45 AM", "11:00 AM", "11:15 AM", "11:30 AM", "11:45 AM", "12:00 PM", "12:15 PM", "12:30 PM", "12:45 PM", "01:00 PM", "01:15 PM", "01:30 PM", "01:45 PM", "02:00 PM", "02:15 PM", "02:30 PM", "02:45 PM", "03:00 PM", "03:15 PM", "03:30 PM", "03:45 PM", "04:00 PM", "04:15 PM", "04:30 PM", "04:45 PM", "05:00 PM", "05:15 PM", "05:30 PM", "05:45 PM", "06:00 PM", "06:15 PM", "06:30 PM", "06:45 PM", "07:00 PM", "07:15 PM", "07:30 PM", "07:45 PM", "08:00 PM", "08:15 PM", "08:30 PM", "08:45 PM", "09:00 PM", "09:15 PM", "09:30 PM", "09:45 PM", "10:00 PM", "10:15 PM", "10:30 PM", "10:45 PM", "11:00 PM", "11:15 PM", "11:30 PM", "11:45 PM");
        dpStart.setValue(LocalDate.now());
        dpEnd.setValue(LocalDate.now());
        Callback<ListView<Customer>, ListCell<Customer>> callback = new Callback<ListView<Customer>, ListCell<Customer>>() {
            @Override
            public ListCell<Customer> call(ListView<Customer> x) {
                return new ListCell<Customer>() {
                    @Override
                    protected void updateItem(Customer customer, boolean isEmpty) {
                        super.updateItem(customer, isEmpty);
                        if (customer == null || isEmpty) {
                            setGraphic(null);
                        } else {
                            setText(customer.getCustomerId() + ": " + customer.getCustomerName());
                        }
                    }
                };
            }
        };
        cbCustomer.setButtonCell(callback.call(null));
        cbCustomer.setCellFactory(callback);
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
        PseudoClass invalid = PseudoClass.getPseudoClass("invalid");
        if(tbTitle.getText().isEmpty()) {
            tbTitle.pseudoClassStateChanged(invalid, true);
            valid = false;
        } else {
            tbTitle.pseudoClassStateChanged(invalid, false);
        }
        if(tbUrl.getText().isEmpty()){
            tbUrl.pseudoClassStateChanged(invalid, true);
            valid = false;
        } else {
            tbUrl.pseudoClassStateChanged(invalid, false);
        }
        if(tbLocation.getText().isEmpty()) {
            tbLocation.pseudoClassStateChanged(invalid, true);
            valid = false;
        } else {
            tbLocation.pseudoClassStateChanged(invalid, false);
        }
        if(tbContact.getText().isEmpty()) {
            tbContact.pseudoClassStateChanged(invalid,true);
            valid = false;
        } else {
            tbContact.pseudoClassStateChanged(invalid, false);
        }
        if(cbType.getSelectionModel().isEmpty()) {
            cbType.pseudoClassStateChanged(invalid, true);
            valid = false;
        }
        if(cbCustomer.getSelectionModel().isEmpty()){
            cbCustomer.pseudoClassStateChanged(invalid, true);
            valid = false;
        } else {
            cbCustomer.pseudoClassStateChanged(invalid, true);
        }
        if(!cbStartTime.getSelectionModel().isEmpty() && !cbEndTime.getSelectionModel().isEmpty()){
            if(parseStart().compareTo(parseEnd()) > 0) {
                cbStartTime.pseudoClassStateChanged(invalid, true);
                cbEndTime.pseudoClassStateChanged(invalid, true);
                valid = false;
            } else {
                cbStartTime.pseudoClassStateChanged(invalid,false);
                cbEndTime.pseudoClassStateChanged(invalid, false);
            }
            if(dpStart.getValue() == null || dpEnd.getValue() == null){
                dpStart.pseudoClassStateChanged(invalid, true);
                dpEnd.pseudoClassStateChanged(invalid, true);
                valid = false;
            } else {
                dpEnd.pseudoClassStateChanged(invalid, false);
                dpStart.pseudoClassStateChanged(invalid, false);
                for (Appointment appointment: appointments) {
                    ZonedDateTime start = parseStart();
                    ZonedDateTime end = parseEnd();
                    if((start.isAfter(appointment.getStart()) && start.isBefore(appointment.getEnd()) || (end.isAfter(appointment.getStart()) && end.isBefore(appointment.getEnd())))) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, String.format("This appointment would overlap with your appointment with %s at %tc", appointment.getCustomer().getCustomerName(), appointment.getStart()), ButtonType.OK);
                        alert.showAndWait();
                        valid = false;
                    }
                }
            }
        } else {
            cbStartTime.pseudoClassStateChanged(invalid, true);
            cbEndTime.pseudoClassStateChanged(invalid, true);
            valid = false;
        }
        return valid;
    }
    

    public void init(String username1, ObservableList<Appointment> appointments1){
        username = username1;
        Database database = new Database();
        cbCustomer.setItems(database.getCustomers(username));
        appointments = appointments1;
    }

}
