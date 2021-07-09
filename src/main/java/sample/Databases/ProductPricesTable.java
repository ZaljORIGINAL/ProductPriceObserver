package sample.Databases;

import sample.Databases.Contracts.DatabaseContract;
import sample.Databases.Contracts.PriceTableContract;
import sample.Products.Price;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class ProductPricesTable extends DatabaseTable {
    /*TODO В целях сохренения уникальности наименований таблиц данных
       следует задать следующую логику формирования наименования таблци
       данных по ценам по продукту отдельного магазина */
    public ProductPricesTable(String tableName) throws SQLException{
        this.tableName = tableName;

        openConnection();
        if (!existsTable()){
            createTable();
        }
        closeConnection();
    }

    public String getName() {
        return tableName;
    }

    @Override
    public boolean createTable() throws SQLException {
        String sqlCommand = "CREATE TABLE " + tableName +
                " ( " +
                PriceTableContract.ID_COLUMN + " INT PRIMARY KEY IDENTITY, " +
                PriceTableContract.DATE_COLUMN + " DATETIME NOT NULL, " +
                PriceTableContract.PRICE_COLUMN + " MONEY NOT NULL " +
                ")";
        var statement = connection.prepareStatement(sqlCommand);

        if (statement.executeUpdate() == 1)
            return true;
        else
            return false;
    }

    public Connection openConnection() throws SQLException {
        connection = DriverManager.getConnection(
                DATABASE_URL + "databaseName=" + DATABASE_NAME,
                DatabaseContract.USER_NAME,
                DatabaseContract.USER_PASSWORD);
        return connection;
    }

    public Price getById(int id) throws SQLException{
        var sqlCommand = "SELECT * FROM " +
                tableName + " ";

        var statement =
                connection.prepareStatement(sqlCommand);
        var result = statement.executeQuery();

        Price price = null;
        if (result.next())
            price = extractToPrice(result);

        return price;
    }

    public ArrayList<Price> getById(ArrayList<Integer> ids) throws SQLException{
        ArrayList<Price> prices = new ArrayList<>();
        for (Integer id : ids) {
            var price = getById(id);
            prices.add(price);
        }

        return prices;
    }

    public int insert(Price price) throws SQLException{
        String sqlCommand = "INSERT INTO " +
                tableName + " " +
                "(" +
                PriceTableContract.DATE_COLUMN + ", " +
                PriceTableContract.PRICE_COLUMN + ") " +
                "VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sqlCommand) ) {
            Object time = new java.sql.Timestamp(price.getDate().getTimeInMillis());
            statement.setObject(1, time);
            statement.setFloat(2, price.getPrice());
            return statement.executeUpdate();
        }

    }

    public int insert(ArrayList<Price> prices) throws SQLException{
        int changedRows = 0;
        for (Price price : prices) {
            var result = insert(price);
            changedRows += result;
        }

        return changedRows;
    }

    public Price getLastPrice() throws SQLException{
        String sqlCommand = "SELECT * FROM " +
                tableName + " " +
                "WHERE " + PriceTableContract.ID_COLUMN + "=" +
                "(SELECT " + PriceTableContract.ID_COLUMN + " FROM " +
                tableName +
                "WHERE MAX(" + PriceTableContract.ID_COLUMN + "))";

        PreparedStatement statement = connection.prepareStatement(sqlCommand);
        ResultSet result = statement.executeQuery();
        result.next();

        return extractToPrice(result);
    }

    private Price extractToPrice(ResultSet result) throws SQLException{
        var id = result.getInt(
                PriceTableContract.ID_COLUMN);
        var d = (Timestamp) result.getObject(
                PriceTableContract.DATE_COLUMN);
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(d.getTime());
        var price = result.getFloat(
                PriceTableContract.PRICE_COLUMN);

        return new Price(id, date, price);
    }
}
