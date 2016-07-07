package presentation.data;

import java.util.Arrays;
import java.util.List;

import business.CartItem;
import business.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import presentation.gui.GuiConstants;

public enum BrowseSelectData  {
	INSTANCE;
	
	//Fields that are maintained as user interacts with UI
	private CatalogPres selectedCatalog;
	private ProductPres selectedProduct;
	private CartItemPres selectedCartItem;
	
	public CatalogPres getSelectedCatalog() {
		return selectedCatalog;
	}

	public void setSelectedCatalog(CatalogPres selectedCatalog) {
		this.selectedCatalog = selectedCatalog;
	}

	public ProductPres getSelectedProduct() {
		return selectedProduct;
	}
	
	public Product getProductForProductName(String name) {
		return DefaultData.PRODNAME_TO_PRODUCT.get(name);
	}

	public void setSelectedProduct(ProductPres selectedProduct) {
		this.selectedProduct = selectedProduct;
	}

	public CartItemPres getSelectedCartItem() {
		return selectedCartItem;
	}

	public void setSelectedCartItem(CartItemPres selectedCartItem) {
		this.selectedCartItem = selectedCartItem;
	}
	
	//Saved cart
	public List<CartItemPres> getSavedCartItems() {
		return DefaultData.savedCartItems;
	}
	
	//ShoppingCart model
	private ObservableList<CartItemPres> cartData;
	
	public ObservableList<CartItemPres> getCartData() {
		return cartData;
	}
	
	public CartItemPres cartItemPresFromData(String name, double unitPrice, int quantAvail) {
		CartItem item = new CartItem();
		item.setItemName(name);
		item.setPrice(unitPrice);
		item.setQuantity(quantAvail);
		CartItemPres cartPres = new CartItemPres();
		cartPres.setCartItem(item);
		return cartPres;
	}
	
	public void addToCart(CartItemPres cartPres) {
		ObservableList<CartItemPres> newCartItems =
		           FXCollections.observableArrayList(cartPres);
		//Place the new item at the top of the list
		if(cartData != null) {
			newCartItems.addAll(cartData);
		}
		cartData = newCartItems;
	}
	
	public boolean removeFromCart(ObservableList<CartItemPres> toBeRemoved) {
		if(cartData != null && toBeRemoved != null && !toBeRemoved.isEmpty()) {
			cartData.remove(toBeRemoved.get(0));
			return true;
		}
		return false;
	}
	
	public int quantityAvailable(Product product) {
		//read data from database
		return DefaultData.DEFAULT_QUANTITY_AVAILABLE;
	}
	
	//CatalogList data
	public List<CatalogPres> getCatalogList() {
		return DefaultData.CATALOG_LIST_DATA;
	}
	
	//ProductList data
	public List<ProductPres> getProductList(CatalogPres selectedCatalog) {
		return DefaultData.PRODUCT_LIST_DATA.get(selectedCatalog); 
	}
	
	//ProductDetails data
	// List<String> displayValues = 
	public List<String> getProductDisplayValues(ProductPres productPres) {
		return Arrays.asList(productPres.nameProperty().get(),
				productPres.unitPriceProperty().get(),
				productPres.quantityAvailProperty().get(),
				productPres.descriptionProperty().get());
	}
	
	public List<String> getProductFieldNamesForDisplay() {
		return GuiConstants.DISPLAY_PRODUCT_FIELDS;
	}
	
	//Synchronizers
	private class ShoppingCartSynchronizer implements Synchronizer {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public void refresh(ObservableList list) {
			cartData = list;
		}
	}
	public ShoppingCartSynchronizer getShoppingCartSynchronizer() {
		return new ShoppingCartSynchronizer();
	}
}
