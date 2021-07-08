package sample.Controllers.Fragments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.springframework.util.StringUtils;
import sample.ProductProxys.ProductProxy;
import sample.Products.Product;

import java.io.IOException;

public abstract class ProductParamFragment extends ViewFragment {
    public TextField url;
    public TextField name;
    public ChoiceBox<String> timeToTrigger;

    @Override
    public String getPathToFXML() {
        String pathToFxml = "/fragments/fragment_productParams.fxml";
        return pathToFxml;
    }

    public boolean checkFields(){
        var answer = true;

        var productProxy = checkUrlField();
        if (productProxy != null){
                url.setStyle("-fx-background-color: #c1ffb2");

            if (checkNameField(productProxy)){
                name.setStyle("-fx-background-color: #c1ffb2");
            }else {
                answer = false;
                name.setStyle("-fx-background-color: #FDB7B7;");
            }
            if (checkTriggerField()){
                timeToTrigger.setStyle("-fx-background-color: #c1ffb2");
            }else {
                answer = false;
                timeToTrigger.setStyle("-fx-background-color: #FDB7B7;");
            }

            return answer;
        }else {
            url.setStyle("-fx-background-color: #FDB7B7;");
            return false;
        }
    }

    public abstract Product saveProduct();

    protected abstract ProductProxy getProductProxy(String linkToProduct) throws IOException;

    protected void initTimeToTriggerBox(){
        String[] rows = new String[]{
                "1 час",
                "12 час",
                "24 час",
        };
        ObservableList<String> items =
                FXCollections.observableArrayList(rows);
        timeToTrigger.setItems(items);
    }

    protected ProductProxy checkUrlField(){
        String linkToProduct = url.getText();
        linkToProduct = StringUtils.trimAllWhitespace(linkToProduct);
        if (StringUtils.hasText(linkToProduct)) {
            try {
                var productProxy = getProductProxy(linkToProduct);
                return productProxy;
            }catch (IOException exception) {
                return null;
            }
        }else{
            timeToTrigger.setStyle("-fx-background-color: #FDB7B7;");
            return null;
        }
    }

    protected boolean checkNameField(ProductProxy productProxy){
        String productName = name.getText();
        productName = StringUtils.trimAllWhitespace(productName);
        if (StringUtils.hasText(productName)) {
            name.setText(productName);
        } else {
            String nameFromPage = productProxy.getName();
            name.setText(nameFromPage);
        }

        return true;
    }

    protected boolean checkTriggerField(){
        return true;
    }
}
