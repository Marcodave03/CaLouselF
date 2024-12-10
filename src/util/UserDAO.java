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
	
	public ResultSet login(String username, String password) {
        String query = "SELECT * FROM users WHERE Username = ? AND Password = ?";
        ResultSet resultSet = null;
        try (PreparedStatement ps = connect.preparedStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            resultSet = ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }
	
}
