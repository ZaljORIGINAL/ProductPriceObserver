package sample.Controllers.Fragments;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

public abstract class ViewFragment implements Initializable {
    public Pane mainPane;

    public FXMLLoader initFragmentView(){
        String pathToFXML = getPathToFXML();
        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader(
                    getClass().getResource(pathToFXML));
            loader.setControllerFactory(
                    c -> this);
            loader.load();
        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            loader = null;
        }

        return loader;
    }

    public Pane getMainPanel(){
        return mainPane;
    }

    public abstract String getPathToFXML();
}
