package sample.Databases;

import org.junit.Test;
import sample.Databases.ShopsDatabase.TableCitilinkShop;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class ProductsTableTest {
    @Test
    public void getConnectionTest() throws SQLException {
        TableCitilinkShop tableCitilinkShop =
                new TableCitilinkShop();
        Connection co;
        co = tableCitilinkShop.openConnection();


        assertNotNull(co);
    }
}