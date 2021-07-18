package sample.Controllers.Fragments.MainWindow.Tables;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sample.Controllers.Fragments.ProductConstructorFragment;
import sample.Controllers.Fragments.ProductEditorFragment;
import sample.Controllers.Fragments.ViewFragment;
import sample.Databases.ProductPricesTable;
import sample.Databases.ProductsTable;
import sample.Products.Product;
import sample.Products.ActualProduct;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public abstract class ProductTableView extends ViewFragment {
    private static final Logger logger = LogManager.getLogger(ProductTableView.class);
    public TableView<ActualProduct> tableView;
    public Button tableUpdate;
    protected ProductsTable tableData;

    private TableColumn<ActualProduct, String> nameColumn;
    private TableColumn<ActualProduct, String> priceColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("Инициализация граффических элементов фрагмента...");
        initTableUpdateButton();
        initTableView();
        logger.info("Инициализация граффических элементов фрагмента завершена");
    }

    @Override
    public String getPathToFXML() {
        return "/fragments/fragment_TableView_OneShop.fxml";
    }

    public void updateTable(){
        logger.info("Полное обновление данных в таблице...");
        tableUpdate.setDisable(true);
        try{
            var products = tableData.getAll();
            logger.info("Получен список продуктов из базы данных: " + products.size());

            tableView.getItems().clear();
            logger.info("Графическая таблица очищена.");

            for (Product product : products) {
                logger.info("Обработка продукта, его id: " + product.getIdProduct());
                try {
                    var pricesTable =
                            new ProductPricesTable(
                                    product.getPriceTableName());
                    logger.info("Получена тыблица цен продукта: " + pricesTable.getTableName());
                    var lastPrice = pricesTable.getLastPrice();
                    logger.info("Получена последняя цена продукта: " + lastPrice.getPrice());
                    var actualProduct = new ActualProduct(
                            product,
                            lastPrice);
                    addToViewTable(actualProduct);
                }catch (SQLException exception){
                    logger.info("Ошибка при получении последней цены по продукту.", exception);
                }
            }
        }catch (SQLException exception){
            logger.info("Ошибка при получении списка продуктов магазина из базы данных", exception);
            Alert messageDialog =
                    new Alert(Alert.AlertType.ERROR);
            messageDialog.setTitle("Ошибка");
            messageDialog.setHeaderText("Ошибка при работе с базой данных");
            messageDialog.setContentText(exception.getMessage());
        }

        tableUpdate.setDisable(false);
    }

    public void callProductConstructorDialog(){
        logger.info("Вызов диалога: конструктор продукта");
        Dialog dialog = buildConstructorDialog(
                getFragmentToConstructor());
        logger.info("Получен объект диалогового окня: " + dialog);
        Optional<Pair<String, ActualProduct>> result
                = dialog.showAndWait();
        logger.info("Получен ответ из диалогового окна");

        result.ifPresent(answer->{
            try{
                var actualProduct = answer.getValue();
                logger.info("Получена актуальная информация о продукте.");
                var product = tableData.insert(actualProduct.getProduct());
                if (product.getIdProduct() != -1){
                    logger.info("Получен объект продукта зарегестрированного в системе. ");
                    var priceTable = new ProductPricesTable(product.getPriceTableName());
                    logger.info("Получена таблица цен продукта.");
                    var price = priceTable.insert(actualProduct.getPrice());
                    if (price.getIdPrice() != -1)
                        logger.info("Актуальная цена продукта сохранена");
                    else
                        logger.warn("Неудалось сохранить цену.");
                    addToViewTable(new ActualProduct(product, price));
                }else {
                    logger.warn("Неудалось зарегестрировать продукт в системе.");
                }
            }catch (SQLException exception){
                logger.error("Ошибка при сохранении нового продукта.", exception);
                Alert messageDialog =
                        new Alert(Alert.AlertType.ERROR);
                messageDialog.setTitle("Ошибка");
                messageDialog.setHeaderText("Ошибка в базе данных");
                messageDialog.setContentText(exception.getMessage());
                messageDialog.showAndWait();
            }
        });
    }

    public void callProductEditorDialog(){
        logger.info("Вызов диалога: редактора продукта");
        Dialog dialog = buildEditorDialog(
                getFragmentToEditor());
        logger.info("Получен объект диалогового окня: " + dialog);
        Optional<Pair<String, ActualProduct>> result =
                dialog.showAndWait();
        logger.info("Получен ответ из диалогового окна");

        result.ifPresent(answer->{
            try {
                var actualProduct = answer.getValue();
                logger.info("Получена актуальная информация о продукте.");

                var rows = tableData.update(
                        actualProduct.getProduct());
                logger.info("Количество изменненых строк: " + rows);
                if (rows != 0){
                    tableView.refresh();
                    logger.info("Таблица обнавлена.");
                }
            }catch (SQLException exception){
                logger.error("Ошибка при сохранении изменений продукта.", exception);
                Alert messageDialog =
                        new Alert(Alert.AlertType.ERROR);
                messageDialog.setTitle("Ошибка");
                messageDialog.setHeaderText("Ошибка в базе данных");
                messageDialog.setContentText(exception.getMessage());
                messageDialog.showAndWait();
            }
        });
    }

    public void callProductDeleteDialog(){
        logger.info("Вызов диалога: удаление продукта");
        var itemToDelete
                = tableView.getSelectionModel().getSelectedItem();
        var product = itemToDelete.getProduct();

        Dialog dialog = buildDeleteDialog(product);
        logger.info("Получен объект диалогового окня: " + dialog);

        Optional<ButtonType> option = dialog.showAndWait();
        if (option.get() == ButtonType.OK){
            logger.info("На вопрос удаления продукта ответ положителен.");
            try {
                var pricesTable = new ProductPricesTable(
                        product.getPriceTableName());
                logger.info("Получена таблица цен продукта.");
                if (pricesTable.deleteTable()){
                    logger.info("Таблица цен успешно удалена.");

                    var rows = tableData.delete(product);
                    logger.info("Количество удаленных продуктов: " + rows);

                    if (rows != 0){
                        deleteItemFromViewTable(itemToDelete);
                    }
                }else
                    logger.warn("Ошибка в удалении таблици цен продукта.");
            }catch (SQLException exception){
                logger.error("Ошибка при удалении продукта.", exception);
                Alert messageDialog =
                        new Alert(Alert.AlertType.ERROR);
                messageDialog.setTitle("Ошибка");
                messageDialog.setHeaderText("Ошибка в базе данных");
                messageDialog.setContentText(exception.getMessage());
                messageDialog.showAndWait();
            }
        }
    }

    protected void initTableUpdateButton(){
        tableUpdate.setOnAction(actionEvent -> updateTable());
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

        updateTable();
    }

    protected abstract ProductConstructorFragment getFragmentToConstructor();

    protected abstract ProductEditorFragment getFragmentToEditor();

    protected void addToViewTable(ActualProduct actualProduct){
        tableView.getItems().add(actualProduct);
        logger.info("В граффическую таблицу добавлен элемент. Id добавленного продукта: " + actualProduct.getProduct().getIdProduct());
    }

    protected void deleteItemFromViewTable(ActualProduct item){
        logger.info("Удаление элемента из граффической таблици...");
        List<ActualProduct> viewTableItems =
                tableView.getItems();
        viewTableItems.remove(item);
        logger.info("Id удаленного продукта из графической таблици: " + item.getProduct().getIdProduct());
    }

    protected Dialog buildConstructorDialog(ProductConstructorFragment fragment){
        logger.info("Формирование диалогового окна конструктора продукта...");
        Dialog<Pair<String, ActualProduct>> dialog =
                new Dialog<>();
        dialog.setTitle("Конструктор продукта");
        dialog.setHeaderText("Задайте параметры продукта");

        fragment.initFragmentView();
        dialog.getDialogPane().setContent(
                fragment.getMainPanel());
        logger.info("В диалоговое окно помещен интерфейс конструктора.");

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
                logger.info("Дана команда на сохранение нового продукта...");
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
        logger.info("Формирование диалогового окна редактора продукта...");
        Dialog<Pair<String, ActualProduct>> dialog = new Dialog<>();
        dialog.setTitle("Редактор продукта");
        dialog.setHeaderText("Параметры продукта");

        fragment.initFragmentView();
        dialog.getDialogPane().setContent(
                fragment.getMainPanel());
        logger.info("В диалоговое окно помещен интерфейс конструктора.");

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
                logger.info("Дана команда на сохранение изменений продукта...");
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
        logger.info("Формирование диалогового окна редактора продукта...");
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Уадление записи");
        dialog.setHeaderText("Удалить отслежуемый продукт?");
        dialog.setContentText("Информация о продукте: " +
                "\nНаименование: " + product.getName() +
                "\nСсылка: " + product.getLink());

        return dialog;
    }
}
