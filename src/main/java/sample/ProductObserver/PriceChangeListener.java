package sample.ProductObserver;

import java.util.List;

public interface PriceChangeListener {
    void notifOfPriceChanged(List<PriceChangeListener> list);
}
