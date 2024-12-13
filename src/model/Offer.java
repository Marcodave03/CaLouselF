package model;

public class Offer {
	private String Offer_id;
	private String Item;
	private Integer Price;
	private String Item_id;
	private String User_id;
	public Offer(String offer_id, String item, Integer price, String item_id, String user_id) {
		super();
		Offer_id = offer_id;
		Item = item;
		Price = price;
		Item_id = item_id;
		User_id = user_id;
	}
	public String getOffer_id() {
		return Offer_id;
	}
	public void setOffer_id(String offer_id) {
		Offer_id = offer_id;
	}
	public String getItem() {
		return Item;
	}
	public void setItem(String item) {
		Item = item;
	}
	public Integer getPrice() {
		return Price;
	}
	public void setPrice(Integer price) {
		Price = price;
	}
	public String getItem_id() {
		return Item_id;
	}
	public void setItem_id(String item_id) {
		Item_id = item_id;
	}
	public String getUser_id() {
		return User_id;
	}
	public void setUser_id(String user_id) {
		User_id = user_id;
	}
	
}
