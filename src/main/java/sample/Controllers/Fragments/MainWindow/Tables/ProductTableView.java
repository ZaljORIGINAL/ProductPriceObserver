package sample.Controllers.Fragments.MainWindow.Tables;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.util.Pair;
import sample.Controllers.Fragments.ProductConstructorFragment;
import sample.Controllers.Fragments.ProductEditorFragment;
import sample.Controllers.Fragments.ViewFragment;
import sample.Databases.ProductsTable;
import sample.Products.Price;
import sample.Products.Product;
import sample.Products.ProductProperty;

import java.net.URL;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public abstract class ProductTableView extends ViewFragment {
    public TableView<ProductProperty> tableView;
    public Button tableUpdate;

    protected ProductsTable tableData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTableUpdateButton();
        initTableView();
    }

    public void updateTable() throws SQLException{
        ObservableList<ProductProperty> items =
                FXCollections.observableArrayList();
        tableData.openConnection();
        var products = tableData.getAll();
        for (Product product : products) {
            addToViewTable(product);
        }
    }

    public void callProductConstructorDialog(){
        Dialog dialog = buildConstructorDialog(
                getFragmentToConstructor());
        Optional<Pair<String, Product>> result
                = dialog.showAndWait();
        result.ifPresent(answer->{
            try {
                var product = answer.getValue();
                tableData.openConnection();
                var rows = tableData.insert(product);
                tableData.closeConnection();

                if (rows != 0)
                    addToViewTable(product);
            }catch (SQLException exception){
                Alert messageDialog =
                        new Alert(Alert.AlertType.ERROR);
                messageDialog.setTitle("Ошибка");
                messageDialog.setHeaderText("Ошибка в базе данных");
                messageDialog.setContentText(exception.getMessage());
            }
        });
    }

    public void callProductEditorDialog(){
        Dialog dialog = buildEditorDialog(
                getFragmentToEditor());
        Optional<Pair<String, Product>> result =
                dialog.showAndWait();
        result.ifPresent(answer->{
            try {
                /*TODO Можно сделать проверку на наличие продукта в базе.*/
                var product = answer.getValue();
                tableData.openConnection();
                var rows = tableData.update(product);
                tableData.closeConnection();

                if (rows != 0)
                    updateToViewTableItem(product);
            }catch (SQLException exception){
                Alert messageDialog =
                        new Alert(Alert.AlertType.ERROR);
                messageDialog.setTitle("Ошибка");
                messageDialog.setHeaderText("Ошибка в базе данных");
                messageDialog.setContentText(exception.getMessage());
            }
        });
    }

    public void callProductDeleteDialog(){
        var productProperty
                = tableView.getSelectionModel().getSelectedItem();
        var product = productProperty.getProduct();

        Dialog dialog = buildDeleteDialog(product);

        Optional<ButtonType> option = dialog.showAndWait();
        if (option.get() == ButtonType.OK){
            try {
                /*TODO Можно сделать проверку на наличие продукта в базе.*/
                tableData.openConnection();
                var rows = tableData.delete(product);
                tableData.closeConnection();

                if (rows != 0)
                    deleteItemFromViewTable(product);
            }catch (SQLException exception){
                Alert messageDialog =
                        new Alert(Alert.AlertType.ERROR);
                messageDialog.setTitle("Ошибка");
                messageDialog.setHeaderText("Ошибка в базе данных");
                messageDialog.setContentText(exception.getMessage());
            }
        }
    }

    protected void initTableUpdateButton(){
        tableUpdate.setOnAction(actionEvent -> {
            try {
                updateTable();
            }catch (SQLException exception){
                tableView.getItems().clear();
                Alert messageDialog =
                        new Alert(Alert.AlertType.ERROR);
                messageDialog.setTitle("Ошибка");
                messageDialog.setHeaderText("Ошибка при работе с базой данных");
                messageDialog.setContentText(exception.getMessage());
            }
        });
    }

    protected abstract void initTableView();

    protected abstract ProductConstructorFragment getFragmentToConstructor();

    protected abstract ProductEditorFragment getFragmentToEditor();

    protected void addToViewTable(Product product){
        ProductProperty productProperty;
        try{
            var pricesTable = product.getPriceTable();
            var lastPrice = pricesTable.getLastPrice();
            productProperty = new ProductProperty(
                    product,
                    lastPrice);
        }catch (SQLException exception){
            /*TODO Добавить лог об ошибке. Так же сделать сдел
            *  При ошибке вывести запись о товаре, но в место
            *  цены указать: "Нет информации"*/
            Price price = new Price(
                    Calendar.getInstance(),
                    -1);
            productProperty = new ProductProperty(
                    product,
                    price);
        }

        tableView.getItems().add(productProperty);
    }

    protected void updateToViewTableItem(Product product){
        /*TODO
        *  1. Найти в визуальной таблице запись о обнавляемом продукте
        *  2. Зная, что элементы таблици являются Property объектами
        *  достаточно задать им изменение, таблица сама обновится. */
        List<ProductProperty> productProperties =
                tableView.getItems();
        for (ProductProperty property:
             productProperties) {
            var toComparison = property.getProduct();
            if (product.getId() == toComparison.getId()){
                Price price;
                try{
                    var pricesTable = product.getPriceTable();
                    price = pricesTable.getLastPrice();
                }catch (SQLException exception){
                    /*TODO Добавить лог об ошибке.
                     *  При ошибке вывести запись о товаре, но в место
                     *  цены указать: "Нет информации"*/
                    price = new Price(
                            Calendar.getInstance(),
                            -1);
                }
                property.setProduct(product);
                property.setPrice(price);
            }
        }
    }

    protected void deleteItemFromViewTable(Product product){
        /*TODO
        *  1. Найти в визуальной таблице запись о обнавляемом продукте
        *  2. Удалить запись в визуальной таблице*/
        List<ProductProperty> productProperties =
                tableView.getItems();
        for (ProductProperty property:
                productProperties) {
            var toComparison = property.getProduct();
            if (product.getId() == toComparison.getId()){
                productProperties.remove(property);
            }
        }
    }

    protected Dialog buildConstructorDialog(ProductConstructorFragment fragment){
        Dialog<Pair<String, Product>> dialog =
                new Dialog<>();
        dialog.setTitle("Конструктор продукта");
        dialog.setHeaderText("Задайте параметры продукта");

        fragment.initFragmentView();
        dialog.getDialogPane().setContent(
                fragment.getMainPanel());

        ButtonType addProductButton =
                new ButtonType(
                        "Добавить",
                        ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes()
                .addAll(
                        addProductButton,
                        ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton->{
            if (dialogButton == addProductButton){
                if (fragment.checkFields()){
                    var product =
                            fragment.saveProduct();
                    var answer = new Pair<>(
                            "answer",
                            product);

                    return answer;
                }
            }

            //Ну тут хз
            return null;
        });
        return null;
    }

    protected Dialog buildEditorDialog(ProductEditorFragment fragment){
        Dialog<Pair<String, Product>> dialog = new Dialog<>();
        dialog.setTitle("Редактор продукта");
        dialog.setHeaderText("Параметры продукта");

        fragment.initFragmentView();
        dialog.getDialogPane().setContent(
                fragment.getMainPanel());

        ButtonType saveChangeButton =
                new ButtonType(
                        "Сохранить",
                        ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes()
                .addAll(
                        saveChangeButton,
                        ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton->{
            if (dialogButton == saveChangeButton){
                if (fragment.checkFields()){
                    var product =
                            fragment.saveProduct();
                    var answer = new Pair<>(
                            "answer",
                            product);

                    return answer;
                }
            }

            //Ну тут хз
            return null;
        });
        return null;
    }

    protected Dialog buildDeleteDialog(Product product){
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Уадление записи");
        dialog.setHeaderText("Удалить отслежуемый продукт?");
        dialog.setContentText("Информация о продукте: " +
                "\nНаименование: " + product.getName() +
                "\nСсылка: " + product.getLink());

        return dialog;
    }
}
