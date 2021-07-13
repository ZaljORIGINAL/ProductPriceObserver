package sample.Controllers.Fragments;

import sample.Products.Product;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class ProductEditorFragment extends ProductParamFragment{
    protected Product product;

    public ProductEditorFragment(Product product){
        this.product = product;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTriggerSelectBox();

        //FIXME Требуется установить актуальный период тригера время
        linkField.setText(product.getLink());
        nameField.setText(product.getName());
    }

    @Override
    public Product saveProduct() {
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
        return product;
    }
}
