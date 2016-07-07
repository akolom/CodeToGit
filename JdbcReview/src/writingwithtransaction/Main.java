package writingwithtransaction;

import java.sql.SQLException;

public class Main {

	public static void main(String[] args) {
		UpdateCustomer rc = new UpdateCustomer();
		Object[] updateInfo = prepareUpdateInfo();
		Customer cust = (Customer)updateInfo[0];
		UpdateData ud = (UpdateData)updateInfo[1];
		
		try {
			int newCustId = rc.customerUpdates(cust,  ud);
			System.out.println("Updates successful. New customer id for " +
			   cust.getName() + " is " + newCustId);
		} catch(SQLException e) {
			for(Throwable t : e) {
				System.out.println("* " + t.getMessage());
			}
		}

	}
	private static Object[] prepareUpdateInfo() {
		UpdateData upData = new UpdateData("Don Lansing", "Mountain View");
		Customer cust = new Customer("Don Lansing","111 Channing Ave","Los Altos", "94301");
		Object[] retval = {cust,upData};
		return retval;
	}
}
