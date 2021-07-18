package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import sample.Controllers.Windows.MainWindow;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    private static final Logger logger = LogManager.getLogger(Main.class);
    public static final ApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);

    @Override
    public void start(Stage primaryStage){
        logger.info("Запуск программы");

        try {
            logger.info("Загрузка граффического интерфейса...");
            URL pathToFxml =
                    getClass().getResource("/windows/window_main.fxml");
            var mainWindow = new MainWindow(context);
            var loader = new FXMLLoader(pathToFxml);
            loader.setControllerFactory(c-> mainWindow);
            Parent root = loader.load();
            primaryStage.setTitle("Просмотр цен");
            primaryStage.setScene(new Scene(root));
            primaryStage.setOnCloseRequest(windowEvent -> mainWindow.finishAllServices());
            primaryStage.show();
            logger.info("Граффического интерфейса главного окна успешно запущен.");
        }catch (IOException exception){
            logger.error("Неудалось загрузить граффическое представление главного окна!", exception);
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("Ошибка");
            dialog.setHeaderText("Ошибка в запуске программы.");
            dialog.setContentText(exception.getMessage());
            dialog.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
