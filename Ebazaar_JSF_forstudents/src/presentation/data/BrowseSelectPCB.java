/*
 * This is a Presentation Control Bean for the browse and select use case.  It
 * is intended to hold JSF action methods for the Browse and 
 * Select use case.
 * 
 * It will also hold fields accessed by the JSF pages.
 * 
 */
package presentation.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import middleware.exceptions.DatabaseException;
import presentation.control.Authorization;
import presentation.control.Callback;
import business.usecasecontrol.BrowseAndSelectController;
import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.exceptions.UnauthorizedException;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.Catalog;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.Product;
import business.externalinterfaces.ShoppingCartSubsystem;
import business.logging.EbazSimpleFormatter;
import business.productsubsystem.ProductSubsystemFacade;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;


@Named("bsPCB")
@SessionScoped
public class BrowseSelectPCB implements Serializable {
	
	static {
    	Handler[] handlers = Logger.getGlobal().getHandlers();
    	for(Handler h : handlers) {
    		Logger.getGlobal().removeHandler(h);
    	}
    	
    	ConsoleHandler ch = new ConsoleHandler();
    	ch.setFormatter(new EbazSimpleFormatter());
    	Logger.getGlobal().addHandler(ch);
    	Logger.getGlobal().info("Logger.getGlobal().info('message')"); 
    } 
    private String catalog;
    private String productName;
    private String quantityRequested;
    private String total;
    private int numberOfItems;
    
    @Inject
    private SessionData sessionContext;

    public SessionData getSessionContext() {
        return sessionContext;
    }

    public int getNumberOfItems() {
        numberOfItems = cartItems.size();
        return numberOfItems;
    }
    
    // = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    // ProductSubsystem stubProdSS = new MPTestProductSubsystemFacade();
    // private String greeting;
    // private String totalShoppingCost;
    private List<CartItem> cartItems;
    private boolean editableItem;
    //  private String itemUnitPrice;
    private BrowseAndSelectController bsController = BrowseAndSelectController.INSTANCE;
    //Items selected by user for deletion
    private HashMap<String, Boolean> checked = new HashMap<String, Boolean>();
    private HashMap<String, Boolean> checkedForEdit = new HashMap<String, Boolean>();

    public HashMap<String, Boolean> getChecked() {
        return checked;
    }

    public void setChecked(HashMap<String, Boolean> checked) {
        this.checked = checked;
    }

    public boolean isEditableItem() {
        return editableItem;
    }

    public void setEditableItem(boolean editableItem) {
        this.editableItem = editableItem;
    }

    public HashMap<String, Boolean> getCheckedForEdit() {
        return checkedForEdit;
    }

    public void setCheckedForEdit(HashMap<String, Boolean> checkedForEdit) {
        this.checkedForEdit = checkedForEdit;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

   

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getQuantityRequested() {
        return quantityRequested;
    }

    public void setQuantityRequested(String q) {
        this.quantityRequested = q;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public SelectItem[] getCatalogs() {
        SelectItem[] catalogs = null;
        try {
            catalogs = new SelectItem[bsController.getCatalogNames().size()];
            List<String> cats = bsController.getCatalogNames();
            int count = 0;
            for (String cat : cats) {
                catalogs[count++] = new SelectItem(cat);
            }

        } catch (BackendException e) {
        	FacesContext context = FacesContext.getCurrentInstance();
        	ConfigurableNavigationHandler handler 
        	  = (ConfigurableNavigationHandler) context.getApplication().getNavigationHandler();
            handler.performNavigation("errorDb");
        }
        return catalogs;
    }

    public SelectItem[] getProducts() throws BackendException {
    	Catalog cat = bsController.catalogFromCatName(catalog);
        List<Product> productList = bsController.getProducts(cat);
        SelectItem[] products = new SelectItem[productList.size()];
        for (int i = 0; i < productList.size(); i++) {
            String prodName = productList.get(i).getProductName();
            products[i] = new SelectItem(prodName);
        }
        return products;
    }

    public String addToCart() {

        return "quantity";
    }

    public String addQuantity() {

        int quant = Integer.parseInt(quantityRequested);
        try {
            ShoppingCartSubsystem ss = obtainCurrentShoppingCartSubsystem();
       
            //put the item inside a shopping cart
            bsController.addCartItem(ss, productName, quant);
        } catch (BackendException ex) {
        	FacesContext context = FacesContext.getCurrentInstance();
        	ConfigurableNavigationHandler handler 
        	  = (ConfigurableNavigationHandler) context.getApplication().getNavigationHandler();
            handler.performNavigation("errorDb");
        }

        return "cartitem";
    }

    public void retrieveSavedCart(Requirement r) {
        //implement
    }

    public void saveCart(Requirement r) {
       //implement

    }
    private ShoppingCartSubsystem obtainCurrentShoppingCartSubsystem() {
	    ShoppingCartSubsystem externalShopCart = sessionContext.getShopCartSS();
	    CustomerSubsystem cust = sessionContext.getCust();
	    ShoppingCartSubsystem retVal =
	            bsController.obtainCurrentShoppingCartSubsystem(cust, externalShopCart);
	    if (retVal == null) { //this can happen only if cust==null && extShopCart == null
	        retVal = new ShoppingCartSubsystemFacade();
	        sessionContext.setShopCartSS(retVal);
	    }
	    return retVal;
    }

    

    public static void main(String[] arg) {
    }

    public List<CartItem> getCartItems() {
        cartItems = bsController.getCartItems(obtainCurrentShoppingCartSubsystem());
        //cartItemReqCapture.setCurrentCartItems(cartItems);
        return cartItems; //here beside returning set the cartItem to the request bean 
    }

    // @param cartItems the cartItems to set
    public void setCartItems(List<CartItem> Items) {
        this.cartItems = Items;
    }

    public Product getProduct() {
        Product ob = null;
        try {
            ob = bsController.getProductForProductName(productName);
        } catch (BackendException e) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage errMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Database exception: " + e.getMessage(), null);
            context.addMessage("cartItemsTable", errMsg);
        }
        return ob;
    }

    public CartItem[] changeListtoArray() {

        CartItem[] itemArray = new CartItem[cartItems.size()];

        for (int i = 0; i < itemArray.length; i++) {
            CartItem item = cartItems.get(i);
            itemArray[i] = item;
        }
        return itemArray;
    }

    private void deleteSelectedItems() {
        //List converted to array to avoid ConcurrentModificationException
        CartItem[] itemArray = changeListtoArray();
        int numDeleted = 0;
        for (int i = 0; i < itemArray.length; i++) {
            CartItem item = itemArray[i];

            if (checked.get(item.getProductName())) {
                cartItems.remove(item);
                ++numDeleted;
            }
        }
        checked.clear();
        if (numDeleted == 0) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage errMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No item selected for delete.", null);
            context.addMessage("cartItemsTable", errMsg);
        }
    }

