package sample.Products;

import sample.Databases.ProductPricesTable;

import java.sql.SQLException;
import java.util.Date;

public class Product {
    private int id;
    private String url;
    private String name;
    private ProductPricesTable priceTable;

    public Product(int id, String url, String name, String priceTableName){
        try {
            this.id = id;
            this.url = url;
            this.name = name;
            priceTable = new ProductPricesTable(priceTableName);
        }catch (SQLException exception){
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public Product(String url, String name){
        this.id = -1;
        this.url = url;
        this.name = name;
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

    public String getTableName() {
        return priceTable.getName();
    }

    public ProductPricesTable getPriceTable(){
        return priceTable;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public void setName(String name){
        this.name = name;
    }

    public void addPrice(Price price) throws SQLException {
        priceTable.insert(price);
    }
}
