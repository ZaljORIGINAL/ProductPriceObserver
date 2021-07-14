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

    public void check(List<ProductToolsFactory> toolsFactories){
        var runnable = getRunnable(toolsFactories);
        new Thread(runnable).start();
    }

    private Runnable getRunnable(List<ProductToolsFactory> toolsFactories){
        return () -> {
            for (ProductToolsFactory toolsFactory : toolsFactories) {
                try {
                    var productTable = toolsFactory.getProductsTable();
                    var products = productTable.getByTrigger(period);
                    for (Product product : products) {
                        try {
                            var priceTableName = product.getPriceTableName();
                            var priceTable = new ProductPricesTable(priceTableName);
                            ProductProxy productProxy = toolsFactory.getParser(
                                    product.getLink());
                            var price = productProxy.getPrice();
                            priceTable.insert(price);
                        }catch (SQLException | IOException exception){
                            //TODO Записать лог об ошибке
                        }
                    }
                }catch (SQLException exception){
                    //TODO Записать лог об ошибке
                }
            }
        };
    }
}
