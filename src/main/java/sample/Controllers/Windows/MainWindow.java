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
    public ChoiceBox<String> shopChoice;
    public Pane tablePanel;
    public Button addNewProduct;
    public Button getMoreInfo;
    public Button deleteProduct;
    private ProductTableView productViewTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Инициализация элементов упраавления
        initShopChoice();
        initAddNewProductButton();
        initGetMoreInfoButton();
        initDeleteProductButton();
    }

    private void initShopChoice() {
        String[] choiceVariants = new String[]
                {
                        OptionsHelper.SHOP_CITILINK
                };
        shopChoice.getItems().addAll(choiceVariants);
        shopChoice.setOnAction(actionEvent -> switchTableView());
        shopChoice.setValue(choiceVariants[0]);
    }

    private void initAddNewProductButton(){
        addNewProduct.setDisable(true);
        addNewProduct.setOnAction(actionEvent -> clickAddNewProduct());
    }

    private void initGetMoreInfoButton(){
        getMoreInfo.setDisable(true);
        getMoreInfo.setOnAction(actionEvent -> clickShowInfoAbout());
    }

    private void initDeleteProductButton(){
        deleteProduct.setDisable(true);
        deleteProduct.setOnAction(actionEvent -> clickDeleteProduct());
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
            *       1. Неудалось считать: Заблокировать все элементы управления: кнопку обновления, визуальную таблиу.*/
        }
        productViewTable.initFragmentView();
        var children = tablePanel.getChildren();
        children.clear();
        children.add(productViewTable.getMainPanel());

        var table = productViewTable.tableView;
        table.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null){
                        addNewProduct.setDisable(false);
                        getMoreInfo.setDisable(false);
                        deleteProduct.setDisable(false);
                    }else{
                        addNewProduct.setDisable(true);
                        getMoreInfo.setDisable(true);
                        deleteProduct.setDisable(true);
                    }
                });
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
}
