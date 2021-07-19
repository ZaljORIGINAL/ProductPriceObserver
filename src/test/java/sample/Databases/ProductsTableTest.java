package sample.Databases;

import org.junit.Test;
import sample.Products.Product;
import sample.ShopToolsFactories.Factorys.CitilinkShopToolsFactory;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class ProductsTableTest {

    @Test
    public void insertTest() throws SQLException{
        var table = new ProductsTable("products_test");

        var toolsFactory = new CitilinkShopToolsFactory();
        var product = new Product(
                toolsFactory.getShopId(),
                "url",
                "product",
                3600000);
        var result = table.insert(product);

        if (result.getIdProduct() == -1)
            fail();
        if (result.getIdShop() != product.getIdShop())
            fail();
        if (!result.getLink().equals(product.getLink()))
            fail();
        if (!result.getName().equals(product.getName()))
            fail();
        if (result.getObserverPeriod() != product.getObserverPeriod())
            fail();
    }

    @Test
    public void deleteTest() throws SQLException{
        var table = new ProductsTable("products_test");

        var toolsFactory = new CitilinkShopToolsFactory();
        var product = new Product(
                toolsFactory.getShopId(),
                "url",
                "product",
                3600000);
        var registeredProduct = table.insert(product);
        var deleted = table.delete(registeredProduct.getIdProduct());

        assertEquals(deleted, 1);
    }

    @Test
    public void updateTest() throws SQLException{
        var table = new ProductsTable("products_test");

        var toolsFactory = new CitilinkShopToolsFactory();
        var product = new Product(
                toolsFactory.getShopId(),
                "url",
                "product",
                3600000);
        var registeredProduct = table.insert(product);
        registeredProduct.setName("New name");
        registeredProduct.setName("New link");
        var changed = table.update(registeredProduct);
        var result = table.getById(registeredProduct.getIdProduct());

        if (changed == 0)
            fail();
        if (result.getIdShop() != registeredProduct.getIdShop())
            fail();
        if (!result.getLink().equals(registeredProduct.getLink()))
            fail();
        if (!result.getName().equals(registeredProduct.getName()))
            fail();
        if (result.getObserverPeriod() != registeredProduct.getObserverPeriod())
            fail();
    }
}