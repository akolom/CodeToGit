package business.usecasecontrol;


import java.util.List;
import java.util.stream.Collectors;

import business.RulesQuantity;
import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.Catalog;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.Product;
import business.externalinterfaces.ProductSubsystem;
import business.externalinterfaces.Rules;
import business.externalinterfaces.ShoppingCartSubsystem;
import business.productsubsystem.ProductSubsystemFacade;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;
import business.util.DataUtil;
import middleware.exceptions.DatabaseException;

public enum BrowseAndSelectController {
	INSTANCE;
	
	public List<CartItem> getCartItems(ShoppingCartSubsystem shopCartSS) {
        List<CartItem> list = shopCartSS.getCartItems();
        return list;
    }
	

	public void runQuantityRules(Product product, String quantityRequested)
			throws RuleException, BusinessException {

		ProductSubsystem prodSS = new ProductSubsystemFacade();
		
		//find current quant avail since quantity may have changed
		//since product was first loaded into UI
		int currentQuantityAvail = prodSS.readQuantityAvailable(product);
		Rules transferObject = new RulesQuantity(currentQuantityAvail, quantityRequested);//
		transferObject.runRules();

	}
	
	public List<Catalog> getCatalogs() throws BackendException {
		ProductSubsystem pss = new ProductSubsystemFacade();
		return pss.getCatalogList();
	}
	
	public List<String> getCatalogNames() throws BackendException {
		return getCatalogs().stream().map(cat -> cat.getName())
				.collect(Collectors.toList());
	}
	public ShoppingCartSubsystem obtainCurrentShoppingCartSubsystem(CustomerSubsystem cust, 
            ShoppingCartSubsystem extCartSS) {
       if (cust == null) {
           return extCartSS;
       } else { 
           return cust.getShoppingCart();
       }
   }
	
	public void addCartItem(ShoppingCartSubsystem shopCartSS, String productName, int quantity) throws BackendException {

		ProductSubsystem prodSS = new ProductSubsystemFacade();
		
		Product product = prodSS.getProductFromName(productName);
        String quantityReq = (new Integer(quantity)).toString();
        double totalPrice = product.getUnitPrice() * quantity;
        String totalPriceStr = (new Double(totalPrice)).toString();
        addCartItem(shopCartSS, productName, quantityReq, totalPriceStr, null);
    }
	public void addCartItem(ShoppingCartSubsystem shopCartSS, String name, String quantityReq, String totalprice, 
			Integer positionOfEdit) throws BackendException {

        shopCartSS.addCartItem(name, quantityReq, totalprice, positionOfEdit);
       
    }

	
	public Catalog catalogFromCatName(String name) throws BackendException {
		ProductSubsystem pss = new ProductSubsystemFacade();
		return pss.catalogForCatalogName(name);
	}
	
	public List<Product> getProducts(Catalog catalog) throws BackendException {
		ProductSubsystem pss = new ProductSubsystemFacade();
		return pss.getProductList(catalog);
	}
	public Product getProductForProductName(String name) throws BackendException {
		ProductSubsystem pss = new ProductSubsystemFacade();
		return pss.getProductFromName(name);
	}
	
	/** Assume customer is logged in */
	public CustomerProfile getCustomerProfile() {
		CustomerSubsystem cust = DataUtil.readCustFromCache();
		return cust.getCustomerProfile();
	}
}
