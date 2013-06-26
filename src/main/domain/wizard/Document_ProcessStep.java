package main.domain.wizard;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import main.domain.bpmn.Document;

/**
 * 
 * @author pierre.bartholomae
 *
 */
@Entity
@Table(name = "document_processstep")
public class Document_ProcessStep implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9024384674449385759L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bpmn.document_ProcessStep_id_seq")
	@SequenceGenerator(name = "bpmn.document_ProcessStep_id_seq", sequenceName = "bpmn.document_ProcessStep_id_seq",
			allocationSize = 1)
	@Column(name = "document_processstepid", unique = true, nullable = false, updatable = false)
	private Integer document_ProcessStepID;
	
	@ManyToOne
	@JoinColumn(name = "documentid", referencedColumnName = "documentid", nullable = false, updatable = false, insertable = true)
	private Document document;
	
	@ManyToOne
	@JoinColumn(name = "processstepid", referencedColumnName = "processstepid", nullable = false, updatable = false, insertable = true)
	private ProcessStep processStep;
	
	@Column(name = "created")
	private Date created;
	
	@Column(name = "updated")
	private Date updated;
	
	/**
	 * constructs documentProcessStep
	 */
	public Document_ProcessStep() {
	}

	public Integer getDocument_ProcessStepID() {
		return document_ProcessStepID;
	}

	public void setDocument_ProcessStepID(Integer document_ProcessStepID) {
		this.document_ProcessStepID = document_ProcessStepID;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public ProcessStep getProcessStep() {
		return processStep;
	}

	public void setProcessStep(ProcessStep processStep) {
		this.processStep = processStep;
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
		result = prime * result
				+ ((document == null) ? 0 : document.hashCode());
		result = prime
				* result
				+ ((document_ProcessStepID == null) ? 0
						: document_ProcessStepID.hashCode());
		result = prime * result
				+ ((processStep == null) ? 0 : processStep.hashCode());
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
		Document_ProcessStep other = (Document_ProcessStep) obj;
		if (created == null) {
			if (other.created != null) {
				return false;
			}
		} else if (!created.equals(other.created)) {
			return false;
		}
		if (document == null) {
			if (other.document != null) {
				return false;
			}
		} else if (!document.equals(other.document)) {
			return false;
		}
		if (document_ProcessStepID == null) {
			if (other.document_ProcessStepID != null) {
				return false;
			}
		} else if (!document_ProcessStepID.equals(other.document_ProcessStepID)) {
			return false;
		}
		if (processStep == null) {
			if (other.processStep != null) {
				return false;
			}
		} else if (!processStep.equals(other.processStep)) {
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
		return "Document_ProcessStep [document_ProcessStepID="
				+ document_ProcessStepID + ", document=" + document
				+ ", processStep=" + processStep + ", created=" + created
				+ ", updated=" + updated + "]";
	}

}
