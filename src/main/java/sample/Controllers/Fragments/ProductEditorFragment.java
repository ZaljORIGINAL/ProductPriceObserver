package sample.Controllers.Fragments;

import sample.Databases.ProductPricesTable;
import sample.Products.ActualProduct;
import sample.Products.Price;
import sample.Products.Product;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public abstract class ProductEditorFragment extends ProductParamFragment{
    protected Product product;

    public ProductEditorFragment(Product product){
        this.product = product;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initView();

        //FIXME Требуется установить актуальный период тригера время
        try {
            linkField.setText(product.getLink());
            nameField.setText(product.getName());
            nameField.setDisable(false);
            choiceField.setDisable(false);
            var pricesTable =
                    new ProductPricesTable(product.getPriceTableName());
            var prices = pricesTable.getAll();
            for (Price price : prices) {
                priceTable.getItems().add(price);
            }
            priceTable.refresh();
            priceTable.setDisable(false);
        }catch (SQLException exception){
            //TODO Вывести лог ошибки.
        }
    }

    @Override
    public ActualProduct saveProduct() {
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

        product.setUrl(url);
        product.setName(productName);
        product.setTriggerPeriod(triggerPeriod);
        var price = priceTable.getItems().get(
                priceTable.getItems().size() - 1);

        return new ActualProduct(product, price);
    }
}
