package main.ui.wizard;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.faces.bean.SessionScoped;
import javax.inject.Named;

import main.domain.wizard.DecisionGlossary;
import main.service.db.DbService;
import main.service.db.DecisionGlossaryService;


/**
 * Controller for Table DecisionGlossary.
 * @author Christoph Gerdon
 * Contains methods for creating, updating and reading Object of the type DecisionGlossaryController.
 */
@Named("decisionglossary")
@SessionScoped
public class DecisionGlossaryController  {
	private int decisionGlossaryID;
	private String decisionname;
	private Timestamp created;
	private Timestamp updated;
	private DecisionGlossaryService decision;

	/**
	 * Constructor.
	 * @throws IOException
	 */
	public DecisionGlossaryController() throws IOException {
		this.decision = new DecisionGlossaryService();
	}

	/**
	 * Calles the DbService to create a new decision.
	 * @return String
	 */
	public String createNewDecisionGlossary() {
		DecisionGlossary newdecision = new DecisionGlossary(this.decisionname);
		DbService db = new DbService();
		return db.createNewSth(newdecision);
	}
	
	/**
	 * updates a decision by id
	 * @return boolean
	 */
	public boolean updateDecisionGlossary() {
		return this.decision.updateDecisionGlossary(this.decisionGlossaryID, this.decisionname);
	}

	public List<DecisionGlossary> getAllDecisionGlossarys() {
		return this.decision.getAllDecisionGlossarys();
	}
	
	public DecisionGlossary getDecisionGlossaryById() {
		return this.decision.getDecisionGlossaryById(this.decisionGlossaryID);
	}

	/**
	 * deletes a decision by id
	 * @return boolean
	 */
	public boolean deleteDecisionGlossaryById() {
		return this.decision.deleteDecisionGlossaryById(this.decisionGlossaryID);
	}


	public int getDecisionGlossaryID() {
		return decisionGlossaryID;
	}


	public void setDecisionGlossaryID(int decisionGlossaryID) {
		this.decisionGlossaryID = decisionGlossaryID;
	}


	public String getDecisionname() {
		return decisionname;
	}


	public void setDecisionname(String decisionname) {
		this.decisionname = decisionname;
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
