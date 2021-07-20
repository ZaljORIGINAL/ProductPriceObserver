package sample.ProductProxys.Parsers;

import org.jsoup.Jsoup;
import org.junit.Test;
import sample.Products.Product;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class CitilinkShopProductParserTest {

    @Test
    public void connectionTest_WebSite() throws IOException {
        var parser = new CitilinkShopProductParser();
        var document = parser.connect("https://www.citilink.ru/");

        assertNotEquals(document, null);
    }

    @Test
    public void connectionTest_LocalFile() throws IOException {
        Path resourceDirectory = Paths.get("src","test","resources", "ProductProxys", "Parsers", "Citilink", "CitilinkProductPage.html");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        var productPageFile = new File(absolutePath);

        var parser = new CitilinkShopProductParser();
        var document = parser.connect(productPageFile.getPath());

        assertNotEquals(document, null);
    }

    @Test
    public void getNameTest() throws IOException{
        Path resourceDirectory = Paths.get("src","test","resources", "ProductProxys", "Parsers", "Citilink", "CitilinkProductPage.html");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        var productPageFile = new File(absolutePath);
        var parser = new CitilinkShopProductParser(productPageFile.getPath());
        var productName = parser.getName();

        assertEquals(productName, "Наименование");
    }

    @Test
    public void getPriceTest() throws IOException {
        Path resourceDirectory = Paths.get("src","test","resources", "ProductProxys", "Parsers", "Citilink", "CitilinkProductPage.html");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        var productPageFile = new File(absolutePath);
        var parser = new CitilinkShopProductParser(productPageFile.getPath());
        var productPrice = parser.getPrice();

        if (productPrice != 1690f)
            fail();
    }
}