package sample.Products;

public class Product {
    private final int idProduct;
    private final int idShop;
    private String url;
    private String name;
    private long observerPeriod;

    public Product(int idProduct, int idShop,String url, String name, long observerPeriod){
        this.idProduct = idProduct;
        this.idShop = idShop;
        this.url = url;
        this.name = name;
        this.observerPeriod = observerPeriod;
    }

    public Product(int idShop, String url, String name, long observerPeriod){
        this.idProduct = -1;
        this.idShop = idShop;
        this.url = url;
        this.name = name;
        this.observerPeriod = observerPeriod;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public int getIdShop() {
        return idShop;
    }

    public String getLink() {
        return url;
    }

    public String getName() {
        return name;
    }

    public long getObserverPeriod() {
        return observerPeriod;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setObserverPeriod(long observerPeriod) {
        this.observerPeriod = observerPeriod;
    }
}
