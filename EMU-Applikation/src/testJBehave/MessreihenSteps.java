package testJBehave;

import java.io.IOException;
import java.sql.SQLException;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;
import org.testng.Assert;

import business.BasisModel;
import business.Messreihe;
import business.Messung;
import javafx.collections.ObservableList;

public class MessreihenSteps extends Steps {
	BasisModel bm;
	ObservableList<Messreihe> listeMessreihen;
	Messung[] listeMessungen;
	int id;
	
	@Given("eine Datenbank")
	public void setDatabase(){
		bm = new BasisModel();
	}
	
	@When("die Messreihen aus der Datenbank abgefragt werden")
	public void getMessreihen(){	
		try {
			listeMessreihen = bm.leseMessreihenAusDB();
		} catch (ClassNotFoundException | SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Then("sollten $anzMessreihen in das Programm geladen werden")
	public void checkMessreihenListe(int anzMessreihen){
	
		Assert.assertTrue(listeMessreihen.size() == anzMessreihen);
	}
	
	@Given("eine Messreihe mit der ID $id")
	public void setId(@Named("messreiheid")int id){
		this.bm = new BasisModel();
		this.id = id;

	}
	
	@When("die Messreihe an die Datenbankanbindung uebergeben wird")
	public void getMessungen(){	
		try {
			listeMessungen = bm.leseMessungenAusDB(id);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Then("werden $anzahl Messungen der Messreihe geladen")
	public void checkMessungen(@Named("anzahlMessungen")int anzahl){
	
		Assert.assertTrue(listeMessungen.length == anzahl);
	}
	
}
