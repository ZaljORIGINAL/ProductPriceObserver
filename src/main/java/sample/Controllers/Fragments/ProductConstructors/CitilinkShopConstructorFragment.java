package sample.Controllers.Fragments.ProductConstructors;

import org.springframework.util.StringUtils;
import sample.Controllers.Fragments.ProductConstructorFragment;
import sample.Controllers.Fragments.ProductParamFragment;
import sample.ProductProxys.Parsers.CitilinkShopProductParser;
import sample.Products.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CitilinkShopConstructorFragment extends ProductConstructorFragment {
    public CitilinkShopConstructorFragment(){
        //TODO Инициализировать соответствующий ProductProxy
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
