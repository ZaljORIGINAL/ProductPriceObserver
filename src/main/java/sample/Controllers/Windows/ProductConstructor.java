package sample.Controllers.Windows;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import sample.Controllers.Fragments.ConstructorFragment;
import sample.Controllers.Fragments.MainWindow.Tables.CitilinkShopTableView;
import sample.Controllers.Fragments.ProductConstructors.CitilinkShopConstructorFragment;
import sample.Databases.DatabaseTable;
import sample.OptionsHelper;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductConstructor implements Initializable {
    public Pane mainPane;
    public Pane fragmentPane;
    public Button accept;
    public ChoiceBox shopChoice;
    private ConstructorFragment fragment;
    private DatabaseTable databaseTable;

    public ProductConstructor(DatabaseTable databaseTable){
        this.databaseTable = databaseTable;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initShopChoice();

        fragment.initFragmentView();
        fragmentPane.getChildren().clear();
        fragmentPane.getChildren().add(fragment.getMainPanel());
        accept.setText("Сохранить");
    }

    private void initShopChoice() {
        String[] choiceVariants = new String[]
                {
                        OptionsHelper.SHOP_CITILINK
                };
        shopChoice.getItems().addAll(choiceVariants);
        shopChoice.setValue(shopChoice.getItems().get(0));
        shopChoice.setOnAction(actionEvent -> switchTableView());
    }

    private void switchTableView(){
        var selected = (String) shopChoice.getValue();
        switch (selected){
            case OptionsHelper.SHOP_CITILINK:
            {
                fragment = new CitilinkShopConstructorFragment();
            }break;

            default:
                //Вывести лог о незарегестрированном варианте
        }

        fragment.initFragmentView();
        fragmentPane.getChildren().clear();
        var fragmentPane = fragment.getMainPanel();
        fragmentPane.getChildren().add(fragmentPane);
    }

    public void clickAccept(ActionEvent actionEvent) {
        if (fragment.checkFields()){
            //TODO Вывести сообщение о результате сохранения
            if (fragment.saveProduct()){
            }
        }
    }
}
