package sample.Databases.Contracts;

public abstract class ProductTableContract {
    public static final String TABLE_NAME = "products";

    public static final String ID_COLUMN = "id";
    public static final String ID_SHOP_COLUMN = "idShop";
    public static final String URL_COLUMN = "url";
    public static final String NAME_COLUMN = "name";
    public static final String OBSERVER_PERIOD_COLUMN = "observerPeriod";

    public static final int MAX_SYMBOL_TO_URL = 250;
    public static final int MAX_SYMBOL_TO_NAME = 100;
}
