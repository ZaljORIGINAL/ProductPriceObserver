package sample.Controllers.Fragments;

import sample.Products.Product;

public abstract class ProductEditorFragment extends ProductParamFragment{
    protected Product product;

    public ProductEditorFragment(Product product){
        this.product = product;
    }
}
