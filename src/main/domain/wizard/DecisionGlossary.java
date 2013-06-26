package main.domain.wizard;

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
 * @author pierre.bartholomae
 *
 */
@Entity
@Table(name = "decisionglossary")
public class DecisionGlossary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8804812039167471918L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bpmn.decisionGlossary_id_seq")
	@SequenceGenerator(name = "bpmn.decisionGlossary_id_seq", sequenceName = "bpmn.decisionGlossary_id_seq",
			allocationSize = 1)
	@Column(name = "decisionglossaryid", unique = true, nullable = false, updatable = false)
	private Integer decisionGlossaryID;
	@Column(name = "name")
	private String name;
	@Column(name = "created")
	private Date created;
	@Column(name = "updated")
	private Date updated;
	
	public DecisionGlossary() {
	}

	public DecisionGlossary(String name) {
		super();
		this.name = name;
		this.created = new Date();
		this.updated = new Date();
	}

	public Integer getDecisionGlossaryID() {
		return decisionGlossaryID;
	}

	public void setDecisionGlossaryID(Integer decisionGlossaryID) {
		this.decisionGlossaryID = decisionGlossaryID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime
				* result
				+ ((decisionGlossaryID == null) ? 0 : decisionGlossaryID
						.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((updated == null) ? 0 : updated.hashCode());
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
		DecisionGlossary other = (DecisionGlossary) obj;
		if (created == null) {
			if (other.created != null) {
				return false;
			}
		} else if (!created.equals(other.created))
			return false;
		if (decisionGlossaryID == null) {
			if (other.decisionGlossaryID != null) {
				return false;
			}
		} else if (!decisionGlossaryID.equals(other.decisionGlossaryID))
			return false;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name))
			return false;
		if (updated == null) {
			if (other.updated != null) {
				return false;
			}
		} else if (!updated.equals(other.updated)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "DecisionGlossary [decisionGlossaryID=" + decisionGlossaryID
				+ ", name=" + name + ", created="
				+ created + ", updated=" + updated + "]";
	}
	

}
