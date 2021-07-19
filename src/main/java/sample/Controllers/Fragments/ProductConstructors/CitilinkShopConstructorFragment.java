package sample.Controllers.Fragments.ProductConstructors;

import sample.Controllers.Fragments.ProductConstructorFragment;
import sample.ProductProxys.Parsers.CitilinkShopProductParser;
import sample.ProductProxys.ProductProxy;
import sample.ShopToolsFactories.ShopToolsFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CitilinkShopConstructorFragment extends ProductConstructorFragment {
    public CitilinkShopConstructorFragment(ShopToolsFactory shopTools){
        super(shopTools);
    }
}
