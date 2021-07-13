package sample.Databases;

import sample.Databases.Contracts.DatabaseContract;

import java.sql.*;

public abstract class DatabaseTable {
    protected String tableName;

    public DatabaseTable(String tableName) throws SQLException {
        this.tableName = tableName;

        if (!existsTable())
            createTable();
    }

    public String getTableName(){
        return tableName;
    }

    public Connection getConnection() throws SQLException {
        return open();
    }

    public abstract boolean createTable() throws SQLException;

    public boolean deleteTable() throws SQLException{
        String sqlCommand = "DROP TABLE " + tableName;

        try (var connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.executeUpdate();
            return !existsTable();
        }
    }

    public boolean existsTable() throws SQLException{
        try (var connection = getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            var result = metaData.getTables(
                    null,
                    null,
                    tableName,
                    null);
            return result.next();
        }
    }

    public Connection open() throws SQLException{
        var connection = DriverManager.getConnection(
                DatabaseContract.DATABASE_URL +
                DatabaseContract.DATABASE_NAME);

        return connection;
    }
}
