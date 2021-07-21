package sample.ProductObserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import sample.Databases.ProductsTable;
import sample.Products.Price;
import sample.Products.Product;
import sample.ShopToolsFactories.ShopToolsFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

//TODO Надо как то найти способ понятного вывода логов при многопатоке.
public class PriceObserver {
    private final static Logger logger = LogManager.getLogger(PriceObserver.class);

    private final long period;
    private final ProductsTable productsTable;
    private List<PriceChangeListener> priceChangeListenerList;

    public PriceObserver(long period,
                         ProductsTable productsTable,
                         List<PriceChangeListener> priceChangeListenerList){
        this.period = period;
        this.productsTable = productsTable;
        this.priceChangeListenerList = priceChangeListenerList;

        logger.info("Создан экземпляр класса PriceObserver. Обработает товары с периодом обновления: " + period + " мс.");
    }

    public void start(List<ShopToolsFactory> toolsFactories){
        logger.info("Запщено обновление товаров (период обновления: " + period + " мс.)");
        var runnable = getRunnable(toolsFactories);
        new Thread(runnable).start();
        logger.info("Запущен поток на обновление цен.");
    }

    private Runnable getRunnable(List<ShopToolsFactory> shopTools){
        return () -> check(shopTools);
    }

    private void check(List<ShopToolsFactory> shopsTools){
        logger.info("Получена таблица продуктов: " + productsTable.getTableName());
        for (ShopToolsFactory shopTools : shopsTools){
            try{
                var products = productsTable.getByTrigger(shopTools.getShopId(), period);
                logger.info("Oт магазина id = "  + shopTools.getShopId() + " получено " + products.size());
                var priceMap = getActualPrice(shopTools, products);
                logger.info("Успешно получено " + priceMap.size() + " из " + products.size());
                if (priceMap.size()!=0){
                    for (PriceChangeListener listener : priceChangeListenerList) {
                        listener.notifOfPriceChanged(priceMap, shopTools);
                    }
                    logger.info("Подписчкик (" + priceChangeListenerList + ") оповещены об изменении.");
                }
            }catch (SQLException exception){
                logger.error("Ошибка при запросе на получение продуктов!", exception);
            }catch (NullPointerException exception){
                logger.error("Неудалось получить объект таблици цен пролуктов!", exception);
            }
        }
    }

    public Map<Integer, Price> getActualPrice(ShopToolsFactory shopTools, List<Product> products){
        var map = new HashMap<Integer, Price>();
        for (Product product : products) {
            try {
                var parser = shopTools.getParser(product.getLink());
                logger.info("Получен парсер по продукту.\n" +
                        "\tНаименование продукта: " + product.getName() + "\n" +
                        "\tСсылка на продукт: " + product.getLink());
                var priceValue = parser.getPrice();
                var price = new Price(
                        Calendar.getInstance(),
                        priceValue);
                logger.info("Получена новая цена: " + price.getPrice());

                map.put(product.getIdProduct(), price);
            } catch (IOException exception) {
                logger.error("Ошибка при получении парсера для просмотра актуальной цены!", exception);
            }
        }

        return map;
    }
}
