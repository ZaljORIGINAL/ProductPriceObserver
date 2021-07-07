package sample.Controllers.Fragments.MainWindow.Tables.ProductsTables;

import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Controllers.Fragments.MainWindow.Tables.ProductTableView;
import sample.Databases.ShopsDatabase.TableCitilinkShop;
import sample.Products.ProductProperty;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CitilinkProductTableView extends ProductTableView {
    private TableColumn<ProductProperty, Object> nameColumn;
    private TableColumn<ProductProperty, Float> priceColumn;

    /*TODO В случае если будет ошибка, то требуется сгенерировать
    *  который будет затычкой и сообщить что с данными что то не так
    *  Так же у него должна быть кнопка обновления, что бы попробовать
    *  сделать запрос поновой.*/
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
        return "/fragments/fragment_TableView_Product_CitilinkShop.fxml";
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
    protected Dialog buildConstructorDialog() {
        return null;
    }

    @Override
    protected Dialog buildEditorDialog() {
        return null;
    }

    @Override
    protected Dialog buildDeleteDialog() {
        return null;
    }
}
