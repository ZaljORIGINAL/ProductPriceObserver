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
    public TextField linkField;
    public TextField nameField;
    public ChoiceBox<String> choiceField;

    @Override
    public String getPathToFXML() {
        String pathToFxml = "/fragments/fragment_productParams.fxml";
        return pathToFxml;
    }

    //TODO Если имя не было введено, то прописать автоматически, НО НЕ СОХРАНЯТЬ.
    // продемонстрировать пользоватлю вставленное имя для его редактирования.
    public boolean checkFields(){
        var answer = true;

        var productProxy = checkUrlField();
        if (productProxy != null){
                linkField.setStyle("-fx-background-color: #c1ffb2");

            if (checkNameField(productProxy)){
                nameField.setStyle("-fx-background-color: #c1ffb2");
            }else {
                answer = false;
                nameField.setStyle("-fx-background-color: #FDB7B7;");
            }
            if (checkTriggerField()){
                choiceField.setStyle("-fx-background-color: #c1ffb2");
            }else {
                answer = false;
                choiceField.setStyle("-fx-background-color: #FDB7B7;");
            }

            return answer;
        }else {
            linkField.setStyle("-fx-background-color: #FDB7B7;");
            return false;
        }
    }

    public abstract Product saveProduct();

    protected abstract ProductProxy getProductProxy(String linkToProduct) throws IOException;

    protected void initTriggerSelectBox(){
        String[] rows = new String[]{
                "1 час",
                "12 час",
                "24 час",
        };
        ObservableList<String> items =
                FXCollections.observableArrayList(rows);
        choiceField.setItems(items);
        choiceField.setValue(rows[0]);
    }

    protected ProductProxy checkUrlField(){
        String linkToProduct = linkField.getText();
        linkToProduct = StringUtils.trimAllWhitespace(linkToProduct);
        if (StringUtils.hasText(linkToProduct)) {
            try {
                var productProxy = getProductProxy(linkToProduct);
                return productProxy;
            }catch (IOException exception) {
                return null;
            }
        }else{
            choiceField.setStyle("-fx-background-color: #FDB7B7;");
            return null;
        }
    }

    protected boolean checkNameField(ProductProxy productProxy){
        String productName = nameField.getText();
        if (StringUtils.hasText(productName)) {
            nameField.setText(productName);
        } else {
            String nameFromPage = productProxy.getName();
            nameField.setText(nameFromPage);
        }

        return true;
    }

    protected boolean checkTriggerField(){
        return true;
    }
}
