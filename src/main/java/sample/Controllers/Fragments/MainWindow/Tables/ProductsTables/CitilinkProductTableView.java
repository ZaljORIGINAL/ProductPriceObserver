package sample.Controllers.Fragments.MainWindow.Tables.ProductsTables;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;
import sample.Controllers.Fragments.MainWindow.Tables.ProductTableView;
import sample.Controllers.Fragments.ProductConstructors.CitilinkShopConstructorFragment;
import sample.Databases.ShopsDatabase.TableCitilinkShop;
import sample.Products.Product;
import sample.Products.ProductProperty;

import java.sql.SQLException;

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
        Dialog<Pair<String, Product>> dialog =
                new Dialog<>();
        dialog.setTitle("Конструктор продукта");
        dialog.setHeaderText("Задайте параметры продукта");

        var constructorFragmetn =
                new CitilinkShopConstructorFragment();
        constructorFragmetn.initFragmentView();
        dialog.getDialogPane().setContent(
                constructorFragmetn.getMainPanel());

        ButtonType addProductButton =
                new ButtonType(
                        "Добавить",
                        ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes()
                .addAll(
                        addProductButton,
                        ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton->{
            if (dialogButton == addProductButton){
                if (constructorFragmetn.checkFields()){
                    var product =
                            constructorFragmetn.generateProduct();
                    var answer = new Pair<>(
                            "answer",
                            product);

                    return answer;
                }
            }

            //Ну тут хз
            return null;
        });
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
