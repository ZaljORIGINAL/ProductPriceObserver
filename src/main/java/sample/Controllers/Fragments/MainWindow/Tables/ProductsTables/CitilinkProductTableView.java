package sample.Controllers.Fragments.MainWindow.Tables.ProductsTables;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Controllers.Fragments.MainWindow.Tables.ProductTableView;
import sample.Controllers.Fragments.ProductConstructorFragment;
import sample.Controllers.Fragments.ProductConstructors.CitilinkShopConstructorFragment;
import sample.Controllers.Fragments.ProductEditorFragment;
import sample.Controllers.Fragments.ProductEditors.CitilinkShopEditorFragment;
import sample.Databases.Contracts.ProductTableContract;
import sample.Databases.ProductsTable;
import sample.Products.ActualProduct;

import java.sql.SQLException;

public class CitilinkProductTableView extends ProductTableView {

    public CitilinkProductTableView() throws SQLException{
        tableData = new ProductsTable(ProductTableContract.CITILINK_TABLE);
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
