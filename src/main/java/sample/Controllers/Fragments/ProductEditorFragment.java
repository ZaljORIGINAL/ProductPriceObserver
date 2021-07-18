package sample.Controllers.Fragments;

import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sample.Databases.ProductPricesTable;
import sample.Products.ActualProduct;
import sample.Products.Price;
import sample.Products.Product;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public abstract class ProductEditorFragment extends ProductParamFragment{
    protected static final Logger logger = LogManager.getLogger(ProductEditorFragment.class);

    protected Product product;

    public ProductEditorFragment(Product product){
        this.product = product;
        logger.info("Создан объект фрагмента редактора продукта. О продукте:\n" +
                "\t" + product.getName() + "\n" +
                "\t" + product.getLink() + "\n" +
                "\t" + product.getObserverPeriod());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initView();

        //FIXME Требуется установить актуальный период тригера время
        try {
            logger.info("Устновка данных в поля...");
            linkField.setText(product.getLink());
            logger.info("Установлена ссылка: " + linkField.getText());
            nameField.setText(product.getName());
            logger.info("Установлено имя: " + nameField.getText());
            nameField.setDisable(false);
            choiceField.setDisable(false);
            var pricesTable =
                    new ProductPricesTable(product.getPriceTableName());
            logger.info("Получена таблица цен: " + pricesTable.getTableName());
            var prices = pricesTable.getByProduct();
            logger.info("Получен список цен за все время: " + prices.size());
            for (Price price : prices) {
                priceTable.getItems().add(price);
            }
            priceTable.refresh();
            priceTable.setDisable(false);
            logger.info("Данные продукта успешно установлены в поля.");
        }catch (SQLException exception){
            logger.info("Ошибка при установке данных в поля редактора.", exception);
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("Ошибка");
            dialog.setHeaderText("Ошибка в установке данных");
            dialog.setContentText(exception.getMessage());
        }
    }

    @Override
    public ActualProduct saveProduct() {
        logger.info("Помещение изменений в объект...");
        String url = this.linkField.getText();
        String productName = this.nameField.getText();
        long triggerPeriod;
        switch (choiceField.getSelectionModel().getSelectedItem()){
            case "12 час":
                triggerPeriod = 43200000;
                break;
            case "24 час":
                triggerPeriod = 86400000;
                break;
            default:
                triggerPeriod = 3600000;
        }
        logger.info("Данные с полей получены. Помещение их в объект продукта.");
        product.setUrl(url);
        product.setName(productName);
        product.setObserverPeriod(triggerPeriod);

        var price = priceTable.getItems().get(
                priceTable.getItems().size() - 1);

        var actualProduct = new ActualProduct(product, price);
        logger.info("Изменения получены. Возвращение экземпляра класса ActualProduct: " + actualProduct);
        return actualProduct;
    }
}
