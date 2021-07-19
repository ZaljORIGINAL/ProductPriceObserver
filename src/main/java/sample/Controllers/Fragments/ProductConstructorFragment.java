package sample.Controllers.Fragments;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sample.Products.ActualProduct;
import sample.Products.Product;
import sample.ShopToolsFactories.ShopToolsFactory;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class ProductConstructorFragment extends ProductParamFragment {
    protected static final Logger logger = LogManager.getLogger(ProductConstructorFragment.class);

    public ProductConstructorFragment(ShopToolsFactory shopTools){
        super(shopTools);
        logger.info("Создан объект фрагмента редактора продукта.");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initView();
    }

    @Override
    public ActualProduct saveProduct() {
        logger.info("Потготовка объекта для сохранения продукта...");
        String url = this.linkField.getText();
        String productName = this.nameField.getText();

        long triggerPeriod;
        switch (choiceField.getSelectionModel().getSelectedItem()){
            case "12 час":
                triggerPeriod = 43200000;
                break;
            case "24 час":
                triggerPeriod = 86400000;
                break;
            default:
                triggerPeriod = 3600000;
        }
        logger.info("Данные с полей получены. Помещение их в объект продукта.");

        var product = new Product(shopTools.getShopId(), url, productName, triggerPeriod);
        logger.info("Создан объект продукта: " + product);
        var price = priceTable.getItems().get(
                priceTable.getItems().size() - 1);
        logger.info("Создан объект актуальной цены: " + price);

        var actualProduct = new ActualProduct(product, price);
        logger.info("Возвращение экземпляра класса ActualProduct: " + actualProduct);
        return actualProduct;
    }
}
