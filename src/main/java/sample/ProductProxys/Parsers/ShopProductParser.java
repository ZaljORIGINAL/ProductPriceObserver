package sample.ProductProxys.Parsers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sample.ProductProxys.ProductProxy;

import java.io.File;
import java.io.IOException;

public abstract class ShopProductParser implements ProductParser {
    protected static final Logger logger = LogManager.getLogger(ShopProductParser.class);

    protected Document document;

    public ShopProductParser(){}

    public ShopProductParser(String linkToProduct) throws IOException {
        connect(linkToProduct);
        logger.info("Создан экземпляр класса.");
    }

    public Document connect(String linkToProduct) throws IOException{
        if (linkToProduct.contains("https://")){
            document = Jsoup.connect(linkToProduct)
                    .userAgent("Chrome/81.0.4044.138")
                    .get();
        }else{
            var file = new File(linkToProduct);
            document = Jsoup.parse(file, "UTF-8");
        }

        return document;
    }
}
