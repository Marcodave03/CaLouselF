package view.Seller;

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

public class SellerHomePage {
	private Scene scene;
	private BorderPane bp;
	private VBox vb;

	private TableView<Item> itemTable;
	private TableColumn<Item, String> idCol, nameCol, categoryCol, sizeCol, statusCol;
	private TableColumn<Item, Integer> priceCol;
	private TableColumn<Item, Void> actionCol;

	private Button addItemButton;

	private ItemController itemController;
	private User user;

	public SellerHomePage(Stage primaryStage, User user) {
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
		idCol = new TableColumn<>("ID");
		nameCol = new TableColumn<>("Name");
		categoryCol = new TableColumn<>("Category");
		sizeCol = new TableColumn<>("Size");
		priceCol = new TableColumn<>("Price");
		statusCol = new TableColumn<>("Status");
		actionCol = new TableColumn<>("Action");

		addItemButton = new Button("Add Item");
	}

	private void arrange() {
		idCol.setCellValueFactory(new PropertyValueFactory<>("item_id"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("item_name"));
		categoryCol.setCellValueFactory(new PropertyValueFactory<>("item_category"));
		sizeCol.setCellValueFactory(new PropertyValueFactory<>("item_size"));
		priceCol.setCellValueFactory(new PropertyValueFactory<>("item_price"));
		statusCol.setCellValueFactory(new PropertyValueFactory<>("item_status"));

		itemTable.getColumns().addAll(idCol, nameCol, categoryCol, sizeCol, priceCol, statusCol, actionCol);

		ObservableList<Item> itemList = itemController.ViewSellerItem(user.getUser_id());
		itemTable.setItems(itemList);
		System.out.println("User ID: " + user.getUser_id());
		vb = new VBox(10, addItemButton, itemTable);
		vb.setPadding(new Insets(10));
		bp.setCenter(vb);
	}

	private void eventHandler(Stage primaryStage) {
		addItemButton.setOnAction(e -> showAddItemForm(primaryStage));
		actionCol.setCellFactory(param -> new TableCell<>() {
			private final Button editBtn = new Button("Edit");
			private final Button deleteBtn = new Button("Delete");
			private final HBox btnBox = new HBox(10, editBtn, deleteBtn);

			{
				btnBox.setAlignment(Pos.CENTER);

				editBtn.setOnAction(event -> {
					Item item = getTableView().getItems().get(getIndex());
					showEditItemForm(primaryStage, item);
				});

				deleteBtn.setOnAction(event -> {
					Item item = getTableView().getItems().get(getIndex());
					boolean success = itemController.DeleteItem(item.getItem_id());
					if (success) {
						getTableView().getItems().remove(item);
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
	}

	private void showAddItemForm(Stage parentStage) {
		Stage formStage = new Stage();
		formStage.setTitle("Add Item");

		GridPane formLayout = new GridPane();
		formLayout.setPadding(new Insets(10));
		formLayout.setHgap(10);
		formLayout.setVgap(10);

		Label nameLbl = new Label("Item Name:");
		TextField nameTf = new TextField();

		Label categoryLbl = new Label("Category:");
		TextField categoryTf = new TextField();

		Label sizeLbl = new Label("Size:");
		TextField sizeTf = new TextField();

		Label priceLbl = new Label("Price:");
		TextField priceTf = new TextField();

		Button submitBtn = new Button("Submit");

		submitBtn.setOnAction(event -> {
			String name = nameTf.getText();
			String category = categoryTf.getText();
			String size = sizeTf.getText();
			String price = priceTf.getText();

			String validationResult = itemController.CheckItemValidation(name, category, size, price);
			if (!validationResult.equals("valid")) {
				Alert alert = new Alert(Alert.AlertType.ERROR, validationResult, ButtonType.OK);
				alert.show();
				return;
			}

			boolean success = itemController.UploadItem(name, category, size, Integer.parseInt(price), user.getUser_id());
			if (success) {
				itemTable.setItems(itemController.ViewSellerItem(user.getUser_id()));
				formStage.close();
			}
		});

		formLayout.add(nameLbl, 0, 0);
		formLayout.add(nameTf, 1, 0);
		formLayout.add(categoryLbl, 0, 1);
		formLayout.add(categoryTf, 1, 1);
		formLayout.add(sizeLbl, 0, 2);
		formLayout.add(sizeTf, 1, 2);
		formLayout.add(priceLbl, 0, 3);
		formLayout.add(priceTf, 1, 3);
		formLayout.add(submitBtn, 1, 4);

		Scene formScene = new Scene(formLayout, 400, 300);
		formStage.setScene(formScene);
		formStage.initOwner(parentStage);
		formStage.show();
	}
	
	private void showEditItemForm(Stage parentStage, Item item) {
		Stage formStage = new Stage();
		formStage.setTitle("Edit Item");

		GridPane formLayout = new GridPane();
		formLayout.setPadding(new Insets(10));
		formLayout.setHgap(10);
		formLayout.setVgap(10);
		
		Label nameLabel = new Label("Item Name:");
		TextField nameField = new TextField(item.getItem_name());

		Label categoryLabel = new Label("Category:");
		TextField categoryField = new TextField(item.getItem_category());

		Label sizeLabel = new Label("Size:");
		TextField sizeField = new TextField(item.getItem_size());

		Label priceLabel = new Label("Price:");
		TextField priceField = new TextField(item.getItem_price().toString());

		Button submitButton = new Button("Update");

		submitButton.setOnAction(event -> {
			String name = nameField.getText();
			String category = categoryField.getText();
			String size = sizeField.getText();
			String price = priceField.getText();

			String validationResult = itemController.CheckItemValidation(name, category, size, price);
			if (!validationResult.equals("valid")) {
				Alert alert = new Alert(Alert.AlertType.ERROR, validationResult, ButtonType.OK);
				alert.show();
				return;
			}

			boolean success = itemController.EditItem(item.getItem_id(), name, category, size, Integer.parseInt(price));
			if (success) {
				itemTable.setItems(itemController.ViewSellerItem(user.getUser_id()));
				formStage.close();
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

	public Scene getScene() {
		return scene;
	}

}
