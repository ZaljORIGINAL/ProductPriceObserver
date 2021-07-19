package sample.Products;

import java.util.Calendar;

public class Price {
    private final int idPrice;
    private final Calendar date;
    private final float price;

    public Price(int idPrice, Calendar date, float price){
        this.idPrice = idPrice;
        this.date = date;
        this.price = price;
    }

    public Price(Calendar date, float price){
        this.idPrice = -1;
        this.date = date;
        this.price = price;
    }

    public int getIdPrice() {
        return idPrice;
    }

    public Calendar getCalendar() {
        return date;
    }

    public float getPrice() {
        return price;
    }
}
