package writingwithtransaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class UpdateCustomer {
	private String insert = "INSERT INTO CUSTOMER (name, street, city, zip) "
			+"VALUES (?, ?, ?, ?)";  
	private String update = "UPDATE CUSTOMER SET city = ? WHERE name = ?";
	private static final Logger LOG = Logger.getLogger(UpdateCustomer.class.getName());
	
	public int customerUpdates(Customer cust, UpdateData update) throws SQLException {
		int generatedKey = -1;
		//Step 1: Get Connection, manage using try-with-resournces
		try(Connection conn = ConnectManager.getConnection()) {
			
			try {
				//Step 2: Use Connection to create PreparedStatements
				PreparedStatement insertStatement = prepareCustInsertQuery(cust, conn);
				PreparedStatement updateStatement = prepareUpdateQuery(update, conn);
			
				//Step 3: begin transaction
				conn.setAutoCommit(false);	
				
				//Step 4: execute queries
				generatedKey = performInsert(insertStatement);
				performUpdate(updateStatement);
				
				//Step 5: commit (if queries fail, this line won't execute)
				conn.commit();
			} catch(SQLException e) {
				//Step 5: if an operation fails, rollback
				conn.rollback();
				//log exception
				StringBuilder sb = new StringBuilder();
				
				//SQLException now implements Iterable, so 
				//it is possible to iterate through chained 
				//exceptions like this
				for(Throwable t : e) {
					sb.append("+ " + e.getMessage()+ "\n");
				}
				LOG.warning("SQLException thrown:\n" + sb.toString());
				throw e;
			} finally{ 
				//Step 6: restore default state of autoCommit (no cleanup necessary)
				conn.setAutoCommit(true);
				
			}
		}
		return generatedKey;
	}
	private int performInsert(PreparedStatement stmt) throws SQLException {
		stmt.executeUpdate();
		
		int key = -1;
		ResultSet rs = stmt.getGeneratedKeys();
		if (rs.next()) {
			key = rs.getInt(1);
			LOG.info("Generated key for auto_increment id column after insert: "
					+ key);
		} else {
			LOG.info("No generated key for " + stmt.toString());
		}
		
		return key;
	}
	private void performUpdate(PreparedStatement stmt) throws SQLException {
		stmt.executeUpdate();
	}
	private PreparedStatement prepareCustInsertQuery(Customer cust, Connection conn) 
			throws SQLException{
		//to get the autogenerated key, we must include the extra parameter here
		PreparedStatement insertStatement = conn.prepareStatement(
				insert, Statement.RETURN_GENERATED_KEYS);
		insertStatement.setString(1, cust.getName());
		insertStatement.setString(2, cust.getStreet());
		insertStatement.setString(3, cust.getCity());
		insertStatement.setString(4, cust.getZip());
		return insertStatement;
		
	}
	
	private PreparedStatement prepareUpdateQuery(UpdateData ud, Connection conn) 
			throws SQLException {
		PreparedStatement updateStatement = conn.prepareStatement(update);
		updateStatement.setString(1, ud.getName());
		updateStatement.setString(2, ud.getCity());
		return updateStatement;
	}
	private Customer populateCustomer(ResultSet rs) throws SQLException {
		String name = null;
		String street = null;
		String city = null;
		String zip = null;
		while(rs.next()) {
			name = rs.getString("name").trim();
			street = rs.getString("street").trim();
			city = rs.getString("city").trim();
			zip = rs.getString("zip").trim();
		}
		return new Customer(name, street, city, zip);
	}
}
