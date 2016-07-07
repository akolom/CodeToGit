
package business.usecasecontrol;

import java.util.List;

import business.exceptions.BackendException;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.Order;
import business.externalinterfaces.OrderItem;
import business.externalinterfaces.OrderSubsystem;
import business.ordersubsystem.OrderSubsystemFacade;

/**
 * @author pcorazza
 */
public enum ViewOrdersController   {
	INSTANCE;
	
	public List<Order> getOrderHistory(CustomerSubsystem cust) {
		return cust.getOrderHistory();
	}
	
	

	public List<OrderItem> getOrderItemsForOrderId(Integer id)  throws BackendException {
		OrderSubsystem oss = new OrderSubsystemFacade();
		return oss.getOrderItems(id);
	}
	
	
}
