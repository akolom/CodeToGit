package presentation.data;

import java.time.format.DateTimeFormatter;

import presentation.gui.GuiConstants;
import presentation.gui.GuiUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import business.Order;

public class OrderPres {
	private Order order;
	
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	public SimpleStringProperty orderIdProperty() {
		return new SimpleStringProperty(order.getOrderID());
	}
	public SimpleStringProperty dateProperty() {
		return new  SimpleStringProperty(
			order.getDate().format(
				DateTimeFormatter.ofPattern(GuiConstants.DATE_FORMAT)));
		
	}
	public SimpleStringProperty totalPriceProperty() {
		return new SimpleStringProperty(
				String.format("%.2f", order.getTotalPrice()));
	}

	public ObservableList<OrderItemPres> getOrderItemsPres() {
		return GuiUtils.orderItemsToOrderItemsPres(order.getOrderItems());
	}
	
}
