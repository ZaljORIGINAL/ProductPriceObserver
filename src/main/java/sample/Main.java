package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import sample.ProductObserver.TriggerTusk;

import java.net.URL;
import java.util.Calendar;
import java.util.Timer;

public class Main extends Application {
    private static final Logger logger = LogManager.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) throws Exception{
        //https://sematext.com/blog/log4j2-tutorial/#toc-log4j-2-configuration-1
        for (int i = 0; i < 100000; i++){
            logger.info("Info");
            logger.warn("Warn");
            logger.error("Error");
        }

        Timer timer = new Timer();
        timer.schedule(
                new TriggerTusk(),
                Calendar.getInstance().getTime(),
                3600000);

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
