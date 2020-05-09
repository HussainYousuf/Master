package application;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.authorize.Environment;
import net.authorize.api.contract.v1.CreateTransactionRequest;
import net.authorize.api.contract.v1.CreateTransactionResponse;
import net.authorize.api.contract.v1.CreditCardType;
import net.authorize.api.contract.v1.MerchantAuthenticationType;
import net.authorize.api.contract.v1.MessageTypeEnum;
import net.authorize.api.contract.v1.PaymentType;
import net.authorize.api.contract.v1.TransactionRequestType;
import net.authorize.api.contract.v1.TransactionResponse;
import net.authorize.api.contract.v1.TransactionTypeEnum;
import net.authorize.api.controller.CreateTransactionController;
import net.authorize.api.controller.base.ApiOperationBase;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class CustomerController {

	@FXML
    private PieChart c_pieGraph;
	@FXML
    private PieChart w_pieChart;
	
	

	private int cno;
	@FXML
    private Tab accountTab;
	@FXML
    private Tab transactionTab;
	@FXML
    private Tab l_tab;
	@FXML
	private TextField fname;
	@FXML
	private TextField lname;
	@FXML
	private TextField email;
	@FXML
	private TextField phone;
	@FXML
	private TextField zip;
	@FXML
	private TextField address;
	@FXML
	private TextField country;
	@FXML
	private TextField passwdField;
	@FXML
	private TextField custID;
	@FXML
	private TextField m_status;
	@FXML
	private TextField e_status;
	@FXML
	private TextField gender;
	@FXML
	private DatePicker dob;
	@FXML
	private TextField cnic;
	@FXML
	private TableView accTable;
	@FXML
	private TableView transactionTable;
	
	

	@FXML
    private Button editBtn;
	@FXML
	private TextField w_amnt;
	@FXML
	private TextField d_amnt;
	@FXML
	private TextField w_accno;
	@FXML
	private TextField l_name;
	@FXML
	private TextField l_amnt;
	@FXML
	private ComboBox<String> l_combo;
	@FXML
	private TextField l_pay_amnt;
	@FXML
	private TableView l_table;
	
	
	@FXML
	private TextField t_accno;
	@FXML
	private TextField t_amnt;
	@FXML
	private TextField recepient_accno;
	@FXML
	private ComboBox<String> branchCombo;
	@FXML
	private TextField initAmnt;
	@FXML
	private ToggleGroup aGrp;
	@FXML
	private ToggleGroup l_grp;

	private boolean edit_toggle;
	
	private ObservableList<String> loan_list = FXCollections.observableArrayList();

	
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
	
	private void setPieChart() {
		ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
		Connection c;
		try {
			c = DAO.getDBConnection();
			ResultSet rs = c.createStatement().executeQuery("Select concat(fname,' ',lname) as name, sum(t1.amount) from transactions t1, customer c, transfer t2, account a where t1.id = t2.id "
					+ "and c.cno = a.cno and a.ano = t2.acc_no_recepient and t1.cno = " + cno + " group by name");
			while(rs.next()) {
				String name = rs.getString(1);
				float amnt = rs.getFloat(2);
				data.add(new PieChart.Data(name,amnt));         
			}
			c_pieGraph.setTitle("History");
			c_pieGraph.setData(data);
			c.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setw_PieChart() {
		ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
		Connection c;
		try {
			c = DAO.getDBConnection();
			ResultSet rs = c.createStatement().executeQuery("select _type,sum(amount) from transactions where cno = " + cno + " group by _type");
			while(rs.next()) {
				String name = rs.getString(1);
				if(name.equals("Transfer")) continue;
				float amnt = rs.getFloat(2);
				data.add(new PieChart.Data(name,amnt));         
			}
			w_pieChart.setTitle("History");
			w_pieChart.setData(data);
			c.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	private void init() {
		try {
			Connection conn = DAO.getDBConnection();
			ResultSet rs = conn.createStatement().executeQuery("Select name from loan");
			loan_list.clear();
			while(rs.next()) {
				loan_list.add(rs.getString(1));
			}
			l_combo.setItems(loan_list);
			initProfile();
		    new TrayNotification("Welcome", fname.getText() + " " + lname.getText(), NotificationType.SUCCESS).showAndDismiss(Duration.seconds(5));
			rs = conn.createStatement().executeQuery("Select bname from branch");
			ObservableList<String> row = FXCollections.observableArrayList();
			while (rs.next()) {
				row.add(rs.getString(1));
			}
			branchCombo.setItems(row);
	
			setPieChart();
			setw_PieChart();
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void makeTransaction() {
		// Common code to set for all requests
		ApiOperationBase.setEnvironment(Environment.SANDBOX);

		MerchantAuthenticationType merchantAuthenticationType = new MerchantAuthenticationType();
		merchantAuthenticationType.setName("5S47dCxB");
		merchantAuthenticationType.setTransactionKey("997QavW8ka3MN66X");
		ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);

		// Populate the payment data
		PaymentType paymentType = new PaymentType();
		CreditCardType creditCard = new CreditCardType();
		creditCard.setCardNumber("4242424242424242");
		creditCard.setExpirationDate("0822");
		paymentType.setCreditCard(creditCard);

		// Create the payment transaction request
		TransactionRequestType txnRequest = new TransactionRequestType();
		txnRequest.setTransactionType(TransactionTypeEnum.AUTH_CAPTURE_TRANSACTION.value());
		txnRequest.setPayment(paymentType);
		txnRequest.setAmount(new BigDecimal(500.00));

		// Make the API Request
		CreateTransactionRequest apiRequest = new CreateTransactionRequest();
		apiRequest.setTransactionRequest(txnRequest);
		CreateTransactionController controller = new CreateTransactionController(apiRequest);
		controller.execute();

		CreateTransactionResponse response = controller.getApiResponse();

		if (response != null) {

			// If API Response is ok, go ahead and check the transaction response
			if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {

				TransactionResponse result = response.getTransactionResponse();
				if (result.getResponseCode().equals("1")) {
					String info = result.getResponseCode() + " Successful Credit Card Transaction " + result.getAuthCode() + " " + result.getTransId();
				    new TrayNotification("MESSAGE FROM AUTHORIZE.NET",info, NotificationType.SUCCESS).showAndDismiss(Duration.seconds(60));
				} else {
				    new TrayNotification("MESSAGE FROM AUTHORIZE.NET","Failed Transaction", NotificationType.ERROR).showAndDismiss(Duration.seconds(60));
				}
			} else {
			    new TrayNotification("MESSAGE FROM AUTHORIZE.NET","Failed Transaction", NotificationType.ERROR).showAndDismiss(Duration.seconds(60));
			}
		}
		
		
	}
	
	
	@FXML
	public void onEdit(ActionEvent event) {
		edit_toggle = !edit_toggle;
		if(edit_toggle) {
			editBtn.setText("Save");
		}else {
			editBtn.setText("Edit");
			try {
				Connection c = DAO.getDBConnection();
				PreparedStatement statement = c.prepareStatement("update customer set fname = ? , lname = ? , dob = ? , email = ? , phone = ? , zip = ? , cnic = ? , country = ? , address = ? , sex = ? , status = ? , marital_status = ? , passwd = ? where cno = " + cno);
				statement.setString(1, fname.getText());
				statement.setString(2, lname.getText());
				java.util.Date date = java.util.Date.from(dob.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
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
			    new TrayNotification("Edit was Successful","", NotificationType.SUCCESS).showAndDismiss(Duration.seconds(5));
				
			} catch (SQLException e) {
			    new TrayNotification("Edit Failed","", NotificationType.ERROR).showAndDismiss(Duration.seconds(5));
				e.printStackTrace();
			}
		}
		
	}

	
	@FXML
	public void onLoan(ActionEvent event) {

		Connection c;
		try {
			String insert = String.format("insert into loan values (%d,%.2f,'%s','%s','Pending',%.2f,%.2f,now(),0,null)", cno,Float.parseFloat(l_amnt.getText()),l_name.getText(),((RadioButton) l_grp.getSelectedToggle()).getText(),0.0,Float.parseFloat(l_amnt.getText()));
			c = DAO.getDBConnection();
			c.createStatement().execute(insert);
			loan_list.add(l_name.getText());
			l_combo.setItems(loan_list);
			c.close();
			Utility.DISPLAY_TABLE(l_table, "select * from loan where cno = " + cno);
		    new TrayNotification("Loan Inserted Successfully", "", NotificationType.SUCCESS).showAndDismiss(Duration.seconds(5));
		} catch (Exception e) {
			e.printStackTrace();
		    new TrayNotification("Loan Insertion Failed", "", NotificationType.ERROR).showAndDismiss(Duration.seconds(5));
		}
		
	}
	
	
	@FXML
	public void onPay(ActionEvent event) {

		
		try {
			Connection c = DAO.getDBConnection();
			ResultSet rs = c.createStatement().executeQuery("Select amount,paid,remaining from loan where name like '" + l_combo.getSelectionModel().getSelectedItem() + "' and cno = " + cno);
			float amount = 0;
			float paid = 0;
			while(rs.next()) {
				amount = rs.getFloat(1);
				paid = rs.getFloat(2);
			}
			float payed = Float.parseFloat(l_pay_amnt.getText());
			payed += paid;
			String update;
			String name = l_combo.getSelectionModel().getSelectedItem();
			if(payed >= amount)  update = String.format("update loan set status = 'Payed' , paid = %.2f , remaining = 0.00 , date_return = now() where name like '%s'",payed,name);
			else update = String.format("update loan set status = 'Pending' , paid = %.2f , remaining = %.2f  where name like '%s'",payed,amount - payed,name);
			c.createStatement().executeUpdate(update);
			c.close();
			Utility.DISPLAY_TABLE(l_table, "select * from loan where cno = " + cno);
		    new TrayNotification("Payment was Successful", "", NotificationType.SUCCESS).showAndDismiss(Duration.seconds(5));

		} catch (SQLException e) {
			e.printStackTrace();
		    new TrayNotification("Payment Failed", "", NotificationType.ERROR).showAndDismiss(Duration.seconds(5));

		}
		
		
		
	}

	
	@FXML
	public void onCancel(ActionEvent event) {

		editBtn.setText("Edit");
		edit_toggle = false;
		initProfile();
		
	}

	@FXML
	void onAddAccount(ActionEvent event) throws SQLException {
		Connection conn = null;
		try {
			conn = DAO.getDBConnection();
			conn.setAutoCommit(false);
			int branchNo;
			float amnt;
			ResultSet rs = conn.createStatement().executeQuery("select bno from branch where bname like '" + branchCombo.getSelectionModel().getSelectedItem().toString()+"'");
			rs.next();
			branchNo = rs.getInt(1);
			amnt = Float.parseFloat(initAmnt.getText());
			PreparedStatement statement = conn.prepareStatement("insert into account values (?,?,?,?,now(),null)");
			statement.setInt(1,branchNo);
			statement.setString(2, ((RadioButton)aGrp.getSelectedToggle()).getText());
			statement.setFloat(3, amnt);
			statement.setInt(4,cno);
			statement.execute();
			statement.close();
			conn.commit();
			
			Utility.DISPLAY_TABLE(accTable, "select bname,acc_type,total_amount,concat(fname,' ',lname) as name,account.date_entered,ano from account,customer,branch where account.cno = customer.cno and account.bno = branch.bno and account.cno = " + cno);
		    new TrayNotification("Account Created", "", NotificationType.SUCCESS).showAndDismiss(Duration.seconds(5));
		    
		}catch(Exception e) {
			conn.rollback();
		    new TrayNotification("Account Creation Failed", "", NotificationType.ERROR).showAndDismiss(Duration.seconds(5));
			e.printStackTrace();
		}finally {
			conn.close();
		}		
	}

	public void setCno(int cno) {
		this.cno = cno;
		init();
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
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
    void onTabChange(Event event) {
		if(accountTab.isSelected()) Utility.DISPLAY_TABLE(accTable, "select bname,acc_type,total_amount,concat(fname,' ',lname) as name,account.date_entered,ano from account,customer,branch where account.cno = customer.cno and account.bno = branch.bno and account.cno = " + cno);
		if(transactionTab.isSelected()) Utility.DISPLAY_TABLE(transactionTable, "select * from transactions where cno = " + cno);
		if(l_tab.isSelected()) Utility.DISPLAY_TABLE(l_table, "select * from loan where cno = " + cno);
    }
	
	@FXML
    void onWithDraw(ActionEvent event) throws SQLException {
		
		
		
		Connection conn = null;
		
		try {
			conn = DAO.getDBConnection();
			conn.setAutoCommit(false);
			
			float amnt = Float.parseFloat(w_amnt.getText());
			int accno = Integer.parseInt(w_accno.getText());
			ResultSet rs = conn.createStatement().executeQuery("select total_amount from account where ano = " + accno);
			if(!rs.next()) throw new Exception();
			float total_amount = rs.getInt(1);
			if(total_amount < amnt) throw new Exception();
			total_amount -= amnt;
			conn.createStatement().execute("update account set total_amount = " + total_amount + " where ano = " + accno);
			
			PreparedStatement statement = conn.prepareStatement("Insert Into transactions values (?,?,?,?,now(),null)");
			statement.setInt(1, cno);
			statement.setInt(2, accno);
			statement.setFloat(3, amnt);
			statement.setString(4, "WithDraw");
			statement.executeUpdate();
			
			conn.commit();
			
		    new TrayNotification("WithDrawl was Successful", "", NotificationType.SUCCESS).showAndDismiss(Duration.seconds(5));
		    setw_PieChart();
			
		} catch (SQLException e) {
			conn.rollback();
		    new TrayNotification("WithDrawl Failed", "", NotificationType.ERROR).showAndDismiss(Duration.seconds(5));
			e.printStackTrace();
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
		    new TrayNotification("WithDrawl Failed", "", NotificationType.ERROR).showAndDismiss(Duration.seconds(5));
		} finally {
			conn.close();
		}
    }
	
	@FXML
    void onDeposit(ActionEvent event) throws SQLException {
		
		makeTransaction();
		
		Connection conn = null;
		
		System.out.println("reached");
		
		try {
			float amnt = Float.parseFloat(d_amnt.getText());
			int accno = Integer.parseInt(w_accno.getText());
			conn = DAO.getDBConnection();
			conn.setAutoCommit(false);

			ResultSet rs = conn.createStatement().executeQuery("select total_amount from account where ano = " + accno);
			if(!rs.next()) throw new Exception();
			float total_amount = rs.getInt(1);
			total_amount += amnt;
			conn.createStatement().execute("update account set total_amount = " + total_amount + " where ano = " + accno);
			
			
			PreparedStatement statement = conn.prepareStatement("Insert Into transactions values (?,?,?,?,now(),null)");
			statement.setInt(1, cno);
			statement.setInt(2, accno);
			statement.setFloat(3, amnt);
			statement.setString(4, "Deposit");
			statement.executeUpdate();
			
			conn.commit();
		    new TrayNotification("Deposit was Successful", "", NotificationType.SUCCESS).showAndDismiss(Duration.seconds(5));
		    setw_PieChart();
			
		} catch (SQLException e) {
			conn.rollback();
			e.printStackTrace();
		    new TrayNotification("Desposit Failed", "", NotificationType.ERROR).showAndDismiss(Duration.seconds(5));

		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
		    new TrayNotification("Desposit Failed", "", NotificationType.ERROR).showAndDismiss(Duration.seconds(5));

		} finally {
			conn.close();
		}
    }
	
	@FXML
    void onTransfer(ActionEvent event) throws SQLException {
		
		Connection conn = null;
		
		try {
			float amnt = Float.parseFloat(t_amnt.getText());
			int accno = Integer.parseInt(t_accno.getText());
			int recepient_num = Integer.parseInt(recepient_accno.getText());
			conn = DAO.getDBConnection();
			conn.setAutoCommit(false);

			ResultSet rs = conn.createStatement().executeQuery("select total_amount from account where ano = " + accno);
			if(!rs.next()) throw new Exception();
			float total_amount = rs.getInt(1);
			if(total_amount < amnt) throw new Exception();
			total_amount -= amnt;
			conn.createStatement().execute("update account set total_amount = " + total_amount + " where ano = " + accno);
			rs = conn.createStatement().executeQuery("select total_amount from account where ano = " + recepient_num);
			if(!rs.next()) throw new Exception();
			float r_total_amount = rs.getInt(1);
			r_total_amount += amnt;
			conn.createStatement().execute("update account set total_amount = " + r_total_amount + " where ano = " + recepient_num);

			PreparedStatement statement = conn.prepareStatement("Insert Into transactions values (?,?,?,?,now(),null)",Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, cno);
			statement.setInt(2, accno);
			statement.setFloat(3, amnt);
			statement.setString(4, "Transfer");
			statement.executeUpdate();
			
			ResultSet generatedKeys = statement.getGeneratedKeys();
			generatedKeys.next();
			int id = generatedKeys.getInt(1);
			
			statement = conn.prepareStatement("Insert Into transfer values (?,?)");
			statement.setInt(1, recepient_num);
			statement.setInt(2, id);
			statement.executeUpdate();

			
			conn.commit();
		    new TrayNotification("Transfer was Successful", "", NotificationType.SUCCESS).showAndDismiss(Duration.seconds(5));
		    setPieChart();
			
			
		} catch (SQLException e) {
			conn.rollback();
			e.printStackTrace();
		    new TrayNotification("Transfer Failed", "", NotificationType.ERROR).showAndDismiss(Duration.seconds(5));

		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
		    new TrayNotification("Transfer Failed", "", NotificationType.ERROR).showAndDismiss(Duration.seconds(5));

		} finally {
			conn.close();
		}
		
    }
	
	

}
