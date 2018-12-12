package capstone;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
import javax.swing.JScrollPane;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import java.awt.Font;

public class CustomerMaintApp extends JFrame {

	private JFrame frame;
	private static JTable table;
	static DefaultTableModel model = new DefaultTableModel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CustomerMaintApp window = new CustomerMaintApp();
					window.frame.setVisible(true);

					model.addColumn("Email");
					model.addColumn("First Name");
					model.addColumn("Last Name");

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CustomerMaintApp() {
		initialize();
		refresh();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Customer Manager");
		frame.setResizable(false);
		frame.setBounds(100, 100, 675, 342);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(31, 55, 624, 173);
		frame.getContentPane().add(scrollPane);

		table = new JTable(model);
		scrollPane.setViewportView(table);

		JButton btnAdd = new JButton("Add");

		// When action button is clicked
		btnAdd.addActionListener((ActionEvent) -> {
			String email = "";
			boolean isValidated;
			do {
				email = JOptionPane.showInputDialog(null, "Enter the Email", "New Customer Email",
						JOptionPane.QUESTION_MESSAGE);
				isValidated = validateEmail(email);
				if (!isValidated) {
					JOptionPane.showMessageDialog(null, email + " is not a valid email.", "Invalid Email",
							JOptionPane.ERROR_MESSAGE);
				}

			} while (!isValidated);

			String first = JOptionPane.showInputDialog(null, "Enter the First Name", "New Customer First Name",
					JOptionPane.QUESTION_MESSAGE);
			String last = JOptionPane.showInputDialog(null, "Enter the Last Name", "New Customer Last Name",
					JOptionPane.QUESTION_MESSAGE);
			try {
				CustomerDB.AddCustomer(first, last, email);
				refresh();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		btnAdd.setBounds(28, 240, 117, 29);
		frame.getContentPane().add(btnAdd);

		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener((ActionEvent) -> {
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			int selectedRowIndex = table.getSelectedRow();
			String emailSelection = model.getValueAt(selectedRowIndex, 0).toString();
			String newEmail = "";

			boolean isValidated;
			do {
				newEmail = JOptionPane.showInputDialog(null, "Enter the Email", "Edit Customer Email",
						JOptionPane.QUESTION_MESSAGE);
				isValidated = validateEmail(newEmail);
				if (!isValidated) {
					JOptionPane.showMessageDialog(null, newEmail + " is not a valid email.", "Invalid Email",
							JOptionPane.ERROR_MESSAGE);
				}

			} while (!isValidated);

			String newFirstName = JOptionPane.showInputDialog(null, "Enter the customer's new first name",
					"Edit Customer First Name", JOptionPane.QUESTION_MESSAGE);
			String newLastName = JOptionPane.showInputDialog(null, "Enter the customer's new last name",
					"Edit Customer Last Name", JOptionPane.QUESTION_MESSAGE);

			try {
				CustomerDB.editCustomer(newEmail, newFirstName, newLastName, emailSelection);
			} catch (Exception e) {
				e.printStackTrace();
			}

			refresh();

		});
		btnEdit.setBounds(157, 240, 117, 29);
		frame.getContentPane().add(btnEdit);

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener((ActionEvent) -> {

			DefaultTableModel model = (DefaultTableModel) table.getModel();
			int selectedRowIndex = table.getSelectedRow();
			String emailSelection = model.getValueAt(selectedRowIndex, 0).toString();

			JOptionPane.showMessageDialog(null, emailSelection + " was deleted from the database");

			try {
				CustomerDB.deleteCustomerByEmail(emailSelection);
			} catch (Exception e) {
				e.printStackTrace();
			}

			refresh();

		});
		btnDelete.setBounds(286, 240, 117, 29);
		frame.getContentPane().add(btnDelete);

		JLabel lblCustomers = new JLabel("Customers");
		lblCustomers.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		lblCustomers.setBounds(275, 6, 125, 37);
		frame.getContentPane().add(lblCustomers);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener((ActionEvent) -> {
			JOptionPane.showMessageDialog(null, "Goodbye!");

			frame.dispose();
		});
		btnExit.setBounds(552, 240, 117, 29);
		frame.getContentPane().add(btnExit);

		JButton btnHelp = new JButton("Help");
		btnHelp.addActionListener((ActionEvent) -> {
			JOptionPane.showMessageDialog(null,
					"Click 'add' to add a new customer to the databse.\n"
							+ "Select a Customer and click 'edit' to update a customer's information in the databse.\n"
							+ "Select a Customer and click 'delete' to remove that customer from the database.\n"
							+ "Click 'exit' to close this application.",
					"Help", JOptionPane.INFORMATION_MESSAGE);
		});
		btnHelp.setBounds(415, 240, 117, 29);
		frame.getContentPane().add(btnHelp);

	}

	public static void refresh() {

		try {
			ResultSet res = CustomerDB.getCustomers();

			table.setModel(DbUtils.resultSetToTableModel(res));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static boolean validateEmail(String email) {
		String emailRegex = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-zA-Z]{2,})$";
		Pattern emailPat = Pattern.compile(emailRegex);
		Matcher matcher = emailPat.matcher(email);
		return matcher.matches();
	}

}