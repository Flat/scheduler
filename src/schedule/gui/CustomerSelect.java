package schedule.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import schedule.Customer;

public class CustomerSelect {
    @FXML
    private Button btnCancel;

    @FXML
    private Button btnNew;

    @FXML
    private Button btnEdit;

    @FXML
    private TableView<Customer> tableCustomers;

    @FXML
    private TableColumn<Customer, String> tcId;

    @FXML
    private TableColumn<Customer, String> tcName;

    @FXML
    private TableColumn<Customer, String> tcAddress1;

    @FXML
    private TableColumn<Customer, String> tcAddress2;

    @FXML
    private TableColumn<Customer, String> tcCity;

    @FXML
    private TableColumn<Customer, String> tcZip;

    @FXML
    private TableColumn<Customer, String> tcPhone;

    @FXML
    private TableColumn<Customer, String> tcCountry;
}
