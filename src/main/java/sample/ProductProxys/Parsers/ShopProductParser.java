package sample.ProductProxys.Parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sample.ProductProxys.ProductProxy;

import java.io.IOException;

public abstract class ShopProductParser implements ProductParser {
    protected Document document;

    public ShopProductParser(String linkToProduct) throws IOException {
        document = Jsoup.connect(linkToProduct).get();
    }
}
