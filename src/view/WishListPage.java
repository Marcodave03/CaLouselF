package view;

import controller.ItemController;
import controller.WishlistController;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Item;
import model.User;
import model.Wishlist;
import session.SessionManager;

public class WishListPage {
	private Scene scene;
	private BorderPane bp;
	private VBox vb;

	private TableView<Item> itemTable;
	private TableColumn<Item, String> nameCol, categoryCol, sizeCol;
	private TableColumn<Item, Integer> priceCol;
	private TableColumn<Item, Void> actionCol;

	private ItemController itemController;
	private WishlistController wishlistController;
	private User user;
	private Hyperlink WishlistLink;
	private Hyperlink AllItemLink;
	private Hyperlink TransactionLink;

	public WishListPage(Stage primaryStage) {
		this.user = SessionManager.getCurrentUser();
		itemController = new ItemController();
		wishlistController = new WishlistController();
		init();
		arrange();
		eventHandler(primaryStage);
		primaryStage.setScene(scene);
	}

	private void init() {
		bp = new BorderPane();
		scene = new Scene(bp, 900, 450);

		itemTable = new TableView<>();
		nameCol = new TableColumn<>("Name");
		categoryCol = new TableColumn<>("Category");
		sizeCol = new TableColumn<>("Size");
		priceCol = new TableColumn<>("Price");
		actionCol = new TableColumn<>("Action");
		
		itemTable.getColumns().addAll(nameCol, categoryCol, sizeCol, priceCol, actionCol);
		
		HBox navbar = new HBox(20);
		WishlistLink = new Hyperlink("Wishlist");
	    AllItemLink = new Hyperlink("All Items");
	    TransactionLink = new Hyperlink("Transaction History");
        navbar.getChildren().addAll(AllItemLink, WishlistLink, TransactionLink);
        navbar.setAlignment(Pos.CENTER);
        navbar.setPadding(new Insets(10));
        bp.setTop(navbar);
	}

	private void arrange() {
		nameCol.setCellValueFactory(new PropertyValueFactory<>("item_name"));
		categoryCol.setCellValueFactory(new PropertyValueFactory<>("item_category"));
		sizeCol.setCellValueFactory(new PropertyValueFactory<>("item_size"));
		priceCol.setCellValueFactory(new PropertyValueFactory<>("item_price"));

		ObservableList<Item> itemList = wishlistController.ViewWishlist(user.getUser_id());
		itemTable.setItems(itemList);
		System.out.println("User ID: " + user.getUser_id());
		vb = new VBox(10, itemTable);
		vb.setPadding(new Insets(10));
		bp.setCenter(vb);
	}

	private void eventHandler(Stage primaryStage) {
	    actionCol.setCellFactory(param -> new TableCell<>() {
	        private final Button removeBtn = new Button("Remove");
	        private final HBox btnBox = new HBox(10, removeBtn);

	        {
	            btnBox.setAlignment(Pos.CENTER);
	            removeBtn.setOnAction(event -> {
	            	Item item = getTableView().getItems().get(getIndex());
	                boolean success = wishlistController.RemoveWishlist(user.getUser_id(),item.getItem_id());
	                if (success) {
	                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Removed from wishlist!", ButtonType.OK);
	                    alert.showAndWait();
	                    getTableView().getItems().remove(getIndex()); // Refresh tabel
	                } else {
	                    Alert alert = new Alert(Alert.AlertType.ERROR, "Error removing item from wishlist.", ButtonType.OK);
	                    alert.showAndWait();
	                    getTableView().getItems().remove(getIndex());
	                }
	            });
	        }

	        @Override
	        protected void updateItem(Void unused, boolean empty) {
	            super.updateItem(unused, empty);

	            if (empty || getTableView().getItems().get(getIndex()) == null) {
	                setGraphic(null);
	                return;
	            }

	            setGraphic(btnBox); 
	            setAlignment(Pos.CENTER);
	        }
	    });
		
	    WishlistLink.setOnAction(e -> {
            new WishListPage(primaryStage);
        });
		AllItemLink.setOnAction(e -> {
            new BuyerHomePage(primaryStage);
        });
		TransactionLink.setOnAction(e -> {
            new TransactionHistoryPage(primaryStage);
        });
	}

	public Scene getScene() {
		return scene;
	}

}
