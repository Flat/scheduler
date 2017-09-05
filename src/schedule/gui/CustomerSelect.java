package schedule.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import schedule.Customer;
import schedule.Database;

import java.io.IOException;

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
    private TableColumn<Customer, String> tcActive;

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
        tcActive.setCellValueFactory(new PropertyValueFactory<>("Active"));

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
        Button button = (Button) actionEvent.getSource();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomerManager.fxml"));
        Parent root;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Stage stage = new Stage();
        stage.setTitle("Customer Manager");
        stage.setScene(new Scene(root, 500, 400));
        CustomerManager customerManager = fxmlLoader.getController();
        customerManager.initFields(tableCustomers.getSelectionModel().getSelectedItem());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(button.getScene().getWindow());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.showAndWait();
        Customer cust = customerManager.getCustomer();
        if(cust != null){
            Database database = new Database();
            database.addCustomer(cust, username);
            updateTable();
        }
    }

    public void newCustomer(ActionEvent actionEvent){
        Button button = (Button) actionEvent.getSource();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomerManager.fxml"));
        Parent root;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Stage stage = new Stage();
        stage.setTitle("Customer Manager");
        stage.setScene(new Scene(root, 500, 400));
        CustomerManager customerManager = fxmlLoader.getController();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(button.getScene().getWindow());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.showAndWait();
        Customer cust = customerManager.getCustomer();
        if(cust != null){
            Database database = new Database();
            database.addCustomer(cust, username);
            updateTable();
        }
    }

    public void cancel() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
