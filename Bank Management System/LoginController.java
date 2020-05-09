package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;

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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class LoginController {

	@FXML
	private TextField numField;
	@FXML
	TextField databaseName;
	@FXML
	TextField mysql_user;
	@FXML
	TextField mysql_pass;

	@FXML
	private Label label;

	@FXML
	private ComboBox<String> combo;

	@FXML
	private PasswordField passwd;

	@FXML
	private Button lgnBtn;

	@FXML
	private Button createBtn;

	
	public static String DB_USER;
	public static String DB_NAME;
	public static String DB_PASS;

	@FXML
	public void initialize() {
		ObservableList<String> data = FXCollections.observableArrayList("Admin", "Customer");
		combo.setItems(data);
		combo.getSelectionModel().select(1);
		mysql_user.setText("root");
		databaseName.setText("demo");
	}

	@FXML
	public void onLogin(ActionEvent event) throws IOException {
		try {
			int num = Integer.parseInt(numField.getText());
			String pass = passwd.getText();
			String type = combo.getSelectionModel().getSelectedItem().toString();
			Connection conn = DAO.getDBConnection();
			ResultSet rs;
			if (type.equals("Customer")) {
				rs = conn.createStatement().executeQuery(
						"Select fname from customer where cno = " + num + " and passwd like " + "'" + pass + "'");
				if (!rs.next())
					throw new Exception();
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				stage.close();
				Parent root = null;
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("Customer.fxml"));
					root = loader.load();
					CustomerController customerController = loader.getController();
					customerController.setCno(num);
				} catch (IOException e) {
					e.printStackTrace();
				}
				stage.setScene(new Scene(root));
				stage.show();

			} else {

				if(num == 0 && pass.equals("admin"))  viewAdmin(event);
				
				
			}

		} catch (Exception e) {
			e.printStackTrace();
			label.setText("Invalid Login...");
			label.setVisible(true);	
		    new TrayNotification("Invalid Login", "Please provide correct credentials", NotificationType.ERROR).showAndDismiss(Duration.seconds(5));
		}

	}

	@FXML
	void onConnection(ActionEvent event) {
		DB_USER = mysql_user.getText().trim();
		DB_NAME = databaseName.getText().trim();
		DB_PASS = mysql_pass.getText().trim();
		lgnBtn.setDisable(false);
		combo.setDisable(false);
		createBtn.setDisable(false);
		numField.setDisable(false);
		passwd.setDisable(false);
	}

	@FXML
	public void onCreateAcc(ActionEvent event) {

		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("AddCustomer.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("text-field-red-border.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}

	void viewAdmin(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("Admin.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stage.setScene(new Scene(root));
		stage.show();
	}

}
