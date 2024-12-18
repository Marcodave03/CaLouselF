package controller;

import util.Connect;
import util.UserDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.stage.Stage;
import model.User;
import session.SessionManager;

public class UserController {
	private static Connect connect = Connect.getInstance();
	private UserDAO userDAO;
	
	
	
	public UserController(UserDAO userDAO) {
		super();
		this.userDAO = userDAO;
	}

	//Register 
	public boolean register(String username, String password, String phoneNumber, String address, String role) {		
		try {
            User newUser = new User("0", username, password, phoneNumber, address, role);
            userDAO.register(newUser);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
	}

	//Login
	public User login(String username, String password) {
	    if ("admin".equals(username) && "admin".equals(password)) {
	        User adminUser = new User("0", "admin", "admin", "admin", "admin", "admin");
	        SessionManager.setCurrentUser(adminUser);
	        return adminUser;
	    }
	    else {
	    	 User user = userDAO.login(username, password);
	 	    if (user != null) {
	 	        SessionManager.setCurrentUser(user);
	 	    }
	 	    return user;
	    }
	}
	
	//Logout
	public void logout() {
	    if (SessionManager.isLoggedIn()) {
	        SessionManager.clearSession();
	        System.out.println("User logged out successfully.");
	    } else {
	        System.out.println("No user is currently logged in.");
	    }
	}
	
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
