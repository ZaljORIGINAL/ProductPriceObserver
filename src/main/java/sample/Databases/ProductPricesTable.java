package sample.Databases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sample.Databases.Contracts.ProductPricesTableContract;
import sample.Products.Price;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProductPricesTable extends DatabaseTable {
    protected static final Logger logger = LogManager.getLogger(ProductPricesTable.class);

    public ProductPricesTable(String tableName) throws SQLException {
        super(tableName);
    }

    @Override
    public boolean createTable() throws SQLException {
        logger.info("Запрос на создание таблици цен...");
        try (var connection = getConnection()){
            String sqlCommand = "CREATE TABLE " +
                    tableName + " " +
                    "(" +
                    ProductPricesTableContract.ID_COLUMN + " SERIAL PRIMARY KEY, " +
                    ProductPricesTableContract.ID_PRODUCT_COLUMN + " INTEGER NOT NULL REFERENCES Products(Id) ON DELETE CASCADE, " +
                    ProductPricesTableContract.DATE_COLUMN + " TIMESTAMP NOT NULL, " +
                    ProductPricesTableContract.PRICE_COLUMN + " NUMERIC NOT NULL " +
                    ")";

            logger.info("Конструкция запроса: " + sqlCommand);
            try(var statement =
                        connection.prepareStatement(sqlCommand)){
                return statement.execute();
            }
        }
    }

    public List<Price> getByProduct(int idProduct) throws SQLException{
        logger.info("Запрос на получение всех элементов таблици");
        try (var connection = getConnection()){
            String sqlCommand = "SELECT * FROM " +
                    tableName + " " +
                    "WHERE " + ProductPricesTableContract.ID_PRODUCT_COLUMN + " = ?";

            logger.info("Конструкция запроса: " + sqlCommand);
            try (var statement =
                    connection.prepareStatement(sqlCommand)){
                statement.setInt(1, idProduct);
                try (var result = statement.executeQuery()){
                    List<Price> answer = new ArrayList<>();
                    while (result.next()){
                        var price = extractToPrice(result);
                        answer.add(price);
                    }

                    logger.info("Количество полученных записей: " + answer.size());
                    return answer;
                }
            }
        }
    }


    public Price getLastPriceByProduct(int idProduct) throws SQLException{
        logger.info("Запрос на получение последней цены...");
        try (var connection = getConnection()){
            String sqlCommand = "SELECT * FROM " +
                    tableName + " " +
                    "WHERE " + ProductPricesTableContract.ID_COLUMN + "=" +
                        "(SELECT MAX(" + ProductPricesTableContract.ID_COLUMN + ") FROM " +
                            "(SELECT * WHERE " + ProductPricesTableContract.ID_PRODUCT_COLUMN + " = ?) " +
                    ")";

            logger.info("Конструкция запроса: " + sqlCommand);
            try (var statement =
                         connection.prepareStatement(sqlCommand)) {
                ResultSet result = statement.executeQuery();
                result.next();

                return extractToPrice(result);
            }
        }
    }

    public Price insert(Price price) throws SQLException{
        logger.info("Запрос на добавление записи о цене...");
        try (var connection = getConnection()){
            String sqlCommand = "INSERT INTO " +
                    tableName + " " +
                    "(" +
                    ProductPricesTableContract.ID_PRODUCT_COLUMN + ", " +
                    ProductPricesTableContract.DATE_COLUMN + ", " +
                    ProductPricesTableContract.PRICE_COLUMN + ") " +
                    "VALUES (?, ?, ?) RETURNING " + ProductPricesTableContract.ID_COLUMN;

            logger.info("Конструкция запроса: " + sqlCommand);
            try (var statement = connection.prepareStatement(sqlCommand) ) {
                Timestamp time = new java.sql.Timestamp(price.getCalendar().getTimeInMillis());
                statement.setInt(1, price.getIdProduct());
                statement.setTimestamp(2, time);
                statement.setFloat(3, price.getPrice());
                try (var result = statement.executeQuery()){
                    result.next();
                    var id = result.getInt(ProductPricesTableContract.ID_COLUMN);

                    return new Price(id, price.getCalendar(), price.getPrice());
                }
            }
        }
    }

    private Price extractToPrice(ResultSet result) throws SQLException{
        var id = result.getInt(
                ProductPricesTableContract.ID_COLUMN);
        var idProduct = result.getInt(
                ProductPricesTableContract.ID_PRODUCT_COLUMN);
        var d = (Timestamp) result.getObject(
                ProductPricesTableContract.DATE_COLUMN);
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(d.getTime());
        var price = result.getFloat(
                ProductPricesTableContract.PRICE_COLUMN);

        return new Price(id, idProduct, date, price);
    }
}
