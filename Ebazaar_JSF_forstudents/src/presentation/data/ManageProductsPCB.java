package presentation.data;

import business.exceptions.BackendException;
import business.exceptions.UnauthorizedException;
import business.externalinterfaces.Product;
import business.usecasecontrol.ManageProductsController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import java.util.logging.Logger;

import presentation.control.Authorization;
import presentation.control.Callback;



@Named(value = "manageProductsPCB")
@SessionScoped
public class ManageProductsPCB implements Serializable{
	private final static Logger LOG = Logger.getLogger(ManageProductsPCB.class.getSimpleName());
    private String catalogSelected;
    private List<String> catalogs = new ArrayList<String>();
    private List<Product> products = new ArrayList<Product>();
    private ManageProductsController prodsControl = business.usecasecontrol.ManageProductsController.INSTANCE;
    private final String DEFAULT_CATALOG = "Books";
    private HashMap<String,Boolean> checked = new HashMap<String,Boolean>(); 
    private String selectedProductName;
    private boolean disabled=true;
    private String prodName;
    private String unitPrice;
    private String mfDate;
    private String quantityAvail;
    private Product productToEdit;
     @Inject
    private SessionData sessionContext;

    public Product getProductToEdit() {
        return productToEdit;
    }

    public void setProductToEdit(Product productToEdit) {
        this.productToEdit = productToEdit;
    }
    
    
    public List<String> getCatalogs() {
    	//implement
    	LOG.warning("getCalalogs method in ManageProductsPCB not implemented");
        
        return new ArrayList<String>();
    }
    
    //Gets the products related to the selected catalog
    public List<Product> getProducts() {
        try {
            String catalog = getCatalogSelected();
            products = prodsControl.getProductsList(catalog);
            return products;
        } catch(BackendException e) {
            MessagesUtil.displayError(e.getMessage());
            return new ArrayList<Product>();
        }
    }
     
     
     
    public boolean manageProducts(Requirement r) {
        //Authorize first
        try {
            Authorization.checkAuthorization(r, sessionContext.custIsAdmin());
        } catch (UnauthorizedException e) {
            MessagesUtil.displayError(e.getMessage());
            return false;
        }
        
        catalogs = getCatalogs();
        products = getProducts();
        return true;
        
    }
    
    public boolean manageCatalogs(Requirement r) {
        //Authorize first
        try {
            Authorization.checkAuthorization(r, sessionContext.custIsAdmin());
        } catch (UnauthorizedException e) {
            MessagesUtil.displayError(e.getMessage());
            return false;
        }
        
        catalogs = getCatalogs();
        return true;
        
    }
public void changeProductsForNewCatalog(ValueChangeEvent event) {
        
       products.clear();
       
       catalogSelected=event.getNewValue().toString();
       
       products = getProducts();
       
}
          
                
    public String getCatalogSelected() {
        return (catalogSelected==null) ? DEFAULT_CATALOG : catalogSelected;
    }

    public void setCatalogSelected(String catalogSelected) {
        this.catalogSelected = catalogSelected;
    }
    
    public HashMap<String, Boolean> getChecked() {
        return checked;
    }

    public void setChecked(HashMap<String, Boolean> checked) {
        this.checked = checked;
    }
    
    

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

   

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getMfDate() {
        return mfDate;
    }

    public void setMfDate(String mfDate) {
        this.mfDate = mfDate;
    }

    public String getQuantityAvail() {
        return quantityAvail;
    }

    public void setQuantityAvail(String quantityAvail) {
        this.quantityAvail = quantityAvail;
    }
 
    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getSelectedProductName() {
        return selectedProductName;
    }

    public void setSelectedProductName(String selectedProductName) {
        this.selectedProductName = selectedProductName;
    }

    private String newCatalogName;
    
