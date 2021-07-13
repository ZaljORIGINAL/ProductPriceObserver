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
    private TableColumn<ActualProduct, String> nameColumn;
    private TableColumn<ActualProduct, String> priceColumn;

    public CitilinkProductTableView() throws SQLException{
        tableData = new ProductsTable(ProductTableContract.CITILINK_TABLE);
    }

    @Override
    public String getPathToFXML() {
        return "/fragments/fragment_TableView_OneShop.fxml";
    }

    @Override
    protected void initTableView() {
        nameColumn = new TableColumn<>("Товар");
        nameColumn.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getProduct().getName()));
        tableView.getColumns().add(nameColumn);

        priceColumn = new TableColumn<>("Последняя цена");
        priceColumn.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getPrice().getPrice())));
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
