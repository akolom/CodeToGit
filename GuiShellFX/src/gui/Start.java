package gui;

import javafx.collections.ObservableList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Start extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	private static Stage primaryStage;
	
	 
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("E-Bazaar Welcome Page");
		VBox topContainer = new VBox();		
		//create components
		HBox embeddedText = createLabelBox();
		MenuBar menuBar = createMenuBar();
		//add components to container
		topContainer.getChildren().add(menuBar);
		topContainer.getChildren().add(embeddedText);

		//place into scene and into stage
		primaryStage.setScene(new Scene(topContainer, 500, 200));
		primaryStage.show();
	}
	
	private HBox createLabelBox() {
		Text label = new Text("E-Bazaar");
		label.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 60));
		label.setFill(Color.DARKRED);
		HBox labelBox = new HBox(10);
		labelBox.setAlignment(Pos.CENTER);
		labelBox.getChildren().add(label);
		return labelBox;
		
	}
	
	private MenuBar createMenuBar() {
		MenuBar retval = new MenuBar();
		
		//create menus to put into menu bar
		Menu custMenu = new Menu("Customer");
		custMenu.getItems().addAll(onlinePurchase(), retrieveCart(), reviewOrders(), exitApp());
		Menu adminMenu = new Menu("Administrator");
		adminMenu.getItems().addAll(maintainCatalogs(), maintainProducts());
		
		//add menus to menubar
		retval.getMenus().addAll(custMenu, adminMenu);
		return retval;
	
	}
	private MenuItem maintainCatalogs() {
		MenuItem retval = new MenuItem("Maintain Catalogs");
		retval.setOnAction(evt -> {	
			MaintainCatalogsWindow maintain = new MaintainCatalogsWindow(primaryStage);
			ObservableList<Catalog> list = FXCollections.observableList(
					DefaultData.CATALOG_LIST_DATA);
			maintain.setData(list);
			maintain.show();  
	        primaryStage.hide();
					
		});
		return retval;
	}
	private MenuItem maintainProducts() {
		MenuItem retval = new MenuItem("Maintain Products");
		retval.setOnAction(evt -> {	
			MaintainProductsWindow maintain = new MaintainProductsWindow(primaryStage);
			ObservableList<Product> list = FXCollections.observableList(
					DefaultData.PRODUCT_LIST_DATA.get(DefaultData.BOOKS_CATALOG));
			maintain.setData(DefaultData.CATALOG_LIST_DATA, list);
			maintain.show();  
	        primaryStage.hide();
					
		});
		return retval;
	}
	private MenuItem onlinePurchase() {
		MenuItem retval = new MenuItem("Online Purchase");
		retval.setOnAction(evt -> {	
			CatalogListWindow catalogs = CatalogListWindow.getInstance(primaryStage, DefaultData.CATALOG_LIST_DATA);
			//catalogs.setStage(primaryStage);
	        //catalogs.setData(DefaultData.CATALOG_LIST_DATA);
	        catalogs.show();  
	        primaryStage.hide();
					
		});
		return retval;
	}
	private MenuItem retrieveCart() {
		MenuItem retval = new MenuItem("Retrieve Saved Cart");
		retval.setOnAction(evt -> {	
			ShoppingCartWindow cartWindow = ShoppingCartWindow.INSTANCE;
			cartWindow.setData(FXCollections.observableList(DefaultData.savedCartItems));
			cartWindow.setPrimaryStage(primaryStage);
			cartWindow.show(); 
	        primaryStage.hide();					
		});
		return retval;
	}
	
	private MenuItem reviewOrders() {
		MenuItem retval = new MenuItem("Review Orders");
		retval.setOnAction(evt -> {	
			OrdersWindow ordWin = new OrdersWindow(primaryStage);
			ordWin.setData(FXCollections.observableList(DefaultData.ALL_ORDERS));
	        ordWin.show();
	        primaryStage.hide();
					
		});
		return retval;
	}
	private MenuItem exitApp() {
		MenuItem retval = new MenuItem("Exit");
		retval.setOnAction(evt -> Platform.exit());
		return retval;
	}
	
}
