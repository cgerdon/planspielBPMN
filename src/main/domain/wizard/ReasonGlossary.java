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
@Table(name = "reasonglossary")
public class ReasonGlossary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7295546277409217393L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bpmn.reasonGlossary_id_seq")
	@SequenceGenerator(name = "bpmn.reasonGlossary_id_seq", sequenceName = "bpmn.reasonGlossary_id_seq",
			allocationSize = 1)
	@Column(name = "reasonglossaryid", unique = true, nullable = false, updatable = false)
	private Integer reasonGlossaryID;
	@Column(name = "name")
	private String name;
	@Column(name = "created")
	private Date created;
	@Column(name = "updated")
	private Date updated;
	
	public ReasonGlossary() {
	}

	public ReasonGlossary(String name) {
		super();
		this.name = name;
		this.created = new Date();
		this.updated = new Date();
	}

	public Integer getReasonGlossaryID() {
		return reasonGlossaryID;
	}

	public void setReasonGlossaryID(Integer reasonGlossaryID) {
		this.reasonGlossaryID = reasonGlossaryID;
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
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime
				* result
				+ ((reasonGlossaryID == null) ? 0 : reasonGlossaryID.hashCode());
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
		if (getClass() != obj.getClass()){
			return false;
		}
		ReasonGlossary other = (ReasonGlossary) obj;
		if (created == null) {
			if (other.created != null) {
				return false;
			}
		} else if (!created.equals(other.created)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (reasonGlossaryID == null) {
			if (other.reasonGlossaryID != null) {
				return false;
			}
		} else if (!reasonGlossaryID.equals(other.reasonGlossaryID)) {
			return false;
		}
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
		return "ReasonGlossary [reasonGlossaryID=" + reasonGlossaryID
				+ ", name=" + name + ", created="
				+ created + ", updated=" + updated + "]";
	}
}
