package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Item;
import util.Connect;

public class HomeView {

    private Connect connect = Connect.getInstance();
    
    Scene scene;
    Button backButton;
    Button addItemButton;
    BorderPane bp;
    GridPane gp;
    Menu menu;
    MenuItem menu1;
    MenuItem menu2;
    MenuItem menu3;

    Menu products;
    MenuItem products1;
    MenuItem products2;

    MenuBar menuBar;

    TableView<Item> itemTable;
    TableColumn<Item, String> idCol;
    TableColumn<Item, String> nameCol;
    TableColumn<Item, String> categoryCol;
    TableColumn<Item, String> sizeCol;
    TableColumn<Item, Integer> priceCol;
    TableColumn<Item, String> statusCol;

    VBox vb;
    
    private ObservableList<Item> itemList;

    public void fetchItemsFromDatabase() {
        String query = "SELECT * FROM item";
        connect.resultSet = connect.execute(query);

        itemList = FXCollections.observableArrayList();

        try {
            while (connect.resultSet.next()) {
                // Fetch data from ResultSet
                String id = connect.resultSet.getString("item_id");
                String name = connect.resultSet.getString("item_name");
                String category = connect.resultSet.getString("item_category");
                String size = connect.resultSet.getString("item_size");
                int price = connect.resultSet.getInt("item_price");
                String status = connect.resultSet.getString("item_status");
                String wishlist = connect.resultSet.getString("item_wishlist");
                String offer = connect.resultSet.getString("item_offer_status");

                // Add a new Item object to the ObservableList
                itemList.add(new Item(id, name, size, price, category, status, wishlist, offer));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set the items in the TableView
        itemTable.setItems(itemList);
    }

    public void init() {
        bp = new BorderPane();
        scene = new Scene(bp, 800, 450);

        menu = new Menu("Menu");
        menu1 = new MenuItem("Profile");
        menu2 = new MenuItem("Settings");
        menu3 = new MenuItem("Contacts");

        products = new Menu("Products");
        products1 = new MenuItem("Ferrari");
        products2 = new MenuItem("Ferrari 2");

        menuBar = new MenuBar();

        itemTable = new TableView<>();
        idCol = new TableColumn<>("ID");
        nameCol = new TableColumn<>("Name");
        categoryCol = new TableColumn<>("Category");
        sizeCol = new TableColumn<>("Size");
        priceCol = new TableColumn<>("Price");
        statusCol = new TableColumn<>("Status");

        vb = new VBox();

        addItemButton = new Button("Add Item"); // Initialize Add Item button
    }

    public void arrange() {
        menu.getItems().addAll(menu1, menu2, menu3);
        products.getItems().addAll(products1, products2);

        menuBar.getMenus().addAll(menu, products);
       
        idCol.setCellValueFactory(new PropertyValueFactory<>("Item_id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Item_name"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("Item_category"));
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("Item_size"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("Item_price"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("Item_status"));

        idCol.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.16));
        nameCol.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.16));
        categoryCol.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.16));
        sizeCol.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.16));
        priceCol.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.16));
        statusCol.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.16));

        itemTable.getColumns().addAll(idCol, nameCol, categoryCol, sizeCol, priceCol, statusCol);
        fetchItemsFromDatabase();
        vb.getChildren().addAll(menuBar, addItemButton, itemTable);

        vb.setSpacing(10);
        vb.setPadding(new Insets(10));
        bp.setTop(vb);
    }

    public void eventHandler(Stage primaryStage) {
        addItemButton.setOnAction(e -> {
            // Show Add Item Form
            showAddItemForm(primaryStage);
        });
    }

    private void showAddItemForm(Stage parentStage) {
        Stage formStage = new Stage();
        formStage.setTitle("Add Item");

        GridPane formLayout = new GridPane();
        formLayout.setPadding(new Insets(10));
        formLayout.setHgap(10);
        formLayout.setVgap(10);

        Label nameLabel = new Label("Item Name:");
        TextField nameField = new TextField();

        Label categoryLabel = new Label("Category:");
        TextField categoryField = new TextField();

        Label sizeLabel = new Label("Size:");
        TextField sizeField = new TextField();

        Label priceLabel = new Label("Price:");
        TextField priceField = new TextField();

        Button submitButton = new Button("Submit");

        submitButton.setOnAction(event -> {
            String name = nameField.getText();
            String category = categoryField.getText();
            String size = sizeField.getText();
            String price = priceField.getText();

            if (name.isEmpty() || category.isEmpty() || size.isEmpty() || price.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "All fields must be filled!", ButtonType.OK);
                alert.show();
                return;
            }
            

            try {
                int priceValue = Integer.parseInt(price);
                String query = "INSERT INTO Item (item_id, item_name, item_category, item_size, item_price, item_status, item_wishlist, item_offer_status) " +
                        "VALUES ('0', '" + name + "', '" + category + "', '" + size + "', " + priceValue + ", 'pending', '', '')";

                connect.executeUpdate(query);
           
                Item newItem = new Item("0", name, size, priceValue, category, "pending", "", "");
                itemList.add(newItem); // Add to the ObservableList
                itemTable.refresh();

                formStage.close();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Price must be a number!", ButtonType.OK);
                alert.show();
            }
        });

        formLayout.add(nameLabel, 0, 0);
        formLayout.add(nameField, 1, 0);
        formLayout.add(categoryLabel, 0, 1);
        formLayout.add(categoryField, 1, 1);
        formLayout.add(sizeLabel, 0, 2);
        formLayout.add(sizeField, 1, 2);
        formLayout.add(priceLabel, 0, 3);
        formLayout.add(priceField, 1, 3);
        formLayout.add(submitButton, 1, 4);

        Scene formScene = new Scene(formLayout, 400, 300);
        formStage.setScene(formScene);
        formStage.initOwner(parentStage);
        formStage.show();
    }

    public HomeView(Stage primaryStage) {
        init();
        arrange();
        eventHandler(primaryStage);
        primaryStage.setScene(scene);
    }

    public Scene getScene() {
        return scene;
    }
}
