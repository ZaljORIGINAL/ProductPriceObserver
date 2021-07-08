package sample.Controllers.Windows;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import sample.Controllers.Fragments.MainWindow.Tables.ProductsTables.CitilinkProductTableView;
import sample.Controllers.Fragments.MainWindow.Tables.ProductTableView;
import sample.OptionsHelper;

import java.net.URL;
import java.sql.SQLException;
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
        switchTableView();
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

    private void switchTableView(){
        var selected = (String) shopChoice.getValue();
        try {
            switch (selected){
                case OptionsHelper.SHOP_CITILINK:
                {
                    productViewTable = new CitilinkProductTableView();
                }break;

                default:
                    //Вывести лог о незарегестрированном варианте
            }
        }catch (SQLException exception){
            /*TODO Херня, надо сделать так: конструкторы фрагментов должны сами обрабатывать ошибку.
            *  Например:
            *       1. Если отсутсвует база данных, вывети сообщеие: "Данные на компьюторе на обнаружены. Будет создана новая база."
            *       2. Неудалось считать: Заблокировать все элементы управления: кнопку обновления, визуальную таблиу.*/
        }
        productViewTable.initFragmentView();
        productViewTable.initFragmentView();
        tablePanel.getChildren().clear();
        tablePanel.getChildren().add(productViewTable.getMainPanel());
    }

    private void clickAddNewProduct(){
        productViewTable.callProductConstructorDialog();
    }

    private void clickShowInfoAbout() {
        productViewTable.callProductEditorDialog();
    }

    private void clickDeleteProduct() {
        productViewTable.callProductDeleteDialog();
    }

    private void clickOpenSettings() {
    }
}
