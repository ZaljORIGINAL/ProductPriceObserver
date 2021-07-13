package sample.Controllers.Fragments;

import sample.Products.ActualProduct;
import sample.Products.Product;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class ProductConstructorFragment extends ProductParamFragment {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initView();
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

        var product = new Product(url, productName, triggerPeriod);
        var price = priceTable.getItems().get(
                priceTable.getItems().size() - 1);

        return new ActualProduct(product, price);
    }
}
