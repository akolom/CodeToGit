/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation.data;

import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.ShoppingCartSubsystem;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named("sessionContext")
@SessionScoped
public class SessionData implements Serializable {
    
	private CustomerSubsystem cust;
    private ShoppingCartSubsystem shopCartSS;
    private boolean isLoggedIn = false;
  
    public boolean custIsAdmin() {
        if(cust == null) return false;
        return cust.isAdmin();
    }

    public ShoppingCartSubsystem getShopCartSS() {
        return shopCartSS;
    }
    
    public void clearShopCart() {
        shopCartSS = null;
    }

    public void setCust(CustomerSubsystem cust) {
        this.cust = cust;
    }

    public CustomerSubsystem getCust() {
        return cust;
    }

    public void setShopCartSS(ShoppingCartSubsystem shopCartSS) {
        this.shopCartSS = shopCartSS;
    }
    
    public boolean getIsLoggedIn() {
        return isLoggedIn;
    }
    
    public void setIsLoggedIn(boolean b) {
        isLoggedIn = b;
    }
    
    public SessionData() {
       
    }
    private static final long serialVersionUID = -2621927042825785171L;
    
}
