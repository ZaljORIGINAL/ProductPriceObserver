package sample;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sample.ProductObserver.TriggerTusk;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class Main extends Application {
    private static final Logger logger = LogManager.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage){
        logger.info("Запуск программы");

        //https://habr.com/ru/post/260953/
        //Для задачи имени потока отвечающего за обновление цен продуктов.
        logger.info("Потготовка таймера на обновление цен с периодом в 1час.");
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("Задача обновления цен-%d")
                .build();
        ScheduledExecutorService executorService =
                Executors.newSingleThreadScheduledExecutor(threadFactory);
        executorService.scheduleAtFixedRate(
                ()-> {
                    new Thread(new TriggerTusk()).start();
                },
                0,
                1,
                TimeUnit.HOURS);
        logger.info("Установлен таймер на обновления цен товаров.");

        try {
            logger.info("Загрузка граффического интерфейса...");
            URL pathToFxml =
                    getClass().getResource("/windows/window_main.fxml");
            var loader = new FXMLLoader(pathToFxml);
            Parent root = loader.load();
            primaryStage.setTitle("Просмотр цен");
            primaryStage.setScene(new Scene(root));
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

        //Сбрасываем задачу на обновление цены продуктов.
        /*List<Runnable> shutdownTasks = executorService.shutdownNow();
        logger.info("Отмена задач на обновление цен продуктов.");*/
    }

    public static void main(String[] args) {
        launch(args);
    }
}
