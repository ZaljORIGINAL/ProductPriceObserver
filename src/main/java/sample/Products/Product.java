package sample.Products;

import sample.Databases.ProductPricesTable;

import java.sql.SQLException;

public class Product {
    private final int id;
    private String url;
    private String name;
    private long triggerPeriod;
    private String priceTableName;

    public Product(int id, String url, String name, long triggerPeriod, String priceTableName){
        this.id = id;
        this.url = url;
        this.name = name;
        this.triggerPeriod = triggerPeriod;
        this.priceTableName = priceTableName;
    }

    public Product(String url, String name, long triggerPeriod){
        this.id = -1;
        this.url = url;
        this.name = name;
        this.triggerPeriod = triggerPeriod;
    }

    public int getId() {
        return id;
    }

    public String getLink() {
        return url;
    }

    public String getName() {
        return name;
    }

    public long getTriggerPeriod() {
        return triggerPeriod;
    }

    public String getPriceTableName(){
        return priceTableName;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setTriggerPeriod(long triggerPeriod) {
        this.triggerPeriod = triggerPeriod;
    }

    public void addPrice(Price price) throws SQLException {
        var table = new ProductPricesTable(priceTableName);
        table.insert(price);
    }
}
