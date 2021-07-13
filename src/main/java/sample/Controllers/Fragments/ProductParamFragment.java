package sample.Controllers.Fragments;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import org.springframework.util.StringUtils;
import sample.ProductProxys.ProductProxy;
import sample.Products.ActualProduct;
import sample.Products.Price;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class ProductParamFragment extends ViewFragment {
    public TextField linkField;
    public Button checkLinkButton;
    public TextField nameField;
    public ChoiceBox<String> choiceField;
    public TableView<Price> priceTable;
    public TableColumn<Price, String> dateColumn;
    public TableColumn<Price, String> priceColumn;

    @Override
    public String getPathToFXML() {
        String pathToFxml = "/fragments/fragment_productParams.fxml";
        return pathToFxml;
    }

    public boolean checkFields(){
        var answer = true;

        var productProxy = checkUrlField();
        if (productProxy != null){
            checkNameField(productProxy);
            return answer;
        }else {
            return false;
        }
    }

    public abstract ActualProduct saveProduct();

    protected abstract ProductProxy getProductProxy(String linkToProduct) throws IOException;

    protected void initView(){
        initCheckLinkFieldButton();
        initNameField();
        initTriggerSelectBox();
        initPricesTable();
    }

    protected void initCheckLinkFieldButton(){
        checkLinkButton.setOnAction(actionEvent -> {
            var productProxy = checkUrlField();
            if (productProxy != null){
                String productName = productProxy.getName();
                Price price = productProxy.getPrice();

                nameField.setText(productName);
                nameField.setDisable(false);

                choiceField.setDisable(false);

                priceTable.getItems().add(price);
                priceTable.refresh();
                priceTable.setDisable(false);
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
        String linkToProduct = linkField.getText();
        linkToProduct = StringUtils.trimAllWhitespace(linkToProduct);
        if (StringUtils.hasText(linkToProduct)) {
            try {
                var productProxy = getProductProxy(linkToProduct);
                linkField.setStyle("-fx-background-color: #c1ffb2");
                return productProxy;
            }catch (IOException exception) {
                //TODO Лог об ошибке
            }
        }

        choiceField.setStyle("-fx-background-color: #FDB7B7;");
        return null;
    }

    protected boolean checkNameField(ProductProxy productProxy){
        String productName = nameField.getText();
        if (!StringUtils.hasText(productName)) {
            String nameFromPage = productProxy.getName();
            nameField.setText(nameFromPage);
        }

        nameField.setStyle("-fx-background-color: #c1ffb2");
        return true;
    }
}
