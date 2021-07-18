package sample.Controllers.Fragments.MainWindow.Tables.ProductsTables;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import sample.Controllers.Fragments.MainWindow.Tables.ProductTableView;
import sample.Controllers.Fragments.ProductConstructorFragment;
import sample.Controllers.Fragments.ProductConstructors.CitilinkShopConstructorFragment;
import sample.Controllers.Fragments.ProductEditorFragment;
import sample.Controllers.Fragments.ProductEditors.CitilinkShopEditorFragment;
import sample.Databases.Contracts.ProductTableContract;
import sample.Databases.ProductsTable;
import sample.Products.ActualProduct;
import sample.ShopToolsFactories.Factorys.CitilinkShopToolsFactory;

import java.sql.SQLException;

public class CitilinkProductTableView extends ProductTableView {
    private static final Logger logger = LogManager.getLogger(CitilinkProductTableView.class);

    public CitilinkProductTableView(ApplicationContext context) throws SQLException{
        super(context);
        tableData = context.getBean(ProductsTable.class);
        this.factory = context.getBean(CitilinkShopToolsFactory.class);
        logger.info("Создан объект графического представления таблици.");
    }

    @Override
    protected ProductConstructorFragment getFragmentToConstructor() {
        return new CitilinkShopConstructorFragment();
    }

    @Override
    protected ProductEditorFragment getFragmentToEditor() {
        var productProperty
                = tableView.getSelectionModel().getSelectedItem();
        var product = productProperty.getProduct();
        return new CitilinkShopEditorFragment(product);
    }
}
