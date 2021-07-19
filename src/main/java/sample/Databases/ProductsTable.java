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
        logger.info("Запрос на создание таблици продуктов...");
        try (var connection = getConnection()){
            String sqlCommand = "CREATE TABLE " +
                    tableName + " " +
                    "(" +
                    ProductTableContract.ID_COLUMN + " SERIAL PRIMARY KEY, " +
                    ProductTableContract.ID_SHOP_COLUMN + " INTEGER NOT NULL, " +
                    ProductTableContract.URL_COLUMN + " CHARACTER VARYING("+ ProductTableContract.MAX_SYMBOL_TO_URL +") NOT NULL, " +
                    ProductTableContract.NAME_COLUMN + " CHARACTER VARYING("+ ProductTableContract.MAX_SYMBOL_TO_NAME +") NOT NULL, " +
                    ProductTableContract.OBSERVER_PERIOD_COLUMN + " BIGINT NOT NULL" +
                    ")";

            logger.info("Конструкция запроса: " + sqlCommand);
            try(var statement =
                        connection.prepareStatement(sqlCommand)){
                return statement.execute();
            }
        }
    }

    public Product getById(int idProduct) throws SQLException{
        logger.info("Запрос на получение товаров по параметру:\n" +
                "\tid продукта: " + idProduct);
        try (var connection = getConnection()){
            String sqlCommand = "SELECT * FROM " +
                    tableName +" " +
                    "WHERE " + ProductTableContract.ID_COLUMN + " = ?";
            logger.info("Конструкция запроса: " + sqlCommand);

            try(var statement =
                        connection.prepareStatement(sqlCommand)) {
                statement.setInt(1, idProduct);
                try (var result = statement.executeQuery()) {
                    result.next();
                    var product = extractToProduct(result);
                    return product;
                }
            }
        }
    }

    public List<Product> getByShop(int idShop) throws SQLException{
        logger.info("Запрос на получение товаров по параметру:\n" +
                "\tid магазина: " + idShop);
        try (var connection = getConnection()){
            String sqlCommand = "SELECT * FROM " +
                    tableName +" " +
                    "WHERE " + ProductTableContract.ID_SHOP_COLUMN + " = ?";
            logger.info("Конструкция запроса: " + sqlCommand);
            try(var statement =
                        connection.prepareStatement(sqlCommand)) {
                statement.setInt(1, idShop);
                try (var result = statement.executeQuery()) {
                    ArrayList<Product> products = new ArrayList<>();
                    while (result.next()) {
                        var product = extractToProduct(result);
                        products.add(product);
                    }

                    logger.info("Количество полученных записей: " + products.size());
                    return products;
                }
            }
        }
    }

    public List<Product> getByTrigger(int idShop, long triggerTime) throws SQLException{
        logger.info("Запрос на получение товаров по параметрам:\n" +
                "\tid магазина: " + idShop + "\n" +
                "\tПериод обнавления: " + triggerTime);
        String sqlCommand = "SELECT * FROM " +
                tableName + " " +
                "WHERE " +
                ProductTableContract.ID_SHOP_COLUMN + " = ? " +
                "AND " +
                ProductTableContract.OBSERVER_PERIOD_COLUMN + " = ?";

        logger.info("Конструкция запроса: " + sqlCommand);
        try (var connection = getConnection()){
            try (var statement = connection.prepareStatement(sqlCommand)){
                statement.setInt(1, idShop);
                statement.setLong(2, triggerTime);
                try (var result = statement.executeQuery()){
                    List<Product> answer = new ArrayList<>();
                    while (result.next()){
                        var product = extractToProduct(result);
                        answer.add(product);
                    }

                    logger.info("Количество полученных записей: " + answer.size());
                    return answer;
                }
            }
        }
    }

    public Product insert(Product product) throws SQLException{
        logger.info("Добавить в таблицу товар:\n" +
                "\tid магазина: " + product.getIdShop() + "\n" +
                "\tURL на страницу товара: " + product.getLink() + "\n" +
                "\tНаименование товара: " + product.getName() + "\n" +
                "\tПериод обновления цены: " + product.getObserverPeriod());
        try (var connection = getConnection()){
            String sqlCommand = "INSERT INTO " +
                    tableName + " " +
                    "(" +
                    ProductTableContract.ID_SHOP_COLUMN + ", " +
                    ProductTableContract.URL_COLUMN + ", " +
                    ProductTableContract.NAME_COLUMN + ", " +
                    ProductTableContract.OBSERVER_PERIOD_COLUMN +
                    ") " +
                    "VALUES (?, ?, ?, ?) " +
                    "RETURNING  " + ProductTableContract.ID_COLUMN;

            logger.info("Конструкция запроса: " + sqlCommand);
            try (var statement = connection.prepareStatement(sqlCommand)) {
                statement.setInt(1, product.getIdShop());
                statement.setString(2, product.getLink());
                statement.setString(3, product.getName());
                statement.setLong(4, product.getObserverPeriod());
                try(var result = statement.executeQuery()){
                    result.next();
                    var id = result.getInt(ProductTableContract.ID_COLUMN);
                    product = new Product(
                            id,
                            product.getIdShop(),
                            product.getLink(),
                            product.getName(),
                            product.getObserverPeriod());
                    return product;
                }
            }
        }
    }

    public int update(Product product) throws SQLException{
        logger.info("Запрос на обнавление записи:\n" +
                "\tid записи: " + product.getIdProduct());
        try (var connection = getConnection()){
            String sqlCommand = "UPDATE " +
                    tableName  + " " +
                    "SET " +
                    ProductTableContract.URL_COLUMN + " = ?, " +
                    ProductTableContract.NAME_COLUMN + " = ?, " +
                    ProductTableContract.OBSERVER_PERIOD_COLUMN + " = ? " +
                    "WHERE " +
                    ProductTableContract.ID_COLUMN + " = ?";

            logger.info("Конструкция запроса: " + sqlCommand);
            try (var statement = connection.prepareStatement(sqlCommand)) {
                statement.setString(1, product.getLink());
                statement.setString(2, product.getName());
                statement.setLong(3, product.getObserverPeriod());
                statement.setInt(4, product.getIdProduct());

                return statement.executeUpdate();
            }
        }
    }

    public int delete(int id) throws SQLException{
        logger.info("Запрос на удаление записи. id записи:\n" +
                "\tid записи: " + id);
        try (var connection = getConnection()){
            String sqlCommand = "DELETE FROM " +
                    tableName + " " +
                    "WHERE " +
                    ProductTableContract.ID_COLUMN + " = ?";

            logger.info("Конструкция запроса: " + sqlCommand);
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setInt(1, id);
            return statement.executeUpdate();
        }
    }

    public int delete(Product product) throws SQLException{
        return delete(product.getIdProduct());
    }

    protected Product extractToProduct(ResultSet result) throws SQLException{
        int id = result.getInt(
                ProductTableContract.ID_COLUMN);
        int idShop = result.getInt(
                ProductTableContract.ID_SHOP_COLUMN);
        String url = result.getString(
                ProductTableContract.URL_COLUMN);
        String name = result.getString(
                ProductTableContract.NAME_COLUMN);
        long triggerPeriod = result.getLong(
                ProductTableContract.OBSERVER_PERIOD_COLUMN);

        return new Product(id, idShop, url, name, triggerPeriod);
    }
}
