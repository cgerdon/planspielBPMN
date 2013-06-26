package main.ui.wizard;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.faces.bean.SessionScoped;
import javax.inject.Named;

import main.domain.wizard.ReasonGlossary;
import main.service.db.DbService;
import main.service.db.ReasonGlossaryService;


/**
 * Controller for Table ReasonGlossary.
 * @author Christoph Gerdon
 * Contains methods for creating, updating and reading Object of the type ReasonGlossaryController.
 */
@Named("reasonglossary")
@SessionScoped
public class ReasonGlossaryController  {
	private int reasonGlossaryID;
	private String reasonname;
	private Timestamp created;
	private Timestamp updated;
	private ReasonGlossaryService reason;

	/**
	 * Constructor.
	 * @throws IOException
	 */
	public ReasonGlossaryController() throws IOException {
		this.reason = new ReasonGlossaryService();
	}

	/**
	 * Calles the DbService to create a new reason.
	 * @return String
	 */
	public String createNewReasonGlossary() {
		ReasonGlossary newreason = new ReasonGlossary(this.reasonname);
		DbService db = new DbService();
		return db.createNewSth(newreason);
	}
	/**
	 * updates a reason by id.
	 * @return boolean
	 */
	public boolean updateReasonGlossary() {
		return this.reason.updateReasonGlossary(this.reasonGlossaryID, this.reasonname);
	}

	public List<ReasonGlossary> getAllReasonGlossarys() {
		return this.reason.getAllReasonGlossarys();
	}
	
	public ReasonGlossary getReasonGlossaryById() {
		return this.reason.getReasonGlossaryById(reasonGlossaryID);
	}
	/**
	 * Deletes a reason by id
	 * @return
	 */
	public boolean deleteReasonGlossaryById() {
		return this.reason.deleteReasonGlossaryById(reasonGlossaryID);
	}


	public int getReasonGlossaryID() {
		return reasonGlossaryID;
	}


	public void setReasonGlossaryID(int reasonGlossaryID) {
		this.reasonGlossaryID = reasonGlossaryID;
	}


	public String getReasonname() {
		return reasonname;
	}


	public void setReasonname(String reasonname) {
		this.reasonname = reasonname;
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
