package controller;

import util.Connect;
import util.UserDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.stage.Stage;
import model.User;

public class UserController {
	private static Connect connect = Connect.getInstance();
	private UserDAO userDAO;
	
	
	public UserController(UserDAO userDAO) {
		super();
		this.userDAO = userDAO;
	}

	public UserController(Stage primaryStage) {
	}

	//1. Register
//	public static User Register(String username, String password, String phoneNumber, String address, String role) {
//	    String insertQuery = "INSERT INTO users (Username, Password, Phone_Number, Address, Role) "
//	                       + "VALUES ('" + username + "', '" + password + "', '" + phoneNumber + "', '" + address + "', '" + role + "')";
//
//	    try {
//	        connect.executeUpdate(insertQuery);
//
//	        String getIdQuery = "SELECT LAST_INSERT_ID() AS user_id";
//	        ResultSet resultSet = connect.execute(getIdQuery);
//
//	        if (resultSet.next()) {
//	            int user_id = resultSet.getInt("user_id");
//	            return new User(String.valueOf(user_id), username, password, phoneNumber, address, role);
//	        }
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	    }
//
//	    return null;
//	}
	
	public void register(String username, String password, String phoneNumber, String address, String role) {
		User newUser = new User(username,password,phoneNumber,address,role);
		userDAO.register(newUser);
	}


	//2. Login
//	public static User Login(String username, String password) {
//		if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
//	        return null; 
//	    }
//		if (username.equals("admin") && password.equals("admin")) {
//			return new User("0", "admin", "admin", "", "");
//		}
//
//		String query = "SELECT * FROM users WHERE Username = '" + username + "' AND Password = '" + password + "'";
//		ResultSet resultSet = connect.execute(query);
//
//		try {
//	        if (resultSet.next()) {
//	            Integer user_id = resultSet.getInt("User_id");
//	            String dbUsername = resultSet.getString("Username");
//	            String role = resultSet.getString("Role");
//	            System.out.println("User found: " + dbUsername + " with role " + role);  // Debugging output
//	            return new User(user_id.toString(), dbUsername, password, "", "");
//	        }
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	    }
//		
//		return null;
//	}

	
	//3. CheckAcccountValidation	
	public static String CheckAccountValidation(String username, String password, String phoneNumber, String address,
			String role) {

		if (username == null || username.isEmpty() || username.length() < 3) {
			return "Username must be at least 3 characters long and cannot be empty.";
		}

		if (password == null || password.isEmpty() || password.length() < 8
				|| !(password.contains("!") || password.contains("@") || password.contains("#")
						|| password.contains("$") || password.contains("%") || password.contains("^")
						|| password.contains("&") || password.contains("*"))) {
			return "Password must be at least 8 characters long and include special characters (!, @, #, $, %, ^, &, *).";
		}

		if (phoneNumber == null || !phoneNumber.startsWith("+62") || phoneNumber.length() < 12) {
			return "Phone number must start with +62 and contain at least 10 digits.";
		}

		if (address == null || address.isEmpty()) {
			return "Address cannot be empty.";
		}

		if (role == null) {
			return "You must select a role (Seller or Buyer).";
		}
		return "";
	}

}
