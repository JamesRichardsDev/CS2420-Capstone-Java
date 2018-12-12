package capstone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class CustomerDB {

	static Connection conn = null;
	static String URL = "jdbc:mysql://localhost:3306/mma?allowPublicKeyRetrieval=true&useSSL=false";
	static String username = "user";
	static String password = "sesame";

	public static ResultSet getCustomers() throws Exception {
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			conn = (Connection) DriverManager.getConnection(URL, username, password);
			statement = (PreparedStatement) conn.prepareStatement("SELECT * FROM MurachDB ORDER BY email");

			result = statement.executeQuery();

		} catch (Exception e) {
			System.out.println(e);
		} finally {

		}

		return result;

	}

	public static void editCustomer(String newEmail, String firstName, String lastName, String oldEmail)
			throws Exception {
		PreparedStatement statement = null;
		try {
			conn = (Connection) DriverManager.getConnection(URL, username, password);
			statement = (PreparedStatement) conn
					.prepareStatement("UPDATE MurachDB SET email=?,first_Name=?,last_Name=? WHERE email=?");
			statement.setString(1, newEmail);
			statement.setString(2, firstName);
			statement.setString(3, lastName);
			statement.setString(4, oldEmail);
			statement.executeUpdate();

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			statement.close();
			conn.close();
		}

	}

	public static void deleteCustomerByEmail(String email) throws Exception {
		PreparedStatement pst = null;
		try {
			conn = (Connection) DriverManager.getConnection(URL, username, password);

			pst = (PreparedStatement) conn.prepareStatement("DELETE FROM MurachDB WHERE email =?");
			pst.setString(1, email);
			pst.executeUpdate();

		} catch (Exception e) {
			System.out.println(e);
		}
		pst.close();
		conn.close();

	}

	public static Customer AddCustomer(String first, String last, String email) throws Exception {

		Customer c = new Customer(first, last, email);
		PreparedStatement pst = null;
		try {
			conn = (Connection) DriverManager.getConnection(URL, username, password);

			pst = (PreparedStatement) conn
					.prepareStatement("INSERT INTO MurachDB (email,first_Name,last_Name) VALUES(?,?,?)");
			pst.setString(1, email);
			pst.setString(2, first);
			pst.setString(3, last);
			pst.executeUpdate();

			JOptionPane.showMessageDialog(null, first + " " + last + " was added to the database");
		} catch (Exception e) {
			System.out.println(e);
		}
		pst.close();
		conn.close();

		return c;
	}

}
