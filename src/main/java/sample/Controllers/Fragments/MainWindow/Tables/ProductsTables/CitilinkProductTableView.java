package sample.Controllers.Fragments.MainWindow.Tables.ProductsTables;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Controllers.Fragments.MainWindow.Tables.ProductTableView;
import sample.Controllers.Fragments.ProductConstructorFragment;
import sample.Controllers.Fragments.ProductConstructors.CitilinkShopConstructorFragment;
import sample.Controllers.Fragments.ProductEditorFragment;
import sample.Controllers.Fragments.ProductEditors.CitilinkShopEditorFragment;
import sample.Databases.ShopsDatabase.TableCitilinkShop;
import sample.Products.ProductProperty;

import java.sql.SQLException;

public class CitilinkProductTableView extends ProductTableView {
    private TableColumn<ProductProperty, Object> nameColumn;
    private TableColumn<ProductProperty, Float> priceColumn;

    public CitilinkProductTableView() throws SQLException{
        tableData = new TableCitilinkShop();

        //Проверка на наличие базы данных
        tableData.openConnection();
        if (!tableData.existsTable()){
            tableData.createTable();
        }
        tableData.closeConnection();
    }

    @Override
    public String getPathToFXML() {
        return "/fragments/fragment_TableView_OneShop.fxml";
    }

    @Override
    protected void initTableView() {
        nameColumn = new TableColumn<>("Товар");
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        tableView.getColumns().add(nameColumn);

        priceColumn = new TableColumn<>("Последняя цена");
        priceColumn.setCellValueFactory(
                new PropertyValueFactory<>("priceValue")
        );
        tableView.getColumns().add(priceColumn);

        try {
            updateTable();
        }catch (SQLException exception){
            tableView.getItems().clear();
            Alert messageDialog =
                    new Alert(Alert.AlertType.ERROR);
            messageDialog.setTitle("Ошибка");
            messageDialog.setHeaderText("Ошибка при работе с базой данных");
            messageDialog.setContentText(exception.getMessage());
        }
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
