package view;

import controller.ItemController;
import controller.OfferController;
import controller.TransactionController;
import controller.UserController;
import controller.WishlistController;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Item;
import model.User;
import session.SessionManager;
import util.UserDAO;

public class BuyerHomePage {
	private Scene scene;
	private BorderPane bp;
	private VBox vb;

	private TableView<Item> itemTable;
	private TableColumn<Item, String> nameCol, categoryCol, sizeCol;
	private TableColumn<Item, Integer> priceCol;
	private TableColumn<Item, Void> actionCol;

	private ItemController itemController;
	private WishlistController wishlistController;
	private TransactionController transactionController;
	private OfferController offerController;
	private User user;
	private Hyperlink WishlistLink;
	private Hyperlink AllItemLink;
	private Hyperlink TransactionLink;
	private Button makeOfferBtn;
	private Hyperlink logoutBtn;
	private UserController userController;
	private UserDAO userDAO;


	public BuyerHomePage(Stage primaryStage) {
		this.user = SessionManager.getCurrentUser();
		itemController = new ItemController();
		wishlistController = new WishlistController();
		transactionController = new TransactionController();
		offerController = new OfferController();
		userController = new UserController(userDAO);
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
		
		HBox navbar = new HBox(20);
		WishlistLink = new Hyperlink("Wishlist");
	    AllItemLink = new Hyperlink("All Items");
	    TransactionLink = new Hyperlink("Transaction History");
	    logoutBtn = new Hyperlink("Logout");
        navbar.getChildren().addAll(AllItemLink, WishlistLink, TransactionLink,logoutBtn);
        navbar.setAlignment(Pos.CENTER);
        navbar.setPadding(new Insets(10));
        bp.setTop(navbar);
	}

	private void arrange() {
		nameCol.setCellValueFactory(new PropertyValueFactory<>("item_name"));
		categoryCol.setCellValueFactory(new PropertyValueFactory<>("item_category"));
		sizeCol.setCellValueFactory(new PropertyValueFactory<>("item_size"));
		priceCol.setCellValueFactory(new PropertyValueFactory<>("item_price"));

		itemTable.getColumns().addAll(nameCol, categoryCol, sizeCol, priceCol, actionCol);

		ObservableList<Item> itemList = itemController.ViewItem();
		itemTable.setItems(itemList);
		System.out.println("User ID: " + user.getUser_id());
		vb = new VBox(10, itemTable);
		vb.setPadding(new Insets(10));
		bp.setCenter(vb);
	}

	private void eventHandler(Stage primaryStage) {
		actionCol.setCellFactory(param -> new TableCell<>() {
			private final Button purchaseBtn = new Button("Purchase");
			private final Button offerBtn = new Button("Make Offer");
			private final Button WishlistBtn = new Button("Add to Wishlist");
			
			private final HBox btnBox = new HBox(10, purchaseBtn, offerBtn,WishlistBtn);
			
			
			{
				btnBox.setAlignment(Pos.CENTER);
				
				offerBtn.setOnAction(event->{
					Item item = getTableView().getItems().get(getIndex());
					showMakeOfferForm(primaryStage, item, user);
				});
			}
			
			{
				btnBox.setAlignment(Pos.CENTER);

				WishlistBtn.setOnAction(event -> {
					Item item = getTableView().getItems().get(getIndex());
					boolean success = wishlistController.AddWishlist(item.getItem_id(),user.getUser_id());
					if (success) {
						Alert alert = new Alert(Alert.AlertType.INFORMATION, "Added to wishlist!", ButtonType.OK);
						alert.showAndWait();
						getTableView().refresh();
					} else {
						Alert alert = new Alert(Alert.AlertType.ERROR, "Error", ButtonType.OK);
						alert.showAndWait();
					}
				});
			}
			

			{
				btnBox.setAlignment(Pos.CENTER);

				purchaseBtn.setOnAction(event -> {
					Item item = getTableView().getItems().get(getIndex());
					boolean checked = wishlistController.CheckWishlist(item.getItem_id(),user.getUser_id());
					boolean success = transactionController.AddTransaction(item.getItem_id(),user.getUser_id());
					System.out.println(checked);
					if (success) {
				        if (checked) {
				            boolean deleted = wishlistController.RemoveWishlist( user.getUser_id(),item.getItem_id());
				            System.out.println("Deleted" + deleted);
				            if (deleted) {
				                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Purchase Successful! Wishlist removed.", ButtonType.OK);
				                alert.showAndWait();
				            } else {
				                Alert alert = new Alert(Alert.AlertType.ERROR, "Error removing item from wishlist.", ButtonType.OK);
				                alert.showAndWait();
				            }
				        } else {
				            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Purchase Successful!", ButtonType.OK);
				            alert.showAndWait();
				        }
				        
				    } else {
				        Alert alert = new Alert(Alert.AlertType.ERROR, "Error processing the purchase.", ButtonType.OK);
				        alert.showAndWait();
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
				Item currentItem = getTableView().getItems().get(getIndex());
				String status = currentItem.getItem_status();
				
				if ("approved".equalsIgnoreCase(status)) {
					setGraphic(btnBox);
				} else {
					setGraphic(null);
				}
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
		logoutBtn.setOnAction(e -> {
			userController.logout();
            new LoginPage(primaryStage,userController);
        });
	}

	private void showMakeOfferForm(Stage parentStage, Item item, User user) {
		Stage formStage = new Stage();
		formStage.setTitle("Make Offer");
		GridPane formLayout = new GridPane();
		formLayout.setPadding(new Insets(10));
		formLayout.setHgap(10);
		formLayout.setVgap(10);
		
		Label nameLbl = new Label("Item Name:");
		TextField nameTf = new TextField(item.getItem_name());
		
		Label priceLbl = new Label("Price Offer:");
		TextField priceTf = new TextField(item.getItem_price().toString());
		
		Button submitBtn = new Button("Submit");
		
		submitBtn.setOnAction(event -> {
			String item_name = nameTf.getText();
			String item_price = priceTf.getText();

			String validationResult = offerController.CheckOfferValidation(item, item_price);
			if (!validationResult.equals("valid")) {
				Alert alert = new Alert(Alert.AlertType.ERROR, validationResult, ButtonType.OK);
				alert.show();
				return;
			}

			boolean success = offerController.AddOfferItem(item_name, Integer.parseInt(item_price), item.getItem_id() , user.getUser_id());
			if (success) {
				itemTable.setItems(itemController.ViewItem());
				formStage.close();
			}
		});
		
		formLayout.add(nameLbl, 0, 0);
		formLayout.add(nameTf, 1, 0);
		formLayout.add(priceLbl, 0, 1);
		formLayout.add(priceTf, 1, 1);
		formLayout.add(submitBtn, 1, 4);
		
		
		Scene formScene = new Scene(formLayout, 400, 300);
		formStage.setScene(formScene);
		formStage.initOwner(parentStage);
		formStage.show();
	}

	public Scene getScene() {
		return scene;
	}

}
