package sample.ProductProxys.Parsers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;
import sample.Products.Price;

import java.io.IOException;
import java.util.Calendar;

public class CitilinkShopProductParser extends ShopProductParser{

    public CitilinkShopProductParser(){
        super();
    };

    public CitilinkShopProductParser(String linkToProduct) throws IOException {
        super(linkToProduct);
        logger.info("Создан экземпляр класса.");
    }

    public CitilinkShopProductParser(Document document){
        this.document = document;
    }

    @Override
    public String getName() {
        logger.info("Чтение наименования продукта с сайта...");
        Elements elements = document.getElementsByClass("Heading Heading_level_1 ProductHeader__title");
        Element nameElement = elements.first();
        String nameValue = nameElement.text();

        logger.info("Полученное наименование продукта с ссайта: " + nameValue);
        return nameValue;
    }

    @Override
    public float getPrice() {
        logger.info("Чтение цены продукта с сайта...");
        Elements elements = document.getElementsByClass("ProductHeader__price-default_current-price ");
        Element priceElement = elements.first();
        String priceString = priceElement.text();
        priceString = StringUtils.trimAllWhitespace(priceString);

        logger.info("Полученная цена продукта с ссайта: " + priceString);
        return Float.parseFloat(priceString);
    }
}
