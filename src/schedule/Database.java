package schedule;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

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
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        if(byWeek){
            int weekofyear = I18n.ToWeekOfYear(zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
            LocalDate weekStart = I18n.startOfWeek(weekofyear);
            LocalDate weekEnd = weekStart.plusDays(6);
            try (Connection connection = getConnection()) {
                log.console("Getting appointments");
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT U03oxz.appointment.appointmentId," +
                        "         U03oxz.customer.customerId," +
                        "         U03oxz.appointment.title AS AppointmentTitle," +
                        "         U03oxz.appointment.description AS AppointmentDescription," +
                        "         U03oxz.appointment.location AS AppointmentLocation," +
                        "         U03oxz.appointment.contact AS AppointmentContact," +
                        "         U03oxz.appointment.url AS AppointmentUrl," +
                        "         U03oxz.appointment.start AS AppointmentStart," +
                        "         U03oxz.appointment.end AS AppointmentEnd," +
                        "         U03oxz.appointment.createDate AS AppointmentCreateDate," +
                        "         U03oxz.appointment.createdBy AS AppointmentCreatedBy," +
                        "         U03oxz.appointment.lastUPDATE AS AppointmentLastUpdate," +
                        "         U03oxz.appointment.lastUpdateBy AS AppointmentLastUpdateBy," +
                        "         U03oxz.customer.customerName AS CustomerName," +
                        "         U03oxz.customer.addressId as AddressId," +
                        "         U03oxz.customer.active as CustomerActive," +
                        "         U03oxz.customer.createDate AS CustomerCreateDate," +
                        "         U03oxz.customer.CreatedBy AS CustomerCreatedBy," +
                        "         U03oxz.customer.lastUpdate AS CustomerLastUpdate," +
                        "         U03oxz.customer.lastUpdateBy AS CustomerLastUpdateBy," +
                        "         U03oxz.address.address AS Address1," +
                        "         U03oxz.address.address2 AS Address2," +
                        "         U03oxz.address.cityId AS CityId," +
                        "         U03oxz.address.postalCode AS ZipCode," +
                        "         U03oxz.address.phone AS AddressPhone," +
                        "         U03oxz.address.createDate AS AddressCreateDate," +
                        "         U03oxz.address.createdBy AS AddressCreatedBy," +
                        "         U03oxz.address.lastUpdate AS AddressLastUpdate," +
                        "         U03oxz.address.lastUpdateBy AS AddressLastUpdateBy," +
                        "         U03oxz.city.city as City," +
                        "         U03oxz.city.countryId as CountryId," +
                        "         U03oxz.city.createDate as CityCreateDate," +
                        "         U03oxz.city.createdBy as CityCreatedBy," +
                        "         U03oxz.city.lastUpdate as CityLastUpdate," +
                        "         U03oxz.city.lastUpdateBy AS CityLastUpdateBy," +
                        "         U03oxz.country.country AS Country," +
                        "         U03oxz.country.createDate AS CountryCreateDate," +
                        "         U03oxz.country.createdBy AS CountryCreatedBy," +
                        "         U03oxz.country.lastUpdate AS CountryLastUpdate," +
                        "         U03oxz.country.lastUpdateBy AS CountryLastUpdatedBy " +
                        "FROM U03oxz.appointment " +
                        "INNER JOIN U03oxz.customer " +
                        "    ON U03oxz.appointment.customerId = U03oxz.customer.customerId " +
                        "INNER JOIN U03oxz.address " +
                        "    ON U03oxz.customer.addressId = U03oxz.address.addressId " +
                        "INNER JOIN U03oxz.city " +
                        "    ON U03oxz.address.cityId = U03oxz.city.cityId " +
                        "INNER JOIN U03oxz.country " +
                        "    ON U03oxz.city.countryId = U03oxz.country.countryId " +
                        "WHERE U03oxz.appointment.createdBy = ? AND U03oxz.appointment.start BETWEEN ?" +
                        "        AND ? ;");
                preparedStatement.setString(1, username);
                preparedStatement.setObject(2, weekStart);
                preparedStatement.setObject(3, weekEnd);
                log.console("Executing sql statement");
                boolean haveResult = preparedStatement.execute();
                if (haveResult) {
                    ResultSet resultSet = preparedStatement.getResultSet();
                    while(resultSet.next()){
                        Country country = new Country(resultSet.getInt("CountryId"), resultSet.getString("Country"), resultSet.getTimestamp("CountryCreateDate").toInstant(), resultSet.getString("CountryCreatedBy"), resultSet.getTimestamp("CountryLastUpdate").toInstant(), resultSet.getString("CountryLastUpdatedBy"));
                        City city = new City(resultSet.getInt("CityId"), resultSet.getString("City"), country, resultSet.getTimestamp("CityCreateDate").toInstant(), resultSet.getString("CityCreatedBy"), resultSet.getTimestamp("CityLastUpdate").toInstant(), resultSet.getString("CityLastUpdateBy"));
                        Address address = new Address(resultSet.getInt("AddressId"), resultSet.getString("Address1"), resultSet.getString("Address2"), city, resultSet.getString("ZipCode"), resultSet.getString("AddressPhone"), resultSet.getTimestamp("AddressCreateDate").toInstant(), resultSet.getString("AddressCreatedBy"), resultSet.getTimestamp("AddressLastUpdate").toInstant(), resultSet.getString("AddressLastUpdateBy"));
                        Customer customer = new Customer(resultSet.getInt("CustomerId"), resultSet.getString("CustomerName"), address, resultSet.getInt("CustomerActive"), resultSet.getTimestamp("CustomerCreateDate").toInstant(), resultSet.getString("CustomerCreatedBy"), resultSet.getTimestamp("CustomerLastUpdate").toInstant(), resultSet.getString("CustomerLastUpdateBy"));
                        appointments.add(new Appointment(resultSet.getInt("AppointmentId"), customer, resultSet.getString("AppointmentTitle"), resultSet.getString("AppointmentDescription"), resultSet.getString("AppointmentLocation"), resultSet.getString("AppointmentContact"), resultSet.getString("AppointmentUrl"), resultSet.getTimestamp("AppointmentStart").toInstant(), resultSet.getTimestamp("AppointmentEnd").toInstant(), resultSet.getTimestamp("AppointmentCreateDate").toInstant(), resultSet.getString("AppointmentCreatedBy"), resultSet.getTimestamp("AppointmentLastUpdate").toInstant(), resultSet.getString("AppointmentLastUpdateBy")));
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
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT U03oxz.appointment.appointmentId," +
                        "         U03oxz.customer.customerId," +
                        "         U03oxz.appointment.title AS AppointmentTitle," +
                        "         U03oxz.appointment.description AS AppointmentDescription," +
                        "         U03oxz.appointment.location AS AppointmentLocation," +
                        "         U03oxz.appointment.contact AS AppointmentContact," +
                        "         U03oxz.appointment.url AS AppointmentUrl," +
                        "         U03oxz.appointment.start AS AppointmentStart," +
                        "         U03oxz.appointment.end AS AppointmentEnd," +
                        "         U03oxz.appointment.createDate AS AppointmentCreateDate," +
                        "         U03oxz.appointment.createdBy AS AppointmentCreatedBy," +
                        "         U03oxz.appointment.lastUPDATE AS AppointmentLastUpdate," +
                        "         U03oxz.appointment.lastUpdateBy AS AppointmentLastUpdateBy," +
                        "         U03oxz.customer.customerName AS CustomerName," +
                        "         U03oxz.customer.addressId as AddressId," +
                        "         U03oxz.customer.active as CustomerActive," +
                        "         U03oxz.customer.createDate AS CustomerCreateDate," +
                        "         U03oxz.customer.CreatedBy AS CustomerCreatedBy," +
                        "         U03oxz.customer.lastUpdate AS CustomerLastUpdate," +
                        "         U03oxz.customer.lastUpdateBy AS CustomerLastUpdateBy," +
                        "         U03oxz.address.address AS Address1," +
                        "         U03oxz.address.address2 AS Address2," +
                        "         U03oxz.address.cityId AS CityId," +
                        "         U03oxz.address.postalCode AS ZipCode," +
                        "         U03oxz.address.phone AS AddressPhone," +
                        "         U03oxz.address.createDate AS AddressCreateDate," +
                        "         U03oxz.address.createdBy AS AddressCreatedBy," +
                        "         U03oxz.address.lastUpdate AS AddressLastUpdate," +
                        "         U03oxz.address.lastUpdateBy AS AddressLastUpdateBy," +
                        "         U03oxz.city.city as City," +
                        "         U03oxz.city.countryId as CountryId," +
                        "         U03oxz.city.createDate as CityCreateDate," +
                        "         U03oxz.city.createdBy as CityCreatedBy," +
                        "         U03oxz.city.lastUpdate as CityLastUpdate," +
                        "         U03oxz.city.lastUpdateBy AS CityLastUpdateBy," +
                        "         U03oxz.country.country AS Country," +
                        "         U03oxz.country.createDate AS CountryCreateDate," +
                        "         U03oxz.country.createdBy AS CountryCreatedBy," +
                        "         U03oxz.country.lastUpdate AS CountryLastUpdate," +
                        "         U03oxz.country.lastUpdateBy AS CountryLastUpdatedBy " +
                        "FROM U03oxz.appointment " +
                        "INNER JOIN U03oxz.customer " +
                        "    ON U03oxz.appointment.customerId = U03oxz.customer.customerId " +
                        "INNER JOIN U03oxz.address " +
                        "    ON U03oxz.customer.addressId = U03oxz.address.addressId " +
                        "INNER JOIN U03oxz.city " +
                        "    ON U03oxz.address.cityId = U03oxz.city.cityId " +
                        "INNER JOIN U03oxz.country " +
                        "    ON U03oxz.city.countryId = U03oxz.country.countryId " +
                        "WHERE U03oxz.appointment.createdBy = ? AND DATE_FORMAT(U03oxz.appointment.start, \"%m\") = ? " +
                        "        AND DATE_FORMAT(U03oxz.appointment.start, \"%Y\") = ?;");
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).getMonthValue());
                preparedStatement.setInt(3, zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).getYear());
                log.console("Executing sql statement");
                boolean haveResult = preparedStatement.execute();
                if (haveResult) {
                    ResultSet resultSet = preparedStatement.getResultSet();
                    while(resultSet.next()){
                        Country country = new Country(resultSet.getInt("CountryId"), resultSet.getString("Country"), resultSet.getTimestamp("CountryCreateDate").toInstant(), resultSet.getString("CountryCreatedBy"), resultSet.getTimestamp("CountryLastUpdate").toInstant(), resultSet.getString("CountryLastUpdatedBy"));
                        City city = new City(resultSet.getInt("CityId"), resultSet.getString("City"), country, resultSet.getTimestamp("CityCreateDate").toInstant(), resultSet.getString("CityCreatedBy"), resultSet.getTimestamp("CityLastUpdate").toInstant(), resultSet.getString("CityLastUpdateBy"));
                        Address address = new Address(resultSet.getInt("AddressId"), resultSet.getString("Address1"), resultSet.getString("Address2"), city, resultSet.getString("ZipCode"), resultSet.getString("AddressPhone"), resultSet.getTimestamp("AddressCreateDate").toInstant(), resultSet.getString("AddressCreatedBy"), resultSet.getTimestamp("AddressLastUpdate").toInstant(), resultSet.getString("AddressLastUpdateBy"));
                        Customer customer = new Customer(resultSet.getInt("CustomerId"), resultSet.getString("CustomerName"), address, resultSet.getInt("CustomerActive"), resultSet.getTimestamp("CustomerCreateDate").toInstant(), resultSet.getString("CustomerCreatedBy"), resultSet.getTimestamp("CustomerLastUpdate").toInstant(), resultSet.getString("CustomerLastUpdateBy"));
                        appointments.add(new Appointment(resultSet.getInt("AppointmentId"), customer, resultSet.getString("AppointmentTitle"), resultSet.getString("AppointmentDescription"), resultSet.getString("AppointmentLocation"), resultSet.getString("AppointmentContact"), resultSet.getString("AppointmentUrl"), resultSet.getTimestamp("AppointmentStart").toInstant(), resultSet.getTimestamp("AppointmentEnd").toInstant(), resultSet.getTimestamp("AppointmentCreateDate").toInstant(), resultSet.getString("AppointmentCreatedBy"), resultSet.getTimestamp("AppointmentLastUpdate").toInstant(), resultSet.getString("AppointmentLastUpdateBy")));
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
