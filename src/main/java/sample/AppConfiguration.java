package sample;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import sample.Databases.Contracts.ProductPricesTableContract;
import sample.Databases.Contracts.ProductTableContract;
import sample.Databases.ProductPricesTable;
import sample.Databases.ProductsTable;
import sample.ShopToolsFactories.Factorys.CitilinkShopToolsFactory;

import java.sql.SQLException;

@Configuration
public class AppConfiguration {
    public static final Logger logger = LogManager.getLogger(AppConfiguration.class);

    @Bean
    @Scope("singleton")
    public CitilinkShopToolsFactory citilinkShopToolsFactory(){
        return new CitilinkShopToolsFactory();
    }

    @Bean
    @Scope("singleton")
    public ProductsTable productsTable(){
        try {
            return new ProductsTable(ProductTableContract.TABLE_NAME);
        } catch (SQLException exception) {
            logger.error("Ошибка при работе с базой данных", exception);
            return null;
        }
    }

    @Bean
    @Scope("singleton")
    public ProductPricesTable productPricesTable(){
        try {
            return new ProductPricesTable(ProductPricesTableContract.TABLE_NAME);
        } catch (SQLException exception) {
            logger.error("Ошибка при работе с базой данных", exception);
            return null;
        }
    }
}
