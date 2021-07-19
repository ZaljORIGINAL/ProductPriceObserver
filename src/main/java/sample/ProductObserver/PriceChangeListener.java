package sample.ProductObserver;

import sample.Products.Price;
import sample.ShopToolsFactories.ShopToolsFactory;

import java.util.Map;

public interface PriceChangeListener {
    void notifOfPriceChanged(Map<Integer, Price> list, ShopToolsFactory shopTools);
}
