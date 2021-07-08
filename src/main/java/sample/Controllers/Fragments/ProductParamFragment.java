package sample.Controllers.Fragments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.springframework.util.StringUtils;
import sample.ProductProxys.ProductProxy;
import sample.Products.Product;

public abstract class ProductParamFragment extends ViewFragment {
    public TextField url;
    public TextField name;
    public ChoiceBox<String> timeToTrigger;
    protected ProductProxy productProxy;

    @Override
    public String getPathToFXML() {
        String pathToFxml = "/fragments/fragment_productParams.fxml";
        return pathToFxml;
    }

    public boolean checkFields(){
        //TODO Перекрашивать поля в соответсвии с результатом
        var answer = true;
        if (!checkUrlField(productProxy))
            answer = false;
        if (!checkNameField())
            answer = false;
        if (!checkTriggerField())
            answer = false;

        saveProduct();
        return answer;
    }

    public Product saveProduct(){
        String url = this.url.getText();
        String productName = this.name.getText();
        //TODO Так же сохранять время тригера
        Product product = new Product(url, productName);
        return product;
    }

    protected void initTimeToTriggerBox(){
        String[] rows = new String[]{
                "Раз в 1 час",
                "Раз в 12 час",
                "Раз в 24 час",
        };
        ObservableList<String> items =
                FXCollections.observableArrayList(rows);
        timeToTrigger.setItems(items);
    }

    protected boolean checkUrlField(ProductProxy productProxy){
        String linkToProduct = url.getText();
        linkToProduct = StringUtils.trimAllWhitespace(linkToProduct);
        if (StringUtils.hasText(linkToProduct)){
            //TODO Прописать логику проверки ссылки на действительность
            return true;
        }else {
            url.setStyle("-fx-background-color: #FDB7B7;");
            return false;
        }
    }

    protected boolean checkNameField(){
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
