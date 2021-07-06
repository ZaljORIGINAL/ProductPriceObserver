package sample.Databases;

import sample.Databases.Contracts.DatabaseContract;
import sample.Databases.Contracts.ProductTableContract;
import sample.Products.Product;

import java.sql.*;
import java.util.ArrayList;

public abstract class ProductsTable extends DatabaseTable {
    public ProductsTable() { }

    public String getName(){
        return tableName;
    }

    public Connection openConnection() throws SQLException {
        connection = DriverManager.getConnection(
                DATABASE_URL + "databaseName=" + DATABASE_NAME + " ",
                DatabaseContract.USER_NAME,
                DatabaseContract.USER_PASSWORD);

        return connection;
    }

    public Product getById(int id) throws SQLException{
        var sqlCommand = "SELECT * FROM " +
                tableName + " WHERE " + ProductTableContract.ID_COLUMN + "=?";

        var statement =
                connection.prepareStatement(sqlCommand);
        statement.setInt(1, id);
        var result = statement.executeQuery();

        Product product = null;
        if (result.next())
            product = extractToProduct(result);
        return product;
    }

    public ArrayList<Product> getById(ArrayList<Integer> ids) throws SQLException{
        ArrayList<Product> products = new ArrayList<>();
        for (Integer id : ids) {
            var product = getById(id);
            products.add(product);
        }

        return products;
    }

    public ArrayList<Product> getAll() throws SQLException{
        String sqlCommand = "SELECT * FROM " +
                tableName +" ";
        var statement =
                connection.prepareStatement(sqlCommand);
        var result = statement.executeQuery();

        ArrayList<Product> products = new ArrayList<>();
        while (result.next()){
            var product = extractToProduct(result);
        }

        return products;
    }

    public int insert(Product product) throws SQLException{
        String sqlCommand = "INSERT INTO " +
                tableName + " " +
                "(" +
                ProductTableContract.URL_COLUMN + ", " +
                ProductTableContract.NAME_COLUMN + ") " +
                "VALUES (?, ?)";

        PreparedStatement statement = connection.prepareStatement(sqlCommand);
        statement.setString(1, product.getLink());
        statement.setString(2, product.getName());
        return statement.executeUpdate();
    }

    public int insert(ArrayList<Product> products) throws SQLException{
        int changedRows = 0;
        for (Product product : products) {
            var result = insert(product);
            changedRows += result;
        }

        return changedRows;
    }

    public int update(Product product) throws SQLException{
        String sqlCommand = "UPDATE " +
                tableName  + " " +
                "SET " +
                ProductTableContract.URL_COLUMN + " = ?, " +
                ProductTableContract.NAME_COLUMN + " = ? " +
                "WHERE " +
                ProductTableContract.ID_COLUMN + " = ?";

        PreparedStatement statement = connection.prepareStatement(sqlCommand);
        statement.setString(1, product.getLink());
        statement.setString(2, product.getName());
        statement.setInt(3, product.getId());

        return statement.executeUpdate();
    }

    public int update(ArrayList<Product> products) throws SQLException{
        int changedRows = 0;
        for (Product product : products){
            int answer = update(product);
            changedRows += answer;
        }

        return  changedRows;
    }

    public int delete(int id) throws SQLException{
        String sqlCommand = "DELETE FROM " +
                tableName + " " +
                "WHERE " +
                ProductTableContract.ID_COLUMN + " = ?";

        PreparedStatement statement = connection.prepareStatement(sqlCommand);
        statement.setInt(1, id);
        return statement.executeUpdate();
    }

    public int delete(int[] ids) throws SQLException{
        int changedRows = 0;
        for (int id : ids) {
            int answer = delete(id);
            changedRows += answer;
        }

        return changedRows;
    }

    public int delete(Product product) throws SQLException{
        return delete(product.getId());
    }

    public int delete(ArrayList<Product> products) throws SQLException{
        int changedRows = 0;
        for (Product product : products) {
            int answer = delete(product);
            changedRows += answer;
        }

        return changedRows;
    }

    protected Product extractToProduct(ResultSet result) throws SQLException{
        int id = result.getInt(
                ProductTableContract.ID_COLUMN);
        String url = result.getString(
                ProductTableContract.URL_COLUMN);
        String name = result.getString(
                ProductTableContract.NAME_COLUMN);
        String priceTableName = result.getString(
                ProductTableContract.PRICE_TABLE_NAME_COLUMN);

        return new Product(id, url, name, priceTableName);
    }
}
