package sample.ProductObserver;

import sample.ShopToolsFactories.Factorys.CitilinkShopToolsFactory;
import sample.ShopToolsFactories.ProductToolsFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

public class TriggerTusk extends TimerTask {
    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();
        var hour24 = calendar.get(Calendar.HOUR_OF_DAY);

        List<ProductToolsFactory> ProductTools = getToolsFactories();
        List<PriceObserver> priceObservers = new ArrayList<>();


        priceObservers.add(new PriceObserver(3600000));

        if (hour24%12 == 0){
            priceObservers.add(new PriceObserver(43200000));
        }

        if (hour24%24 == 0){
            priceObservers.add(new PriceObserver(86400000));
        }

        for (ProductToolsFactory tools: ProductTools) {
            /* Источник:
            *   https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/CountDownLatch.html
            *   https://pro-java.ru/parallelizm-v-java/klass-countdownlatch-primery-realizacii-koda-v-java/
            */
            try{
                CountDownLatch finishSignal =
                        new CountDownLatch(priceObservers.size());
                for (PriceObserver priceObserver: priceObservers) {
                    priceObserver.check(tools, finishSignal);
                }
                finishSignal.await();
            }catch (InterruptedException exception){
                //TODO Прописать лог
            }
        }

    }

    private List<ProductToolsFactory> getToolsFactories(){
        List<ProductToolsFactory> tables =
                new ArrayList<>();
        tables.add(
                new CitilinkShopToolsFactory());

        return tables;
    }
}