    public String editCatalogName(){
        
        for(String cat:catalogs){
             if(checked.get(cat)){
                catalogSelected=cat;
                return "editCatalog";
             }
          }
        //return the page itself if no catalog is selected
        return null;
    }
    public String saveEditedCatalogName(){
        //do an update 
          for(String cat:catalogs){
              
               if(cat.equals(catalogSelected)){
                   catalogSelected = cat;  
               }
           }
        checked.clear(); 
        return "manageCatalogWindow";
    }
    public String getNewCatalogName() {
        return newCatalogName;
    }

    public void setNewCatalogName(String newCatalogName) {
        this.newCatalogName = newCatalogName;
    }
  
    
    public String addCatalog(){
    
        return "manageCatalogWindow";
       // newCatalogName=null;
    }
    /*
     public Catalog[] changeListtoArray(){
        // //list converted to array to avoid to Avoid ConcurrentModificationException 
      Catalog[] itemArray=new  Catalog[catalogs.size()];
       
        for (int i=0;i<itemArray.length;i++) {
           Catalog item=catalogs.get(i);
            itemArray[i]=item;
        }
        return itemArray;
      }*/
 
      public String deleteCatalog(){
          /*
           //list converted to array to avoid to Avoid ConcurrentModificationException
           Catalog[] itemArray=changeListtoArray();
      
        for (int i=0;i<itemArray.length;i++) {
            Catalog cat=itemArray[i];
            
               if(checked.get(cat.getName())){
                   String catId=cat.getCatId();
                    List<product>  prod = stabController.getproductList();
                       for(product p:prod){
                           if(p.getCatalogId().toString().equals(catId)){
                                 FacesMessage msg = new FacesMessage("Catalog you select has products, it can't be deleted");
                                 FacesContext.getCurrentInstance().addMessage(null, msg);
                                 checked.clear(); 
                                return null;
                           }
                       }
                     //get the product list and see if any of them have the id
                 catalogs.remove(cat);   
               }
            }*/
     checked.clear(); 
     return null;
    }
     public String addNewproduct(){
         setMfDate(null);
         setQuantityAvail(null);
         setUnitPrice(null);
         
         setProdName(null);
         
         return "addNewproduct";
     }
     public String saveNewProduct(){ 
         //implement
    	 LOG.warning("saveNewProduct in ManageProductsPCB not implemented");
         return "manageProductWindow";
     }
   public String editProduct(){
       
    
          for(Product p:products){
              
              if(checked.get(p.getProductName())){
                  productToEdit = p;
                  checked.clear();
                  return "editProduct";
                 }
              }
          //return the page itself if no product is selected
          return null;
       
        }
   public String saveEditedProduct(){
	   LOG.warning("saveEditedProduct in ManageProductsPCB not implemented");
           for(Product p:products){
              
//               if( p.getCatalogId().equals(productToEdit.getCatalogId())&&
//                       p.getMfgDate().equals(productToEdit.getMfgDate())){
//                  
//                   p.setProductName(productToEdit.getProductName()); 
//                   p.setQuantityAvail(productToEdit.getQuantityAvail());
//                   p.setUnitPrice(productToEdit.getUnitPrice()); 
//               }
           }
        checked.clear(); 
        return "manageProductWindow";
       }
   public String deleteProduct(){
       
          for(Product p:products){
              
              if(checked.get(p.getProductName())){
                  products.remove(p);

                  ///need also to remove it from the back end
                  //stabController.deleteProduct(p);
                  checked.clear();
                  return null;
                 }
              }
      return null; 
   }
    
   public ManageProductsCallback getManageProductsCallback() {
       return new ManageProductsCallback();
   }
    
   public class ManageProductsCallback implements Callback {

        public final Requirement REQUIREMENT = Requirement.MANAGE_PRODUCTS;

        public String doUpdate() {
            if(manageProducts(REQUIREMENT)) return null;
            return "index";
        }
    }
   
   public ManageCatalogsCallback getManageCatalogsCallback() {
       return new ManageCatalogsCallback();
   }
   
   public class ManageCatalogsCallback implements Callback {

        public final Requirement REQUIREMENT = Requirement.MANAGE_CATALOGS;

        public String doUpdate() {
            if(manageCatalogs(REQUIREMENT)) {
                return null;
            } 
            return "index";
        }
    }
}