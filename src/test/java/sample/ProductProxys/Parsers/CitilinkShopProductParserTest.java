package sample.ProductProxys.Parsers;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class CitilinkShopProductParserTest {

    @Test
    public void getName() {
        String link = "https://www.citilink.ru/product/smartfon-xiaomi-redmi-9-64gb-4gb-fioletovyi-3g-4g-6-53-and10-wifi-nfc-1391488/";
        String toCheck = "Смартфон XIAOMI Redmi 9 64Gb, фиолетовый";
        String fromPage = "";
        try {
            CitilinkShopProductParser parser = new CitilinkShopProductParser(link);
            fromPage = parser.getName();
            System.out.println("Контрольное значение: " + toCheck);
            System.out.println("Полученное значение: " + fromPage);
        }catch (IOException exception){
            System.out.println("Что то не так: " + exception.getMessage());
        }
        assertEquals(fromPage, toCheck);
    }

    @Test
    public void getPrice() {
        String link = "https://www.citilink.ru/product/smartfon-xiaomi-redmi-9-64gb-4gb-fioletovyi-3g-4g-6-53-and10-wifi-nfc-1391488/";
        float toCheck = 11490;
        float priceValue = 0;
        try{
            CitilinkShopProductParser parser = new CitilinkShopProductParser(link);
            var price = parser.getPrice();
            System.out.println("Контрольное значение: " + toCheck);
            System.out.println("Полученное значение: " + price.getPrice());
            priceValue = price.getPrice();
        }catch (IOException exception){
            System.out.println("Что то не так: " + exception.getMessage());
        }
        assertEquals(priceValue, toCheck, 0);
        }
}