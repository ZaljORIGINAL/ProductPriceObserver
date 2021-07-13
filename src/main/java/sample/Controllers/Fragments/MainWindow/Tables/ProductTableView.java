package sample.Controllers.Fragments.MainWindow.Tables;

import javafx.scene.control.*;
import javafx.util.Pair;
import sample.Controllers.Fragments.ProductConstructorFragment;
import sample.Controllers.Fragments.ProductEditorFragment;
import sample.Controllers.Fragments.ViewFragment;
import sample.Databases.ProductPricesTable;
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
        tableUpdate.setDisable(true);
        var products = tableData.getAll();
        tableView.getItems().clear();
        for (Product product : products) {
            addToViewTable(product);
        }
        tableUpdate.setDisable(false);
    }

    //TODO При добавлении продукта получить цену
    public void callProductConstructorDialog(){
        Dialog dialog = buildConstructorDialog(
                getFragmentToConstructor());
        Optional<Pair<String, Product>> result
                = dialog.showAndWait();
        result.ifPresent(answer->{
            try{
                var product = tableData.insert(
                        answer.getValue());
                if (product.getId() != -1){
                    addToViewTable(product);
                }
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
                var product = answer.getValue();
                var rows = tableData.update(product);

                if (rows != 0)
                    updateItemInViewTable(product);
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
                var rows = tableData.delete(product);

                var pricesTable = new ProductPricesTable(
                        product.getPriceTableName());
                var isDeleted = pricesTable.deleteTable();
                if (rows != 0 && isDeleted)
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
            var pricesTableName = product.getPriceTableName();
            var pricesTable = new ProductPricesTable(pricesTableName);
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

    protected void updateItemInViewTable(Product product){
        List<ProductProperty> productProperties =
                tableView.getItems();
        for (ProductProperty property:
             productProperties) {
            var toComparison = property.getProduct();
            if (product.getId() == toComparison.getId()){
                Price price;
                try {
                var pricesTableName = product.getPriceTableName();
                var pricesTable = new ProductPricesTable(pricesTableName);
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
        List<ProductProperty> productProperties =
                tableView.getItems();
        ProductProperty productToDelete = null;
        for (ProductProperty property:
                productProperties) {
            var toComparison = property.getProduct();
            if (product.getId() == toComparison.getId()){
                productToDelete = property;
                break;
            }
        }

        if (productToDelete != null)
            productProperties.remove(productToDelete);
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
            return null;
        });
        return dialog;
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

            return null;
        });
        return dialog;
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
