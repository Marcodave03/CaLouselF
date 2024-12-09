package view;

import controller.UserController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;
import util.UserDAO;
import view.Admin.AdminHomePage;
import view.Seller.SellerHomePage;

public class LoginPage {
	private Scene scene;
	private BorderPane bp;
	private VBox vb;
	private GridPane gp;

	private TextField usernameTf;
	private PasswordField passwordField;

	private Button loginBtn;
	private Label registerLbl;
	private Hyperlink registerLink;
	private Label errorLbl;
	
	private UserController userController;

	public LoginPage(Stage primaryStage) {
		init();
		arrange();
//		eventHandler(primaryStage);
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

		errorLbl = new Label();
		errorLbl.setStyle("-fx-text-fill: red;");

		loginBtn = new Button("Login");
		registerLbl = new Label("New Here?");
		registerLink = new Hyperlink("Register");
		UserDAO userDAO = new UserDAO();
		userController = new UserController(userDAO);
	}

	private void arrange() {
		Label loginLbl = new Label("Login Page");
		loginLbl.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

		Label usernameLbl = new Label("Username");
		Label passLbl = new Label("Password");

		gp.add(usernameLbl, 0, 0);
		gp.add(usernameTf, 1, 0);
		gp.add(passLbl, 0, 1);
		gp.add(passwordField, 1, 1);

		HBox registerBox = new HBox(5);
		registerBox.setAlignment(Pos.CENTER);
		registerBox.getChildren().addAll(registerLbl, registerLink);

		vb.getChildren().addAll(loginLbl, gp, errorLbl, loginBtn, registerBox);

		bp.setCenter(vb);
	}
	
//	private void eventHandler(Stage primaryStage) 
//	{
//		loginBtn.setOnAction(e -> {
//			String username = usernameTf.getText();
//			String password = passwordField.getText();
//			
//			User user = UserController.Login(username, password);
//			
//			if (user == null) {
//		        errorLbl.setText("Invalid username or password.");
//		    } else if (user.getRole().equals("admin")) {
//		        new AdminHomePage(primaryStage);
//		    } else if (user.getRole().equals("Seller")) {
//		        new SellerHomePage(primaryStage, user);
//		    } else if(user.getRole().equals("Buyer")) {
//		    	new BuyerHomePage(primaryStage, user);
//		    }
//		});
//		
//		registerLink.setOnAction(e -> {
//            new RegisterPage(primaryStage);
//        });
//	}
	
	public Scene getScene() {
        return scene;
    }
}
