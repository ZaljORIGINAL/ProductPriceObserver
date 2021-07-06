package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;

import java.net.URL;

public class Main extends Application {
    private ApplicationContext context;

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL pathToFxml =
                getClass().getResource("/windows/window_main.fxml");
        var loader = new FXMLLoader(pathToFxml);
        Parent root = loader.load();
        primaryStage.setTitle("Просмотр цен");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
