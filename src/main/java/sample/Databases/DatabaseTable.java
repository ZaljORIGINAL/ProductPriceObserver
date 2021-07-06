package sample.Databases;

import sample.Databases.Contracts.DatabaseContract;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class DatabaseTable {
    protected static final String DATABASE_URL =
            DatabaseContract.DATABASE_URL;
    protected static final String DATABASE_NAME =
            DatabaseContract.DATABASE_NAME;
    protected String tableName;
    protected Connection connection;

    public DatabaseTable() { }

    public abstract boolean createTable() throws SQLException;

    public boolean deleteTable() throws SQLException{
        String sqlCommand = "DROP TABLE " + tableName;

        PreparedStatement statement = connection.prepareStatement(sqlCommand);
        return statement.execute();
    }

    public boolean existsTable() throws SQLException{
        DatabaseMetaData metaData = connection.getMetaData();
        var result = metaData.getTables(
                null,
                null,
                tableName,
                null);
        if (result.next())
            return true;
        return false;
    }

    public abstract Connection openConnection() throws SQLException;

    public boolean closeConnection() throws SQLException{
        if (connection != null)
            connection.close();
        else
            return true;

        return connection.isClosed();
    }
}
