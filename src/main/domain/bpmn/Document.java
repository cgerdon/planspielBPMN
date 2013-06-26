package main.domain.bpmn;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import main.domain.wizard.Document_ProcessStep;

/**
 * 
 * @author Felix
 *
 */
@Entity
@Table(name = "document")
public class Document {
	
	public Document(String name, String rfidID) {
		super();
		this.name = name;
		this.rfidID = rfidID;
		this.mailBoxID = null;
		this.created = new Date();
		this.updated = new Date();
	}
	
	public Document(String name, String rfidID, int mailBoxID) {
		super();
		this.name = name;
		this.rfidID = rfidID;
		this.mailBoxID = mailBoxID;
		this.created = new Date();
		this.updated = new Date();
	}

	private static final long serialVersionUID = 8902806983161063026L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bpmn.document_id_seq")
	@SequenceGenerator(name = "bpmn.document_id_seq", sequenceName = "bpmn.document_id_seq",
			allocationSize = 1)
	@Column(name = "documentid", unique = true, nullable = false, updatable = false)
	private int documentID;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mailboxid")
	private Mailbox mailbox;
	@Column(name = "name")
	private String name;
	@Column(name = "rfidid")
	private String rfidID;
	@Column(name = "mailboxid")
	private Integer mailBoxID;
	@Column(name = "created")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@OneToMany(mappedBy="document")
	private List<Document_ProcessStep> docSteps;
	

	@Column(name = "updated")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated;
	
	public Document() {
		
	}
	
	public void addDocStep(Document_ProcessStep docStep) {
		this.docSteps.add(docStep);
	}
	
	public int getDocumentID() {
		return documentID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRfidID() {
		return rfidID;
	}
	public void setRfidID(String rfidID) {
		this.rfidID = rfidID;
	}
	public Integer getMailBoxID() {
		return mailBoxID;
	}
	public void setMailBoxID(Integer mailBoxID) {
		this.mailBoxID = mailBoxID;
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
	public List<Document_ProcessStep> getDocSteps() {
		return docSteps;
	}

	public void setDocSteps(List<Document_ProcessStep> docSteps) {
		this.docSteps = docSteps;
	}

	

	@Override
	public String toString() {
		return "Document [documentid=" + documentID + ", mailbox=" + mailbox
				+ ", name=" + name + ", rfidid=" + rfidID + ", mailboxid="
				+ mailBoxID + ", created=" + created + ", updated=" + updated
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((rfidID == null) ? 0 : rfidID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Document other = (Document) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (rfidID == null) {
			if (other.rfidID != null)
				return false;
		} else if (!rfidID.equals(other.rfidID))
			return false;
		return true;
	}
}
