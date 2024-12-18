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
	
	//OfferPrice -> menggunakan tabel baru untuk menyimpan bid dari harga berbagai buyer
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

	//PriceValidation -> memastikan input harus lebih besar dari harga item saat ini 
	public String CheckOfferValidation(Item item,String Item_price) {
		if (item == null) {
			return "Item name cannot be empty.";
		}
		try {
			if (item.getItem_price() > Integer.parseInt(Item_price)) {
				return "Price must higher.";
			}
		} catch (NumberFormatException e) {
			return "Price must be a valid number.";
		}
		return "valid";
	}
	
	//ViewOfferItem
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
	

	
	
	//ApproveOffer -> menyetujui offer dan membuat transaksi
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
	
	//DeclineOffer -> menolak offer dan menghapus offer
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
	
	//Update price berdasarkan offer
	public boolean EditPrice(String Item_id, Integer Item_price) {
		String query = "UPDATE item SET "
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


	
}
