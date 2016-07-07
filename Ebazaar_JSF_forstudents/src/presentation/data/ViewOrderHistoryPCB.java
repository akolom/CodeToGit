/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation.data;

import business.exceptions.BackendException;
import business.exceptions.UnauthorizedException;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.Order;
import business.externalinterfaces.OrderItem;
import business.externalinterfaces.Product;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import presentation.control.Authorization;
import presentation.control.Callback;
import business.usecasecontrol.ViewOrdersController;

/**
 *
 * @author FGel
 */
@Named(value = "viewOrderHistoryPCB")
@SessionScoped
public class ViewOrderHistoryPCB implements Serializable {
	private final static Logger LOG = Logger.getLogger(ViewOrderHistoryPCB.class.getSimpleName());

	/**
	 * Creates a new instance of viewOrderHistoryPCB
	 */
	public ViewOrderHistoryPCB() {
	}

	private List<Order> orders = new ArrayList<Order>();
	private List<Product> products = new ArrayList<Product>();
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
	private ViewOrdersController usecaseControl = ViewOrdersController.INSTANCE;

	private HashMap<Integer, Boolean> checked = new HashMap<Integer, Boolean>();
	// private Integer selectedOrderId;
	private Product orderItemProduct;
	private Integer selectedOrderId;

	@Inject
	SessionData sessionContext;
	CustomerSubsystem cust;

	public Integer getSelectedOrderId() {
		return selectedOrderId;
	}

	public void setSelectedOrderId(Integer selectedOrderId) {
		this.selectedOrderId = selectedOrderId;
	}

	public Product getOrderItemProduct() {
		return orderItemProduct;
	}

	public void setOrderItemProduct(Product orderItemProduct) {
		this.orderItemProduct = orderItemProduct;
	}

	public HashMap<Integer, Boolean> getChecked() {
		return checked;
	}

	public void setChecked(HashMap<Integer, Boolean> checked) {
		this.checked = checked;
	}

	public List<Order> getOrders() {
        cust = sessionContext.getCust();
        LOG.warning("Method getOrders in ViewOrderHistoryPCB not impelmented");
        return new ArrayList<>();
    }

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public List<Product> getProducts() {
		products = new ArrayList<Product>();
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public String getOrderDetail() {

		for (Order order : orders) {

			selectedOrderId = order.getOrderId();

			if (checked.get(selectedOrderId)) {
				orderItems = getOrderItemsForOrderId();
				System.out.print("ccc");
				checked.clear();
				return "orderDetailsWindow";
			}
		}

		// return the same page if no order is selected
		return null;
	}

	// method return orders with specific order ID
	public List<OrderItem> getOrderItemsForOrderId() {
		//implement
		LOG.warning("Method getOrderItemsForOrderId in ViewOrderHistoryPCB not implemented");
		try {
			return usecaseControl.getOrderItemsForOrderId(selectedOrderId);
		} catch (BackendException e) {
			MessagesUtil.displayError(e.getMessage());
		}
		return null;

	}

//	public String getProductName(int productId) {
//		try {
//			return usecaseControl.getProductName(productId);
//		} catch (BackendException e) {
//			MessagesUtil.displayError(e.getMessage());
//		}
//		return null;
//	}
//
//	public String getProductUnitPrice(int productId) {
//		try {
//			return usecaseControl.getProductUnitPrice(productId);
//		} catch (BackendException e) {
//			MessagesUtil.displayError(e.getMessage());
//		}
//		return null;
//	}

	public String prepareOrderHistory(Requirement r, String pageCalledFrom) {
		// Check authorization first
		try {
			Authorization.checkAuthorization(r, sessionContext.custIsAdmin());
		} catch (UnauthorizedException e) {
			MessagesUtil.displayError(e.getMessage());
			return pageCalledFrom;
		}

		orders = getOrders();
		return "orderHistoryWindow";
	}

	public ViewOrdersCallback getViewOrdersCallback() {
		return new ViewOrdersCallback();
	}

	public class ViewOrdersCallback implements Callback {

		public final Requirement REQUIREMENT = Requirement.VIEW_ORDER_HISTORY;

		@Override
		public String doUpdate() {
			return prepareOrderHistory(REQUIREMENT, "index");
		}
	}
}
