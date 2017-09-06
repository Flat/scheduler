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
            log.login("User " + textfieldUser.getText() + " logged in");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("schedule.fxml"));
            Parent root;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Stage stage = new Stage();
            stage.setTitle(I18n.getLocalizedString("scheduleTitle"));
            stage.setScene(new Scene(root, 600, 400));
            ScheduleController scheduleController = fxmlLoader.getController();
            scheduleController.setUsername(textfieldUser.getText());
            scheduleController.initTableData();
            stage.show();
            Stage mainStage = (Stage) btnLogin.getScene().getWindow();
            mainStage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, I18n.getLocalizedString("invalidLogin"));
            alert.showAndWait();
        }
    }

}
