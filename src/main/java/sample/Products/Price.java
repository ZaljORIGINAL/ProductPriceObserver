package sample.Products;

import java.util.Calendar;
import java.util.Date;

public class Price {
    private int id;
    private Calendar date;
    private float price;

    public Price(int id, Calendar date, float price){
        this.id = id;
        this.date = date;
        this.price = price;
    }

    public Price(Calendar date, float price){
        this.id = -1;
        this.date = date;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public Calendar getCalendar() {
        return date;
    }

    public float getPrice() {
        return price;
    }
}
