package schedule.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import schedule.Customer;

public class CustomerManager {

    private Customer customer;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    private TextField tfAddress1;

    @FXML
    private TextField tfAddress2;

    @FXML
    private TextField cbPhone;

    @FXML
    private TextField tfZipCode;

    @FXML
    private ComboBox<String> cbCity;

    @FXML
    private ComboBox<String> cbCountry;

    @FXML
    private CheckBox cbActive;

    public void setCustomer(Customer cust) {
        customer = cust;
    }
}
