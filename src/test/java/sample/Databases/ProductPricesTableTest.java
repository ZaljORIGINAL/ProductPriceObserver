package sample.Databases;

import org.junit.Test;
import sample.Products.Price;

import java.sql.SQLException;
import java.util.Calendar;

import static org.junit.Assert.*;

public class ProductPricesTableTest {

    @Test
    public void createTableTest() throws SQLException {
        ProductPricesTable table = new ProductPricesTable("prices3");
        table.openConnection();
        table.createTable();
        table.closeConnection();
    }

    @Test
    public void openConnectionTest() throws SQLException {
        ProductPricesTable table = new ProductPricesTable("prices3");
        var connection = table.openConnection();
        assertNotNull(connection);
    }

    @Test
    public void getById() throws SQLException {
        Calendar calendar = Calendar.getInstance();
        var price = new Price(calendar, 100.0f);
        ProductPricesTable table = new ProductPricesTable("prices3");
        table.openConnection();
        var priceFromTable = table.getById(2);
        Calendar date = priceFromTable.getDate();
        System.out.println("День: " + date.get(Calendar.DAY_OF_MONTH) + "\n" +
                "Год: " + date.get(Calendar.YEAR) + "\n" +
                "Час: " + date.get(Calendar.HOUR_OF_DAY) + "\n" +
                "Минута: " + date.get(Calendar.MINUTE));
        System.out.println(priceFromTable.getPrice());
    }

    @Test
    public void insert() throws SQLException {
        Calendar calendar = Calendar.getInstance();
        var price = new Price(calendar, 100.0f);
        ProductPricesTable table = new ProductPricesTable("prices3");
        table.openConnection();
        table.insert(price);
    }
}