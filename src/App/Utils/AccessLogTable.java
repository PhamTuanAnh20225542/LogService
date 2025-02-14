package App.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import Log.LogEntry.AccessLog;
import Log.Parser.ParseAccessLog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;


public class AccessLogTable{

	public static String dateTarget;
	public static String ipTarget;
	public static int statusTarget;
	public static int hourTarget;

    @SuppressWarnings("unchecked")
	public static void createTable(TableView<AccessLog> logTable){
		// Declare each column
		TableColumn<AccessLog, String> typeColumn = new TableColumn<>("Type");
		TableColumn<AccessLog, String> IpColumn = new TableColumn<>("IP");
		TableColumn<AccessLog, String> timeColumn = new TableColumn<>("Timestamp");
		TableColumn<AccessLog, String> requestColumn = new TableColumn<>("Request Message");
		TableColumn<AccessLog, Integer> statusColumn = new TableColumn<>("Status");
		TableColumn<AccessLog, Integer> bytesSentColumn = new TableColumn<>("Bytes");
		TableColumn<AccessLog, String> userColumn = new TableColumn<>("User Agent");

		// Set cell 
		typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
		IpColumn.setCellValueFactory(new PropertyValueFactory<>("IP"));
		timeColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
		userColumn.setCellValueFactory(new PropertyValueFactory<>("userAgent"));
		requestColumn.setCellValueFactory(new PropertyValueFactory<>("requestUrl"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusCode"));
		bytesSentColumn.setCellValueFactory(new PropertyValueFactory<>("bytesSent"));

		// Format each column
		IpColumn.setMinWidth(10);
		IpColumn.setMaxWidth(500);

		timeColumn.setMinWidth(10);
		timeColumn.setMaxWidth(500);

		requestColumn.setMinWidth(10);
		requestColumn.setMaxWidth(250);

		statusColumn.setMinWidth(10);
		statusColumn.setMaxWidth(50);

		bytesSentColumn.setMinWidth(10);
		bytesSentColumn.setMaxWidth(50);

		logTable.getColumns().addAll(typeColumn, IpColumn, timeColumn, requestColumn, statusColumn, bytesSentColumn, userColumn);
	}
	
	public static void  addData(TableView<AccessLog> logTable, String typeOfFilter){
		String filePath = Config.apacheLogPath;
		ObservableList<AccessLog> logs = FXCollections.observableArrayList();
        try {
			ParseAccessLog parser = new ParseAccessLog();
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
				if (parser.getDate(line).equals(dateTarget)) {
					if (typeOfFilter.equals("")) {
						logs.add(parser.parse(line)); 
					}
					else if ((typeOfFilter.equals("Hour")) && (parser.getHour(line) == hourTarget)) {
						logs.add(parser.parse(line)); 
					}
					else if ((typeOfFilter.equals("IP")) && (parser.getIp(line).equals(ipTarget))) {
						logs.add(parser.parse(line));  
					}
					else if ((typeOfFilter.equals("Status")) && (parser.getStatusCode(line) == statusTarget)) {
						logs.add(parser.parse(line)); 
					} 
				}         
            }
			logTable.setItems(logs);;
			bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
