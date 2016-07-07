package presentation.data;

import business.Address;
import javafx.beans.property.SimpleStringProperty;

public class CustomerPres {
	private final SimpleStringProperty firstName = new SimpleStringProperty();
	private final SimpleStringProperty lastName = new SimpleStringProperty();
	private Address address;
	public CustomerPres(String fName, String lName, Address addr) {
		firstName.set(fName);
		lastName.set(lName);
		address = addr;
	}
	public CustomerPres(String fName, String lName) {
		this(fName, lName, null);
	}
	public CustomerPres() {
		this(null,null,null);
	}
	public SimpleStringProperty firstNameProperty() {
		return firstName;
	}
	public SimpleStringProperty lastNameProperty() {
		return lastName;
	}
	public SimpleStringProperty fullNameProperty() {
		return new SimpleStringProperty(firstName.get() + " " + lastName.get());
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address a) {
		address = a;
	}
	public SimpleStringProperty streetProperty() {
		return new SimpleStringProperty(address.getStreet());
	}
	public SimpleStringProperty cityProperty() {
		return new SimpleStringProperty(address.getCity());
	}
	public SimpleStringProperty stateProperty() {
		return new SimpleStringProperty(address.getState());
	}
	public SimpleStringProperty zipProperty() {
		return new SimpleStringProperty(address.getZip());
	}
	public void setStreet(SimpleStringProperty s) {
		address.setStreet(s.get());
	}
	public void setCity(SimpleStringProperty s) {
		address.setCity(s.get());
	}
	public void setState(SimpleStringProperty s) {
		address.setState(s.get());
	}
	public void setZip(SimpleStringProperty s) {
		address.setZip(s.get());
	}
	
}
