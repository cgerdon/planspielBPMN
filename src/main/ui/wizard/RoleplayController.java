package main.ui.wizard;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.faces.bean.SessionScoped;
import javax.inject.Named;

import main.domain.bpmn.Roleplay;
import main.service.db.DbService;
import main.service.db.RoleplayService;


/**
 * Controller for Table RolePlay.
 * @author Christoph Gerdon
 * Contains methods for creating, updating and reading Object of the type RoleplayController.
 */
@Named("roleplay")
@SessionScoped
public class RoleplayController  {
	private int rolePlayID;
	private String rolePlayName = "";
	private String rolePlayStatus = "";
	private int rolePlayCurrentRoundNo = 0;
	private Timestamp rolePlayCreated;
	private Timestamp rolePlayUpdated;
	private DbService db;
	private RoleplayService roleplay;

	/**
	 * Constructor.
	 * @throws IOException
	 */
	public RoleplayController() throws IOException {
		this.db = new DbService();
		this.roleplay = new RoleplayService();
	}

	/**
	 * Calles the DbService to create a new Roleplay.
	 * @return String
	 */
	public String createNewRoleplay() {
		
		Roleplay newroleplay = new Roleplay(this.rolePlayName);
		return db.createNewSth(newroleplay);
	}

	public DbService getDb() {
		return this.db;
	}
	public void setDb(DbService db) {
		this.db = db;
	}

	public List<Roleplay> getAllRoleplays() {
		return this.roleplay.getAllRoleplays();
	}
	
	public Roleplay getRoleplayById() {
		return this.roleplay.getRoleplayById(this.rolePlayID);
	}
	/**
	 * Deletes a roleplay by id.
	 * @return boolean
	 */
	public boolean deleteRoleplayById() {
		return this.roleplay.deleteRoleplayById(this.rolePlayID);
	}
	/**
	 * updates a roleplay by id.
	 * @return boolean
	 */
	public boolean updateRoleplay() {
		return this.roleplay.updateRoleplay(this.rolePlayID, this.rolePlayName, this.rolePlayStatus, this.rolePlayCurrentRoundNo);
	}
	
	public int getRolePlayID() {
		return rolePlayID;
	}


	public void setRolePlayID(int rolePlayID) {
		this.rolePlayID = rolePlayID;
	}


	public String getRolePlayName() {
		return rolePlayName;
	}


	public void setRolePlayName(String rolePlayName) {
		this.rolePlayName = rolePlayName;
	}


	public String getRolePlayStatus() {
		return rolePlayStatus;
	}


	public void setRolePlayStatus(String rolePlayStatus) {
		this.rolePlayStatus = rolePlayStatus;
	}


	public int getRolePlayCurrentRoundNo() {
		return rolePlayCurrentRoundNo;
	}


	public void setRolePlayCurrentRoundNo(int rolePlayCurrentRoundNo) {
		this.rolePlayCurrentRoundNo = rolePlayCurrentRoundNo;
	}


	public Timestamp getRolePlayCreated() {
		return rolePlayCreated;
	}


	public void setRolePlayCreated(Timestamp rolePlayCreated) {
		this.rolePlayCreated = rolePlayCreated;
	}


	public Timestamp getRolePlayUpdated() {
		return rolePlayUpdated;
	}


	public void setRolePlayUpdated(Timestamp rolePlayUpdated) {
		this.rolePlayUpdated = rolePlayUpdated;
	}

}
