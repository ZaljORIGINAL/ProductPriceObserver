module Task2 {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires spring.core;
    requires spring.context;
    requires java.sql;
    requires org.jsoup;

    opens sample;
    opens sample.Databases;
    opens sample.Databases.Contracts;
    opens sample.Databases.ShopsDatabase;
    opens sample.Observers;
    opens sample.Observers.Types;
    opens sample.Observers.Types.Parsing;
    opens sample.Products;
    opens sample.Controllers.Windows;
    opens sample.Controllers.Fragments;
    opens sample.Controllers.Fragments.MainWindow.Tables;
    opens sample.Controllers.Fragments.MainWindow.Tables.ProductsTables;
    opens sample.Controllers.Fragments.ProductConstructors;
    opens sample.Controllers.Fragments.ProductEditors;
}