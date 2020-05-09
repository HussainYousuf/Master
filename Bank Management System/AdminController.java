package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class AdminController {

	@FXML
	private TextField fname;

	@FXML
	private TextField lname;

	@FXML
	private TextField email;

	@FXML
	private TextField phone;

	@FXML
	private TextField address;

	@FXML
	private TextField country;

	@FXML
	private DatePicker dob;

	@FXML
	private TextField cnic;

	@FXML
	private Button editBtn;

	@FXML
	private TextField zip;

	@FXML
	private TextField passwdField;

	@FXML
	private TextField custID;

	@FXML
	private TextField m_status;

	@FXML
	private TextField gender;

	@FXML
	private TextField e_status;

	@FXML
	private TextField bname;
	@FXML
	private TextField searchQuery;

	@FXML
	private ListView<String> listView;
	@FXML
	private ListView<String> listView2;
	@FXML
	private ListView<HBoxCell> notificationListView;

	ObservableList<String> data;
	ObservableList<String> data2;

	static int cno;

	@FXML
	private TableView<?> transactionTable;
	@FXML
	private TableView<?> custTable;
	@FXML
	private TableView<?> loanTable;
	@FXML
	private TableView<?> accountTable;

	
	@FXML
	private Tab accountTab;
	@FXML
	private Tab custTab;
	@FXML
	private Tab loanTab;
	@FXML
	private Tab transactionTab;
	@FXML
	private Tab editTab;
	@FXML
	private Tab graphTab;
	@FXML
	private TabPane tabPane;

	private boolean edit_toggle;
	

    @FXML
    private PieChart pieChart1;

    @FXML
    private PieChart pieChart2;
    
    @FXML
    private PieChart pieChart3;

    @FXML
    private PieChart pieChart4;
    
    
    
    void setAccountChart() {
    	try {
    		ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
			Connection c = DAO.getDBConnection();
			ResultSet rs = c.createStatement().executeQuery("Select concat(fname,' ',lname) as name, sum(total_amount) from customer c, account a where a.cno = c.cno group by name");
			while(rs.next()) {
				String name = rs.getString(1);
				float amount = rs.getFloat(2);
				data.add(new PieChart.Data(name,amount));
			}
			pieChart1.setTitle("Accounts");
			pieChart1.setData(data);
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    void setTransferChart() {
    	try {
    		ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
			Connection c = DAO.getDBConnection();
			ResultSet rs = c.createStatement().executeQuery("Select concat(fname,' ',lname) as name, sum(t1.amount) from transactions t1, customer c, transfer t2, account a where t1.id = t2.id and c.cno = a.cno and a.ano = t2.acc_no_recepient group by name");
			while(rs.next()) {
				String name = rs.getString(1);
				float amount = rs.getFloat(2);
				data.add(new PieChart.Data(name,amount));
			}
			pieChart2.setTitle("Transfers");
			pieChart2.setData(data);
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    void setLoanChart() {
    	try {
    		ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
			Connection c = DAO.getDBConnection();
			ResultSet rs = c.createStatement().executeQuery("Select name, sum(amount) from loan group by name");
			while(rs.next()) {
				String name = rs.getString(1);
				float amount = rs.getFloat(2);
				data.add(new PieChart.Data(name,amount));
			}
			pieChart3.setTitle("Loans");
			pieChart3.setData(data);
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    void setdepoWithChart() {
    	try {
    		ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
			Connection c = DAO.getDBConnection();
			ResultSet rs = c.createStatement().executeQuery("select _type,sum(amount) from transactions where _type not like 'Transfer' group by _type");
			while(rs.next()) {
				String name = rs.getString(1);
				float amount = rs.getFloat(2);
				data.add(new PieChart.Data(name,amount));
			}
			pieChart4.setTitle("Deposits/WithDrawls");
			pieChart4.setData(data);
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

	@FXML
	void onBack(ActionEvent event) {
		try {
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Welcome");
			stage.setResizable(false);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void initialize() {

		ButtonCell.THIS = this;
		ButtonCell2.THIS = this;

		data = FXCollections.observableArrayList();
		data2 = FXCollections.observableArrayList();
		HBoxCell.listView = notificationListView;
		try {
			Connection c = DAO.getDBConnection();
			ResultSet rs = c.createStatement().executeQuery("Select message,customer_id from notification");
			List<HBoxCell> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new HBoxCell(rs.getString(1), rs.getInt(2)));
			}
			ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
			notificationListView.setItems(myObservableList);

			String SQL = "select * from customer limit 1";
			rs = c.createStatement().executeQuery(SQL);
			for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
				data.add(rs.getMetaData().getColumnName(i));
				data2.add("");
			}
			c.close();
		} catch (Exception e) {

		}

		listView.setItems(data);
		listView2.setItems(data2);
	}

	@FXML
	void onTabChange(Event event) {
		if (custTab.isSelected()) {
			Utility.DISPLAY_TABLE2(custTable, "select * from customer");
		}
		if (loanTab.isSelected()) {
			Utility.DISPLAY_TABLE(loanTable, "select * from loan");
		}
		if (accountTab.isSelected()) {
			Utility.DISPLAY_TABLE(accountTable, "select bname,acc_type,total_amount,concat(fname,' ',lname) as name,account.date_entered,ano from account,customer,branch where account.cno = customer.cno and account.bno = branch.bno");
		}
		if (graphTab.isSelected()) {
			setAccountChart();
			setTransferChart();
			setLoanChart();
			setdepoWithChart();
		}
		if (transactionTab.isSelected()) {
			String query = "select (select concat(fname,' ',lname) from customer where cno = t1.cno) as name, (select bname from branch,account where branch.bno = account.bno and t1.acc_no = account.ano) as branch_name, _type, amount, (select concat(fname,' ',lname) from customer c, account a where c.cno = a.cno and a.ano = t2.acc_no_recepient) as nameOfRecepient, (select b.bname from branch b, account a where b.bno = a.bno and a.ano = t2.acc_no_recepient) as branchOfRecepient, _date from transactions t1 left join transfer t2 on t2.id = t1.id";
			Utility.DISPLAY_TABLE(transactionTable, query);
		}
	}

	@FXML
	void onRemove(ActionEvent event) {
		String item = listView.getSelectionModel().getSelectedItem();
		int index = listView.getSelectionModel().getSelectedIndex();
		data.set(index, "");
		data2.set(index, item);
		listView2.setItems(data2);
	}

	@FXML
	void onAdd(ActionEvent event) {
		String item = listView2.getSelectionModel().getSelectedItem();
		int index = listView2.getSelectionModel().getSelectedIndex();
		data2.set(index, "");
		data.set(index, item);
		listView.setItems(data);
	}

	@FXML
	void onShow(ActionEvent event) {
		String query = "Select ";
		for (String i : data) {
			if (i.length() <= 0)
				continue;
			query += i + ",";
		}
		query = query.substring(0, query.length() - 1);
		query += " from customer";
		Utility.DISPLAY_TABLE2(custTable, query);
	}

	@FXML
	void onAddBranch(ActionEvent event) {

		try {
			Connection c = DAO.getDBConnection();
			c.createStatement().execute("Insert into branch values (" + "'" + bname.getText() + "'" + ",null)");
			c.close();
			new TrayNotification("Branch Inserted", "", NotificationType.SUCCESS).showAndDismiss(Duration.seconds(5));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			new TrayNotification("Branch Insertion Failed", "", NotificationType.ERROR)
					.showAndDismiss(Duration.seconds(5));
			e.printStackTrace();
		}
	}

	@FXML
	void onSearch(ActionEvent event) {
		String query = searchQuery.getText();
		Utility.DISPLAY_TABLE2(custTable, query);
	}

	@FXML
	void onCancel(ActionEvent event) {
		editBtn.setText("Edit");
		edit_toggle = false;
		initProfile();
	}

	@FXML
	void onEdit(ActionEvent event) {

		edit_toggle = !edit_toggle;
		if (edit_toggle) {
			editBtn.setText("Save");
		} else {
			editBtn.setText("Edit");
			try {
				Connection c = DAO.getDBConnection();
				PreparedStatement statement = c.prepareStatement(
						"update customer set fname = ? , lname = ? , dob = ? , email = ? , phone = ? , zip = ? , cnic = ? , country = ? , address = ? , sex = ? , status = ? , marital_status = ? , passwd = ? where cno = "
								+ cno);
				statement.setString(1, fname.getText());
				statement.setString(2, lname.getText());
				java.util.Date date = java.util.Date
						.from(dob.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				statement.setDate(3, sqlDate);
				statement.setString(4, email.getText());
				statement.setString(5, phone.getText());
				statement.setString(6, zip.getText());
				statement.setString(7, cnic.getText());
				statement.setString(8, country.getText());
				statement.setString(9, address.getText());
				statement.setString(10, gender.getText());
				statement.setString(11, e_status.getText());
				statement.setString(12, m_status.getText());
				statement.setString(13, passwdField.getText());
				statement.executeUpdate();
				initProfile();
				c.close();
				new TrayNotification("Edit was Successful", "", NotificationType.SUCCESS)
						.showAndDismiss(Duration.seconds(5));

			} catch (SQLException e) {
				new TrayNotification("Edit Failed", "", NotificationType.ERROR).showAndDismiss(Duration.seconds(5));
				e.printStackTrace();
			}
		}

	}

	private void initProfile() {
		try {
			Connection c = DAO.getDBConnection();
			ResultSet rs = c.createStatement().executeQuery("Select * from customer where cno=" + cno);
			while (rs.next()) {
				fname.setText(rs.getString(1));
				lname.setText(rs.getString(2));
				dob.setValue(Utility.LOCAL_DATE(rs.getString(3)));
				email.setText(rs.getString(4));
				phone.setText(rs.getString(5));
				zip.setText(rs.getString(6));
				cnic.setText(rs.getString(7));
				country.setText(rs.getString(8));
				address.setText(rs.getString(9));
				gender.setText(rs.getString(10));
				e_status.setText(rs.getString(11));
				m_status.setText(rs.getString(12));
				custID.setText(rs.getString(14));
				passwdField.setText(rs.getString(15));
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void callDelete() {
		try {
			Connection c = DAO.getDBConnection();
			c.createStatement().execute("Delete from customer where cno = " + cno);
			c.close();
			Utility.DISPLAY_TABLE2(custTable, "select * from customer");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void callEdit() {
		initProfile();
		tabPane.getSelectionModel().select(editTab);
	}

}

class HBoxCell extends HBox {
	Label label = new Label();
	Button button = new Button("Allow");
	int customerId;
	static ListView<HBoxCell> listView;

	HBoxCell(String labelText, int id) {
		super();
		customerId = id;
		label.setText(labelText);
		label.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(label, Priority.ALWAYS);
		this.getChildren().addAll(label, button);
		HBoxCell This = this;
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					Connection c = DAO.getDBConnection();
					c.createStatement().execute("delete from notification where customer_id = " + customerId);
					c.close();
					listView.getItems().remove(This);
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		});
	}
}