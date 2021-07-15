package sample.Controllers.Fragments.MainWindow.Tables;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.util.Pair;
import sample.Controllers.Fragments.ProductConstructorFragment;
import sample.Controllers.Fragments.ProductEditorFragment;
import sample.Controllers.Fragments.ViewFragment;
import sample.Databases.ProductPricesTable;
import sample.Databases.ProductsTable;
import sample.Products.Price;
import sample.Products.Product;
import sample.Products.ActualProduct;

import java.net.URL;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public abstract class ProductTableView extends ViewFragment {
    public TableView<ActualProduct> tableView;
    public Button tableUpdate;
    protected ProductsTable tableData;

    private TableColumn<ActualProduct, String> nameColumn;
    private TableColumn<ActualProduct, String> priceColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTableUpdateButton();
        initTableView();
    }

    @Override
    public String getPathToFXML() {
        return "/fragments/fragment_TableView_OneShop.fxml";
    }

    public void updateTable() throws SQLException{
        tableUpdate.setDisable(true);
        var products = tableData.getAll();

        tableView.getItems().clear();

        for (Product product : products) {
            try {
                var pricesTable =
                        new ProductPricesTable(
                                product.getPriceTableName());
                var lastPrice = pricesTable.getLastPrice();
                var actualProduct = new ActualProduct(
                        product,
                        lastPrice);
                addToViewTable(actualProduct);
            }catch (SQLException exception){
                //TODO Записать лог о неудачи считывания цены
            }
        }
        tableUpdate.setDisable(false);
    }

    public void callProductConstructorDialog(){
        Dialog dialog = buildConstructorDialog(
                getFragmentToConstructor());
        Optional<Pair<String, ActualProduct>> result
                = dialog.showAndWait();
        result.ifPresent(answer->{
            try{
                var actualProduct = answer.getValue();
                var product = tableData.insert(actualProduct.getProduct());
                var priceTable = new ProductPricesTable(product.getPriceTableName());
                var price = priceTable.insert(actualProduct.getPrice());

                if (product.getId() != -1){
                    addToViewTable(new ActualProduct(product, price));
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
        Optional<Pair<String, ActualProduct>> result =
                dialog.showAndWait();
        result.ifPresent(answer->{
            try {
                var actualProduct = answer.getValue();
                var rows = tableData.update(
                        actualProduct.getProduct());

                if (rows != 0)
                    tableView.refresh();
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

    protected void initTableView() {
        nameColumn = new TableColumn<>("Товар");
        nameColumn.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getProduct().getName()));
        tableView.getColumns().add(nameColumn);

        priceColumn = new TableColumn<>("Последняя цена");
        priceColumn.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getPrice().getPrice())));
        tableView.getColumns().add(priceColumn);

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
    }

    protected abstract ProductConstructorFragment getFragmentToConstructor();

    protected abstract ProductEditorFragment getFragmentToEditor();

    protected void addToViewTable(ActualProduct actualProduct){
        tableView.getItems().add(actualProduct);
    }

    protected void updateItemInViewTable(ActualProduct actualProduct){
        List<ActualProduct> items =
                tableView.getItems();
        for (ActualProduct property:
             items) {
            var toComparison = property.getProduct();
            if (actualProduct.getProduct().getId() == toComparison.getId()){
                items.set(toComparison.getId(), actualProduct);
                tableView.refresh();
                return;
            }
        }
    }

    protected void deleteItemFromViewTable(Product product){
        List<ActualProduct> productProperties =
                tableView.getItems();
        ActualProduct productToDelete = null;
        for (ActualProduct property:
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
        Dialog<Pair<String, ActualProduct>> dialog =
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
        Dialog<Pair<String, ActualProduct>> dialog = new Dialog<>();
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
