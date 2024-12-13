package view.Seller;

import controller.OfferController;
import controller.TransactionController;
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
import model.Offer;
import session.SessionManager;
import model.User;

public class OfferPage {
    private Scene scene;
    private BorderPane bp;
    private VBox vb;

    private TableView<Offer> offerTable;
    private TableColumn<Offer, String> offerIdCol, itemCol, itemIdCol, userIdCol;
    private TableColumn<Offer, Integer> priceCol;
    private TableColumn<Offer, Void> actionCol;

    private OfferController offerController;
    private TransactionController transactionController;
    private User user;

    private Hyperlink SellerLink;
    private Hyperlink OfferLink;

    public OfferPage(Stage primaryStage) {
        this.user = SessionManager.getCurrentUser();
        offerController = new OfferController();
        transactionController =  new TransactionController();
        init();
        arrange();
        eventHandler(primaryStage);
        primaryStage.setScene(scene);
    }

    private void init() {
        bp = new BorderPane();
        scene = new Scene(bp, 900, 450);

        offerTable = new TableView<>();
        offerIdCol = new TableColumn<>("Offer ID");
        itemCol = new TableColumn<>("Item Name");
        itemIdCol = new TableColumn<>("Item ID");
        userIdCol = new TableColumn<>("User ID");
        priceCol = new TableColumn<>("Price");
        actionCol = new TableColumn<>("Action");

        HBox navbar = new HBox(20);
        SellerLink = new Hyperlink("All Items");
        OfferLink = new Hyperlink("Customer Offer");
        navbar.getChildren().addAll(SellerLink, OfferLink);
        navbar.setAlignment(Pos.CENTER);
        navbar.setPadding(new Insets(10));
        bp.setTop(navbar);
    }

    private void arrange() {
        offerIdCol.setCellValueFactory(new PropertyValueFactory<>("Offer_id")); 
        itemCol.setCellValueFactory(new PropertyValueFactory<>("Item"));       
        itemIdCol.setCellValueFactory(new PropertyValueFactory<>("Item_id"));  
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("User_id"));  
        priceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));

        offerTable.getColumns().addAll(offerIdCol, itemCol, itemIdCol, userIdCol, priceCol, actionCol);

        String userIdString = user.getUser_id();
        ObservableList<Offer> offerList = offerController.ViewOfferedItem();
        System.out.println("Offerllist" + offerList);
        offerTable.setItems(offerList);

        if (user.getUser_id() == null || user.getUser_id().isEmpty()) {
            System.out.println("User ID failed to pass");
        } else {
            System.out.println("User ID is: " + user.getUser_id());
        }
        System.out.println("User being passed to OfferListPage: " + user.getRole());
        System.out.println("User being passed to OfferListPage: " + user.getUsername());

        vb = new VBox(10, offerTable);
        vb.setPadding(new Insets(10));
        bp.setCenter(vb);
    }

    private void eventHandler(Stage primaryStage) {
    	actionCol.setCellFactory(param -> new TableCell<>() {
			private final Button acceptBtn = new Button("Accept Offer");
			private final Button declineBtn = new Button("Decline Offer");
			private final HBox btnBox = new HBox(10, acceptBtn, declineBtn);
			
			{
				btnBox.setAlignment(Pos.CENTER);

				acceptBtn.setOnAction(event -> {
					Offer offer = getTableView().getItems().get(getIndex());
					System.out.println(offer.getOffer_id() + " " + offer.getItem_id());
					boolean success = offerController.ApproveOffer(offer.getOffer_id());
					if(success) {
						boolean approve =  transactionController.AddTransaction(offer.getItem_id(),offer.getUser_id());
						 Alert alert = new Alert(Alert.AlertType.INFORMATION, "Approved, Transaction Processed", ButtonType.OK);
			             alert.showAndWait();
					}else {
						 Alert alert = new Alert(Alert.AlertType.INFORMATION, "Error", ButtonType.OK);
				         alert.showAndWait();
					}
					getTableView().refresh();
				});

				declineBtn.setOnAction(event -> {
					Offer offer = getTableView().getItems().get(getIndex());
					boolean success = offerController.DeclineOffer(offer.getOffer_id());
					if(success) {
						 
						 Alert alert = new Alert(Alert.AlertType.INFORMATION, "Decline Offer", ButtonType.OK);
			             alert.showAndWait();
					}else {
						 Alert alert = new Alert(Alert.AlertType.INFORMATION, "Error", ButtonType.OK);
				         alert.showAndWait();
					}
					getTableView().refresh();
					
				});
			}
			
			@Override
			protected void updateItem(Void unused, boolean empty) {
				super.updateItem(unused, empty);

				if (empty || getTableView().getItems().get(getIndex()) == null) {
					setGraphic(null);
					return;
				}
				Offer currentItem = getTableView().getItems().get(getIndex());
				 if (empty || getIndex() >= getTableView().getItems().size()) {
				        setGraphic(null); 
				    } else {
				        setGraphic(btnBox);
				    }
				setAlignment(Pos.CENTER);
			}
		});
    	
        SellerLink.setOnAction(e -> {
            new SellerHomePage(primaryStage);
        });
        OfferLink.setOnAction(e -> {
            new OfferPage(primaryStage);
        });
    }

    public Scene getScene() {
        return scene;
    }
}
