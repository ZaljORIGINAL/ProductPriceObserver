package sample.Databases;

import sample.Databases.Contracts.ProductPricesContract;
import sample.Products.Price;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProductPricesTable extends DatabaseTable {
    public ProductPricesTable(String tableName) throws SQLException {
        super(tableName);
    }

    @Override
    public boolean createTable() throws SQLException {
        try (var connection = getConnection()){
            String sqlCommand = "CREATE TABLE " +
                    tableName + " " +
                    "(" +
                    ProductPricesContract.ID_COLUMN + " SERIAL PRIMARY KEY, " +
                    ProductPricesContract.DATE_COLUMN + " TIMESTAMP NOT NULL, " +
                    ProductPricesContract.PRICE_COLUMN + " NUMERIC NOT NULL " +
                    ")";

            try(var statement =
                        connection.prepareStatement(sqlCommand)){
                return statement.execute();
            }
        }
    }

    public List<Price> getAll() throws SQLException{
        try (var connection = getConnection()){
            String sqlConnection = "SELECT * FROM " +
                    tableName + " ";
            try (var statement =
                    connection.prepareStatement(sqlConnection)){
                try (var result = statement.executeQuery()){
                    List<Price> answer = new ArrayList<>();
                    while (result.next()){
                        var price = extractToPrice(result);
                        answer.add(price);
                    }
                    return answer;
                }
            }
        }
    }

    public Price getById(int id) throws SQLException{
        try (var connection = getConnection()){
            var sqlCommand = "SELECT * FROM " +
                    tableName + " ";

            try (var statement =
                         connection.prepareStatement(sqlCommand)) {
                try (var result = statement.executeQuery()) {

                    Price price = null;
                    if (result.next())
                        price = extractToPrice(result);

                    return price;
                }
            }
        }
    }

    public ArrayList<Price> getById(ArrayList<Integer> ids) throws SQLException{
        ArrayList<Price> prices = new ArrayList<>();
        for (Integer id : ids) {
            var price = getById(id);
            prices.add(price);
        }

        return prices;
    }

    public Price insert(Price price) throws SQLException{
        try (var connection = getConnection()){
            String sqlCommand = "INSERT INTO " +
                    tableName + " " +
                    "(" +
                    ProductPricesContract.DATE_COLUMN + ", " +
                    ProductPricesContract.PRICE_COLUMN + ") " +
                    "VALUES (?, ?) RETURNING " + ProductPricesContract.ID_COLUMN;

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

    public List<Price> insert(ArrayList<Price> prices) throws SQLException{
        List<Price> inserted = new ArrayList<>();
        for (Price price : prices) {
            var result = insert(price);
            inserted.add(result);
        }

        return inserted;
    }

    public Price getLastPrice() throws SQLException{
        try (var connection = getConnection()){
            String sqlCommand = "SELECT * FROM " +
                    tableName + " " +
                    "WHERE " + ProductPricesContract.ID_COLUMN + "=" +
                    "(SELECT MAX(" + ProductPricesContract.ID_COLUMN + ") FROM " +
                    tableName + " " +
                    ")";

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
