package main.ui.wizard;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.faces.bean.SessionScoped;
import javax.inject.Named;

import main.domain.wizard.ActionGlossary;
import main.service.db.ActionGlossaryService;
import main.service.db.DbService;


/**
 * Controller for Table ActionGlossary.
 * @author Christoph Gerdon
 * Contains methods for creating, updating and reading Object of the type ActionGlossaryController.
 */
@Named("actionglossary")
@SessionScoped
public class ActionGlossaryController  {
	private int actionGlossaryID;
	private String actionname;
	private Timestamp created;
	private Timestamp updated;
	private ActionGlossaryService action;

	/**
	 * Constructor.
	 * @throws IOException
	 */
	public ActionGlossaryController() throws IOException {
		this.action = new ActionGlossaryService();
	}

	/**
	 * Calles the DbService to create a new action.
	 * @return String
	 */
	public String createNewActionGlossary() {
		ActionGlossary newaction = new ActionGlossary(this.actionname);
		DbService db = new DbService();
		return db.createNewSth(newaction);
	}
	
	/**
	 * updates an action by the id.
	 * @return
	 */
	public boolean updateActionGlossary() {
		return this.action.updateActionGlossary(this.actionGlossaryID, this.actionname);
	}

	public List<ActionGlossary> getAllActionGlossarys() {
		return this.action.getAllActionGlossarys();
	}
	
	public ActionGlossary getActionGlossaryById() {
		return this.action.getActionGlossaryById(actionGlossaryID);
	}

	/**
	 * deletes an action by the id
	 * @return
	 */
	public boolean deleteActionGlossaryById() {
		System.out.println(this.actionGlossaryID);
		return this.action.deleteActionGlossaryById(this.actionGlossaryID);
	}


	public int getActionGlossaryID() {
		return actionGlossaryID;
	}


	public void setActionGlossaryID(int actionGlossaryID) {
		this.actionGlossaryID = actionGlossaryID;
	}


	public String getActionname() {
		return actionname;
	}


	public void setActionname(String actionname) {
		this.actionname = actionname;
	}


	public Timestamp getCreated() {
		return created;
	}


	public void setCreated(Timestamp created) {
		this.created = created;
	}


	public Timestamp getUpdated() {
		return updated;
	}


	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

}
