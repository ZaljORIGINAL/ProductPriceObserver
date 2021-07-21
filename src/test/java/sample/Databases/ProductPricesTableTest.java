package sample.Databases;

import org.junit.Test;
import sample.Products.Price;
import sample.Products.Product;
import sample.ShopToolsFactories.Factorys.CitilinkShopToolsFactory;

import java.sql.SQLException;
import java.util.Calendar;

import static org.junit.Assert.*;

public class ProductPricesTableTest {

    /* FIXME Тесты не работают по следующей ошибке:
    *   ОШИБКА: INSERT или UPDATE в таблице "productprice_test" нарушает ограничение внешнего ключа "productprice_test_idproduct_fkey"
        Подробности: Ключ (idproduct)=(25) отсутствует в таблице "products*/

    public void insertTest() throws SQLException {
        //Потготовка продукта
        var productsTable = new ProductsTable("products_test");
        var shopTools = new CitilinkShopToolsFactory();
        var product = new Product(
                shopTools.getShopId(),
                "link", "name", 1000);
        var actualProduct = productsTable.insert(product);

        //Потготовка цены
        var price = new Price(
                Calendar.getInstance(),
                14f);

        //Исполнение действия
        var priceTable = new ProductPricesTable("productprice_test");
        var actualPrice = priceTable.insert(actualProduct.getIdProduct(), price);

        //Проверка
        if (actualPrice.getIdPrice() != -1){
            if (actualPrice.getCalendar().getTimeInMillis() != price.getCalendar().getTimeInMillis())
                fail();
            if (actualPrice.getPrice() != price.getPrice())
                fail();
        }else
            fail();
    }

    public void getByProductTest(){

    }
}