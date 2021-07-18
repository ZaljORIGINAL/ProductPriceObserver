package sample.ProductObserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sample.Databases.ProductPricesTable;
import sample.ProductProxys.ProductProxy;
import sample.Products.Product;
import sample.ShopToolsFactories.ProductToolsFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

//TODO Надо как то найти способ понятного вывода логов при многопатоке.
public class PriceObserver {
    private final static Logger logger = LogManager.getLogger(PriceObserver.class);

    private final long period;

    public PriceObserver(long period){
        this.period = period;
        logger.info("Создан экземпляр класса PriceObserver. Обработает товары с периодом обновления: " + period + " мс.");
    }

    public void check(List<ProductToolsFactory> toolsFactories){
        logger.info("Запущено обновление товаров (период обновления: " + period + " мс.)");
        var runnable = getRunnable(toolsFactories);
        new Thread(runnable).start();
        logger.info("Запусчен поток на обновление цен.");
    }

    private Runnable getRunnable(List<ProductToolsFactory> toolsFactories){
        return () -> {
            for (ProductToolsFactory toolsFactory : toolsFactories) {
                try {
                    var productTable = toolsFactory.getProductsTable();
                    logger.info("Получена таблица продуктов: " + productTable.getTableName());
                    var products = productTable.getByTrigger(, period, );
                    logger.info("Количество продуктов на обновление: " + products.size());
                    for (Product product : products) {
                        try {
                            var priceTableName = product.getPriceTableName();
                            var priceTable = new ProductPricesTable(priceTableName);
                            logger.info("Получена таблица цен: " + priceTable.getTableName());
                            ProductProxy productProxy = toolsFactory.getParser(
                                    product.getLink());
                            logger.info("Получен парсер по продукту.\n" +
                                    "\tНаименование продукта: " + product.getName() + "\n" +
                                    "\tСсылка на продукт: " + product.getLink());
                            var price = productProxy.getPrice();
                            logger.info("Получена новая цена: " + price.getPrice());
                            priceTable.insert(price);
                        }catch (SQLException | IOException exception){
                            logger.error("Ошибка в обновлении цены продукта:\n" +
                                    "\tНаименование продукта: " + product.getName() + "\n" +
                                    "\tСсылка на продукт: " + product.getLink(), exception);
                        }
                    }
                }catch (SQLException exception){
                    logger.error("Ошибка при работе с таблицей продуктов магазина.", exception);
                }
            }
        };
    }
}
