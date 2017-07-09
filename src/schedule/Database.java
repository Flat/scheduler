package schedule;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Database ourInstance = new Database();

    public static Database getInstance() {
        return ourInstance;
    }

    private static Connection connection = null;



    private Database() {
        try {
            debug.log("Connecting to database");
            connection = DriverManager.getConnection("jdbc:mysql://52.206.157.109/U03oxz", "U03oxz", "53688041085");
            debug.log("Connected to database");
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, I18n.getLocalizedString("databaseConnectionError"));
            alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> System.exit(1));
        }
    }
}
