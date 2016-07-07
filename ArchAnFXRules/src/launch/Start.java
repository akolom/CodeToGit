package launch;

import javafx.application.Application;
import javafx.application.Platform;
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
import presentation.control.BrowseSelectUIControl;
import presentation.control.ManageProductsUIControl;
import presentation.control.ViewOrdersUIControl;


public class Start extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		BrowseSelectUIControl.INSTANCE.setPrimaryStage(primaryStage);
		
		//used for Back button
		ViewOrdersUIControl.INSTANCE.setPrimaryStage(primaryStage);
		
		//used for Back button
		ManageProductsUIControl.INSTANCE.setPrimaryStage(primaryStage);
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
		retval.setOnAction(ManageProductsUIControl.INSTANCE.getMaintainCatalogsHandler());
		return retval;
	}
	private MenuItem maintainProducts() {
		MenuItem retval = new MenuItem("Maintain Products");
		retval.setOnAction(ManageProductsUIControl.INSTANCE.getMaintainProductsHandler());
		return retval;
	} 
	private MenuItem onlinePurchase() {
		MenuItem retval = new MenuItem("Online Purchase");
		retval.setOnAction(BrowseSelectUIControl.INSTANCE.getOnlinePurchaseHandler());
		return retval;
	}
	
	private MenuItem retrieveCart() {
		MenuItem retval = new MenuItem("Retrieve Saved Cart");
		retval.setOnAction(BrowseSelectUIControl.INSTANCE.getRetrieveSavedCartHandler());
		return retval;
	}
	
	private MenuItem reviewOrders() {
		MenuItem retval = new MenuItem("Review Orders");
		retval.setOnAction(ViewOrdersUIControl.INSTANCE.getViewOrdersHandler());	
		return retval;
	}
	private MenuItem exitApp() {
		MenuItem retval = new MenuItem("Exit");
		retval.setOnAction(evt -> Platform.exit());
		return retval;
	}
	
}
