package sample.Controllers.Fragments.ProductConstructors;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.springframework.util.StringUtils;
import sample.Controllers.Fragments.ConstructorFragment;
import sample.Databases.ShopsDatabase.TableCitilinkShop;
import sample.ProductProxys.Parsers.CitilinkShopProductParser;
import sample.Products.Product;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CitilinkShopConstructorFragment extends ConstructorFragment {

    public TextField linkField;
    public TextField nameField;
    public ChoiceBox choiceField;

    public CitilinkShopConstructorFragment(){
        //FIXME Такая суета с этими коннектами
        productsTable = new TableCitilinkShop();
        try {
            productsTable.openConnection();
            if (!productsTable.existsTable()){
                productsTable.createTable();
            }
            productsTable.closeConnection();
        }catch (SQLException exception){
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    @Override
    public String getPathToFXML() {
        return "/fragments/fragment_productConstructor_CitilinkShop.fxml";
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @Override
    public boolean checkFields(){
        var answer = true;
        if (!checkLinkField())
            answer = false;
        if (!checkNameField())
            answer = false;

        generateProduct();
        return answer;
    }

    @Override
    public Product generateProduct() {
        String url = linkField.getText();
        String productName = nameField.getText();
        Product product = new Product(url, productName);

        var rows = 0;
        try {
            productsTable.openConnection();
            rows += productsTable.insert(product);
        } catch (SQLException exception) {
            exception.printStackTrace();
            //todo вывечти лог
        } finally {
            try{
                productsTable.closeConnection();
            }catch (SQLException exception){
                exception.printStackTrace();
                //todo вывечти лог
            }
        }

        return rows != 0;
    }

    protected boolean checkLinkField(){
        String linkToProduct = linkField.getText();
        linkToProduct = StringUtils.trimAllWhitespace(linkToProduct);
        if (StringUtils.hasText(linkToProduct)){
            try {
                this.productProxy =
                        new CitilinkShopProductParser(linkToProduct);
                linkField.setText(linkToProduct);
                return true;
            }catch (IOException exception){
                exception.printStackTrace();
                System.out.println(exception.getMessage());
                //TODO прописать логи и вывести диалоговое окно, что товар не найден
                linkField.setStyle("-fx-background-color: #FDB7B7;");
                return false;
            }
        }else {
            linkField.setStyle("-fx-background-color: #FDB7B7;");
            return false;
        }
    }

    protected boolean checkNameField(){
        String productName = nameField.getText();
        productName = StringUtils.trimAllWhitespace(productName);
        if (StringUtils.hasText(productName)) {
            nameField.setText(productName);
        } else {
            String nameFromPage = productProxy.getName();
            nameField.setText(nameFromPage);
        }

        return true;
    }

    protected boolean checkTimerField(){
        return true;
    }
}
