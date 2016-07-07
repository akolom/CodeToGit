package ui;

import control.Control;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Remarks extends Stage {
	Text heading = new Text("");
	TextArea remarks = new TextArea();
	public void setRemarks(String s) {
		remarks.setText(s);
	}
	public void setHeading(String s) {
		heading.setText(s);
	}
	public Remarks() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        
        heading.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(heading, 0, 0);

        grid.add(remarks, 0, 1);
        

        
        Button backBtn = new Button("Back to Start");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(backBtn);
        grid.add(hbBtn, 0, 4);

        backBtn.setOnAction(evt -> {	
        	Control.INSTANCE.backToStart(this); 
        });

        //Scene scene = new Scene(grid, 300, 200);
        Scene scene = new Scene(grid);
        setScene(scene);
    }
}
