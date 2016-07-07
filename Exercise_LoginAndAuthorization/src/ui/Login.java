
package ui;

import control.Control;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Login extends Stage  {
	public static final Login INSTANCE = new Login();
	private Login() {}
	private boolean initialized = false;
	private TextField username = new TextField();
	private PasswordField password = new PasswordField();
	private Text messageBar = new Text();
	public String getUserName() {
		return username.getText();
	}
	public String getPassword() {
		return password.getText();
	}
	public void setMessage(String text) {
		messageBar.setText(text);
	}
	public void clearMessage() {
		messageBar.setText("");
	}
	public void clearFields() {
		username.setText("");
		password.setText("");
	}
	public void init() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Login");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userLabel = new Label("User Name:");
        grid.add(userLabel, 0, 1);

        grid.add(username, 1, 1);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 2);
        grid.add(password, 1, 2);

        Button btn = new Button("Sign in");
        Button backBtn = new Button("Back");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        hbBtn.getChildren().add(backBtn);
        grid.add(hbBtn, 1, 4);  
        grid.add(messageBar, 1, 6);
        GridPane.setHalignment(messageBar, HPos.RIGHT);

        btn.setOnAction(evt -> {
        	Control.INSTANCE.login();
        });
        backBtn.setOnAction(evt -> {	
        	Control.INSTANCE.backToStart(); 
        });
        //Scene scene = new Scene(grid, 300, 200);
        Scene scene = new Scene(grid);
        setScene(scene);
    }

	public Text getMessageBar() {
		return messageBar;
	}

	public boolean initialized() {
		return initialized;
	}
	
}