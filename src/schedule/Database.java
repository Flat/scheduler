package schedule;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;
import java.time.ZonedDateTime;
import java.util.Calendar;

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

    public ObservableList<Appointment> getAppointments(String username, ZonedDateTime zonedDateTime, Boolean byWeek) {
        Timestamp current = I18n.toUTC(zonedDateTime);
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        if(byWeek){
            int weekofyear = I18n.timestampToWeekOfYear(I18n.toUTC(zonedDateTime));
            Date weekStart = I18n.startOfWeek(weekofyear);
            Date weekEnd = I18n.endOfWeek(weekofyear);
            try (Connection connection = getConnection()) {
                log.console("Getting appointments");
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT U03oxz.appointment.appointmentId,\n" +
                        "         U03oxz.customer.customerId,\n" +
                        "         U03oxz.appointment.title AS AppointmentTitle,\n" +
                        "         U03oxz.appointment.description AS AppointmentDescription,\n" +
                        "         U03oxz.appointment.location AS AppointmentLocation,\n" +
                        "         U03oxz.appointment.contact AS AppointmentContact,\n" +
                        "         U03oxz.appointment.url AS AppointmentUrl,\n" +
                        "         U03oxz.appointment.start AS AppointmentStart,\n" +
                        "         U03oxz.appointment.end AS AppointmentEnd,\n" +
                        "         U03oxz.appointment.createDate AS AppointmentCreateDate,\n" +
                        "         U03oxz.appointment.createdBy AS AppointmentCreatedBy,\n" +
                        "         U03oxz.appointment.lastUPDATE AS AppointmentLastUpdate,\n" +
                        "         U03oxz.appointment.lastUpdateBy AS AppointmentLastUpdateBy,\n" +
                        "         U03oxz.customer.customerName AS CustomerName,\n" +
                        "         U03oxz.customer.addressId as AddressId,\n" +
                        "         U03oxz.customer.active as CustomerActive,\n" +
                        "         U03oxz.customer.createDate AS CustomerCreateDate,\n" +
                        "         U03oxz.customer.CreatedBy AS CustomerCreatedBy,\n" +
                        "         U03oxz.customer.lastUpdate AS CustomerLastUpdate,\n" +
                        "         U03oxz.customer.lastUpdateBy AS CustomerLastUpdateBy,\n" +
                        "         U03oxz.address.address AS Address1,\n" +
                        "         U03oxz.address.address2 AS Address2,\n" +
                        "         U03oxz.address.cityId AS CityId,\n" +
                        "         U03oxz.address.postalCode AS ZipCode,\n" +
                        "         U03oxz.address.phone AS AddressPhone,\n" +
                        "         U03oxz.address.createDate AS AddressCreateDate,\n" +
                        "         U03oxz.address.createdBy AS AddressCreatedBy,\n" +
                        "         U03oxz.address.lastUpdate AS AddressLastUpdate,\n" +
                        "         U03oxz.address.lastUpdateBy AS AddressLastUpdateBy,\n" +
                        "         U03oxz.city.city as City,\n" +
                        "         U03oxz.city.countryId as CountryId,\n" +
                        "         U03oxz.city.createDate as CityCreateDate,\n" +
                        "         U03oxz.city.createdBy as CityCreatedBy,\n" +
                        "         U03oxz.city.lastUpdate as CityLastUpdate,\n" +
                        "         U03oxz.city.lastUpdateBy AS CityLastUpdateBy,\n" +
                        "         U03oxz.country.country AS Country,\n" +
                        "         U03oxz.country.createDate AS CountryCreateDate,\n" +
                        "         U03oxz.country.createdBy AS CountryCreatedBy,\n" +
                        "         U03oxz.country.lastUpdate AS CountryLastUpdate,\n" +
                        "         U03oxz.country.lastUpdateBy AS CountryLastUpdatedBy\n" +
                        "FROM U03oxz.appointment\n" +
                        "INNER JOIN U03oxz.customer\n" +
                        "    ON U03oxz.appointment.customerId = U03oxz.customer.customerId\n" +
                        "INNER JOIN U03oxz.address\n" +
                        "    ON U03oxz.customer.addressId = U03oxz.address.addressId\n" +
                        "INNER JOIN U03oxz.city\n" +
                        "    ON U03oxz.address.cityId = U03oxz.city.cityId\n" +
                        "INNER JOIN U03oxz.country\n" +
                        "    ON U03oxz.city.countryId = U03oxz.country.countryId\n" +
                        "WHERE U03oxz.appointment.createdBy = \"?\"AND U03oxz.appointment.start BETWEEN ?\n" +
                        "        AND ?;");
                preparedStatement.setString(1, username);
                preparedStatement.setDate(2, weekStart);
                preparedStatement.setDate(3, weekEnd);
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

        } else {
            try (Connection connection = getConnection()) {
                log.console("Getting appointments");
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT U03oxz.appointment.appointmentId,\n" +
                        "         U03oxz.customer.customerId,\n" +
                        "         U03oxz.appointment.title AS AppointmentTitle,\n" +
                        "         U03oxz.appointment.description AS AppointmentDescription,\n" +
                        "         U03oxz.appointment.location AS AppointmentLocation,\n" +
                        "         U03oxz.appointment.contact AS AppointmentContact,\n" +
                        "         U03oxz.appointment.url AS AppointmentUrl,\n" +
                        "         U03oxz.appointment.start AS AppointmentStart,\n" +
                        "         U03oxz.appointment.end AS AppointmentEnd,\n" +
                        "         U03oxz.appointment.createDate AS AppointmentCreateDate,\n" +
                        "         U03oxz.appointment.createdBy AS AppointmentCreatedBy,\n" +
                        "         U03oxz.appointment.lastUPDATE AS AppointmentLastUpdate,\n" +
                        "         U03oxz.appointment.lastUpdateBy AS AppointmentLastUpdateBy,\n" +
                        "         U03oxz.customer.customerName AS CustomerName,\n" +
                        "         U03oxz.customer.addressId as AddressId,\n" +
                        "         U03oxz.customer.active as CustomerActive,\n" +
                        "         U03oxz.customer.createDate AS CustomerCreateDate,\n" +
                        "         U03oxz.customer.CreatedBy AS CustomerCreatedBy,\n" +
                        "         U03oxz.customer.lastUpdate AS CustomerLastUpdate,\n" +
                        "         U03oxz.customer.lastUpdateBy AS CustomerLastUpdateBy,\n" +
                        "         U03oxz.address.address AS Address1,\n" +
                        "         U03oxz.address.address2 AS Address2,\n" +
                        "         U03oxz.address.cityId AS CityId,\n" +
                        "         U03oxz.address.postalCode AS ZipCode,\n" +
                        "         U03oxz.address.phone AS AddressPhone,\n" +
                        "         U03oxz.address.createDate AS AddressCreateDate,\n" +
                        "         U03oxz.address.createdBy AS AddressCreatedBy,\n" +
                        "         U03oxz.address.lastUpdate AS AddressLastUpdate,\n" +
                        "         U03oxz.address.lastUpdateBy AS AddressLastUpdateBy,\n" +
                        "         U03oxz.city.city as City,\n" +
                        "         U03oxz.city.countryId as CountryId,\n" +
                        "         U03oxz.city.createDate as CityCreateDate,\n" +
                        "         U03oxz.city.createdBy as CityCreatedBy,\n" +
                        "         U03oxz.city.lastUpdate as CityLastUpdate,\n" +
                        "         U03oxz.city.lastUpdateBy AS CityLastUpdateBy,\n" +
                        "         U03oxz.country.country AS Country,\n" +
                        "         U03oxz.country.createDate AS CountryCreateDate,\n" +
                        "         U03oxz.country.createdBy AS CountryCreatedBy,\n" +
                        "         U03oxz.country.lastUpdate AS CountryLastUpdate,\n" +
                        "         U03oxz.country.lastUpdateBy AS CountryLastUpdatedBy\n" +
                        "FROM U03oxz.appointment\n" +
                        "INNER JOIN U03oxz.customer\n" +
                        "    ON U03oxz.appointment.customerId = U03oxz.customer.customerId\n" +
                        "INNER JOIN U03oxz.address\n" +
                        "    ON U03oxz.customer.addressId = U03oxz.address.addressId\n" +
                        "INNER JOIN U03oxz.city\n" +
                        "    ON U03oxz.address.cityId = U03oxz.city.cityId\n" +
                        "INNER JOIN U03oxz.country\n" +
                        "    ON U03oxz.city.countryId = U03oxz.country.countryId\n" +
                        "WHERE U03oxz.appointment.createdBy = \"?\"AND DATE_FORMAT(U03oxz.appointment.start, \"%m\") = ?\n" +
                        "        AND DATE_FORMAT(U03oxz.appointment.start, \"%Y\") = ?;");
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, I18n.timestampToMonth(current));
                preparedStatement.setInt(3, I18n.timestampToYear(current));
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

}
