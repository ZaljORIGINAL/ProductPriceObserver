package sample.Products;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;

public class ActualProduct {
    private Product product;
    private Price price;

    public ActualProduct(Product product, Price price){
        this.product = product;
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public Price getPrice(){
        return price;
    }

    public void setProduct(Product product){
        if (this.product.getId() == product.getId()){
            this.product = product;
        }else
            throw new RuntimeException("Передан неверный экземпляр объекта типа Product. Id не совпадает! " +
                    "\nRequired: " + this.product.getId() +
                    "\nProvided: " + product.getId());
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}
