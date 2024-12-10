package util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.User;

public class UserDAO {
	private Connect connect = Connect.getInstance();
	
	public void register(User user) {
		String query =  "INSERT INTO users (Username, Password, Phone_Number, Address, Role) VALUES (?, ?, ?, ?, ?)";
		PreparedStatement ps = connect.preparedStatement(query);
		try {
			ps.setString(1,user.getUsername());
			ps.setString(2,user.getPassword());
			ps.setString(3,user.getPhone_Number());
			ps.setString(4,user.getAddress());
			ps.setString(5,user.getRole());
			ps.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public User login(String username, String password) {
	    String query = "SELECT * FROM users WHERE Username = ? AND Password = ?";
	    try (PreparedStatement ps = connect.preparedStatement(query)) {
	        ps.setString(1, username);
	        ps.setString(2, password);

	        try (ResultSet resultSet = ps.executeQuery()) {
	            if (resultSet.next()) {
	                Integer user_id = resultSet.getInt("User_id");
	                String dbUsername = resultSet.getString("Username");
	                String role = resultSet.getString("Role");
	                return new User(user_id.toString(), dbUsername, password, "", role);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
}
