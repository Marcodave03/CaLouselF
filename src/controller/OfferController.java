package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Item;
import model.Offer;
import util.Connect;

public class OfferController {
	
	private  Connect connect = Connect.getInstance();
	
	
	public boolean AddOfferItem(String Item,Integer Price, String Item_id, String User_id) {
		String query = "INSERT INTO offer (item, price, item_id, user_id) "
                + "VALUES ('" + Item + "', '" + Price + "', '" + Item_id + "', '" + User_id + "')";
		System.out.println(query);
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
	
//	public ObservableList<Offer> ViewOfferedItem(String Offer_id) {
//		ObservableList<Offer> offerList = FXCollections.observableArrayList();
//		StringBuilder query = new StringBuilder("SELECT * FROM offer WHERE 1=1");
//
//		if (Offer_id != null && !Offer_id.isEmpty()) {
//			query.append(" AND offer_id = '").append(Offer_id).append("'");
//			System.out.println("printed 111");
//		}
//		
//		try {
//			connect.resultSet = connect.execute(query.toString());
//			while (connect.resultSet.next()) {
//				String offerid = connect.resultSet.getString("offer_id");
//				String itemname = connect.resultSet.getString("item");
//				int price = connect.resultSet.getInt("price");
//				String userid = connect.resultSet.getString("item_id");
//				String itemid = connect.resultSet.getString("user_id");
//
//				offerList.add(new Offer(offerid, itemname, price, itemid, userid));
//				System.out.println(offerid +" "+ itemname +" "+ price + " " + userid + " " + itemid);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("Failed fetch");
//		}
//		System.out.println("done");
//		return offerList;
//	}
	
	public ObservableList<Offer> ViewOfferedItem() {
	    ObservableList<Offer> offerList = FXCollections.observableArrayList();
	    String query = "SELECT * FROM offer"; // Fetch all rows from the offer table

	    try {
	        connect.resultSet = connect.execute(query);
	        while (connect.resultSet.next()) {
	            String offerid = connect.resultSet.getString("offer_id");
	            String itemname = connect.resultSet.getString("item");
	            int price = connect.resultSet.getInt("price");
	            String itemid = connect.resultSet.getString("item_id");
	            String userid = connect.resultSet.getString("user_id");

	            offerList.add(new Offer(offerid, itemname, price, itemid, userid));
	            System.out.println("Fetched Offer: " + offerid + " " + itemname);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("Failed to fetch data");
	    }

	    System.out.println("Returning offer list of size: " + offerList.size());
	    return offerList;
	}
	

	
	
	//10. ApproveItem
	public boolean ApproveOffer(String Offer_id) {
		String query = "DELETE FROM offer WHERE Offer_id = '" + Offer_id + "'";

		try {
			connect.executeUpdate(query);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//11. DeclineItem
	public boolean DeclineOffer(String Offer_id) {
		String query = "DELETE FROM offer WHERE Offer_id = '" + Offer_id + "'";

		try {
			connect.executeUpdate(query);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
//	public boolean offerTransaction(String Offer_id) {
//		
//		String query = "INSERT INTO transaction (Item_id, User_id)"
//						+ "VALUES ('"+Item_id+"', '"+User_id+"')";
//		try {
//			connect.executeUpdate(query);
//			return true;
//		}   catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	}


	
}
