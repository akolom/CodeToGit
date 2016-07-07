package presentation.data;

import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import presentation.gui.GuiConstants;

public enum CheckoutData {
	INSTANCE;
	
	//private final String 
	
	//Customer Ship Address Data
	private ObservableList<CustomerPres> shipAddresses = loadShipAddresses();
	
	//Customer Bill Address Data
	private ObservableList<CustomerPres> billAddresses = loadBillAddresses();
	
	private ObservableList<CustomerPres> loadShipAddresses() {		
	    List<CustomerPres> list = DefaultData.CUSTS_ON_FILE
						   .stream()
						   .filter(cust -> cust.getAddress().isShippingAddress())
						   .collect(Collectors.toList());
		return FXCollections.observableList(list);				   
										   
	}
	private ObservableList<CustomerPres> loadBillAddresses() {
		List<CustomerPres> list = DefaultData.CUSTS_ON_FILE
				   .stream()
				   .filter(cust -> cust.getAddress().isBillingAddress())
				   .collect(Collectors.toList());
		return FXCollections.observableList(list);
	}
	public ObservableList<CustomerPres> getCustomerShipAddresses() {
		return shipAddresses;
	}
	public ObservableList<CustomerPres> getCustomerBillAddresses() {
		return billAddresses;
	}
	public List<String> getDisplayAddressFields() {
		return GuiConstants.DISPLAY_ADDRESS_FIELDS;
	}
	public List<String> getDisplayCredCardFields() {
		return GuiConstants.DISPLAY_CREDIT_CARD_FIELDS;
	}
	public List<String> getCredCardTypes() {
		return GuiConstants.CREDIT_CARD_TYPES;
	}
	public List<String> getDefaultShippingData() {
		return DefaultData.DEFAULT_SHIP_DATA;
	}
	
	public List<String> getDefaultBillingData() {
		return DefaultData.DEFAULT_BILLING_DATA;
	}
	
	public List<String> getDefaultPaymentInfo() {
		return DefaultData.DEFAULT_PAYMENT_INFO;
	}
	
	private class ShipAddressSynchronizer implements Synchronizer {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void refresh(ObservableList list) {
			shipAddresses = list;
		}
	}
	public ShipAddressSynchronizer getShipAddressSynchronizer() {
		return new ShipAddressSynchronizer();
	}
	
	private class BillAddressSynchronizer implements Synchronizer {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void refresh(ObservableList list) {
			billAddresses = list;
		}
	}
	public BillAddressSynchronizer getBillAddressSynchronizer() {
		return new BillAddressSynchronizer();
	}
	
	public static class ShipBill {
		public boolean isShipping;
		public String label;
		public Synchronizer synch;
		public ShipBill(boolean shipOrBill, String label, Synchronizer synch) {
			this.isShipping = shipOrBill;
			this.label = label;
			this.synch = synch;
		}
		
	}
	
}
