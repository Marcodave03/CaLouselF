package model;

import java.util.UUID;

public class User {
	private String User_id; 
	private String Username;
    private String Password;
    private String Phone_Number;
    private String Address;
    private String Role;
    
	public User(String username, String password, String phone_Number, String address, String role) {
		super();
		this.User_id = generateUserId();
		Username = username;
		Password = password;
		Phone_Number = phone_Number;
		Address = address;
		Role = role;
	}

	private String generateUserId() {
        return UUID.randomUUID().toString(); 
    }
	
	public String getUser_id() {
		return User_id;
	}

	public void setUser_id(String user_id) {
		User_id = user_id;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getPhone_Number() {
		return Phone_Number;
	}

	public void setPhone_Number(String phone_Number) {
		Phone_Number = phone_Number;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getRole() {
		return Role;
	}

	public void setRole(String role) {
		Role = role;
	}
    
    
}
