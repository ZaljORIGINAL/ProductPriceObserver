package sample.Controllers.Fragments.MainWindow.Tables.ProductsTables;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import sample.Controllers.Fragments.MainWindow.Tables.ProductTableView;
import sample.Controllers.Fragments.ProductConstructorFragment;
import sample.Controllers.Fragments.ProductConstructors.CitilinkShopConstructorFragment;
import sample.Controllers.Fragments.ProductEditorFragment;
import sample.Controllers.Fragments.ProductEditors.CitilinkShopEditorFragment;
import sample.Databases.ProductsTable;
import sample.Products.ActualProduct;
import sample.Products.Price;
import sample.Products.Product;
import sample.ShopToolsFactories.Factorys.CitilinkShopToolsFactory;
import sample.ShopToolsFactories.ShopToolsFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CitilinkProductTableView extends ProductTableView {
    private static final Logger logger = LogManager.getLogger(CitilinkProductTableView.class);

    public CitilinkProductTableView(ApplicationContext context) throws SQLException{
        super(context);
        tableData = context.getBean(ProductsTable.class);
        this.shopTools = context.getBean(CitilinkShopToolsFactory.class);
        logger.info("Создан объект графического представления таблици.");
    }

    @Override
    protected ProductConstructorFragment getFragmentToConstructor() {
        return new CitilinkShopConstructorFragment(context.getBean(CitilinkShopToolsFactory.class));
    }

    @Override
    protected ProductEditorFragment getFragmentToEditor() {
        var item
                = tableView.getSelectionModel().getSelectedItem();
        var shopTools = context.getBean(CitilinkShopToolsFactory.class);
        var product = item.getProduct();
        return new CitilinkShopEditorFragment(shopTools, product);
    }

    @Override
    public void notifOfPriceChanged(Map<Integer, Price> list, ShopToolsFactory shopTools) {
        if (shopTools instanceof CitilinkShopToolsFactory){
            List<ActualProduct> items = tableView.getItems();
            for (ActualProduct item : items) {
                var product = item.getProduct();
                var price = list.get(product.getIdProduct());
                if (price != null)
                    item.setPrice(price);
            }

            tableView.refresh();
        }
    }
}
