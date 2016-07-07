package gui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
	private static final boolean USE_DEFAULT_DATA = false;
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
			CatalogListWindow catalogs = CatalogListWindow.getInstance(primaryStage, readDataFromSource());
			//catalogs.setStage(primaryStage);
	        //catalogs.setData(DefaultData.CATALOG_LIST_DATA);
	        catalogs.show();  
	        primaryStage.hide();
					
		});
		return retval;
	}
	private ObservableList<Catalog> readDataFromSource() {
		if(USE_DEFAULT_DATA) {
			return DefaultData.CATALOG_LIST_DATA;
		} else {
			System.out.println("using db");
			System.out.println(DefaultData.PRODUCT_LIST_DATA);
			
        	Connection con = null;
        	Statement stmt = null;
        	List<Catalog> list = new ArrayList<Catalog>();
        	
    		try {
    			con = ConnectManager.getConnection(ConnectManager.DB.PROD);
    			stmt = con.createStatement();
    			ResultSet rs = stmt.executeQuery("SELECT * FROM CatalogType");
    			
    			while(rs.next()){
    				String id = rs.getString("catalogid");
    				String name = rs.getString("catalogname");
    				System.out.println("id: "+ id + " name: "+name);
    				list.add(new Catalog(id, name));
    			}
    			stmt.close();
    			con.close();
    		}
    		catch(SQLException s){
    			s.printStackTrace();
    		}
    		return FXCollections.observableList(list);
		}
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
			ordWin.setData(FXCollections.observableList(readOrdersFromSource()));
	        ordWin.show();
	        primaryStage.hide();
					
		});
		return retval;
	}
	
	private List<Order> readOrdersFromSource() {
		if(USE_DEFAULT_DATA) {
			return DefaultData.ALL_ORDERS;
		} else {
			//assume custId = 1
			String custId = "1";
			final String orderQuery = "SELECT * FROM ord WHERE custid = ?";			
			List<Order> list = new ArrayList<Order>();
			Connection con = null;
        	PreparedStatement stmt = null;
    		
    		try {
    			con = ConnectManager.getConnection(ConnectManager.DB.ACCT);
    			stmt = con.prepareStatement(orderQuery);
    			stmt.setString(1,  custId);
    			ResultSet rs = stmt.executeQuery();
    			
    			while(rs.next()){
    				int id = rs.getInt("orderid");
    				LocalDate date = GuiUtils.localDateForString(rs.getString("orderdate"));
    				List<OrderItem> items = nextItemList(id);
    				Order order = new Order();
    				order.setOrderID((new Integer(id)).toString());
    				order.setDate(date);
    				order.setOrderItems(items);
    				list.add(order);
    			}
    			stmt.close();
    			con.close();
    		}
    		catch(SQLException s){
    			s.printStackTrace();
    		}
    		return list;
		}
	}
	private List<OrderItem> nextItemList(int orderId) {
		String query = "SELECT o.* FROM accountsdb.orderitem o WHERE o.orderid = ?";
		List<OrderItem> list = new ArrayList<OrderItem>();
		Connection con = null;
    	PreparedStatement stmt = null;
    	
		try {
			con = ConnectManager.getConnection(ConnectManager.DB.ACCT);
			stmt = con.prepareStatement(query);
			stmt.setInt(1,  orderId);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				String id = rs.getString("orderid");
				int prodId = rs.getInt("productid");
				double totalPrice = rs.getDouble("totalprice");
				int quantity = rs.getInt("quantity");
				
				//obtain unitPrice by dividing totalPrice by quantity
				double unitPrice = totalPrice/quantity;
				String prodName = productNameForId(prodId);
				OrderItem orderItem = new OrderItem(prodName, 
						(new Integer(quantity)).toString(), 
						(new Double(unitPrice)).toString());		
				list.add(orderItem);
			}
			stmt.close();
			con.close();
			
		}
		catch(SQLException s){
			s.printStackTrace();
		}
		return list;
	}
	private String productNameForId(int id) {
		String productQuery 
		  = "SELECT p.productname, p.productid FROM productsdb.product p where p.productid = ?";
		Connection con = null;
    	PreparedStatement stmt = null;
    	String productName = null;
 
		try {
			con = ConnectManager.getConnection(ConnectManager.DB.PROD);
			stmt = con.prepareStatement(productQuery);
			stmt.setInt(1,  id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				productName = rs.getString("productname");	
			}
			stmt.close();
			con.close();
		}
		catch(SQLException s){
			s.printStackTrace();
		}
		return productName;
	}
	private MenuItem exitApp() {
		MenuItem retval = new MenuItem("Exit");
		retval.setOnAction(evt -> Platform.exit());
		return retval;
	}
	
}
