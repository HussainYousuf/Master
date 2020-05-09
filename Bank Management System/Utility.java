package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

import com.sun.prism.impl.Disposer.Record;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class Utility { 

	public static final LocalDate LOCAL_DATE(String dateString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate;
		try {
			localDate = LocalDate.parse(dateString, formatter);
		} catch (Exception e) {
			return LocalDate.parse("1999-12-12", formatter);
		}
		return localDate;
	}

	public static void setUpValidation(final TextField tf, boolean isNumber, int len) {
		tf.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				validate(tf);
				if (isNumber) {
					ObservableList<String> styleClass = tf.getStyleClass();
					if (!newValue.matches("\\d*")) {
						tf.setText(newValue.replaceAll("[^\\d]", ""));
					}
					if (newValue.length() < len) {
						if (!styleClass.contains("error")) {
							styleClass.add("error");
						}
					} else {
						styleClass.removeAll(Collections.singleton("error"));
					}
				}
				if (newValue.length() > len)
					tf.setText(newValue.substring(0, len));

			}

		});

		validate(tf);
	}

	private static void validate(TextField tf) {
		ObservableList<String> styleClass = tf.getStyleClass();
		if (tf.getText().trim().length() == 0) {
			if (!styleClass.contains("error")) {
				styleClass.add("error");
			}
		} else {
			styleClass.removeAll(Collections.singleton("error"));
		}
	}

	public static void DISPLAY_TABLE2(TableView tableView, String query) {
		tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		tableView.getColumns().clear();
		tableView.getItems().clear();
		Connection c;
		ObservableList data = FXCollections.observableArrayList();
		try {
			c = DAO.getDBConnection();
			String SQL = query;
			ResultSet rs = c.createStatement().executeQuery(SQL);
			for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
				final int j = i;
				TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
				col.setCellValueFactory(
						new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
							public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
								return new SimpleStringProperty(param.getValue().get(j).toString());
							}
						});
				tableView.setFixedCellSize(50);
				tableView.getColumns().addAll(col);
			}

			while (rs.next()) {
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					if (rs.getString(i) == null)
						row.add("");
					else
						row.add(rs.getString(i));
				}
				data.add(row);

			}

			TableColumn col_action = new TableColumn<>("Action");
			tableView.getColumns().add(col_action);

			col_action.setCellValueFactory(
					new Callback<TableColumn.CellDataFeatures<Record, Boolean>, ObservableValue<Boolean>>() {

						@Override
						public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Record, Boolean> p) {
							return new SimpleBooleanProperty(p.getValue() != null);
						}
					});

			// Adding the Button to the cell
			col_action.setCellFactory(new Callback<TableColumn<Record, Boolean>, TableCell<Record, Boolean>>() {

				@Override
				public TableCell<Record, Boolean> call(TableColumn<Record, Boolean> p) {
					return new ButtonCell();
				}

			});

			
			TableColumn col_action2 = new TableColumn<>("Action");
			tableView.getColumns().add(col_action2);

			col_action2.setCellValueFactory(
					new Callback<TableColumn.CellDataFeatures<Record, Boolean>, ObservableValue<Boolean>>() {

						@Override
						public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Record, Boolean> p) {
							return new SimpleBooleanProperty(p.getValue() != null);
						}
					});

			// Adding the Button to the cell
			col_action2.setCellFactory(new Callback<TableColumn<Record, Boolean>, TableCell<Record, Boolean>>() {

				@Override
				public TableCell<Record, Boolean> call(TableColumn<Record, Boolean> p) {
					return new ButtonCell2();
				}

			});
			
			
			
			
			tableView.setItems(data);
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error on Building Data");
		}

	}
	
	
	
	public static void DISPLAY_TABLE(TableView tableView, String query) {
		tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		tableView.getColumns().clear();
		tableView.getItems().clear();
		Connection c;
		ObservableList data = FXCollections.observableArrayList();
		try {
			c = DAO.getDBConnection();
			String SQL = query;
			ResultSet rs = c.createStatement().executeQuery(SQL);
			for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
				final int j = i;
				TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
				col.setCellValueFactory(
						new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
							public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
								return new SimpleStringProperty(param.getValue().get(j).toString());
							}
						});
				tableView.setFixedCellSize(50);
				tableView.getColumns().addAll(col);
			}

			while (rs.next()) {
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					if (rs.getString(i) == null)
						row.add("");
					else
						row.add(rs.getString(i));
				}
				data.add(row);

			}

			tableView.setItems(data);
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error on Building Data");
		}

	}

}

class ButtonCell extends TableCell<Record, Boolean> {
	final Button cellButton = new Button("Edit");
	static AdminController THIS;

	ButtonCell() {

		// Action when the button is pressed
		cellButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {
				ObservableList<String> items = (ObservableList<String>) ButtonCell.this.getTableView().getItems().get(ButtonCell.this.getIndex());
				AdminController.cno = Integer.parseInt(items.get(items.size()-2));
				THIS.callEdit();
			}
		});
	}

	// Display button if the row is not empty
	@Override
	protected void updateItem(Boolean t, boolean empty) {
		super.updateItem(t, empty);
		if (!empty) {
			setGraphic(cellButton);
		}
	}
}

class ButtonCell2 extends TableCell<Record, Boolean> {
	final Button cellButton = new Button("Delete");
	static AdminController THIS;

	ButtonCell2() {

		// Action when the button is pressed
		cellButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {
				// get Selected Item
				ObservableList<String> items = (ObservableList<String>) ButtonCell2.this.getTableView().getItems().get(ButtonCell2.this.getIndex());
				AdminController.cno = Integer.parseInt(items.get(items.size()-2));
				THIS.callDelete();

			}
		});
	}

	// Display button if the row is not empty
	@Override
	protected void updateItem(Boolean t, boolean empty) {
		super.updateItem(t, empty);
		if (!empty) {
			setGraphic(cellButton);
		}
	}
}
