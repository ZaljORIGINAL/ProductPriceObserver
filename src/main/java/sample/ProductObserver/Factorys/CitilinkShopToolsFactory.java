package sample.ProductObserver.Factorys;

import sample.Databases.ProductsTable;
import sample.Databases.ShopsDatabase.TableCitilinkShop;
import sample.ProductObserver.ProductToolsFactory;
import sample.ProductProxys.Parsers.CitilinkShopProductParser;
import sample.ProductProxys.Parsers.ProductParser;
import sample.ProductProxys.ShopApi.ProductApi;

import java.io.IOException;
import java.sql.SQLException;

public class CitilinkShopToolsFactory extends ProductToolsFactory {

    @Override
    public ProductsTable getProductsTable() throws SQLException {
        return new TableCitilinkShop();
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
