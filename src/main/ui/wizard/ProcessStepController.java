package main.ui.wizard;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.faces.bean.SessionScoped;
import javax.inject.Named;

import main.domain.wizard.ProcessStep;
import main.service.db.DbService;
import main.service.db.ProcessstepService;


/**
 * Controller for Table ProcessStep.
 * @author Christoph Gerdon
 *Contains methods for creating, updating and reading Object of the type ProcessStep.
 */
@Named("processstep")
@SessionScoped
public class ProcessStepController  {
	private int processStepId;
	private String name;
	private int userId;
	private int actionGlossaryId;
	private int decisionGlossaryId;
	private int reasonGlossaryId;
	private int rolePlayId;
	private int roundNr;
	private Timestamp created;
	private Timestamp updated;
	private DbService db;
	private ProcessstepService processStep;

	/**
	 * Constructor.
	 * @throws IOException
	 */
	public ProcessStepController() throws IOException {
		this.db = new DbService();
		this.processStep = new ProcessstepService();
	}


	public DbService getDb() {
		return this.db;
	}
	public void setDb(DbService db) {
		this.db = db;
	}

	
	
	public int getProcessStepId() {
		return processStepId;
	}


	public void setProcessStepId(int processStepId) {
		this.processStepId = processStepId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public int getActionGlossaryId() {
		return actionGlossaryId;
	}


	public void setActionGlossaryId(int actionGlossaryId) {
		this.actionGlossaryId = actionGlossaryId;
	}


	public int getDecisionGlossaryId() {
		return decisionGlossaryId;
	}


	public void setDecisionGlossaryId(int decisionGlossaryId) {
		this.decisionGlossaryId = decisionGlossaryId;
	}


	public int getReasonGlossaryId() {
		return reasonGlossaryId;
	}


	public void setReasonGlossaryId(int reasonGlossaryId) {
		this.reasonGlossaryId = reasonGlossaryId;
	}


	public int getRolePlayId() {
		return rolePlayId;
	}


	public void setRolePlayId(int rolePlayId) {
		this.rolePlayId = rolePlayId;
	}


	public int getRoundNr() {
		return roundNr;
	}


	public void setRoundNr(int roundNr) {
		this.roundNr = roundNr;
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

	/**
	 * Creates a new processstep.
	 * @return String
	 */
	public String createNewProcessStep() {	
		return db.createNewSth(processStep);
	}

	public List<ProcessStep> getAllProcessStep() {
		return this.processStep.getAllProcessStep();
	}

	public ProcessStep getProcessStepById() {
		return this.processStep.getProcessStepById(this.processStepId);
	}

	/**
	 * deletes a processstep by id.
	 * @return boolean
	 */
	public boolean deleteProcessStepById() {
		return this.processStep.deleteProcessStepById(this.processStepId);
	}
}
