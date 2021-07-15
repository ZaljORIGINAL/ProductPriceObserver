module Task2 {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires spring.core;
    requires spring.context;
    requires java.sql;
    requires org.jsoup;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires com.google.common;

    opens sample;
    opens sample.Controllers.Windows;
    opens sample.Controllers.Fragments;
    opens sample.Controllers.Fragments.MainWindow.Tables;
    opens sample.Controllers.Fragments.MainWindow.Tables.ProductsTables;
    opens sample.Controllers.Fragments.ProductConstructors;
    opens sample.Controllers.Fragments.ProductEditors;
    opens sample.Databases;
    opens sample.Databases.Contracts;
    opens sample.ProductObserver;
    opens sample.ShopToolsFactories.Factorys;
    opens sample.ProductProxys;
    opens sample.ProductProxys.Parsers;
    opens sample.ProductProxys.ShopApi;
    opens sample.Products;
    opens sample.ShopToolsFactories;
}