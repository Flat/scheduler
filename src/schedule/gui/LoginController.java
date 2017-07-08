package schedule.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import schedule.debug;
import schedule.i18n;

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
        debug.log("Setting up i18n for login screen");
        labelUsername.setText(i18n.getLocalizedString("loginUsername"));
        labelPassword.setText(i18n.getLocalizedString("loginPassword"));
        btnLogin.setText(i18n.getLocalizedString("loginButton"));
    }
}
