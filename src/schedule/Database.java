package schedule;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javax.xml.transform.Result;
import java.sql.*;
import java.text.DateFormatSymbols;
import java.time.*;
import java.time.format.DateTimeFormatter;

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

    public ObservableList<Customer> getCustomers(String username) {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT U03oxz.customer.customerId," +
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
                    "FROM U03oxz.customer " +
                    "INNER JOIN U03oxz.address " +
                    "    ON U03oxz.customer.addressId = U03oxz.address.addressId " +
                    "INNER JOIN U03oxz.city " +
                    "    ON U03oxz.address.cityId = U03oxz.city.cityId " +
                    "INNER JOIN U03oxz.country " +
                    "    ON U03oxz.city.countryId = U03oxz.country.countryId " +
                    "WHERE U03oxz.customer.createdBy = ? ;");
            preparedStatement.setString(1, username);
            boolean haveResult = preparedStatement.execute();
            if(haveResult){
                ResultSet resultSet = preparedStatement.getResultSet();
                while(resultSet.next()){
                    Country country = new Country(resultSet.getInt("CountryId"), resultSet.getString("Country"), resultSet.getTimestamp("CountryCreateDate").toInstant(), resultSet.getString("CountryCreatedBy"), resultSet.getTimestamp("CountryLastUpdate").toInstant(), resultSet.getString("CountryLastUpdatedBy"));
                    City city = new City(resultSet.getInt("CityId"), resultSet.getString("City"), country, resultSet.getTimestamp("CityCreateDate").toInstant(), resultSet.getString("CityCreatedBy"), resultSet.getTimestamp("CityLastUpdate").toInstant(), resultSet.getString("CityLastUpdateBy"));
                    Address address = new Address(resultSet.getInt("AddressId"), resultSet.getString("Address1"), resultSet.getString("Address2"), city, resultSet.getString("ZipCode"), resultSet.getString("AddressPhone"), resultSet.getTimestamp("AddressCreateDate").toInstant(), resultSet.getString("AddressCreatedBy"), resultSet.getTimestamp("AddressLastUpdate").toInstant(), resultSet.getString("AddressLastUpdateBy"));
                    Customer customer = new Customer(resultSet.getInt("CustomerId"), resultSet.getString("CustomerName"), address, resultSet.getInt("CustomerActive"), resultSet.getTimestamp("CustomerCreateDate").toInstant(), resultSet.getString("CustomerCreatedBy"), resultSet.getTimestamp("CustomerLastUpdate").toInstant(), resultSet.getString("CustomerLastUpdateBy"));
                    customers.add(customer);
                }
            }
        } catch(java.sql.SQLException e) {
            log.console(e.getMessage());
            e.printStackTrace();
        }

        return customers;
    }

    public void deleteAppointment(Appointment appointment){
        try(Connection connection = getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM U03oxz.appointment WHERE U03oxz.appointment.appointmentId = ? ;");
            preparedStatement.setInt(1, appointment.getAppointmentid());
            preparedStatement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void addAppointment(Appointment appointment) {
        try(Connection connection = getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO U03oxz.appointment (customerId, title, description, location, contact, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? );");
            preparedStatement.setInt(1, appointment.getCustomer().getCustomerId());
            preparedStatement.setString(2, appointment.getTitle());
            preparedStatement.setString(3, appointment.getDescription());
            preparedStatement.setString(4, appointment.getLocation());
            preparedStatement.setString(5, appointment.getContact());
            preparedStatement.setString(6, appointment.getUrl());
            preparedStatement.setTimestamp(7, Timestamp.from(appointment.getStart().toInstant()));
            preparedStatement.setTimestamp(8, Timestamp.from(appointment.getEnd().toInstant()));
            preparedStatement.setTimestamp(9, Timestamp.from(appointment.getCreateDate()));
            preparedStatement.setString(10, appointment.getCreatedBy());
            preparedStatement.setTimestamp(11, Timestamp.from(appointment.getLastUpdate()));
            preparedStatement.setString(12, appointment.getLastUpdateBy());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCustomer(Customer customer, String username) {
        if(customer.getCustomerId() == 0) {
            int countryId = addorselectCountry(customer, username);
            int cityId = addorselectCity(customer, username, countryId);
            int addressId = addAddress(customer, username, cityId);
            assert countryId != 0;
            assert cityId != 0;
            assert addressId != 0;
            try(Connection connection = getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO U03oxz.customer(customerName, addressId, active, createDate, createdBy, lastUpdateBy) VALUES(?, ?, ?, ?, ?, ?)");
                preparedStatement.setString(1, customer.getCustomerName());
                preparedStatement.setInt(2, addressId);
                preparedStatement.setInt(3, 1);
                preparedStatement.setTimestamp(4, Timestamp.from(Instant.now()));
                preparedStatement.setString(5, username);
                preparedStatement.setString(6, username);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try(Connection connection = getConnection()){
                PreparedStatement updateCustomer = connection.prepareStatement("UPDATE U03oxz.customer SET customerName = ?, active = ?, lastUpdateBy = ? WHERE customerId = ?");
                updateCustomer.setString(1, customer.getCustomerName());
                updateCustomer.setInt(2, customer.getActive());
                updateCustomer.setString(3, username);
                updateCustomer.setInt(4, customer.getCustomerId());
                PreparedStatement updateAddress = connection.prepareStatement("UPDATE U03oxz.address SET address = ?, address2 = ?, cityId = ?, postalCode = ?, phone = ?, lastUpdateBy = ? WHERE addressId = ?");
                int countryId = addorselectCountry(customer, username);
                int cityId = addorselectCity(customer, username, countryId);
                assert cityId != 0;
                updateAddress.setString(1, customer.getAddress().getAddress());
                updateAddress.setString(2, customer.getAddress().getAddress2());
                updateAddress.setInt(3, cityId);
                updateAddress.setString(4, customer.getAddress().getPostalCode());
                updateAddress.setString(5, customer.getAddress().getPhone());
                updateAddress.setString(6, username);
                updateAddress.setInt(7, customer.getAddress().getAddressId());
                updateAddress.execute();
                updateCustomer.execute();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }

    }

    private int addorselectCountry(Customer customer, String username) {
        try(Connection connection = getConnection()) {
            PreparedStatement selectCountry = connection.prepareStatement("SELECT * FROM U03oxz.country WHERE U03oxz.country.country = ?");
            PreparedStatement insertCountry = connection.prepareStatement("INSERT INTO U03oxz.country(country, createDate, createdBy, lastUpdateBy) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            selectCountry.setString(1, customer.getAddress().getCity().getCountry().getCountry());
            insertCountry.setString(1, customer.getAddress().getCity().getCountry().getCountry());
            insertCountry.setTimestamp(2, Timestamp.from(Instant.now()));
            insertCountry.setString(3, username);
            insertCountry.setString(4, username);
            ResultSet resultCountry = selectCountry.executeQuery();
            if(resultCountry.next()){
                return resultCountry.getInt("countryId");
            } else {
                insertCountry.executeUpdate();
                ResultSet countryIdResult = insertCountry.getGeneratedKeys();
                if(countryIdResult.next()) {
                    return countryIdResult.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int addorselectCity(Customer customer, String username, int countryId){
        try(Connection connection = getConnection()) {
            PreparedStatement selectCity = connection.prepareStatement("SELECT * FROM U03oxz.city WHERE U03oxz.city.city = ? AND U03oxz.city.countryId = ?");
            PreparedStatement insertCity =  connection.prepareStatement("INSERT INTO U03oxz.city(city, countryId, createdBy, createDate, lastUpdateBy) VALUES(?, ?, ?, ? , ?)", Statement.RETURN_GENERATED_KEYS);
            selectCity.setString(1, customer.getAddress().getCity().getCity());
            selectCity.setInt(2, countryId);
            ResultSet resultCity = selectCity.executeQuery();
            if(resultCity.next()){
                return resultCity.getInt("cityId");
            } else {
                insertCity.setString(1, customer.getAddress().getCity().getCity());
                insertCity.setInt(2, countryId);
                insertCity.setString(3, username);
                insertCity.setTimestamp(4, Timestamp.from(Instant.now()));
                insertCity.setString(5, username);
                insertCity.executeUpdate();
                ResultSet cityIdResult = insertCity.getGeneratedKeys();
                if(cityIdResult.next()){
                    return cityIdResult.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;

    }

    private int addAddress(Customer customer, String username, int cityId) {
        try(Connection connection = getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO U03oxz.address(address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy) VALUES(?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, customer.getAddress().getAddress());
            preparedStatement.setString(2, customer.getAddress().getAddress2());
            preparedStatement.setInt(3, cityId);
            preparedStatement.setString(4, customer.getAddress().getPostalCode());
            preparedStatement.setString(5, customer.getAddress().getPhone());
            preparedStatement.setTimestamp(6, Timestamp.from(Instant.now()));
            preparedStatement.setString(7, username);
            preparedStatement.setString(8, username);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()){
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void scheduleByConsultant(ZonedDateTime zonedDateTime){
        try(Connection connection = getConnection()) {
            PreparedStatement getConsultants = connection.prepareStatement("SELECT * FROM U03oxz.user");
            ResultSet consultants = getConsultants.executeQuery();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Monthly Schedule by Consultant\n");
            while(consultants.next()){
                String consultant = consultants.getString("userName");
                stringBuilder.append(consultant);
                stringBuilder.append(":\n");
                ObservableList<Appointment> appointments = getAppointments(consultant, zonedDateTime, false);
                appointments.forEach((appointment -> {
                    stringBuilder.append("Appointment: ");
                    stringBuilder.append(appointment.getTitle());
                    stringBuilder.append(" with ");
                    stringBuilder.append(appointment.getCustomer().getCustomerName());
                    stringBuilder.append(" on ");
                    stringBuilder.append(appointment.getStart().format(DateTimeFormatter.ofPattern("E M d, u")));
                    stringBuilder.append(" ends on ");
                    stringBuilder.append(appointment.getEnd().format(DateTimeFormatter.ofPattern("E M d, u")));
                    stringBuilder.append("\n\n");
                }));
            }
            log.report("ScheduleByConsultant.txt", stringBuilder.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void numCust(){
        try(Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(customerId) FROM U03oxz.customer;");
            ResultSet numCustomers = preparedStatement.executeQuery();
            if(numCustomers.next()){
                log.report("NumberOfCustomers.txt", String.format("Number of customers as of %tc: %d", ZonedDateTime.now(ZoneId.systemDefault()), numCustomers.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void apptTypeMonth(int month, int year){
        try(Connection connection = getConnection()) {
            PreparedStatement initialStatement = connection.prepareStatement("SELECT COUNT(appointmentId) FROM U03oxz.appointment WHERE DATE_FORMAT(U03oxz.appointment.start, \"%m\") = ? AND DATE_FORMAT(U03oxz.appointment.start, \"%Y\") = ? AND U03oxz.appointment.description = \"Initial\";");
            PreparedStatement followupStatement = connection.prepareStatement("SELECT COUNT(appointmentId) FROM U03oxz.appointment WHERE DATE_FORMAT(U03oxz.appointment.start, \"%m\") = ? AND DATE_FORMAT(U03oxz.appointment.start, \"%Y\") = ? AND U03oxz.appointment.description = \"Followup\";");
            PreparedStatement closingStatement = connection.prepareStatement("SELECT COUNT(appointmentId) FROM U03oxz.appointment WHERE DATE_FORMAT(U03oxz.appointment.start, \"%m\") = ? AND DATE_FORMAT(U03oxz.appointment.start, \"%Y\") = ? AND U03oxz.appointment.description = \"Closing\";");
            initialStatement.setInt(1, month);
            initialStatement.setInt(2, year);
            followupStatement.setInt(1, month);
            followupStatement.setInt(2, year);
            closingStatement.setInt(1, month);
            closingStatement.setInt(2, year);
            ResultSet initial = initialStatement.executeQuery();
            ResultSet followup = followupStatement.executeQuery();
            ResultSet closing = closingStatement.executeQuery();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(String.format("Appointments by Type for the month of %tB\n", LocalDate.of(year, month, 1)));
            if (initial.next()){
                stringBuilder.append(String.format("Initial: %d\n", initial.getInt(1)));
            } else {
                stringBuilder.append(String.format("Initial: %d\n", 0));
            }
            if (followup.next()){
                stringBuilder.append(String.format("Followup: %d\n", followup.getInt(1)));
            } else {
                stringBuilder.append(String.format("Followup: %d\n", 0));
            }
            if (closing.next()){
                stringBuilder.append(String.format("Closing: %d\n", closing.getInt(1)));
            } else {
                stringBuilder.append(String.format("Closing: %d\n", 0));
            }
            log.report("AppointmentTypesByMonth.txt", stringBuilder.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
