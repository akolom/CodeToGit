package presentation.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import presentation.data.BrowseSelectData;
import presentation.data.CheckoutData;
import presentation.gui.CatalogListWindow;
import presentation.gui.FinalOrderWindow;
import presentation.gui.OrderCompleteWindow;
import presentation.gui.PaymentWindow;
import presentation.gui.ShippingBillingWindow;
import presentation.gui.ShoppingCartWindow;
import presentation.gui.TermsWindow;
import rulesengine.OperatingException;
import rulesengine.ReteWrapper;
import rulesengine.ValidationException;
import business.Address;
import business.CreditCard;
import business.Util;
import business.rulesbeans.AddressBean;
import business.rulesbeans.RuleException;

public enum CheckoutUIControl {
	INSTANCE;

	// Windows managed by CheckoutUIControl
	ShippingBillingWindow shippingBillingWindow;
	PaymentWindow paymentWindow;
	TermsWindow termsWindow;
	FinalOrderWindow finalOrderWindow;
	OrderCompleteWindow orderCompleteWindow;

	public ShippingBillingWindow getShippingBillingWindow() {
		return shippingBillingWindow;
	}

	// handler for ShoppingCartWindow proceeding to checkout
	private class ProceedFromCartToShipBill implements
			EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			ShoppingCartWindow.INSTANCE.clearMessages();
			ShoppingCartWindow.INSTANCE.setTableAccessByRow();
			ShoppingCartWindow.INSTANCE.hide();
			shippingBillingWindow = new ShippingBillingWindow();
			shippingBillingWindow.loadDefaultData(
					CheckoutData.INSTANCE.getDefaultShippingData(),
					CheckoutData.INSTANCE.getDefaultBillingData());
			shippingBillingWindow.show();

		}
	}

	public ProceedFromCartToShipBill getProceedFromCartToShipBill() {
		return new ProceedFromCartToShipBill();
	}

	// handlers for ShippingBillingWindow
	private class BackToShoppingCartHandler implements
			EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			ShoppingCartWindow.INSTANCE.show();
			shippingBillingWindow.clearMessages();
			shippingBillingWindow.hide();
		}
	}

	public BackToShoppingCartHandler getBackToShoppingCartHandler() {
		return new BackToShoppingCartHandler();
	}

	private class ProceedToPaymentHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			shippingBillingWindow.clearMessages();
			boolean rulesOk = true;
			List<String> shipUpdates = null;
			List<String> billUpdates = null;
			if (shippingBillingWindow.getSaveShipAddr()) {
				try {
					shipUpdates = runAddressRules(shippingBillingWindow
							.getShippingAddress());
				} catch (RuleException e) {
					rulesOk = false;
					shippingBillingWindow
							.displayError("Shipping address error: "
									+ e.getMessage());
				}
			}
			if (rulesOk) {
				if (shippingBillingWindow.getSaveBillAddr())
					try {
						billUpdates = runAddressRules(shippingBillingWindow
								.getBillingAddress());
					} catch (RuleException e) {
						rulesOk = false;
						shippingBillingWindow
								.displayError("Billing address error: "
										+ e.getMessage());
					}
				if (rulesOk) {
					paymentWindow = new PaymentWindow();
					paymentWindow.show();
					shippingBillingWindow.hide();
					System.out.println("Rules passed!");
					if (shipUpdates != null) {
						System.out.println("Shipping address updates: "
								+ shipUpdates.toString());
					}
					if (billUpdates != null) {
						System.out.println("Billing address updates: "
								+ shipUpdates.toString());
					}
				}
			}
		}
	}
	
	

	private List<String> runAddressRules(Address addr) throws RuleException {
		try {
			// set up
			String moduleName = "rules-address";
			BufferedReader rulesReader = Util.pathToRules(getClass().getClassLoader(), "address-rules.clp");

			String deftemplateName = "address-template";
			AddressBean addrbean = new AddressBean(addr);
			HashMap<String, AddressBean> h = new HashMap<>();
			h.put(deftemplateName, addrbean);

			// start up the rules engine
			ReteWrapper engine = new ReteWrapper();
			engine.setRulesAsString(rulesReader);
			engine.setCurrentModule(moduleName);
			engine.setTable(h);
			engine.runRules();
			System.out.println(engine.getUpdates());
			return engine.getUpdates();
		} catch (ValidationException ex) {
			throw new RuleException(ex.getMessage());
		} catch (IOException ex) {
			throw new RuleException(ex.getMessage());
		} catch (OperatingException ex) {
			throw new RuleException(ex.getMessage());
		} catch (Exception ex) {
			throw new RuleException(ex.getMessage());
		}
	}

	public ProceedToPaymentHandler getProceedToPaymentHandler() {
		return new ProceedToPaymentHandler();
	}

	public class SaveShipChangeListener implements ChangeListener<Boolean> {
		@Override
		public void changed(ObservableValue<? extends Boolean> observed,
				Boolean oldval, Boolean newval) {
			//shippingBillingWindow.displayInfo("Need to write code for this.");
		}
	}

	public class SaveBillChangeListener implements ChangeListener<Boolean> {
		@Override
		public void changed(ObservableValue<? extends Boolean> observed,
				Boolean oldval, Boolean newval) {
			//shippingBillingWindow.displayInfo("Need to write code for this.");

		}
	}

	public SaveShipChangeListener getSaveShipChangeListener() {
		return new SaveShipChangeListener();
	}

	public SaveBillChangeListener getSaveBillChangeListener() {
		return new SaveBillChangeListener();
	}

	// handlers for PaymentWindow
	private class BackToShipBillWindow implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			paymentWindow.clearMessages();
			shippingBillingWindow.show();
			paymentWindow.hide();
		}
	}

	public BackToShipBillWindow getBackToShipBillWindow() {
		return new BackToShipBillWindow();
	}

	private class BackToCartHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			paymentWindow.clearMessages();
			ShoppingCartWindow.INSTANCE.show();
			paymentWindow.hide();
		}
	}

	public BackToCartHandler getBackToCartHandler() {
		return new BackToCartHandler();
	}

	private class ProceedToTermsHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			//Check payment rules before proceeding
			
			paymentWindow.clearMessages();
			paymentWindow.hide();
			termsWindow = new TermsWindow();
			termsWindow.show();

		}
	}
	
	@SuppressWarnings("unused")
	private void runPaymentRules(CreditCard cc, Address billingAddress) throws RuleException {
		//implement
	}

	public ProceedToTermsHandler getProceedToTermsHandler() {
		return new ProceedToTermsHandler();
	}

	// handlers for TermsWindow

	private class ToCartFromTermsHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			termsWindow.hide();
			ShoppingCartWindow.INSTANCE.show();
		}
	}

	public ToCartFromTermsHandler getToCartFromTermsHandler() {
		return new ToCartFromTermsHandler();
	}

	private class AcceptTermsHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			finalOrderWindow = new FinalOrderWindow();
			finalOrderWindow.setData(BrowseSelectData.INSTANCE.getCartData());
			finalOrderWindow.show();
			termsWindow.hide();
		}
	}

	public AcceptTermsHandler getAcceptTermsHandler() {
		return new AcceptTermsHandler();
	}

	// handlers for FinalOrderWindow
	private class SubmitHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			orderCompleteWindow = new OrderCompleteWindow();
			orderCompleteWindow.show();
			finalOrderWindow.clearMessages();
			finalOrderWindow.hide();
		}

	}

	public SubmitHandler getSubmitHandler() {
		return new SubmitHandler();
	}

	private class CancelOrderHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			finalOrderWindow.displayInfo("Order Cancelled");
		}
	}

	public CancelOrderHandler getCancelOrderHandler() {
		return new CancelOrderHandler();
	}

	private class ToShoppingCartFromFinalOrderHandler implements
			EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			ShoppingCartWindow.INSTANCE.show();
			finalOrderWindow.hide();
			finalOrderWindow.clearMessages();
		}
	}

	public ToShoppingCartFromFinalOrderHandler getToShoppingCartFromFinalOrderHandler() {
		return new ToShoppingCartFromFinalOrderHandler();
	}

	// handlers for OrderCompleteWindow

	private class ContinueFromOrderCompleteHandler implements
			EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent evt) {
			CatalogListWindow.getInstance().show();
			orderCompleteWindow.hide();
		}
	}

	public ContinueFromOrderCompleteHandler getContinueFromOrderCompleteHandler() {
		return new ContinueFromOrderCompleteHandler();
	}
}
