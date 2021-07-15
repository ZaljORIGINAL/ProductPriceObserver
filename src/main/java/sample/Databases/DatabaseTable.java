package sample.Databases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sample.Databases.Contracts.DatabaseContract;

import java.sql.*;

public abstract class DatabaseTable {
    protected static final Logger logger = LogManager.getLogger(DatabaseTable.class);
    protected String tableName;

    public DatabaseTable(String tableName) throws SQLException {
        logger.info("Конструирование объекта таблици базы данных...");
        this.tableName = tableName;

        if (!existsTable())
            createTable();
        logger.info("Объект таблици базы данных создан.");
    }

    public String getTableName(){
        return tableName;
    }

    public Connection getConnection() throws SQLException {
        logger.info("Запрос соединения к базе данных...");
        return open();
    }

    public abstract boolean createTable() throws SQLException;

    public boolean deleteTable() throws SQLException{
        logger.info("Удаление таблици: " + tableName +"...");
        String sqlCommand = "DROP TABLE " + tableName;

        try (var connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.executeUpdate();
            if(!existsTable()){
                logger.info("Таблица успешно удалена.");
                return true;
            }else {
                logger.info("Таблица не удалена.");
                return false;
            }
        }
    }

    public boolean existsTable() throws SQLException{
        logger.info("Поиск таблици: " + tableName + "...");
        try (var connection = getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            var result = metaData.getTables(
                    null,
                    null,
                    tableName,
                    null);
            if (result.next()){
                logger.info("Таблица найдена.");
                return true;
            }else {
                logger.info("Таблица не найдена.");
                return false;
            }
        }
    }

    public Connection open() throws SQLException{
        logger.info("Формирование соединения с базой данных...");
        var connection = DriverManager.getConnection(
                DatabaseContract.DATABASE_URL +
                DatabaseContract.DATABASE_NAME);

        logger.info("Соединение получено.");
        return connection;
    }
}