    private boolean noItemsSelectedForDelete() {
        if (cartItems == null || cartItems.isEmpty()) {
            return true;
        }
        for (CartItem item : cartItems) {
            Boolean checkedValue = checked.get(item.getProductName());
            if (checkedValue != null && checkedValue.equals(Boolean.TRUE)) {
                return false;
            }
        }
        return true;
    }

    public void makeItemEditable(CartItem itemToEdit) {
        for (CartItem items : cartItems) {
            if (items.getProductName().equals(itemToEdit.getProductName())) {
                // this.editableItem=true;
                checkedForEdit.remove(itemToEdit.getProductName());
                checkedForEdit.put(itemToEdit.getProductName(), true);
            }
        }
    }

    public void saveEditedItem(CartItem itemTobesaved, String quantity) {
        checkedForEdit.clear();
        for (CartItem item : cartItems) {
            //set the new quantity
            if (item.getProductName().equals(itemTobesaved.getProductName())) {
                item.setQuantity(quantity);
            }
            //make the items not editable
            checkedForEdit.put(item.getProductName(), false);
        }
        //iterate through the editablecartItem hashMap and make them uneditable
        checkedForEdit.clear();

    }

    //RULES
    //not in use
    public void validateDeleteRequest(FacesContext context, UIComponent toValidate, Object val) {
        if (noItemsSelectedForDelete()) {
            ((UIInput) toValidate).setValid(false);
            FacesMessage errMsg = new FacesMessage("No item selected for delete.");
            context.addMessage(toValidate.getClientId(context), errMsg);
        } else {
            deleteSelectedItems();
        }
    }

    public void validateQuantity(FacesContext context, UIComponent toValidate, Object val) {
        String desired = (String) val;
        try {
        	Product prod = (new ProductSubsystemFacade()).getProductFromName(productName);
            bsController.runQuantityRules(prod, desired);
        } catch (RuleException e) {
            ((UIInput) toValidate).setValid(false);
            String msg = e.getMessage();
            FacesMessage retval = new FacesMessage(msg);
            context.addMessage(toValidate.getClientId(context), retval);
        } catch (BusinessException e) {
            ConfigurableNavigationHandler handler = (ConfigurableNavigationHandler) context.getApplication().getNavigationHandler();
            handler.performNavigation("errorDb");
        } 
    }
    
    public SaveCartCallback getSaveCartCallback() {
        return new SaveCartCallback();
    }

    public class SaveCartCallback implements Callback {

        public final Requirement REQUIREMENT = Requirement.SAVE_CART;

        public String doUpdate() {
            saveCart(REQUIREMENT);
            MessagesUtil.displaySuccess("Cart successfully saved.");
            return null;
        }
    }
    
    public RetrieveCartCallback getRetrieveCartCallback() {
        return new RetrieveCartCallback();
    }


    public class RetrieveCartCallback implements Callback {

        public final Requirement REQUIREMENT = Requirement.RETRIEVE_SAVED_CART;
        public String doUpdate() {
            retrieveSavedCart(REQUIREMENT);

            //No special page to return
            return null;
        }
    }
    private static final long serialVersionUID = 1942469319721370826L;

}
