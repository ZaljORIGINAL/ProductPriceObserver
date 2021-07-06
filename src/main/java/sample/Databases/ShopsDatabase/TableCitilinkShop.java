package sample.Databases.ShopsDatabase;

import sample.Databases.Contracts.ProductTableContract;
import sample.Databases.ProductsTable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TableCitilinkShop extends ProductsTable {
    public TableCitilinkShop(){
        this.tableName = "CitilinkProducts";
    }

    //FIXME Запрос исполняется удачно, но в ответ приходит false
    @Override
    public boolean createTable() throws SQLException {
        String sqlCommand = "CREATE TABLE " + tableName +
                " ( " +
                ProductTableContract.ID_COLUMN + " INT PRIMARY KEY IDENTITY(1,1), " +
                ProductTableContract.URL_COLUMN + " NVARCHAR(" + ProductTableContract.MAX_SYMBOL_TO_URL + ") NOT NULL, " +
                ProductTableContract.NAME_COLUMN + " NVARCHAR(" + ProductTableContract.MAX_SYMBOL_TO_NAME + ") NOT NULL, " +
                ProductTableContract.PRICE_TABLE_NAME_COLUMN + " NVARCHAR(" + ProductTableContract.MAX_SYMBOL_TO_PRICE_TABLE_NAME + ") NOT NULL " +
                " ) ";

        PreparedStatement statement = connection.prepareStatement(sqlCommand);
        return statement.execute();
    }
}
