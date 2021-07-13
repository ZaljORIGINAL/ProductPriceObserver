package sample.ProductObserver;

import sample.Databases.ProductPricesTable;
import sample.ProductProxys.ProductProxy;
import sample.Products.Product;
import sample.ShopToolsFactories.ProductToolsFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class PriceObserver {

    protected long period;

    public PriceObserver(long period){
        this.period = period;
    }

    public void check(ProductToolsFactory tools, CountDownLatch finishSignal){
        try {
            var table = tools.getProductsTable();
            List<Product> products = table.getAll();
            var runnable = getRunnable(products, tools, finishSignal);
            new Thread(runnable).start();
        }catch (SQLException exception){
            //TODO Надо бы узнать какая именно ошибка.
        }
    }

    private Runnable getRunnable(List<Product> products, ProductToolsFactory tools, CountDownLatch finishSignal){
        return () -> {
            for (Product product: products) {
                try {
                    var priceTableName = product.getPriceTableName();
                    var priceTable = new ProductPricesTable(priceTableName);
                    ProductProxy productProxy = tools.getParser(
                            product.getLink());
                    var price = productProxy.getPrice();
                    priceTable.insert(price);
                    finishSignal.countDown();
                }catch (IOException | SQLException exception){
                    //TODO Прописать лог по записи информации о ошибка
                }
            }
        };
    }
}
