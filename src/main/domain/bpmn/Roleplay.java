package main.domain.bpmn;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
/**
 * 
 * @author Christoph Gerdon
 *
 */
@Entity
@Table(name = "roleplay")
public class Roleplay implements Serializable {

	public Roleplay(String rolePlayName) {
		super();
		this.rolePlayName = rolePlayName;
		this.rolePlayStatus = "Aktiv";
		this.rolePlayCurrentRoundNo = 1;
		this.rolePlayUpdated = new Date();
		this.rolePlayCreated = new Date();
	}


	/**
	 *
	 */
	private static final long serialVersionUID = 8902806983161063026L;

	/**
	 *
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bpmn.roleplay_id_seq")
	@SequenceGenerator(name = "bpmn.roleplay_id_seq", sequenceName = "bpmn.roleplay_id_seq",
			allocationSize = 1)
	@Column(name = "roleplayid", unique = true, nullable = false, updatable = false)
	private int rolePlayID;
	@Column(name = "name")
	private String rolePlayName;
	@Column(name = "status")
	private String rolePlayStatus;
	@Column(name = "currentroundno")
	private int rolePlayCurrentRoundNo;
	@Column(name = "updated")
	private Date rolePlayUpdated;
	@Column(name = "created")
	private Date rolePlayCreated;

	public Roleplay() {

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

	public void setRolePlayCurrentRoundNo(int currentRoundNo) {
		this.rolePlayCurrentRoundNo = currentRoundNo;
	}

	public Date getRolePlayUpdated() {
		return rolePlayUpdated;
	}

	public void setRolePlayUpdated(Date rolePlayUpdated) {
		this.rolePlayUpdated = rolePlayUpdated;
	}

	public Date getRolePlayCreated() {
		return rolePlayCreated;
	}

	public void setRolePlayCreated(Date rolePlayCreated) {
		this.rolePlayCreated = rolePlayCreated;
	}

	@Override
	public String toString() {
		return "Roleplay [rolePlayID=" + rolePlayID + ", rolePlayName="
				+ rolePlayName + ", rolePlayStatus=" + rolePlayStatus
				+ ", currentRoundNo=" + rolePlayCurrentRoundNo + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + rolePlayCurrentRoundNo;
		result = prime * result
				+ ((rolePlayCreated == null) ? 0 : rolePlayCreated.hashCode());
		result = prime * result + rolePlayID;
		result = prime * result
				+ ((rolePlayName == null) ? 0 : rolePlayName.hashCode());
		result = prime * result
				+ ((rolePlayStatus == null) ? 0 : rolePlayStatus.hashCode());
		result = prime * result
				+ ((rolePlayUpdated == null) ? 0 : rolePlayUpdated.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Roleplay other = (Roleplay) obj;
		if (rolePlayCurrentRoundNo != other.rolePlayCurrentRoundNo) {
			return false;
		}
		if (rolePlayCreated == null) {
			if (other.rolePlayCreated != null) {
				return false;
			}
		} else if (!rolePlayCreated.equals(other.rolePlayCreated)) {
			return false;
		}
		if (rolePlayID != other.rolePlayID) {
			return false;
		}
		if (rolePlayName == null) {
			if (other.rolePlayName != null) {
				return false;
			}
		} else if (!rolePlayName.equals(other.rolePlayName)) {
			return false;
		}
		if (rolePlayStatus == null) {
			if (other.rolePlayStatus != null) {
				return false;
			}
		} else if (!rolePlayStatus.equals(other.rolePlayStatus)) {
			return false;
		}
		if (rolePlayUpdated == null) {
			if (other.rolePlayUpdated != null) {
				return false;
			}
		} else if (!rolePlayUpdated.equals(other.rolePlayUpdated)) {
			return false;
		}
		return true;
	}

	
	
}
