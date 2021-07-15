package sample.Controllers.Fragments;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class ViewFragment implements Initializable {
    private static final Logger logger = LogManager.getLogger(ViewFragment.class);

    public Pane mainPane;

    public FXMLLoader initFragmentView(){
        logger.info("Запусчена инициализация граффического интерфейса фрагмента...");
        String pathToFXML = getPathToFXML();
        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader(
                    getClass().getResource(pathToFXML));
            loader.setControllerFactory(
                    c -> this);
            loader.load();
            logger.info("Инициализация граффического интерфейса фрагмента завершена.");
        }catch (Exception exception){
            logger.error("Ошибка в инициализации граффического интерфейса фрагмента!", exception);
            loader = null;
        }

        return loader;
    }

    public Pane getMainPanel(){
        return mainPane;
    }

    public abstract String getPathToFXML();
}
