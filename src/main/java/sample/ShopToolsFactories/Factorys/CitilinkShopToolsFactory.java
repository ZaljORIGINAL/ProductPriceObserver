package sample.ShopToolsFactories.Factorys;

import sample.ProductProxys.Parsers.CitilinkShopProductParser;
import sample.ProductProxys.Parsers.ProductParser;
import sample.ProductProxys.ShopApi.ProductApi;
import sample.ShopToolsFactories.ShopToolsFactory;

import java.io.IOException;

public class CitilinkShopToolsFactory extends ShopToolsFactory {

    @Override
    public int getShopId() {
        return 1;
    }

    @Override
    public ProductParser getParser(String linkToProduct) throws IOException {
        return new CitilinkShopProductParser(linkToProduct);
    }

    @Override
    public ProductApi getShopApi() {
        return null;
    }
}
