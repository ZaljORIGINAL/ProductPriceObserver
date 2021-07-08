package sample.Controllers.Fragments;

import sample.Products.Product;

public abstract class ProductConstructorFragment extends ProductParamFragment {
    @Override
    public Product saveProduct() {
        String url = this.url.getText();
        String productName = this.name.getText();
        String triggerPeriod;
        switch (timeToTrigger.getSelectionModel().getSelectedItem()){
            case "1 час":
                triggerPeriod = "1";
                break;
            case "12 час":
                triggerPeriod = "12";
                break;
            case "24 час":
                triggerPeriod = "24";
                break;
            default:
                triggerPeriod = "-1";
        }

        Product product = new Product(url, productName, triggerPeriod);
        return product;
    }
}
