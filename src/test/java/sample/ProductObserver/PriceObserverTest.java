package sample.ProductObserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import sample.AppConfiguration;
import sample.Controllers.Fragments.MainWindow.Tables.ProductsTables.CitilinkProductTableView;
import sample.Databases.Contracts.ProductTableContract;
import sample.Databases.ProductsTable;
import sample.Main;
import sample.Products.Price;
import sample.Products.Product;
import sample.ShopToolsFactories.Factorys.CitilinkShopToolsFactory;
import sample.ShopToolsFactories.ShopToolsFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

public class PriceObserverTest {
    interface SomeCallback{
        void call();
    }

    public class NotifSubscriber implements PriceChangeListener{
        private SomeCallback task;
        public Map<Integer, Price> priceMap;

        public void setTask(SomeCallback callback){
            this.task = callback;
        }

        @Override
        public void notifOfPriceChanged(Map<Integer, Price> list, ShopToolsFactory shopTools) {
            this.priceMap = list;
            task.call();
        }
    }

    @Test
    public void getActualPriceTest() throws SQLException {
        var shopTools = new CitilinkShopToolsFactory();
        List<Product> productList = new ArrayList<>();

        Path resourceDirectory1 = Paths.get("src","test","resources", "ProductObserver", "ProductsPagesNew", "CitilinkProductPage1.html");
        String absolutePath1 = resourceDirectory1.toFile().getAbsolutePath();
        productList.add(
                new Product(1, shopTools.getShopId(), absolutePath1, "Name1", 3600000));
        Path resourceDirectory2 = Paths.get("src","test","resources", "ProductObserver", "ProductsPagesNew", "CitilinkProductPage2.html");
        String absolutePath2 = resourceDirectory2.toFile().getAbsolutePath();
        productList.add(
                new Product(2, shopTools.getShopId(), absolutePath2, "Name1", 3600000));
        Path resourceDirectory3 = Paths.get("src","test","resources", "ProductObserver", "ProductsPagesNew", "CitilinkProductPage3.html");
        String absolutePath3 = resourceDirectory3.toFile().getAbsolutePath();
        productList.add(
                new Product(3, shopTools.getShopId(), absolutePath3, "Name1", 3600000));

        var observer = new PriceObserver(
                360000,
                new ProductsTable("products_test"),
                null);

        var pricesMap = observer.getActualPrice(shopTools, productList);
        if (pricesMap.size() == 3){
            var price1 = pricesMap.get(1);
            if (price1.getPrice() != 1100f)
                fail();
            var price2 = pricesMap.get(2);
            if (price2.getPrice() != 2200f)
                fail();
            var price3 = pricesMap.get(3);
            if (price3.getPrice() != 3300f)
                fail();
        }else
            fail();
    }

    @Test
    public void checkTest_Notification() throws SQLException, InterruptedException {
        //Проверяющий объект. Будет ожидать уведомления от сервиса о окончании работы.
        CountDownLatch waiter = new CountDownLatch(1);
        var subscriber = new NotifSubscriber();

        //Потготовка проверяемого продукта и таблици
        var shopTools = new CitilinkShopToolsFactory();

        Path resourceDirectory1 = Paths.get("src","test","resources", "ProductObserver", "ProductsPagesNew", "CitilinkProductPage1.html");
        String absolutePath1 = resourceDirectory1.toFile().getAbsolutePath();
        var product1 =
                new Product(shopTools.getShopId(), absolutePath1, "Name1", 3600000);

        var productTable = new ProductsTable("products_test");
        var actualProduct = productTable.insert(product1);

        //Проверяющий блок
        var taskToCheck = new SomeCallback() {
            @Override
            public void call() {
                var priceMap = subscriber.priceMap;
                if (priceMap.size() != 0){
                    var price = priceMap.get(actualProduct.getIdProduct());
                    //1100 взято из файла CitilinkProductPage1.html.
                    if (price.getPrice() == 1100)
                        waiter.countDown();
                    else
                        fail();
                }else
                    fail();
            }
        };
        subscriber.setTask(taskToCheck);

        //Список объектов ожидающих обновления цены
        ArrayList<PriceChangeListener> subscribers =
                new ArrayList<>();
        subscribers.add(subscriber);

        var observer = new PriceObserver(
                3600000,
                productTable,
                subscribers);

        //Список магазинов на просмотр
        ArrayList<ShopToolsFactory> shopsTools =
                new ArrayList<>();
        shopsTools.add(shopTools);

        observer.start(shopsTools);
        waiter.await();
    }
}