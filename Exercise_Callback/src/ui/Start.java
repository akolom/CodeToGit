
package ui;

import control.Control;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Start extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    
    private Text messageBar = new Text();
    public void setMessage(String s) {
    	messageBar.setText(s);
    }

    @Override
    public void start(Stage primaryStage) {
    	Control.INSTANCE.setStart(this);
    	Control.INSTANCE.setStage(primaryStage);
        primaryStage.setTitle("Login Techniques");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Options");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

       
        Button loginBtn = new Button("Log In");
        grid.add(loginBtn, 0, 1);
        Button logoutBtn = new Button("Log Out");
        grid.add(logoutBtn, 1, 1);
        Button gradesButn = new Button("Grade Report");
        grid.add(gradesButn, 0, 2);
        Button remarksButn = new Button("Teacher Remarks");
        grid.add(remarksButn, 1, 2);
        grid.add(messageBar, 1, 4);
        
        logoutBtn.setOnAction(Control.INSTANCE.getLogoutHandler());
        loginBtn.setOnAction(Control.INSTANCE.getRequestLoginHandler());
        remarksButn.setOnAction(Control.INSTANCE.getRequestRemarksHandler());
        gradesButn.setOnAction(Control.INSTANCE.getRequestGradesHandler());
        

        //Scene scene = new Scene(grid, 300, 200);
        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}