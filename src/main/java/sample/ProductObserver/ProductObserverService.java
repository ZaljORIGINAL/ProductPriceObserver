package sample.ProductObserver;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import sample.Databases.ProductPricesTable;
import sample.Databases.ProductsTable;
import sample.ShopToolsFactories.Factorys.CitilinkShopToolsFactory;
import sample.ShopToolsFactories.ShopToolsFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class ProductObserverService {
    private final static Logger logger = LogManager.getLogger(ProductObserverService.class);

    private final ApplicationContext context;
    private final ScheduledExecutorService scheduledService;
    private final List<PriceChangeListener> subscribers;

    public ProductObserverService(ApplicationContext context){
        this.context = context;

        //https://habr.com/ru/post/260953/
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("Задача обновления цен-%d")
                .build();
        scheduledService =
                new ScheduledThreadPoolExecutor(3, threadFactory);

        subscribers = new ArrayList<>();
        subscribers.add(context.getBean(ProductPricesTable.class));
    }

    public void start(){
        List<ShopToolsFactory> shopTools = getShopTools();
        final var productsTable =
                context.getBean(ProductsTable.class);

        logger.info("Потготовка таймера на обновление цен с периодом в 1час.");
        scheduledService.scheduleAtFixedRate(
                ()-> {
                    var observer = new PriceObserver(
                            3600000,
                            productsTable,
                            subscribers);
                    observer.start(shopTools);
                },
                0,
                1,
                TimeUnit.HOURS);
        scheduledService.scheduleAtFixedRate(
                ()-> {
                    var observer = new PriceObserver(
                            3600000,
                            productsTable,
                            subscribers);
                    observer.start(shopTools);
                },                0,
                12,
                TimeUnit.HOURS);
        scheduledService.scheduleAtFixedRate(
                ()-> {
                    var observer = new PriceObserver(
                            3600000,
                            productsTable,
                            subscribers);
                    observer.start(shopTools);
                },                0,
                24,
                TimeUnit.HOURS);
        logger.info("Установлен таймер на обновления цен товаров.");
    }

    public void finish(){
        List<Runnable> shutdownTasks = scheduledService.shutdownNow();
        logger.info("Отмена задач на обновление цен продуктов.");
    }

    public void addSubscriber(PriceChangeListener subscriber){
        subscribers.add(subscriber);
    }

    public void removeSubscriber(PriceChangeListener subscriber){
        subscribers.remove(subscriber);
    }

    private List<ShopToolsFactory> getShopTools(){
        ArrayList<ShopToolsFactory> list = new ArrayList<>();
        list.add(context.getBean(CitilinkShopToolsFactory.class));

        return list;
    }
}
