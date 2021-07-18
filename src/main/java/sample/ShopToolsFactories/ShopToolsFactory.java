package sample.ShopToolsFactories;

import sample.Databases.ProductsTable;
import sample.ProductProxys.Parsers.ProductParser;
import sample.ProductProxys.ShopApi.ProductApi;

import java.io.IOException;
import java.sql.SQLException;

public abstract class ShopToolsFactory {

    public abstract int getShopId();
    public abstract ProductParser getParser(String linkToProduct) throws IOException;
    public abstract ProductApi getShopApi();
}
