package sample.Controllers.Fragments.ProductConstructors;

import sample.Controllers.Fragments.ProductConstructorFragment;
import sample.ProductProxys.Parsers.CitilinkShopProductParser;
import sample.ProductProxys.ProductProxy;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CitilinkShopConstructorFragment extends ProductConstructorFragment {
    public CitilinkShopConstructorFragment(){
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @Override
    protected ProductProxy getProductProxy(String linkToProduct) throws IOException {
        return new CitilinkShopProductParser();
    }
}
