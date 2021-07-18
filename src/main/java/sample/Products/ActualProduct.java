package sample.Products;

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
        if (this.product.getIdProduct() == product.getIdProduct()){
            this.product = product;
        }else
            throw new RuntimeException("Передан неверный экземпляр объекта типа Product. Id не совпадает! " +
                    "\nRequired: " + this.product.getIdProduct() +
                    "\nProvided: " + product.getIdProduct());
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}
