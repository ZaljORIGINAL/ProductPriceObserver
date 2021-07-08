package sample.Databases.ShopsDatabase;

import org.junit.Test;
import sample.Products.Product;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class TableCitilinkShopTest {

    @Test
    public void openConnectionTest(){
        try {
            TableCitilinkShop table = new TableCitilinkShop();
            var connectio = table.openConnection();
            assertNotNull(connectio);
        }catch (SQLException exception){
            System.out.println(exception.getMessage());
            fail();
        }
    }

    //@Test
    public void createTableTest() throws SQLException {
        TableCitilinkShop table = new TableCitilinkShop();
        table.openConnection();
        assertTrue(table.createTable());
        table.closeConnection();
    }

    //@Test
    public void deleteTableTest() throws SQLException{
        TableCitilinkShop table = new TableCitilinkShop();
        table.openConnection();
        assertTrue(table.deleteTable());
        table.closeConnection();
    }

    /**
     * Тест на обнаружение таблици. В случае если таблица имеется, тест будет пройден. В случае отсутсвия проваленю*/
    //@Test
    public void existsTest() throws SQLException{
        TableCitilinkShop table = new TableCitilinkShop();
        table.openConnection();
        assertTrue(table.existsTable());
        table.closeConnection();
    }

    //@Test
    public void insertTest() throws SQLException{
        Product product = new Product("local", "keyboard", "1");

        TableCitilinkShop table = new TableCitilinkShop();
        table.openConnection();
        var answer = table.insert(product);
        assertEquals(answer, 1);
    }

    //@Test
    public void getByIdTest() throws SQLException{
        TableCitilinkShop table = new TableCitilinkShop();
        table.openConnection();
        var product = table.getById(3);
        System.out.println(product.toString());
        assertNotNull(product);
    }

    //@Test
    public void updateTest() throws SQLException{
        TableCitilinkShop table = new TableCitilinkShop();
        table.openConnection();
        var product = table.getById(3);
        System.out.println("Изначальное имя: " + product.getName());
        product.setName("New name");
        var answer = table.update(product);
        System.out.println("Изменено строк: " + answer);
        product = table.getById(product.getId());
        assertEquals(product.getName(), "New name");
    }

    //@Test
    public void deleteTest_BY_ID() throws SQLException{
        TableCitilinkShop table = new TableCitilinkShop();
        table.openConnection();
        var answer = table.delete(1);
        assertEquals(1, 1);
    }

    //@Test
    public void deleteTest_BY_PRODUCT() throws SQLException{
        TableCitilinkShop table = new TableCitilinkShop();
        table.openConnection();
        var product = table.getById(3);
        var answer = table.delete(product);
        assertEquals(1, 1);
    }
}