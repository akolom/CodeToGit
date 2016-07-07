package presentation.data;

import business.CartItem;
import javafx.beans.property.SimpleStringProperty;

public class CartItemPres {
	
	private CartItem cartItem;
	public CartItemPres() {}
	public void setCartItem(CartItem item) {
		cartItem = item;
	}
	public CartItem getCartItem() {
		return cartItem;
	}
	public SimpleStringProperty itemNameProperty() {
		return new SimpleStringProperty(cartItem.getItemName());
	}
	
	public void setItemName(SimpleStringProperty name) {
		cartItem.setItemName(name.get());
	}
	
	public SimpleStringProperty quantityProperty() {
		return new SimpleStringProperty((new Integer(cartItem.getQuantity())).toString());
	} 
	
	public void setQuantity(SimpleStringProperty quant) {
		cartItem.setQuantity(Integer.parseInt(quant.get()));
	}
	
	public SimpleStringProperty priceProperty() {
		return new SimpleStringProperty((new Double(cartItem.getPrice())).toString());
	}  
	public void setPrice(SimpleStringProperty price) {
		cartItem.setPrice(Double.parseDouble(price.get()));
	}
	public SimpleStringProperty totalPriceProperty() {
		return new SimpleStringProperty((new Double(cartItem.getTotalPrice())).toString());
	} 
}
