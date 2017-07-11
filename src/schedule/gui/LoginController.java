package schedule.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import schedule.Database;
import schedule.log;
import schedule.I18n;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private Label labelUsername;

    @FXML
    private TextField textfieldUser;

    @FXML
    private Label labelPassword;

    @FXML
    private TextField textfieldPassword;

    @FXML
    private Button btnLogin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        log.console("Setting up i18n for login screen");
        labelUsername.setText(I18n.getLocalizedString("loginUsername"));
        labelPassword.setText(I18n.getLocalizedString("loginPassword"));
        btnLogin.setText(I18n.getLocalizedString("loginButton"));
    }

    @FXML
    public void login() {
        Database database = new Database();
        boolean loggedIn;
        loggedIn = database.loginUser(textfieldUser.getText(), textfieldPassword.getText());
        if (loggedIn) {
            log.file("User " + textfieldUser.getText() + " logged in");
            Parent root;
            try {
                root = FXMLLoader.load(getClass().getResource("schedule.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Stage stage = new Stage();
            stage.setTitle(I18n.getLocalizedString("scheduleTitle"));
            stage.setScene(new Scene(root, 500, 400));
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, I18n.getLocalizedString("invalidLogin"));
            alert.showAndWait();
        }
    }

}
