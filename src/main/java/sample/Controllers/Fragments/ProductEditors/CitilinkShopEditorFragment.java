package sample.Controllers.Fragments.ProductEditors;

import sample.Controllers.Fragments.ProductEditorFragment;
import sample.ProductProxys.Parsers.CitilinkShopProductParser;
import sample.ProductProxys.ProductProxy;
import sample.Products.Product;

import java.io.IOException;
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

        checkFields();
        }

    @Override
    protected ProductProxy getProductProxy(String linkToProduct) throws IOException {
        return new CitilinkShopProductParser();
    }
}
