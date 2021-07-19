package sample.ProductProxys.Parsers;

import org.jsoup.Jsoup;
import org.junit.Test;
import sample.Products.Product;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class CitilinkShopProductParserTest {

    @Test
    public void connectionTest() throws IOException {
        var parser = new CitilinkShopProductParser();
        var document = parser.connect("https://www.citilink.ru/");

        assertNotEquals(document, null);
    }

    @Test
    public void getNameTest() throws IOException{
        String htmlString = "<html>\n" +
                "<body>\n" +
                "<h1 class=\"Heading Heading_level_1 ProductHeader__title\">\n" +
                "    Наименование\n" +
                "</h1>\n" +
                "<span class=\"ProductHeader__price-default_current-price \">\n" +
                "                                    1690\n" +
                "                            </span>\n" +
                "</body>\n" +
                "</html>";

/*
        Path resourceDirectory = Paths.get("src","test","resources", "ProductProxys", "Parsers", "Citilink", "CitilinkProductPage.html");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        var productPageFile = Paths.get(absolutePath);
        try(BufferedReader br = new BufferedReader (new FileReader(productPageFile.toFile())))
        {
            StringBuilder builder = new StringBuilder();
            // чтение построчно
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }

            htmlString = builder.toString();
        }
*/

        var productPage = Jsoup.parse(htmlString);
        var parser = new CitilinkShopProductParser(productPage);
        var productName = parser.getName();

        assertEquals(productName, "Наименование");
    }

    @Test
    public void getPriceTest(){
        String htmlString = "<html>\n" +
                "<body>\n" +
                "<h1 class=\"Heading Heading_level_1 ProductHeader__title\">\n" +
                "    Наименование\n" +
                "</h1>\n" +
                "<span class=\"ProductHeader__price-default_current-price \">\n" +
                "                                    1690\n" +
                "                            </span>\n" +
                "</body>\n" +
                "</html>";

        var productPage = Jsoup.parse(htmlString);
        var parser = new CitilinkShopProductParser(productPage);
        var productPrice = parser.getPrice();

        if (productPrice != 1690f)
            fail();
    }
}