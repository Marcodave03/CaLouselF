package view;

import controller.ItemController;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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

public class BuyerHomePage {
	private Scene scene;
	private BorderPane bp;
	private VBox vb;

	private TableView<Item> itemTable;
	private TableColumn<Item, String> nameCol, categoryCol, sizeCol;
	private TableColumn<Item, Integer> priceCol;
	private TableColumn<Item, Void> actionCol;

	private ItemController itemController;
	private User user;

	public BuyerHomePage(Stage primaryStage, User user) {
		this.user = user;
		itemController = new ItemController();
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
//		addItemButton.setOnAction(e -> showAddItemForm(primaryStage));
		actionCol.setCellFactory(param -> new TableCell<>() {
			private final Button purchaseBtn = new Button("Purchase");
			private final Button offerBtn = new Button("Make Offer");
			private final HBox btnBox = new HBox(10, purchaseBtn, offerBtn);

			{
				btnBox.setAlignment(Pos.CENTER);

//				editBtn.setOnAction(event -> {
//					Item item = getTableView().getItems().get(getIndex());
//					showEditItemForm(primaryStage, item);
//				});
//
//				deleteBtn.setOnAction(event -> {
//					Item item = getTableView().getItems().get(getIndex());
//					boolean success = itemController.DeleteItem(item.getItem_id());
//					if (success) {
//						getTableView().getItems().remove(item);
//					}
//				});
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
	}


	public Scene getScene() {
		return scene;
	}

}
