package util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
			e.printStackTrace();
		}
	}
	
	
	public String getId() {
		try {
			String getIdQuery = "SELECT LAST_INSERT_ID() AS user_id";
			ResultSet resultSet = connect.execute(getIdQuery);
			String id = resultSet.getString("user_id");
			return id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
	public User login(String username, String password) {
	    String query = "SELECT * FROM users WHERE Username = ? AND Password = ?";
	    try (PreparedStatement ps = connect.preparedStatement(query)) {
	        ps.setString(1, username);
	        ps.setString(2, password);

	        try (ResultSet resultSet = ps.executeQuery()) {
	            if (resultSet.next()) {
	                String dbUsername = resultSet.getString("Username");
	                String dbPhone = resultSet.getString("Phone_Number");
	                String role = resultSet.getString("Role");
	                String dbAddress = resultSet.getString("Address");
	                return new User("",dbUsername, password, dbPhone , role, dbAddress);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
}
