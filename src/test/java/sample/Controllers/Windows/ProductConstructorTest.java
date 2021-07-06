package sample.Controllers.Windows;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import sample.Controllers.Fragments.ProductConstructors.CitilinkShopConstructorFragment;

import java.net.URL;

import static org.junit.Assert.*;

public class ProductConstructorTest {

    //FIXME Тест выдает ошибку на моменте загрузки графического окна
    // Parent root = loader.load();
    public void fullSystemTest() throws Exception{
        var fragment = new CitilinkShopConstructorFragment();

        URL pathToFxml =
                getClass().getResource("/windows/window_product_constructor.fxml");
        var loader = new FXMLLoader(pathToFxml);
        loader.setControllerFactory(c -> new ProductConstructor(fragment));
        Parent root = loader.load();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Просмотр цен");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}