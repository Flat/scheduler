package schedule;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        debug.log("Loading main dialog");
        Parent root = FXMLLoader.load(getClass().getResource("gui/login.fxml"));
        primaryStage.setTitle(i18n.getLocalizedString("loginTitle"));
        primaryStage.setScene(new Scene(root, 300, 200));
        primaryStage.show();
    }


    public static void main(String[] args) {
        i18n.testLocale("ja");
        launch(args);
    }
}
