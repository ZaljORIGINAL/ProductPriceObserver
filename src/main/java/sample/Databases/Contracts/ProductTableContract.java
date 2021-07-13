package sample.Databases.Contracts;

public abstract class ProductTableContract {
    //Supported shops
    public static final String CITILINK_TABLE = "citilink_shop";

    public static final String ID_COLUMN = "id";
    public static final String URL_COLUMN = "url";
    public static final String NAME_COLUMN = "name";
    public static final String TRIGGER_COLUMN = "trigger";

    public static final int MAX_SYMBOL_TO_URL = 250;
    public static final int MAX_SYMBOL_TO_NAME = 100;
}
