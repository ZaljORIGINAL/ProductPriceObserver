package sample.Controllers.Fragments.MainWindow.Tables;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Databases.ShopsDatabase.TableCitilinkShop;
import sample.Products.Product;
import sample.Products.ProductProperty;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CitilinkShopTableView extends ShopTableView {
    private TableColumn<ProductProperty, Object> nameColumn;
    private TableColumn<ProductProperty, Float> priceColumn;

    public CitilinkShopTableView(){
        //FIXME Такая суета с этими коннектами
        dataTable = new TableCitilinkShop();
        try {
            dataTable.openConnection();
            if (!dataTable.existsTable()){
                dataTable.createTable();
            }
            dataTable.closeConnection();
        }catch (SQLException exception){
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            nameColumn = new TableColumn<>("Товар");
            nameColumn.setCellValueFactory(
                    new PropertyValueFactory<>("name")
            );
            productsTable.getColumns().add(nameColumn);

            priceColumn = new TableColumn<>("Последняя цена");
            priceColumn.setCellValueFactory(
                    new PropertyValueFactory<>("price")
            );
            productsTable.getColumns().add(priceColumn);

            downloadAllItems();
        }catch (SQLException exception){
            //TODO Вывести лог и отобразить сообение об ошибке.
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    @Override
    public String getPathToFXML() {
        return "/fragments/fragment_TableView_Product_CitilinkShop.fxml";
    }

    @Override
    public void addItem(Product product) {
        //TODO полученый объект добавить в базу данных,
        // получить ответ из базы данных
        // В случае успешного добавления вывести запись в
        // визуальную таблицу
    }

    @Override
    public void deleteItem(Product product) {

    }

    @Override
    public void updateItem(Product product) {

    }
}
