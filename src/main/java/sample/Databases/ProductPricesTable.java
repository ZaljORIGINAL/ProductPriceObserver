package sample.Databases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sample.Databases.Contracts.ProductPricesContract;
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
                    ProductPricesContract.ID_COLUMN + " SERIAL PRIMARY KEY, " +
                    ProductPricesContract.DATE_COLUMN + " TIMESTAMP NOT NULL, " +
                    ProductPricesContract.PRICE_COLUMN + " NUMERIC NOT NULL " +
                    ")";

            logger.info("Конструкция запроса: " + sqlCommand);
            try(var statement =
                        connection.prepareStatement(sqlCommand)){
                return statement.execute();
            }
        }
    }

    public List<Price> getAll() throws SQLException{
        logger.info("Запрос на получение всех элементов таблици");
        try (var connection = getConnection()){
            String sqlCommand = "SELECT * FROM " +
                    tableName + " ";

            logger.info("Конструкция запроса: " + sqlCommand);
            try (var statement =
                    connection.prepareStatement(sqlCommand)){
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

    public Price getById(int id) throws SQLException{
        logger.info("Запрос на получение элемента из таблици: " + id);
        try (var connection = getConnection()){
            var sqlCommand = "SELECT * FROM " +
                    tableName + " ";

            logger.info("Конструкция запроса: " + sqlCommand);
            try (var statement =
                         connection.prepareStatement(sqlCommand)) {
                try (var result = statement.executeQuery()) {

                    Price price = null;
                    if (result.next())
                        price = extractToPrice(result);

                    if (price != null){
                        logger.info("Найдена соответсвующая запись: " + price);
                        return price;
                    }else {
                        logger.info("Запись не найдена!");
                        return price;
                    }
                }
            }
        }
    }

    public Price insert(Price price) throws SQLException{
        logger.info("Запрос на добавление записи о цене...");
        try (var connection = getConnection()){
            String sqlCommand = "INSERT INTO " +
                    tableName + " " +
                    "(" +
                    ProductPricesContract.DATE_COLUMN + ", " +
                    ProductPricesContract.PRICE_COLUMN + ") " +
                    "VALUES (?, ?) RETURNING " + ProductPricesContract.ID_COLUMN;

            logger.info("Конструкция запроса: " + sqlCommand);
            try (var statement = connection.prepareStatement(sqlCommand) ) {
                Timestamp time = new java.sql.Timestamp(price.getCalendar().getTimeInMillis());
                statement.setTimestamp(1, time);
                statement.setFloat(2, price.getPrice());
                try (var result = statement.executeQuery()){
                    result.next();
                    var id = result.getInt(ProductPricesContract.ID_COLUMN);

                    return new Price(id, price.getCalendar(), price.getPrice());
                }
            }
        }
    }

    public Price getLastPrice() throws SQLException{
        logger.info("Запрос на получение последней цены...");
        try (var connection = getConnection()){
            String sqlCommand = "SELECT * FROM " +
                    tableName + " " +
                    "WHERE " + ProductPricesContract.ID_COLUMN + "=" +
                    "(SELECT MAX(" + ProductPricesContract.ID_COLUMN + ") FROM " +
                    tableName + " " +
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

    private Price extractToPrice(ResultSet result) throws SQLException{
        var id = result.getInt(
                ProductPricesContract.ID_COLUMN);
        var d = (Timestamp) result.getObject(
                ProductPricesContract.DATE_COLUMN);
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(d.getTime());
        var price = result.getFloat(
                ProductPricesContract.PRICE_COLUMN);

        return new Price(id, date, price);
    }
}
