package control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import ui.*;
import java.util.logging.Logger;
import data.*;

public enum Control {
	INSTANCE;
	private static final Logger LOG = 
			Logger.getLogger(Control.class.getSimpleName());
	
	Stage primaryStage;

	public void setStage(Stage st) {
		primaryStage = st;
	}
	public void setStart(Start start) {
		this.start = start;
	}


	////// windows managed by Control
	//value set by setStart()
	Start start;
	Login login = Login.INSTANCE;
	AdminOnly adminOnly = AdminOnly.INSTANCE;
	Private privateWindow = Private.INSTANCE;
	Public1 publicWindow1 = Public1.INSTANCE;
	Public2 publicWindow2 = Public2.INSTANCE;
	
	public void hideAll() {
		if(login != null) login.hide();
		if(adminOnly != null) adminOnly.hide();
		if(privateWindow != null) privateWindow.hide();
		if(publicWindow1 != null) publicWindow1.hide();
		if(publicWindow2 != null) publicWindow2.hide();
		if(primaryStage != null) primaryStage.hide();
	}
	

	public void login() {
		String username = login.getUserName();
		String password = login.getPassword();

		if ((username.equals(PrivateLoginData.ADMIN_LOGIN) && password.equals(PrivateLoginData.ADMIN_PWD)) ||
				(username.equals(PrivateLoginData.CUST_LOGIN) && password.equals(PrivateLoginData.CUST_PWD))) {
			login.setMessage("Login successful");
		} else {
			login.setMessage("Login failed");
		}
		
	}

	public void logout() {
		start.getMessageBar().setText("Logout successful");
	}

	public void backToStart() {
		hideAll();
		primaryStage.show();
	}

	private class RequestLoginHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {
			if(!(login.initialized())) login.init();
			login.clearMessage();
			login.clearFields();
			hideAll();
			login.show();
		}
	}

	public RequestLoginHandler getRequestLoginHandler() {
		return new RequestLoginHandler();
	}

	private class Public1Handler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent evt) {
			hideAll();
			if(!(publicWindow1.initialized())) publicWindow1.init();
			publicWindow1.show();
		}
	}

	public Public1Handler getPublic1Handler() {
		return new Public1Handler();
	}

	private class Public2Handler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {
			hideAll();
			if(!(publicWindow2.initialized())) publicWindow2.init();
			publicWindow2.show();
		}
	}

	public Public2Handler getPublic2Handler() {
		return new Public2Handler();
	}

	private class PrivateHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {
			hideAll();
			if(!(privateWindow.initialized())) privateWindow.init();
			privateWindow.show();
			LOG.warning("User is accessing a private page but may not have logged in!");
		}
	}

	public PrivateHandler getPrivateHandler() {
		return new PrivateHandler();
	}

	private class AdminOnlyHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {
			hideAll();
			if(!(adminOnly.initialized())) adminOnly.init();
			adminOnly.show();
			LOG.warning("User is accessing an admin page but may not have admin access!");
		}
	}

	public AdminOnlyHandler getAdminOnlyHandler() {
		return new AdminOnlyHandler();
	}
	

}
