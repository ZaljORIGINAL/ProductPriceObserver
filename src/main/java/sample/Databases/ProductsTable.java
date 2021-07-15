package sample.Databases;

import sample.Databases.Contracts.ProductTableContract;
import sample.Products.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductsTable extends DatabaseTable {

    public ProductsTable(String tableName) throws SQLException {
        super(tableName);
    }

    @Override
    public boolean createTable() throws SQLException {
        try (var connection = getConnection()){
            String sqlCommand = "CREATE TABLE " +
                    tableName + " " +
                    "(" +
                    ProductTableContract.ID_COLUMN + " SERIAL PRIMARY KEY, " +
                    ProductTableContract.URL_COLUMN + " CHARACTER VARYING("+ ProductTableContract.MAX_SYMBOL_TO_URL +") NOT NULL, " +
                    ProductTableContract.NAME_COLUMN + " CHARACTER VARYING("+ ProductTableContract.MAX_SYMBOL_TO_NAME +") NOT NULL, " +
                    ProductTableContract.TRIGGER_COLUMN + " BIGINT NOT NULL" +
                    ")";

            try(var statement =
                        connection.prepareStatement(sqlCommand)){
                return statement.execute();
            }
        }
    }

    public Product getById(int id) throws SQLException{
        try(var connection = getConnection()){
            var sqlCommand = "SELECT * FROM " +
                    tableName + " WHERE " + ProductTableContract.ID_COLUMN + "=?";

            try (var statement =
                         connection.prepareStatement(sqlCommand)) {
                statement.setInt(1, id);
                var result = statement.executeQuery();

                Product product = null;
                if (result.next())
                    product = extractToProduct(result);
                return product;
            }
        }
    }

    public List<Product> getByTrigger(long triggerTime) throws SQLException{
        String sqlCommand = "SELECT * FROM " +
                tableName + " " +
                "WHERE " +
                ProductTableContract.TRIGGER_COLUMN + " = ?";
        try (var connection = getConnection()){
            try (var statement = connection.prepareStatement(sqlCommand)){
                statement.setLong(1, triggerTime);
                try (var result = statement.executeQuery()){
                    List<Product> answer = new ArrayList<>();
                    while (result.next()){
                        var product = extractToProduct(result);
                        answer.add(product);
                    }

                    return answer;
                }
            }
        }
    }

    public List<Product> getAll() throws SQLException{
        try (var connection = getConnection()){
            String sqlCommand = "SELECT * FROM " +
                    tableName +" ";
            try(var statement =
                        connection.prepareStatement(sqlCommand)) {
                try (var result = statement.executeQuery()) {

                    ArrayList<Product> products = new ArrayList<>();
                    while (result.next()) {
                        var product = extractToProduct(result);
                        products.add(product);
                    }

                    return products;
                }
            }
        }
    }

    public Product insert(Product product) throws SQLException{
        try (var connection = getConnection()){
            String sqlCommand = "INSERT INTO " +
                    tableName + " " +
                    "(" +
                    ProductTableContract.URL_COLUMN + ", " +
                    ProductTableContract.NAME_COLUMN + ", " +
                    ProductTableContract.TRIGGER_COLUMN +
                    ") " +
                    "VALUES (?, ?, ?) " +
                    "RETURNING  " + ProductTableContract.ID_COLUMN;

            try (var statement = connection.prepareStatement(sqlCommand)) {
                statement.setString(1, product.getLink());
                statement.setString(2, product.getName());
                statement.setLong(3, product.getTriggerPeriod());
                try(var result = statement.executeQuery()){
                    result.next();
                    var id = result.getInt(ProductTableContract.ID_COLUMN);
                    String priceTableName = tableName + "_price_of_" + id;
                    product = new Product(
                            id,
                            product.getLink(),
                            product.getName(),
                            product.getTriggerPeriod(),
                            priceTableName);
                    return product;
                }
            }
        }
    }

    public int update(Product product) throws SQLException{
        try (var connection = getConnection()){
            String sqlCommand = "UPDATE " +
                    tableName  + " " +
                    "SET " +
                    ProductTableContract.URL_COLUMN + " = ?, " +
                    ProductTableContract.NAME_COLUMN + " = ?, " +
                    ProductTableContract.TRIGGER_COLUMN + " = ? " +
                    "WHERE " +
                    ProductTableContract.ID_COLUMN + " = ?";

            try (var statement = connection.prepareStatement(sqlCommand)) {
                statement.setString(1, product.getLink());
                statement.setString(2, product.getName());
                statement.setLong(3, product.getTriggerPeriod());
                statement.setInt(4, product.getId());

                return statement.executeUpdate();
            }
        }
    }

    public int delete(int id) throws SQLException{
        try (var connection = getConnection()){
            String sqlCommand = "DELETE FROM " +
                    tableName + " " +
                    "WHERE " +
                    ProductTableContract.ID_COLUMN + " = ?";

            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setInt(1, id);
            return statement.executeUpdate();
        }
    }

    public int delete(Product product) throws SQLException{
        return delete(product.getId());
    }

    protected Product extractToProduct(ResultSet result) throws SQLException{
        int id = result.getInt(
                ProductTableContract.ID_COLUMN);
        String url = result.getString(
                ProductTableContract.URL_COLUMN);
        String name = result.getString(
                ProductTableContract.NAME_COLUMN);
        long triggerPeriod = result.getLong(
                ProductTableContract.TRIGGER_COLUMN);
        String priceTableName = tableName + "_price_of_" + id;

        return new Product(id, url, name, triggerPeriod, priceTableName);
    }
}
