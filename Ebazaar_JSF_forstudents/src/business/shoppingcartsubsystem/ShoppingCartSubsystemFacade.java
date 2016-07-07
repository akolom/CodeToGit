package business.shoppingcartsubsystem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import middleware.exceptions.DatabaseException;
import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.Rules;
import business.externalinterfaces.ShoppingCart;
import business.externalinterfaces.ShoppingCartSubsystem;

public class ShoppingCartSubsystemFacade implements ShoppingCartSubsystem {
	public ShoppingCartSubsystemFacade() {
    }
	ShoppingCartImpl liveCart = new ShoppingCartImpl(new LinkedList<CartItem>());
	ShoppingCartImpl savedCart;
	Integer shopCartId;
	CustomerProfile customerProfile;
	Logger log = Logger.getLogger(this.getClass().getPackage().getName());

	
	// interface methods
	public void setLiveCart(ShoppingCart cart) {
        List<CartItem> cartItems = (cart == null) ? new ArrayList<CartItem>() :
                cart.getCartItems();
        liveCart = new ShoppingCartImpl(cartItems);
    }
	public ShoppingCart getSavedCart() {
		return savedCart;
	}
	public void setCustomerProfile(CustomerProfile customerProfile) {
		this.customerProfile = customerProfile;
	}
	
	public void makeSavedCartLive() {
		liveCart = savedCart;
	}
	
	public ShoppingCart getLiveCart() {
		return liveCart;
	}
	

	public void retrieveSavedCart() throws BackendException {
		try {
			DbClassShoppingCart dbClass = new DbClassShoppingCart();
			ShoppingCartImpl cartFound = dbClass.retrieveSavedCart(customerProfile);
			if(cartFound == null) {
				savedCart = new ShoppingCartImpl(new ArrayList<CartItem>());
			} else {
				savedCart = cartFound;
			}
		} catch(DatabaseException e) {
			throw new BackendException(e);
		}

	}
	
	
	
	public void updateShoppingCartItems(List<CartItem> list) {
		liveCart.setCartItems(list);
	}
	
	public List<CartItem> getCartItems() {
		return liveCart.getCartItems();
	}
	
	//static methods
	public static CartItem createCartItem(String productName, String quantity,
            String totalprice) {
		try {
			return new CartItemImpl(productName, quantity, totalprice);
		} catch(BackendException e) {
			throw new RuntimeException("Can't create a cartitem because of productid lookup: " + e.getMessage());
		}
	}


	
	//interface methods for testing
	
	public ShoppingCart getEmptyCartForTest() {
		return new ShoppingCartImpl();
	}

	
	public CartItem getEmptyCartItemForTest() {
		return new CartItemImpl();
	}

	@Override
	public void addCartItem(String name, String quantityReq, String totalPrice, 
			Integer positionOfEdit) throws BackendException {
		if (liveCart == null) {
            liveCart = new ShoppingCartImpl(new LinkedList<CartItem>());
        }
        CartItemImpl item = new CartItemImpl(name, quantityReq, totalPrice);
        if (positionOfEdit == null) {
            liveCart.addItem(item);
        } else {
            liveCart.insertItem(positionOfEdit, item);
        }
		
	}
	

}
