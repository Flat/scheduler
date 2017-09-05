package schedule.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import schedule.Address;
import schedule.City;
import schedule.Country;
import schedule.Customer;

import java.time.Instant;

public class CustomerManager {

    private Customer customer;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    private TextField tfId;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfAddress1;

    @FXML
    private TextField tfAddress2;

    @FXML
    private TextField tfPhone;

    @FXML
    private TextField tfZipCode;

    @FXML
    private TextField tfCity;

    @FXML
    private TextField tfCountry;

    @FXML
    private CheckBox cbActive;

    public void initFields(Customer cust) {
        customer = cust;
        tfId.setText(Integer.toString(customer.getCustomerId()));
        tfName.setText(customer.getCustomerName());
        tfAddress1.setText(customer.getAddress1());
        tfAddress2.setText(customer.getAddress2());
        tfPhone.setText(customer.getPhone());
        tfZipCode.setText(customer.getZip());
        tfCountry.setText(customer.getCountry());
        tfCity.setText(customer.getCity());
        if(customer.getActive() == 1){
            cbActive.setSelected(true);
        } else {
            cbActive.setSelected(false);
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public void save(){
        if(validate()){
            if (tfId.getText().isEmpty() && customer == null){
                Country country = new Country(0, tfCountry.getText(), Instant.now(), "", Instant.now(), "");
                City city = new City(0, tfCity.getText(), country, Instant.now(), "", Instant.now(), "");
                Address address = new Address(0, tfAddress1.getText(), tfAddress2.getText(), city, tfZipCode.getText(), tfPhone.getText(), Instant.now(), "", Instant.now(), "");
                customer = new Customer(0, tfName.getText(), address, cbActive.isSelected() ? 1 : 0, Instant.now(), "", Instant.now(), "");
            } else if(customer != null) {
                Address address = customer.getAddress();
                City city = address.getCity();
                Country country = city.getCountry();
                country.setCountry(tfCountry.getText());
                city.setCity(tfCity.getText());
                address.setCity(city);
                address.setAddress(tfAddress1.getText());
                address.setAddress2(tfAddress2.getText());
                address.setPhone(tfPhone.getText());
                address.setPostalCode(tfZipCode.getText());
                customer.setAddress(address);
                customer.setCustomerName(tfName.getText());
                customer.setActive(cbActive.isSelected() ? 1 : 0);
            }
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.close();
        }
    }

    public void cancel() {
        customer = null;
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private boolean validate(){
        if(tfName.getText().isEmpty() || tfAddress1.getText().isEmpty() || tfAddress2.getText().isEmpty() || tfCity.getText().isEmpty() || tfPhone.getText().isEmpty() || tfZipCode.getText().isEmpty() || tfCountry.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "All fields are required!");
            alert.show();
            return false;
        } else {
            return true;
        }
    }
}
