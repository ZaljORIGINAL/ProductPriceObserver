package sample.ProductProxys.Parsers;

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
    }

    @Override
    public String getName() {
        Elements elements = document.getElementsByClass("Heading Heading_level_1 ProductHeader__title");
        Element nameElement = elements.first();
        String nameValue = nameElement.text();

        return nameValue;
    }

    @Override
    public Price getPrice() {
        Elements elements = document.getElementsByClass("ProductHeader__price-default_current-price ");
        Element priceElement = elements.first();
        String priceValue = priceElement.text();
        priceValue = StringUtils.trimAllWhitespace(priceValue);

        var price = new Price(
                Calendar.getInstance(),
                Float.parseFloat(priceValue));

        return price;
    }
}
