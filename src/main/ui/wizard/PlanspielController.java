package main.ui.wizard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.SessionScoped;
import javax.inject.Named;

import main.domain.bpmn.Lane;
import main.service.bpmn.BpmnProcessingService;
import main.service.db.DbService;


/**
 * Controller for Table RolePlay.
 * @author Christoph Gerdon
 *Contains methods for creating, updating and reading Object of the type PlanspielController.
 */
@Named("planspiel")
@SessionScoped
public class PlanspielController  {
	private String leererString = "";
	private DbService db;
	@SuppressWarnings("unused")
	private BpmnProcessingService bpmnService;

	/**
	 * Constructor.
	 * @throws IOException if error with output-file
	 */
	public PlanspielController() throws IOException {
		this.db = new DbService();
		bpmnService = BpmnProcessingService.getInstance();
	}

	public DbService getDb() {
		return this.db;
	}
	public void setDb(DbService db) {
		this.db = db;
	}
	/**
	 * gets an empty String.
	 * @return String
	 */
	public String getleererString() {
		return this.leererString; 
	}
	/**
	 * Sets an empty String.
	 * @param leererString String to set
	 */
	public void setleererString(String leererString) {
		this.leererString = leererString;
	}

	public List<Lane> getAllFromDB() {
		return new ArrayList<Lane>();
	}

}
