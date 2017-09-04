package schedule.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import schedule.Customer;
import schedule.Database;

import java.awt.event.ActionEvent;

public class CustomerSelect {

    private ObservableList<Customer> customers = FXCollections.observableArrayList();
    private String username;

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

    @FXML
    public void initialize(){
        tableCustomers.setItems(customers);
        tcId.setCellValueFactory(new PropertyValueFactory<>("CustomerId"));
        tcName.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
        tcAddress1.setCellValueFactory(new PropertyValueFactory<>("Address1"));
        tcAddress2.setCellValueFactory(new PropertyValueFactory<>("Address2"));
        tcCity.setCellValueFactory(new PropertyValueFactory<>("City"));
        tcZip.setCellValueFactory(new PropertyValueFactory<>("Zip"));
        tcPhone.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        tcCountry.setCellValueFactory(new PropertyValueFactory<>("Country"));
    }

    public void initTable() {
        updateTable();
    }

    public void setUsername(String user){
        username = user;
    }

    private void updateTable(){
        Database database = new Database();
        customers = database.getCustomers(username);
        tableCustomers.setItems(customers);
    }

    public void editCustomer(ActionEvent actionEvent){
        //Launch editor
        tableCustomers.getSelectionModel().getSelectedItem();
    }

    public void newCustomer(ActionEvent actionEvent){
        //Launch editor
    }
}
