package util;

import java.sql.PreparedStatement;

import model.User;

public class UserDAO {
	private Connect connect = Connect.getInstance();
	
	public void register(User user) {
		String query =  "INSERT INTO users (User_id, Username, Password, Phone_Number, Address, Role) VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement ps = connect.preparedStatement(query);
		try {
			ps.setString(1,user.getUser_id());
			ps.setString(2,user.getUsername());
			ps.setString(3,user.getPassword());
			ps.setString(4,user.getPhone_Number());
			ps.setString(5,user.getAddress());
			ps.setString(6,user.getRole());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
