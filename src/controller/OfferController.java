package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Item;
import util.Connect;

public class OfferController {
	
	private  Connect connect = Connect.getInstance();
	
	
	public boolean AddOfferItem(String Item,String Price, String Item_id, String User_id) {
		String query = "INSERT INTO wishlist (Offer_id, Item, Price, Item_id, User_id)"
						+ "VALUES ('"+Item_id+"', '"+Item+"', '"+Price+"', '"+Item_id+"', '"+User_id+"')";
		try {
			connect.executeUpdate(query);
			return true;
		}   catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	
	public String CheckOfferValidation(String Item_name,String Item_price) {
		if (Item_name == null || Item_name.isEmpty()) {
			return "Item name cannot be empty.";
		}
		try {
			int priceValue = Integer.parseInt(Item_price);
			if (priceValue < 0) {
				return "Price must be a positive number.";
			}
		} catch (NumberFormatException e) {
			return "Price must be a valid number.";
		}
		return "valid";
	}
	
	public ObservableList<Item> ViewOfferedItem(String Item_id, String Item_status) {
		ObservableList<Item> itemList = FXCollections.observableArrayList();
		StringBuilder query = new StringBuilder("SELECT * FROM item WHERE 1=1");

		if (Item_id != null && !Item_id.isEmpty()) {
			query.append(" AND item_id = '").append(Item_id).append("'");
		}
		if (Item_status != null && !Item_status.isEmpty()) {
			query.append(" AND item_status = '").append(Item_status).append("'");
		}

		try {
			connect.resultSet = connect.execute(query.toString());
			while (connect.resultSet.next()) {
				String id = connect.resultSet.getString("item_id");
				String name = connect.resultSet.getString("item_name");
				String category = connect.resultSet.getString("item_category");
				String size = connect.resultSet.getString("item_size");
				int price = connect.resultSet.getInt("item_price");
				String status = connect.resultSet.getString("item_status");
				String wishlist = connect.resultSet.getString("item_wishlist");
				String offer = connect.resultSet.getString("item_offer_status");

				itemList.add(new Item(id, name, size, price, category, status, wishlist, offer));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return itemList;
	}
	
	
	//10. ApproveItem
	public boolean ApproveOffer(String Item_id) {
		String query = "UPDATE item SET item_status = 'approved' WHERE Item_id = '" + Item_id + "'";

		try {
			connect.executeUpdate(query);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//11. DeclineItem
	public boolean DeclineOffer(String Item_id) {
		String query = "DELETE FROM item WHERE Item_id = '" + Item_id + "'";

		try {
			connect.executeUpdate(query);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
