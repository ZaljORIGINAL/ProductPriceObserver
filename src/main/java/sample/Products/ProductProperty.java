package sample.Products;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProductProperty {
    private Product product;
    private Price price;
    private SimpleStringProperty name;
    private SimpleFloatProperty priceValue;

    public ProductProperty(Product product, Price price){
        this.product = product;
        this.price = price;

        name = new SimpleStringProperty(
                product.getName());
        priceValue = new SimpleFloatProperty(
                price.getPrice());
    }

    public Product getProduct() {
        return product;
    }

    public Price getPrice(){
        return price;
    }

    public String getName() {
        return name.get();
    }

    public float getPriceValue() {
        return priceValue.get();
    }

    public void setProduct(Product product){
        if (this.product.getId() == product.getId()){
            this.product = product;
            this.name.set(this.product.getName());
        }else
            throw new RuntimeException("Передан неверный экземпляр объекта типа Product. Id не совпадает! " +
                    "\nRequired: " + this.product.getId() +
                    "\nProvided: " + product.getId());
    }

    public void setPrice(Price price) {
        this.price = price;
        priceValue.set(price.getPrice());
    }
}
