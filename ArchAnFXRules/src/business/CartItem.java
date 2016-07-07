package business;


public class CartItem {
	private String itemName;
	private int quantity;
	private double price;
			
	public CartItem() {
		
	}	
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public double getTotalPrice() {
		return quantity * price;
		
	}
	
	
	
	public String toString() {
		return itemName + ", " + quantity + ", " + price;
	}
	
	/* not used but good technique
	quantity.addListener((observable, oldValue, newValue) -> {
    	if(newValue != null) {
    		setTotalPrice(TableUtil.stringDoublesMultiply(newValue, price.get()));
    		System.out.println(TableUtil.stringDoublesMultiply(newValue, price.get()));
    	}
    	
    	//System.out.println(observable);
    	
    });*/
	
}
