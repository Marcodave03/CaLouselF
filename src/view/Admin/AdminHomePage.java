package view.Admin;

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

public class AdminHomePage {
	private Scene scene;
	private BorderPane bp;
	private VBox vb;

	private TableView<Item> itemTable;
	private TableColumn<Item, String> nameCol, categoryCol, sizeCol;
	private TableColumn<Item, Integer> priceCol;
	private TableColumn<Item, Void> actionCol;

	private ItemController itemController;

	public AdminHomePage(Stage primaryStage) {
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

		nameCol.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.2));
		categoryCol.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.2));
		sizeCol.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.2));
		priceCol.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.2));
		actionCol.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.2));

		itemTable.getColumns().addAll(nameCol, categoryCol, sizeCol, priceCol, actionCol);

		ObservableList<Item> itemList = itemController.ViewRequestedItem(null, "pending");
		itemTable.setItems(itemList);

		vb = new VBox(10, itemTable);
		vb.setPadding(new Insets(10));
		bp.setCenter(vb);
	}

	private void eventHandler(Stage primaryStage) {
		actionCol.setCellFactory(param -> new TableCell<>() {
			private final Button approveButton = new Button("Approve");
			private final Button declineButton = new Button("Decline");
			private final HBox buttonBox = new HBox(10, approveButton, declineButton);

			{
				buttonBox.setAlignment(Pos.CENTER);

				approveButton.setOnAction(event -> {
					Item item = getTableView().getItems().get(getIndex());
					boolean success = itemController.ApproveItem(item.getItem_id());
					if (success) {
						getTableView().getItems().remove(item);
					}
				});

				declineButton.setOnAction(event -> {
					Item item = getTableView().getItems().get(getIndex());
					showDeclineReasonForm(primaryStage, item);
				});
			}

			@Override
			protected void updateItem(Void unused, boolean empty) {
				super.updateItem(unused, empty);

				if (empty) {
					setGraphic(null);
				} else {
					setGraphic(buttonBox);
					setAlignment(Pos.CENTER);
				}
			}
		});
	}

	private void showDeclineReasonForm(Stage parentStage, Item item) {
		Stage formStage = new Stage();
		formStage.setTitle("Decline Reason");
		
		GridPane formLayout = new GridPane();
		formLayout.setPadding(new Insets(10));
		formLayout.setHgap(10);
		formLayout.setVgap(10);
		
		Label reasonLbl = new Label("Decline Reason:");
		TextField reasonTf = new TextField();
		
		Button submitBtn = new Button("Submit");
		
		submitBtn.setOnAction(e -> {
			String reason = reasonTf.getText();
			
			String validationResult = itemController.CheckDeclineReason(reason);
			if (!validationResult.equals("valid")) {
				Alert alert = new Alert(Alert.AlertType.ERROR, validationResult, ButtonType.OK);
				alert.show();
				return;
			}
			
			boolean success = itemController.DeclineItem(item.getItem_id());
	        if (success) {
	            itemTable.getItems().remove(item);
	            formStage.close();
	        }
		});
		
		formLayout.add(reasonLbl, 0, 0);
		formLayout.add(reasonTf, 1, 0);
		formLayout.add(submitBtn, 1, 1);
		
		Scene formScene = new Scene(formLayout, 400, 300);
		formStage.setScene(formScene);
		formStage.initOwner(parentStage);
		formStage.show();
	}

	public Scene getScene() {
		return scene;
	}
}
