package sample.ProductObserver;

import sample.Databases.ProductsTable;
import sample.ProductProxys.Parsers.ProductParser;
import sample.ProductProxys.ShopApi.ProductApi;

import java.io.IOException;
import java.sql.SQLException;

public abstract class ProductToolsFactory {
    public abstract ProductsTable getProductsTable() throws SQLException;
    public abstract ProductParser getParser(String linkToProduct) throws IOException;
    public abstract ProductApi getShopApi();
}
