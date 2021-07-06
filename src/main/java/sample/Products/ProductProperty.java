package sample.Products;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProductProperty {
    private Product product;
    private SimpleStringProperty name;
    private SimpleFloatProperty price;

    public ProductProperty(Product product, Price price){
        this.product = product;

        name = new SimpleStringProperty(
                product.getName());
        this.price = new SimpleFloatProperty(
                price.getPrice());
    }

    public Product getProduct() {
        return product;
    }

    public String getName() {
        return name.get();
    }

    public float getPrice() {
        return price.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setPrice(float price) {
        this.price.set(price);
    }
}
