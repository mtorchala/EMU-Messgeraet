package gui;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import business.BasisModel;
import business.Messreihe;
import business.Timer;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class BasisControl implements Initializable{
	private BasisModel basisModel;
	
	@FXML
	private TextField txtID;
	
	@FXML
	private TextField txtZeitintervall;
	
	@FXML
	private TextField txtVerbraucher;
	
	@FXML
	private TextField txtMessgroesse;
	
	@FXML
	private Button bttnMessreiheSpeichern;
	
	@FXML
	private Button bttnMessreiheStarten;
	
	@FXML
	private Button bttnMessreiheStoppen;
	
	@FXML
	private Button bttnMessreiheLesen;
	
	@FXML
	private TableView tableviewAnzeige;
	
	Timer timer;
	
	

	
	public BasisControl() throws Exception{
		
		basisModel = new BasisModel();
		
	
	}
	
	public String leseDatenAusDB(int messreihenId){
		try {
			return basisModel.leseDatenAusDB(messreihenId);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		}
	}
	
	public double fuehreMessungDurch(int messreihenId, int laufendeNummer) throws Exception {
		try {
			return basisModel.fuehreMessungDurch(messreihenId, laufendeNummer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0.0;
	}
	
	@FXML
	private void messreihenLesen(){
		try {
			ObservableList<Messreihe> ausgeleseneMessreihen = basisModel.leseMessreihenAusDB();
			
			tableviewAnzeige.setItems(ausgeleseneMessreihen);
			
			TableColumn colID = new TableColumn("ID");
			colID.setMinWidth(50);
			colID.setCellValueFactory(new PropertyValueFactory<Messreihe, Integer>("messreihenId"));
	        
			TableColumn colZeitintervall = new TableColumn("Zeitintervall");
	        colZeitintervall.setMinWidth(170);        
	        colZeitintervall.setCellValueFactory(new PropertyValueFactory<Messreihe, Integer>("zeitintervall"));
	        
	        TableColumn colVerbraucher = new TableColumn("Verbraucher");
	        colVerbraucher.setMinWidth(180);        
	        colVerbraucher.setCellValueFactory(new PropertyValueFactory<Messreihe, String>("verbraucher"));    
	        
	        TableColumn colMessgroesse = new TableColumn("Messgroesse");
	        colMessgroesse.setMinWidth(181);
	        colMessgroesse.setCellValueFactory(new PropertyValueFactory<Messreihe, String>("messgroesse"));
	        
	        tableviewAnzeige.getColumns().clear();
	        tableviewAnzeige.getColumns().addAll(colID,colZeitintervall,colVerbraucher,colMessgroesse);
	       
	        tableviewAnzeige.setItems(ausgeleseneMessreihen);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		bttnMessreiheStarten.setDisable(false);
		bttnMessreiheStoppen.setDisable(false);
	}
	
	@FXML
	private void messreiheStarten(){
		//int messreihenId = Integer.parseInt(txtID.getText());
		//txfAusgeleseneWerte.setText(basisControl.leseDatenAusDB(messreihenId));
		Messreihe messreihe = (Messreihe) tableviewAnzeige.getSelectionModel().getSelectedItem();
		
		timer = new Timer(basisModel,messreihe.getMessreihenId(),messreihe.getZeitintervall());
		timer.start();
		
	}
	
	@FXML
	private void messreiheStoppen(){
		//int messreihenId = Integer.parseInt(txtID.getText());
		//txfAusgeleseneWerte.setText(basisControl.leseDatenAusDB(messreihenId));
		timer.setStopp(true);
	}
	
	@FXML
	private void messreiheSpeichern(){
		int messreiheId = Integer.parseInt(txtID.getText());
		int zeitintervall = Integer.parseInt(txtID.getText());
		String verbraucher = txtVerbraucher.getText();
		String messgroesse = txtMessgroesse.getText();
		Messreihe mr = new Messreihe(messreiheId,zeitintervall,verbraucher,messgroesse);
		try {
			basisModel.speichereMessreihe(mr);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		bttnMessreiheStarten.setDisable(true);
		bttnMessreiheStoppen.setDisable(true);
		
		ContextMenu contextmenu = new ContextMenu();
		MenuItem mi = new MenuItem("Darstellung im Diagramm");
		contextmenu.getItems().add(mi);
		

		tableviewAnzeige.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

		    @Override
		    public void handle(MouseEvent t) {
		        if(t.getButton() == MouseButton.SECONDARY) {
		        	contextmenu.show(tableviewAnzeige, t.getScreenX(), t.getScreenY());
		        }
		    }
		});
	}
	
	
}
