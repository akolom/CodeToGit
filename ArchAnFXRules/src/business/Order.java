package business;

import java.time.LocalDate;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

public class Order {
	private List<OrderItem> orderItems;
	private String orderID;
	private LocalDate date;
	
	public Order() {
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	
	public double getTotalPrice() {
		if(orderItems == null) {
			return 0.0;
		} else {
			 DoubleSummaryStatistics summary 
			    = orderItems.stream().collect(
				    Collectors.summarizingDouble(
					   (OrderItem item) -> item.getUnitPrice() * item.getQuantity()));
			 return summary.getSum();
		}
	}
	
	public LocalDate getDate() {
		//note that LocalDates are immutable
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
	
}
