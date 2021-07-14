package sample.ProductObserver;

import sample.ShopToolsFactories.Factorys.CitilinkShopToolsFactory;
import sample.ShopToolsFactories.ProductToolsFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class TriggerTusk extends TimerTask {
    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();
        var hour24 = calendar.get(Calendar.HOUR_OF_DAY);

        List<ProductToolsFactory> toolsFactory = getToolsFactories();

        var observer1Hour = new PriceObserver(3600000);
        observer1Hour.check(toolsFactory);

        if (hour24%12 == 0){
            var observer12Hour = new PriceObserver(43200000);
            observer12Hour.check(toolsFactory);
        }

        if (hour24%24 == 0){
            var observer24Hour = new PriceObserver(86400000);
            observer24Hour.check(toolsFactory);
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
