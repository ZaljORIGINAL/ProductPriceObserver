package sample.Products;

import java.util.Calendar;

public class Price {
    private final int idPrice;
    private final int idProduct;
    private final Calendar date;
    private final float price;

    public Price(int idPrice, int idProduct, Calendar date, float price){
        this.idPrice = idPrice;
        this.idProduct = idProduct;
        this.date = date;
        this.price = price;
    }

    public Price(int idProduct, Calendar date, float price){
        this.idPrice = -1;
        this.idProduct = idProduct;
        this.date = date;
        this.price = price;
    }

    public int getIdPrice() {
        return idPrice;
    }

    public int getIdProduct(){
        return idProduct;
    }

    public Calendar getCalendar() {
        return date;
    }

    public float getPrice() {
        return price;
    }
}
