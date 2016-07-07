package ui;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Implementers of this interface support display of 
 * messages in a messageBar -- including error messages,
 * info messages, and also clearing of messages.
 * 
 * The interface makes it possible to perform operations
 * on windows from the Control class,
 * -- such as initialization, hiding, and showing --
 * in a generic way, without regard for the particular
 * window being accessed.
 * 
 * @author pcorazza
 *
 */
public interface GenericWindow {
	public static final Color ERROR_MESSAGE_COLOR = Color.web("#8E0000"); //dark red
	public static final Color INFO_MESSAGE_COLOR = Color.web("#007A29"); //dark green
	public Text getMessageBar();
	public default void displayError(String msg) {
		getMessageBar().setFill(ERROR_MESSAGE_COLOR);
		getMessageBar().setFont(Font.font("Calibri", FontWeight.BOLD, 16));
		getMessageBar().setText(msg);
	}
	public default void displayInfo(String msg) {
		getMessageBar().setFill(INFO_MESSAGE_COLOR);
		getMessageBar().setFont(Font.font("Calibri", FontWeight.BOLD, 16));
		getMessageBar().setText(msg);
	}
	public default void clearMessages() {
		getMessageBar().setText("");
	}
	
	public void hide();
	public void show();
	public void init();
	public boolean initialized();
}
