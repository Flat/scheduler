package schedule;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;

public class Database {

    public Database() {

    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://52.206.157.109/U03oxz", "U03oxz", "53688041085");
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, I18n.getLocalizedString("databaseConnectionError"));
            alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> System.exit(1));
            return null;
        }
    }

    public boolean loginUser(String username, String password) {
        try (Connection connection = getConnection()) {
            log.console("Generating statement");
            PreparedStatement preparedStatement = connection.prepareStatement("select * from U03oxz.user where userName = ? and password = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            log.console("Executing login query");
            boolean haveResult = preparedStatement.execute();
            if (haveResult) {
                log.console("Checking if returned account is active");
                ResultSet resultSet = preparedStatement.getResultSet();
                if(resultSet.next()){
                    int active = resultSet.getInt("active");
                    if (active == 1){
                        log.console("Login successful!");

                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }

            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ObservableList<Appointment> getAppointments(String username) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try (Connection connection = getConnection()) {
            log.console("Getting appointments");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM U03oxz.appointment INNER JOIN U03oxz.customer ON U03oxz.appointment.customerId = U03oxz.customer.customerId INNER JOIN U03oxz.address ON U03oxz.customer.addressId = U03oxz.address.addressId INNER JOIN U03oxz.city ON U03oxz.address.cityId = U03oxz.city.cityId INNER JOIN U03oxz.country ON U03oxz.city.countryId = U03oxz.country.countryId WHERE U03oxz.appointment.createdBy = ?;");
            preparedStatement.setString(1, username);
            log.console("Executing sql statement");
            boolean haveResult = preparedStatement.execute();
            if (haveResult) {
                ResultSet resultSet = preparedStatement.getResultSet();
                if(resultSet.next()){
                    Country country = new Country(resultSet.getInt("U030oxz.country.countryId"), resultSet.getString("U030oxz.country.country"), resultSet.getTimestamp("U030oxz.country.createDate"), resultSet.getString("U030oxz.country.createdBy"), resultSet.getTimestamp("U030oxz.country.lastUpdate"), resultSet.getString("U030oxz.country.lastUpdateBy"));
                    City city = new City(resultSet.getInt("U030oxz.city.cityId"), resultSet.getString("U030oxz.city.city"), country, resultSet.getTimestamp("U030oxz.city.createDate"), resultSet.getString("U030oxz.city.createdBy"), resultSet.getTimestamp("U030oxz.city.lastUpdate"), resultSet.getString("U030oxz.city.lastUpdateBy"));
                    Address address = new Address(resultSet.getInt("U030oxz.address.addressId"), resultSet.getString("U030oxz.address.address"), resultSet.getString("U030oxz.address.address2"), city, resultSet.getString("U030oxz.address.postalCode"), resultSet.getString("U030oxz.address.phone"), resultSet.getTimestamp("U030oxz.address.createDate"), resultSet.getString("U030oxz.address.createdBy"), resultSet.getTimestamp("U030oxz.address.lastUpdate"), resultSet.getString("U030oxz.address.lastUpdateBy"));
                    Customer customer = new Customer(resultSet.getInt("U030oxz.customer.customerId"), resultSet.getString("U030oxz.customer.customerName"), address, resultSet.getInt("active"), resultSet.getTimestamp("U030oxz.customer.createDate"), resultSet.getString("U030oxz.customer.createdBy"), resultSet.getTimestamp("U030oxz.customer.lastUpdate"), resultSet.getString("U030oxz.customer.lastUpdateBy"));
                    appointments.add(new Appointment(resultSet.getInt("appointmentId"), customer, resultSet.getString("title"), resultSet.getString("description"), resultSet.getString("location"), resultSet.getString("contact"), resultSet.getString("url"), resultSet.getTimestamp("start"), resultSet.getTimestamp("end"), resultSet.getTimestamp("createDate"), resultSet.getString("createdBy"), resultSet.getTimestamp("lastUpdate"), resultSet.getString("lastUpdateBy")));
                }
            }
            return appointments;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
