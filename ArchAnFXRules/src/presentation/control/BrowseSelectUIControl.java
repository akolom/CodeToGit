package presentation.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import business.Product;
import business.Util;
import business.rulesbeans.QuantityBean;
import business.rulesbeans.RuleException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import presentation.data.BrowseSelectData;
import presentation.data.CartItemPres;
import presentation.data.CatalogPres;
import presentation.data.ProductPres;
import presentation.gui.CatalogListWindow;
import presentation.gui.ProductDetailsWindow;
import presentation.gui.ProductListWindow;
import presentation.gui.ShoppingCartWindow;
import rulesengine.OperatingException;
import rulesengine.ReteWrapper;
import rulesengine.ValidationException;



public enum BrowseSelectUIControl {
	//Singleton
	INSTANCE;
	
	//Windows that this controller manages.
	//Since it is difficult to manage CatalogListWindow 
	//(since it is accessed from many places),
	//we access CatalogListWindow statically, 
	//so it is not listed here
	private ProductListWindow productListWindow;
	private ProductDetailsWindow productDetailsWindow;
	private ShoppingCartWindow shoppingCartWindow;	
	private Stage primaryStage;
	
	public void setPrimaryStage(Stage ps) {
		primaryStage = ps;
	}
	
	
	//// Handlers for browse and select portion of Start page
	private class OnlinePurchaseHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			CatalogListWindow catList = CatalogListWindow.getInstance(primaryStage, 
				FXCollections.observableList(BrowseSelectData.INSTANCE.getCatalogList()));
			catList.show();  
	        primaryStage.hide();
		}
	}
	private class RetrieveSavedCartHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			shoppingCartWindow = ShoppingCartWindow.INSTANCE;
			for(CartItemPres c : BrowseSelectData.INSTANCE.getSavedCartItems()) {
				BrowseSelectData.INSTANCE.addToCart(c);
			}
			shoppingCartWindow.setData(BrowseSelectData.INSTANCE.getCartData());
			shoppingCartWindow.setPrimaryStage(primaryStage);
			shoppingCartWindow.show(); 
	        primaryStage.hide();	
		}
	}
	
	
	public OnlinePurchaseHandler getOnlinePurchaseHandler() {
		return new OnlinePurchaseHandler();
	}
	public RetrieveSavedCartHandler getRetrieveSavedCartHandler() {
		return new RetrieveSavedCartHandler();
	}
	
	
	//////////Handlers for CatalogListWindow
	private class ViewProductsHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent evt) {
			TableView<CatalogPres> table = CatalogListWindow.getInstance().getTable();
			CatalogPres cat = table.getSelectionModel().getSelectedItem();
			if(cat == null) {
				CatalogListWindow.getInstance().displayError("Please select a row.");				
			} else {
				BrowseSelectData.INSTANCE.setSelectedCatalog(cat);
				CatalogListWindow.getInstance().clearMessages();
				productListWindow = new ProductListWindow(cat);
				List<ProductPres> prods = BrowseSelectData.INSTANCE.getProductList(cat);
				productListWindow.setData(FXCollections.observableList(prods));
				CatalogListWindow.getInstance().hide();
				productListWindow.show();	
			}			
		}	
	}
	private class BackToPrimaryHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {
			primaryStage.show();
			CatalogListWindow.getInstance().hide();
		}
	}
	
	private class CatalogsToCartHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {
			ShoppingCartWindow.INSTANCE.show();
			CatalogListWindow.getInstance().hide();
		}
	}
	
	public ViewProductsHandler getViewProductsHandler() {
		return new ViewProductsHandler();
	}
	public BackToPrimaryHandler getBackToPrimaryHandler() {
		return new BackToPrimaryHandler();
	}
	public CatalogsToCartHandler getCatalogsToCartHandler() {
		return new CatalogsToCartHandler();
	}
	
	///////// Handlers for Product List Window
	private class BackToCatalogListHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {
			CatalogListWindow.getInstance().show();
			productListWindow.hide();
		}
	}

	private class ViewProductDetailsHandler implements EventHandler<ActionEvent>  {
		public void handle(ActionEvent evt) {
			TableView<ProductPres> table = productListWindow.getTable();
			ProductPres prod = table.getSelectionModel().getSelectedItem();
			if(prod == null) {
				productListWindow.displayError("Please select a row.");
			} else {
				BrowseSelectData.INSTANCE.setSelectedProduct(prod);
				productListWindow.clearMessages();
				productDetailsWindow = new ProductDetailsWindow(prod);
				productListWindow.hide();
				productDetailsWindow.show();
			}
		}
	}
	public BackToCatalogListHandler getBackToCatalogListHandler() {
		return new BackToCatalogListHandler();
	}
	public ViewProductDetailsHandler getViewProductDetailsHandler() {
		return new ViewProductDetailsHandler();
	}
	
	//////////// ProductDetailsWindow handlers
	private class BackToProductListHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {
			productListWindow.show();
			productDetailsWindow.hide();
		}
	}
	private class AddToCartHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {
			int quant = 1;
			double unitPrice = 
				Double.parseDouble(BrowseSelectData.INSTANCE
					.getSelectedProduct().unitPriceProperty().get());
			
			
			String  name = BrowseSelectData.INSTANCE.getSelectedProduct().nameProperty().get();
			CartItemPres cartPres = 
				BrowseSelectData.INSTANCE.cartItemPresFromData(name, unitPrice, quant);
			BrowseSelectData.INSTANCE.addToCart(cartPres);
			 
			shoppingCartWindow = ShoppingCartWindow.INSTANCE;
			shoppingCartWindow.setData(BrowseSelectData.INSTANCE.getCartData());
			shoppingCartWindow.setPrimaryStage(primaryStage);
			shoppingCartWindow.show();
			productDetailsWindow.hide();
		}
	}
	public void runShoppingCartRules() {
		
		
	}
	public void runQuantityRules(Product product, String quantityRequested) throws RuleException {
		int quantityAvail = BrowseSelectData.INSTANCE.quantityAvailable(product);
		try {
			// set up
			String moduleName = "rules-quantity";
			BufferedReader rulesReader = Util.pathToRules(getClass().getClassLoader(), "quantity-rules.clp");

			String deftemplateName = "quantity-template";
			QuantityBean quantityBean = new QuantityBean(quantityRequested, quantityAvail);
			HashMap<String, QuantityBean> h = new HashMap<>();
			h.put(deftemplateName, quantityBean);

			// start up the rules engine
			ReteWrapper engine = new ReteWrapper();
			engine.setRulesAsString(rulesReader);
			engine.setCurrentModule(moduleName);
			engine.setTable(h);
			engine.runRules();
			
		} catch (ValidationException ex) {
			throw new RuleException(ex.getMessage());
		} catch (IOException ex) {
			throw new RuleException(ex.getMessage());
		} catch (OperatingException ex) {
			throw new RuleException(ex.getMessage());
		} catch (Exception ex) {
			throw new RuleException(ex.getMessage());
		}
	}
	
	public BackToProductListHandler getBackToProductListHandler() {
		return new BackToProductListHandler();
	}
	public AddToCartHandler getAddToCartHandler() {
		return new AddToCartHandler();
	}
	
	//////////// Handlers for ShoppingCartWindow 
	
	private class CartContinueHandler implements EventHandler<ActionEvent> {
		
		@Override
		public void handle(ActionEvent arg0) {
			CatalogListWindow window = CatalogListWindow.getInstance(primaryStage, 
				FXCollections.observableList(BrowseSelectData.INSTANCE.getCatalogList()));
			window.clearMessages();
			shoppingCartWindow.hide();
			window.setTableAccessByRow();
			window.show();			
		}	
	}
	public CartContinueHandler getCartContinueHandler() {
		return new CartContinueHandler();
	}
	private class SaveCartHandler implements EventHandler<ActionEvent> {
		
		@Override
		public void handle(ActionEvent evt) {
			shoppingCartWindow.displayInfo("You need to implement this handler.");	
		}	
	}
	public SaveCartHandler getSaveCartHandler() {
		return new SaveCartHandler();
	}
	
	
	
   

	}
