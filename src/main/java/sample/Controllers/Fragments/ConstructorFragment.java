package sample.Controllers.Fragments;

import sample.Databases.ProductsTable;
import sample.ProductProxys.ProductProxy;
import sample.Products.Product;

public abstract class ConstructorFragment extends ViewFragment {
    protected ProductProxy productProxy;
    protected ProductsTable productsTable;

    public abstract boolean checkFields();

    public abstract Product generateProduct();
}
