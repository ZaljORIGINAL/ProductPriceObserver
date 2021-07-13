package sample.ShopToolsFactories.Factorys;

import sample.Databases.Contracts.ProductTableContract;
import sample.Databases.ProductsTable;
import sample.ProductProxys.Parsers.CitilinkShopProductParser;
import sample.ProductProxys.Parsers.ProductParser;
import sample.ProductProxys.ShopApi.ProductApi;
import sample.ShopToolsFactories.ProductToolsFactory;

import java.io.IOException;
import java.sql.SQLException;

public class CitilinkShopToolsFactory extends ProductToolsFactory {

    @Override
    public ProductsTable getProductsTable() throws SQLException {
        return new ProductsTable(ProductTableContract.CITILINK_TABLE);
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
