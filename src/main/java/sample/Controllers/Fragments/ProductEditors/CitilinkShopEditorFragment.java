package sample.Controllers.Fragments.ProductEditors;

import sample.Controllers.Fragments.ProductEditorFragment;
import sample.Controllers.Fragments.ProductParamFragment;
import sample.Products.Product;

import java.net.URL;
import java.util.ResourceBundle;

public class CitilinkShopEditorFragment extends ProductEditorFragment {
    public CitilinkShopEditorFragment(Product product){
        super(product);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.url.setText(product.getLink());
        this.name.setText(product.getName());
        initTimeToTriggerBox();
    }
}
