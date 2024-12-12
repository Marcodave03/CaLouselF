package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Item;
import util.Connect;

public class TransactionController {
	private  Connect connect = Connect.getInstance();
	//1. Purchase
	public boolean AddTransaction(String Item_id, String User_id) {
		String query = "INSERT INTO transaction (Item_id, User_id)"
						+ "VALUES ('"+Item_id+"', '"+User_id+"')";
		try {
			connect.executeUpdate(query);
			return true;
		}   catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public ObservableList<Item> Transactionlist(String User_id) {
	    ObservableList<Item> itemList = FXCollections.observableArrayList(); 
	    String query = "SELECT i.* FROM item i " +
	                   "JOIN transaction w ON i.Item_id = w.Item_id " +
	                   "WHERE w.User_id = '"+ User_id +"'";
	    System.out.println("Executing query: " + query);

	    try {
	        connect.resultSet = connect.execute(query);
	        while (connect.resultSet.next()) {
	            String id = connect.resultSet.getString("item_id");
	            String name = connect.resultSet.getString("item_name");
	            String category = connect.resultSet.getString("item_category");
	            String size = connect.resultSet.getString("item_size");
	            Integer price = connect.resultSet.getInt("item_price");
	            String status = connect.resultSet.getString("item_status");
	            String wishlist = connect.resultSet.getString("item_wishlist");
	            String offer = connect.resultSet.getString("item_offer_status");

	            
	            itemList.add(new Item(id, name, size, price, category, status, wishlist, offer));
	            System.out.println("added");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return itemList;
	}

}
