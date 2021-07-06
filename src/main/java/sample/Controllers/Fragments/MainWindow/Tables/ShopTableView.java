package sample.Controllers.Fragments.MainWindow.Tables;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import sample.Controllers.Fragments.ViewFragment;
import sample.Databases.ProductsTable;
import sample.Databases.ShopsDatabase.TableCitilinkShop;
import sample.Products.Product;
import sample.Products.ProductProperty;

import java.sql.SQLException;

public abstract class ShopTableView extends ViewFragment {
    public TableView<ProductProperty> productsTable;
    protected ProductsTable dataTable;

    public ShopTableView(){
    }

    public abstract void addItem(Product product);
    public abstract void deleteItem(Product product);
    public abstract void updateItem(Product product);
    public void downloadAllItems() throws SQLException{
        ObservableList<ProductProperty> items =
                FXCollections.observableArrayList();
        dataTable.openConnection();
        var products = dataTable.getAll();
        for (Product product : products) {
            var pricesTable = product.getPriceTable();
            var lastPrice = pricesTable.getLastPrice();
            var productProperty = new ProductProperty(
                    product,
                    lastPrice);
            items.add(productProperty);
        }

        productsTable.setItems(items);
    }
}
