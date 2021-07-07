package sample.Controllers.Windows;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.Controllers.Fragments.MainWindow.Tables.ProductsTables.CitilinkProductTableView;
import sample.Controllers.Fragments.MainWindow.Tables.ProductTableView;
import sample.OptionsHelper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {
    public ChoiceBox shopChoice;
    public Pane tablePanel;
    public Button addNewProduct;
    public Button getMoreInfo;
    public Button deleteProduct;
    public Button programSettings;
    private ProductTableView productViewTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Инициализация элементов упраавления
        initShopChoice();
        initAddNewProductButton();
        initGetMoreInfoButton();
        initDeleteProductButton();
        initProgramSettings();
        //Инициафлизация фрагментов окна
        initTablePanel();
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

    private void initAddNewProductButton(){
        addNewProduct.setOnAction(actionEvent -> clickAddNewProduct());
    }

    private void initGetMoreInfoButton(){
        getMoreInfo.setOnAction(actionEvent -> clickShowInfoAbout());
    }

    private void initDeleteProductButton(){
        deleteProduct.setOnAction(actionEvent -> clickDeleteProduct());
    }

    private void initProgramSettings(){
        programSettings.setOnAction(actionEvent -> clickOpenSettings());
    }

    private void initTablePanel(){
        productViewTable = new CitilinkProductTableView();
        productViewTable.initFragmentView();

        //FIXME Получаема панель не выходит за рамки выделенной ей области.
        tablePanel.getChildren().clear();
        tablePanel.getChildren().add(productViewTable.getMainPanel());
    }

    private void switchTableView(){
        var selected = (String) shopChoice.getValue();
        switch (selected){
            case OptionsHelper.SHOP_CITILINK:
            {
                productViewTable = new CitilinkProductTableView();
            }break;

            default:
                //Вывести лог о незарегестрированном варианте
        }

        productViewTable.initFragmentView();
        tablePanel.getChildren().clear();
        tablePanel.getChildren().add(productViewTable.getMainPanel());
    }

    private void clickAddNewProduct(){
        try {
            URL pathToFxml =
                    getClass().getResource("/windows/window_product_constructor.fxml");
            var loader = new FXMLLoader(pathToFxml);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Просмотр цен");
            stage.setScene(new Scene(root));
            stage.show();
        }catch (IOException exception){
            //TODO Вывести сообщение о соответсвующей ошибке.
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void clickShowInfoAbout() {
    }

    private void clickDeleteProduct() {
    }

    private void clickOpenSettings() {
    }
}