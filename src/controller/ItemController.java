package controller;

import util.Connect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Item;

public class ItemController {

	private  Connect connect = Connect.getInstance();

	//1. Upload Item
	public boolean UploadItem(String Item_name, String Item_category, String Item_size, Integer Item_price, String User_id) {
		String query = "INSERT INTO item (item_id, item_name, item_category, item_size, item_price, item_status, item_wishlist, item_offer_status, User_id) "
	             + "VALUES ('0', '" + Item_name + "', '" + Item_category + "', '" + Item_size + "', " + Item_price 
	             + ", 'pending', '', '', '" + Integer.parseInt(User_id) + "')";

		try {
			connect.executeUpdate(query);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//2. EditItem
	public boolean EditItem(String Item_id, String Item_name, String Item_category, String Item_size, Integer Item_price) {
		String query = "UPDATE item SET "
                + "item_name = '" + Item_name + "', "
                + "item_category = '" + Item_category + "', "
                + "item_size = '" + Item_size + "', "
                + "item_price = " + Item_price
                + " WHERE item_id = '" + Item_id + "'";

		try {
			connect.executeUpdate(query);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//3. DeleteItem
	public boolean DeleteItem(String Item_id) {
		String query = "DELETE FROM item WHERE Item_id = '" + Item_id + "'";

		try {
			connect.executeUpdate(query);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	

	//4. ViewItem
	public ObservableList<Item> ViewItem() {
		ObservableList<Item> itemList = FXCollections.observableArrayList();
		String query = "SELECT * FROM item WHERE item_status = 'approved'";

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
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return itemList;
	}
	
	//5. CheckItemValidation
	public String CheckItemValidation(String Item_name, String Item_category, String Item_size,
			String Item_price) {
		if (Item_name == null || Item_name.isEmpty()) {
			return "Item name cannot be empty.";
		}
		if (Item_category == null || Item_category.isEmpty()) {
			return "Category cannot be empty.";
		}
		if (Item_size == null || Item_size.isEmpty()) {
			return "Size cannot be empty.";
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
	
	//6. ViewRequestedItem
	public ObservableList<Item> ViewRequestedItem(String Item_id, String Item_status) {
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
	
	//7. Offer Price
	
	//8. Accept Offer
	
	//9. Decline Offer
	
	//10. ApproveItem
	public boolean ApproveItem(String Item_id) {
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
	public boolean DeclineItem(String Item_id) {
		String query = "DELETE FROM item WHERE Item_id = '" + Item_id + "'";

		try {
			connect.executeUpdate(query);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//12. ViewAcceptedItem 
	
	//13. ViewOfferItem
	
	
	//ViewSellerItem
	public ObservableList<Item> ViewSellerItem(String User_id) {
		ObservableList<Item> itemList = FXCollections.observableArrayList();
		String query = "SELECT * FROM item WHERE User_id = '" + Integer.parseInt(User_id) + "'";
		
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
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return itemList;
	}
	

	
	//CheckDeclineReason
	public String CheckDeclineReason(String Reason)
	{
		if(Reason == null || Reason.isEmpty()) {
			return "Reason cannot be empty.";
		}
		return "valid";
	}

	

	
	
	

}
