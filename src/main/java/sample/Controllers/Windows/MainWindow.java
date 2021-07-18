package sample.Controllers.Windows;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import sample.Controllers.Fragments.MainWindow.Tables.ProductsTables.CitilinkProductTableView;
import sample.Controllers.Fragments.MainWindow.Tables.ProductTableView;
import sample.OptionsHelper;
import sample.ProductObserver.ProductObserverService;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {
    private static final Logger logger = LogManager.getLogger(MainWindow.class);

    public ApplicationContext context;
    public ChoiceBox<String> shopChoice;
    public Pane tablePanel;
    public Button addNewProduct;
    public Button getMoreInfo;
    public Button deleteProduct;

    private final ProductObserverService productObserverService;
    private ProductTableView productViewTable;

    public MainWindow(ApplicationContext context){
        this.context = context;

        productObserverService = new ProductObserverService(context);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("Инициализация главного окна...");
        //Инициализация элементов упраавления
        initShopChoice();
        initAddNewProductButton();
        initGetMoreInfoButton();
        initDeleteProductButton();

        productObserverService.start();
        logger.info("Инициализация главного окна завершена.");
    }

    public void finishAllServices(){
        productObserverService.finish();
    }

    private void initShopChoice() {
        logger.info("Инициализация выпадающего списка для выбора магазина...");
        String[] choiceVariants = new String[]
                {
                        OptionsHelper.SHOP_CITILINK
                };
        shopChoice.getItems().addAll(choiceVariants);
        shopChoice.setOnAction(actionEvent -> switchTableView());
        shopChoice.setValue(choiceVariants[0]);
        logger.info("Инициализация выпадающего списка для выбора магазина завершена");
    }

    private void initAddNewProductButton(){
        logger.info("Инициализация кнопки ДОБАВИТЬ ПРОДУКТ...");
        addNewProduct.setOnAction(actionEvent -> clickAddNewProduct());
        logger.info("Инициализация кнопки ДОБАВИТЬ ПРОДУКТ завершена.");
    }

    private void initGetMoreInfoButton(){
        logger.info("Инициализация кнопки ПРОСМОТРЕТЬ...");
        getMoreInfo.setDisable(true);
        getMoreInfo.setOnAction(actionEvent -> clickShowInfoAbout());
        logger.info("Инициализация кнопки ПРОСМОТРЕТЬ завершена.");
    }

    private void initDeleteProductButton(){
        logger.info("Инициализация кнопки УДАЛИТЬ...");
        deleteProduct.setDisable(true);
        deleteProduct.setOnAction(actionEvent -> clickDeleteProduct());
        logger.info("Инициализация кнопки УДАЛИТЬ завершена.");
    }

    private void switchTableView(){
        logger.info("Вызвана операция смены таблици продуктов магазина...");
        var selected = (String) shopChoice.getValue();
        logger.info("Сменить на: " + selected);
        try {
            switch (selected){
                case OptionsHelper.SHOP_CITILINK:
                {
                    productViewTable = new CitilinkProductTableView(context);
                }break;

                default:
                    //Вывести лог о незарегестрированном варианте
                    logger.warn("Выбранный вариент не зарегестрирован!");
            }

            logger.info("Успешно получено граффическое представление таблици продуктов. " + productViewTable.toString());
        }catch (SQLException exception){
            logger.error("Ошибка в смене граффической таблици прадуктов!", exception);
        }
        productViewTable.initFragmentView();
        var children = tablePanel.getChildren();
        children.clear();
        children.add(productViewTable.getMainPanel());
        logger.info("Граффическая таблица добавлено в главное окно.");

        var table = productViewTable.tableView;
        table.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null){
                        getMoreInfo.setDisable(false);
                        deleteProduct.setDisable(false);
                    }else{
                        getMoreInfo.setDisable(true);
                        deleteProduct.setDisable(true);
                    }
                });
        logger.info("Смена граффической таблици продуктов завершена");
    }

    private void clickAddNewProduct(){
        logger.info("Совершен клик по кнопке ДОБАВИТЬ ТОВАР");
        productViewTable.callProductConstructorDialog();
    }

    private void clickShowInfoAbout() {
        logger.info("Совершен клик по кнопке ПРОСМОТРЕТЬ");
        productViewTable.callProductEditorDialog();
    }

    private void clickDeleteProduct() {
        logger.info("Совершен клик по кнопке УДАЛИТЬ");
        productViewTable.callProductDeleteDialog();
    }
}
