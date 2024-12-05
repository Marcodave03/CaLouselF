package view;

import controller.UserController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;
import view.Seller.SellerHomePage;

public class RegisterPage {
	private Scene scene;
	private BorderPane bp;
	private VBox vb;
	private GridPane gp;

	private TextField usernameTf;
	private PasswordField passwordField;
	private TextField phoneNumberTf;
	private TextField addressTf;
	private ToggleGroup roleGroup;

	private Button registerBtn;
	private Label loginLbl;
	private Hyperlink loginLink;
	private Label errorLbl;

	public RegisterPage(Stage primaryStage) {
		init();
		arrange();
		eventHandler(primaryStage);
		primaryStage.setScene(scene);
	}

	private void init() {
		bp = new BorderPane();
		scene = new Scene(bp, 900, 450);

		vb = new VBox(15);
		vb.setAlignment(Pos.CENTER);

		gp = new GridPane();
		gp.setAlignment(Pos.CENTER);
		gp.setVgap(10);
		gp.setHgap(10);

		usernameTf = new TextField();
		passwordField = new PasswordField();
		phoneNumberTf = new TextField();
		addressTf = new TextField();

		roleGroup = new ToggleGroup();

		errorLbl = new Label();
		errorLbl.setStyle("-fx-text-fill: red;");

		registerBtn = new Button("Register");
		loginLbl = new Label("Already have an account?");
		loginLink = new Hyperlink("Login");
	}

	private void arrange() {
		Label regisLbl = new Label("Register Page");
		regisLbl.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

		Label usernameLbl = new Label("Username");
		Label passLbl = new Label("Password");
		Label phonenumberLbl = new Label("Phone Number");
		Label addressLbl = new Label("Address");
		Label rolesLbl = new Label("Roles");

		RadioButton sellerButton = new RadioButton("Seller");
		RadioButton buyerButton = new RadioButton("Buyer");
		sellerButton.setToggleGroup(roleGroup);
		buyerButton.setToggleGroup(roleGroup);

		HBox rolesContainer = new HBox(10);
		rolesContainer.getChildren().addAll(sellerButton, buyerButton);

		gp.add(usernameLbl, 0, 0);
		gp.add(usernameTf, 1, 0);
		gp.add(passLbl, 0, 1);
		gp.add(passwordField, 1, 1);
		gp.add(phonenumberLbl, 0, 2);
		gp.add(phoneNumberTf, 1, 2);
		gp.add(addressLbl, 0, 3);
		gp.add(addressTf, 1, 3);
		gp.add(rolesLbl, 0, 4);
		gp.add(rolesContainer, 1, 4);

		HBox loginBox = new HBox(5);
		loginBox.setAlignment(Pos.CENTER);
		loginBox.getChildren().addAll(loginLbl, loginLink);

		vb.getChildren().addAll(regisLbl, gp, errorLbl, registerBtn, loginBox);

		bp.setCenter(vb);
	}

	private void eventHandler(Stage primaryStage) {
		registerBtn.setOnAction(e -> {
			String username = usernameTf.getText();
			String password = passwordField.getText();
			String phoneNumber = phoneNumberTf.getText();
			String address = addressTf.getText();
			Toggle selectedToggle = roleGroup.getSelectedToggle();
			String role = (selectedToggle != null) ? ((RadioButton) selectedToggle).getText() : null;

			String validation = UserController.CheckAccountValidation(username, password, phoneNumber, address, role);
			if (!validation.isEmpty()) {
				errorLbl.setText(validation);
				return;
			}
			User user = UserController.Register(username, password, phoneNumber, address, role);

			if (user.getRole().equals("Buyer")) {
				new BuyerHomePage(primaryStage, user);
			} else  {
				new SellerHomePage(primaryStage, user);
			}
		});

		loginLink.setOnAction(e -> {
			new LoginPage(primaryStage);
		});
	}

	public Scene getScene() {
		return scene;
	}
}
