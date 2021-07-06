package sample.Controllers.Fragments;

import sample.Databases.ProductsTable;
import sample.ProductProxys.ProductProxy;

public abstract class ConstructorFragment extends ViewFragment {
    protected ProductProxy productProxy;
    protected ProductsTable productsTable;

    public abstract boolean checkFields();

    public abstract boolean saveProduct();
}
