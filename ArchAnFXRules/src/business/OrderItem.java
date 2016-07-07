package business;


public class OrderItem {
	private int itemID;
	private int orderID;
	private String productName;
	private int quantity;
	private double unitPrice;
	public OrderItem(String name, int quantity, double price) {
		productName = name;
		this.quantity = quantity;
		this.unitPrice = price;
	}
	
	public int getItemID() {
		return itemID;
	}
	public void setItemID(int itemID) {
		this.itemID = itemID;
	}
	public int getOrderID() {
		return orderID;
	}
	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}


	public String getProductName() {
		return productName;
	}
	public void setProductName(String n) {
		productName = n;
	}


	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int q) {
		quantity = q;
	}


	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double price) {
		unitPrice = price;
	}

	public double getTotalPrice() {
		return unitPrice * quantity;
	}


}
