package schedule.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import schedule.Appointment;
import schedule.Database;
import schedule.I18n;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class ScheduleController {

    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    @FXML
    private Button btnPrevMonth;

    @FXML
    private Label labelMonth;

    @FXML
    private Button btnNextMonth;

    @FXML
    private CheckBox cbShowByWeek;

    @FXML
    private Button btnAddAppt;

    @FXML
    private Button btnAddCustomer;

    @FXML
    private Button btnRmAppt;

    @FXML
    private TableView<Appointment> appointmentTable;

    @FXML
    private TableColumn<Appointment, String> tcTitle;

    @FXML
    private TableColumn<Appointment, String> tcType;

    @FXML
    private TableColumn<Appointment, String> tcLocation;

    @FXML
    private TableColumn<Appointment, String> tcUrl;

    @FXML
    private TableColumn<Appointment, String> tcStart;

    @FXML
    private TableColumn<Appointment, String> tcEnd;

    @FXML
    private TableColumn<Appointment, String> tcCustomer;

    @FXML
    private TableColumn<Appointment, String> tcAddress;

    @FXML
    private TableColumn<Appointment, String> tcAddress2;

    @FXML
    private TableColumn<Appointment, String> tcCity;

    @FXML
    private TableColumn<Appointment, String> tcZipCode;

    @FXML
    private TableColumn<Appointment, String> tcPhone;

    @FXML
    private TableColumn<Appointment, String> tcCountry;


    private String username;

    private ZonedDateTime zonedDateTime;

    public void setUsername(String name) {
        username = name;
    }

    public void initTableData() {
        updateTable();
    }

    @FXML
    public void initialize() {
        zonedDateTime = ZonedDateTime.now();
        labelMonth.setText(zonedDateTime.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + zonedDateTime.getYear());
        appointmentTable.setItems(appointments);
        tcTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        tcType.setCellValueFactory(new PropertyValueFactory<>("Description"));
        tcLocation.setCellValueFactory(new PropertyValueFactory<>("Location"));
        tcUrl.setCellValueFactory(new PropertyValueFactory<>("Url"));
        tcStart.setCellValueFactory(appointment -> {
            SimpleStringProperty simpleStringProperty = new SimpleStringProperty();
            simpleStringProperty.setValue(DateTimeFormatter.ofPattern("MM/dd/yyy - HH:mm").format(appointment.getValue().getStart()));
            return simpleStringProperty;
        });
        tcEnd.setCellValueFactory(appointment -> {
            SimpleStringProperty simpleStringProperty = new SimpleStringProperty();
            simpleStringProperty.setValue(DateTimeFormatter.ofPattern("MM/dd/yyy - HH:mm").format(appointment.getValue().getEnd()));
            return simpleStringProperty;
        });
        tcCustomer.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
        tcAddress.setCellValueFactory(new PropertyValueFactory<>("CustomerAddress"));
        tcAddress2.setCellValueFactory(new PropertyValueFactory<>("CustomerAddress2"));
        tcCity.setCellValueFactory(new PropertyValueFactory<>("City"));
        tcZipCode.setCellValueFactory(new PropertyValueFactory<>("Zip"));
        tcPhone.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        tcCountry.setCellValueFactory(new PropertyValueFactory<>("Country"));
    }

    public void cbChecked(ActionEvent actionEvent) {
        updateTable();
    }

    public void removeAppt(ActionEvent actionEvent) {
        Database database = new Database();
        database.deleteAppointment(appointmentTable.getSelectionModel().getSelectedItem());
        updateTable();
    }

    public void newCustomer(ActionEvent actionEvent) {

    }

    public void newAppt(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppointmentManager.fxml"));
        Parent root;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Stage stage = new Stage();
        stage.setTitle("NewAppointment");
        stage.setScene(new Scene(root, 350, 400));
        AppointmentManager appointmentManager = fxmlLoader.getController();
        appointmentManager.init(username, appointments);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(button.getScene().getWindow());
        stage.showAndWait();
        updateTable();
    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void nextWeekorMonth(ActionEvent actionEvent) {
        if (cbShowByWeek.isSelected()) {
            zonedDateTime = zonedDateTime.plusWeeks(1);
            updateTable();
        } else {
            zonedDateTime = zonedDateTime.plusMonths(1);
            updateTable();
        }
    }

    public void prevWeekorMonth(ActionEvent actionEvent) {
        if (cbShowByWeek.isSelected()) {
            zonedDateTime = zonedDateTime.minusWeeks(1);
            updateTable();
        } else {
            zonedDateTime = zonedDateTime.minusMonths(1);
            updateTable();
        }
    }

    private void updateTable() {
        Database database = new Database();
        appointments = database.getAppointments(username, zonedDateTime, cbShowByWeek.isSelected());
        appointmentTable.setItems(appointments);
        updateText();
    }

    private void updateText() {
        if (cbShowByWeek.isSelected()) {
            labelMonth.setText("Week " + Integer.toString(I18n.ToWeekOfYear(zonedDateTime.toLocalDateTime())) + " of " + zonedDateTime.getYear());
        } else {
            labelMonth.setText(zonedDateTime.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + zonedDateTime.getYear());
        }
    }
}
