package business.usecasecontrol;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import business.BusinessConstants;
import business.SessionCache;
import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.ShoppingCartSubsystem;

public enum CheckoutController  {
	INSTANCE;
	
	private static final Logger LOG = Logger.getLogger(CheckoutController.class
			.getPackage().getName());
	
	
	public void runShoppingCartRules(ShoppingCartSubsystem cart) throws RuleException, BusinessException {
		//implement
		
	}
	
	public void runPaymentRules(Address addr, CreditCard cc) throws RuleException, BusinessException {
		//implement
	}
	
	public Address runAddressRules(Address addr) throws RuleException, BusinessException {
		CustomerSubsystem cust = 
			(CustomerSubsystem)SessionCache.getInstance().get(BusinessConstants.CUSTOMER);
		return cust.runAddressRules(addr);
	}
	
	public List<Address> getShippingAddresses(CustomerProfile custProf) throws BackendException {
		//implement
		LOG.warning("Method CheckoutController.getShippingAddresses has not been implemented");
		return new ArrayList<Address>();
	}
	
	public Address getDefaultShippingAddress(CustomerSubsystem cust) {
		return cust.getDefaultBillingAddress();
	}
	public Address getDefaultBillingAddress(CustomerSubsystem cust) {
		return cust.getDefaultBillingAddress();
	}
	public CustomerProfile getCustomerProfile(CustomerSubsystem cust) {
		return cust.getCustomerProfile();
	}
	
	/** Asks the ShoppingCart Subsystem to run final order rules */
	public void runFinalOrderRules(ShoppingCartSubsystem scss) throws RuleException, BusinessException {
		//implement
	}
	
	/** Asks Customer Subsystem to check credit card against 
	 *  Credit Verification System 
	 */
	public void verifyCreditCard() throws BusinessException {
		CustomerSubsystem cust = 
				(CustomerSubsystem)SessionCache.getInstance().get(BusinessConstants.CUSTOMER);			
		cust.checkCreditCard();
	}
	
	public void saveNewAddress(Address addr) throws BackendException {
		CustomerSubsystem cust = 
			(CustomerSubsystem)SessionCache.getInstance().get(BusinessConstants.CUSTOMER);			
		cust.saveNewAddress(addr);
	}
	
	/** Asks Customer Subsystem to submit final order */
	public void submitFinalOrder(CustomerSubsystem cust) throws BackendException {
		//implement
	}


}
