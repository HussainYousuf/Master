package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class AddCustomerController {

	ArrayList<HBox> hboxs = new ArrayList<HBox>();
	ArrayList<TextField> tfs = new ArrayList<TextField>();
	ArrayList<ComboBox> cboxs = new ArrayList<ComboBox>();
	ArrayList<ToggleGroup> tgs = new ArrayList<ToggleGroup>();
	ObservableList<String> row = FXCollections.observableArrayList();

	@FXML
	private TextField passwd;

	@FXML
	private TextField fname;

	@FXML
	private TextField lname;

	@FXML
	private TextField email;

	@FXML
	private TextField phone;

	@FXML
	private TextField CNIC;

	@FXML
	private TextField country;

	@FXML
	private TextField address;

	@FXML
	private ToggleGroup sexGrp;

	@FXML
	private DatePicker dob;

	@FXML
	private ToggleGroup statusGrp;

	@FXML
	private TextField zip;

	@FXML
	private ToggleGroup accGrp;

	@FXML
	private ToggleGroup maritalGrp;

	@FXML
	private ScrollPane scrollPane;

	@FXML
	private Label label;

	@FXML
	Button submit;
	
	

	@FXML
	void initialize() {
		dob.setValue(Utility.LOCAL_DATE("null"));
		try {
			Connection conn = DAO.getDBConnection();
			ResultSet rs = conn.createStatement().executeQuery("Select bname from branch");
			while (rs.next()) {
				row.add(rs.getString(1));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Utility.setUpValidation(fname,false,15);
		Utility.setUpValidation(lname,false,15);
		Utility.setUpValidation(email,false,30);
		Utility.setUpValidation(phone,true,11);
		Utility.setUpValidation(zip,true,5);
		Utility.setUpValidation(CNIC,true,13);
		Utility.setUpValidation(country,false,15);
		Utility.setUpValidation(address,false,50);
		Utility.setUpValidation(passwd,false,8);
	}
	

	@FXML
	void addCustomer(ActionEvent event) {

		
		ArrayList<ObservableList<String>> styleList = new ArrayList<>();
		styleList.add(fname.getStyleClass());
		styleList.add(lname.getStyleClass());
		styleList.add(email.getStyleClass());
		styleList.add(phone.getStyleClass());
		styleList.add(zip.getStyleClass());
		styleList.add(CNIC.getStyleClass());
		styleList.add(country.getStyleClass());
		styleList.add(address.getStyleClass());
		styleList.add(passwd.getStyleClass());
		
		
		
		for(ObservableList<String> list : styleList) {
			if(list.contains("error")) {
				new TrayNotification("Error", "Please provide correct information", NotificationType.ERROR)
				.showAndDismiss(Duration.seconds(5));
				return;
			}
		}
		
		
		try {
			
			Connection conn = DAO.getDBConnection();
			PreparedStatement statement = conn.prepareStatement(
					"insert into customer_copy values (?,?,?,?,?,?,?,?,?,?,?,?,now(),null,?)",
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, fname.getText());
			statement.setString(2, lname.getText());
			java.util.Date date = java.util.Date.from(dob.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			statement.setDate(3, sqlDate);
			statement.setString(4, email.getText());
			statement.setString(5, phone.getText());
			statement.setString(6, zip.getText());
			statement.setString(7, CNIC.getText());
			statement.setString(8, country.getText());
			statement.setString(9, address.getText());
			statement.setString(10, ((RadioButton) sexGrp.getSelectedToggle()).getText());
			statement.setString(11, ((RadioButton) statusGrp.getSelectedToggle()).getText());
			statement.setString(12, ((RadioButton) maritalGrp.getSelectedToggle()).getText());
			statement.setString(13, passwd.getText());
			statement.executeUpdate();
			ResultSet generatedKeys = statement.getGeneratedKeys();
			generatedKeys.next();
			int custNo = generatedKeys.getInt(1);
			statement.close();
			for (int i = 0; i < hboxs.size(); i++) {
				try {
					int branchNo;
					float amnt;
					ResultSet rs = conn.createStatement().executeQuery("select bno from branch where bname like '"
							+ cboxs.get(i).getSelectionModel().getSelectedItem().toString() + "'");
					rs.next();
					branchNo = rs.getInt(1);
					amnt = Float.parseFloat(tfs.get(i).getText());
					statement = conn.prepareStatement("insert into account values (?,?,?,?,now(),null)");
					statement.setInt(1, branchNo);
					statement.setString(2, ((RadioButton) tgs.get(i).getSelectedToggle()).getText());
					statement.setFloat(3, amnt);
					statement.setInt(4, custNo);
					statement.execute();
					statement.close();
				} catch (Exception e) {
					new TrayNotification("Account not created", "", NotificationType.ERROR)
							.showAndDismiss(Duration.seconds(5));
					break;
				}
			}
			conn.close();
			label.setText(label.getText() + custNo);
			label.setVisible(true);
			submit.setDisable(true);
			new TrayNotification("Success", "Customer Created", NotificationType.SUCCESS)
					.showAndDismiss(Duration.seconds(5));

		} catch (SQLException e) {
			new TrayNotification("Customer Not created", "Please provide proper credentials", NotificationType.ERROR)
					.showAndDismiss(Duration.seconds(5));
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
	void addAccount(ActionEvent event) {

		VBox content = new VBox();
		content.spacingProperty().set(10);
		TextField initAmnt = new TextField();
		initAmnt.setPrefSize(fname.getWidth(), fname.getHeight());
		initAmnt.setPromptText("Enter initial amount");
		tfs.add(initAmnt);
		ComboBox<String> box = new ComboBox<String>(row);
		box.setPrefSize(fname.getWidth(), fname.getHeight());
		box.setPromptText("select branch");
		cboxs.add(box);
		RadioButton saving = new RadioButton("Saving");
		saving.setSelected(true);
		RadioButton current = new RadioButton("Current");
		ToggleGroup grp = new ToggleGroup();
		saving.setToggleGroup(grp);
		current.setToggleGroup(grp);
		tgs.add(grp);
		HBox hbox = new HBox();
		hbox.spacingProperty().set(50);
		hbox.getChildren().addAll(initAmnt, box, saving, current);
		hboxs.add(hbox);
		for (HBox h : hboxs)
			content.getChildren().add(h);
		scrollPane.setContent(content);

	}

	@FXML
	void clear(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("AddCustomer.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage.setScene(new Scene(root));
		stage.show();
	}

}
