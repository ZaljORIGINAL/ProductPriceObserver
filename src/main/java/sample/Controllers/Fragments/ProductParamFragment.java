package sample.Controllers.Fragments;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;
import sample.ProductProxys.ProductProxy;
import sample.Products.ActualProduct;
import sample.Products.Price;
import sample.ShopToolsFactories.ShopToolsFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class ProductParamFragment extends ViewFragment {
    protected static final Logger logger = LogManager.getLogger(ProductParamFragment.class);

    public TextField linkField;
    public Button checkLinkButton;
    public TextField nameField;
    public ChoiceBox<String> choiceField;
    public TableView<Price> priceTable;
    public TableColumn<Price, String> dateColumn;
    public TableColumn<Price, String> priceColumn;

    protected ShopToolsFactory shopTools;

    public ProductParamFragment(ShopToolsFactory shopTools){
        this.shopTools = shopTools;
    }

    @Override
    public String getPathToFXML() {
        String pathToFxml = "/fragments/fragment_productParams.fxml";
        return pathToFxml;
    }

    public boolean checkFields(){
        logger.info("Запущена проверка полей...");
        var answer = true;

        var productProxy = checkUrlField();
        if (productProxy != null){
            checkNameField(productProxy);
        }else {
            answer = false;
        }

        logger.info("Результат проверки полей: " + answer);
        return answer;
    }

    public abstract ActualProduct saveProduct();

    protected ProductProxy getProductProxy(String linkToProduct) throws IOException{
        return shopTools.getParser(linkToProduct);
    }

    protected void initView(){
        logger.info("Инициализация граффических элементов фрагмента...");
        initCheckLinkFieldButton();
        initNameField();
        initTriggerSelectBox();
        initPricesTable();
        logger.info("Инициализация граффических элементов фрагмента завершена");
    }

    protected void initCheckLinkFieldButton(){
        checkLinkButton.setOnAction(actionEvent -> {
            logger.info("Запрошены актуальные данные со страници магазина.");
            var productProxy = checkUrlField();
            if (productProxy != null){
                logger.info("Получен proxy продукта.");

                String productName = productProxy.getName();
                logger.info("Полученное наименование продукта с сайта: " + productName);
                var priceValue = productProxy.getPrice();
                var price = new Price(
                        shopTools.getShopId(),
                        Calendar.getInstance(),
                        priceValue);
                logger.info("Получена цена продукта с сайта: " + price.getPrice());

                nameField.setText(productName);
                nameField.setDisable(false);
                logger.info("Наименование продукта установлено: " + nameField.getText() + "\n" +
                        "Поле ввода разблокировано.");

                choiceField.setDisable(false);
                logger.info("Элемент для выбора периода проверки уены разблокировано.");

                priceTable.getItems().add(price);
                priceTable.refresh();
                priceTable.setDisable(false);
                logger.info("Цена добавлена в графичяескую таблицу: " + priceTable.getItems().size() +
                        "Графическая таблица цен разблокирована.");
            }else{
                logger.info("Неудалось получить proxy продукта. Данные с сайта не будут получены!");
            }
        });
    }

    protected void initNameField(){
        nameField.setDisable(true);
    }

    protected void initTriggerSelectBox(){
        String[] rows = new String[]{
                "1 час",
                "12 час",
                "24 час",
        };
        ObservableList<String> items =
                FXCollections.observableArrayList(rows);
        choiceField.setItems(items);
        choiceField.setValue(rows[0]);

        choiceField.setDisable(true);
    }

    protected void initPricesTable(){
        dateColumn = new TableColumn<>("Дата и время");
        dateColumn.setCellValueFactory(
                c -> {
                    String pattern = "H:MM d/MMM/yyyy";
                    var dateFormatter = new SimpleDateFormat(pattern);
                    var date = new Date(c.getValue().getCalendar().getTimeInMillis());
                    return new SimpleStringProperty(dateFormatter.format(date));
                });
        priceTable.getColumns().add(dateColumn);

        priceColumn = new TableColumn<>("Цена");
        priceColumn.setCellValueFactory(
                c -> new SimpleStringProperty(
                        String.valueOf(c.getValue().getPrice())));
        priceTable.getColumns().add(priceColumn);

        priceTable.setDisable(true);
    }

    protected ProductProxy checkUrlField(){
        logger.info("Проверка поля ссылки...");
        String linkToProduct = linkField.getText();
        linkToProduct = StringUtils.trimAllWhitespace(linkToProduct);
        logger.info("Содержимое поля ссылки: " + linkToProduct);
        ProductProxy productProxy = null;
        if (StringUtils.hasText(linkToProduct)) {
            try {
                logger.info("Ссылка получена.");
                logger.info("Запрос на соединение...");
                productProxy = getProductProxy(linkToProduct);
                linkField.setStyle("-fx-background-color: #c1ffb2");
                logger.info("Соединение установлено! Ссылка верна.");
            }catch (IOException exception) {
                logger.error("Неудалось получить соединение с сайтом.", exception);
            }
        }else {
            logger.info("Поле ссылки не содержит ресурс!");
        }

        choiceField.setStyle("-fx-background-color: #FDB7B7;");
        return productProxy;
    }

    protected boolean checkNameField(ProductProxy productProxy){
        logger.info("Проверка поля имени...");
        String productName = nameField.getText();
        logger.info("Содержимое поля имени: " + productName);
        if (!StringUtils.hasText(productName)) {
            logger.info("Поле имени пустое. Получение наименования товара с сайта...");
            String nameFromPage = productProxy.getName();
            logger.info("Полученное наименование с сайта: " + nameFromPage);
            nameField.setText(nameFromPage);
        }

        nameField.setStyle("-fx-background-color: #c1ffb2");
        return true;
    }
}
