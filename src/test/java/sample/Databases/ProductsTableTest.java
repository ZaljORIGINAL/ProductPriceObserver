package sample.Databases;

import org.junit.Test;
import sample.Products.Product;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class ProductsTableTest {
    String tableName = "test_table";

    @Test
    public void insert_HaveNotTable() throws SQLException {
        var productTable = new ProductsTable(tableName);

        var product = new Product("local_url", "cpu", 10);
        var newProduct = productTable.insert(product);
        newProduct.getIdProduct();

        assertNotEquals(newProduct, -1);
    }
}