package main.domain.wizard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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

import main.domain.bpmn.Roleplay;
import main.domain.bpmn.User;

/**
 * 
 * @author pierre.bartholomae
 * 
 */
@Entity
@Table(name = "processstep")
public class ProcessStep implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8689681473872598052L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bpmn.processStep_id_seq")
	@SequenceGenerator(name = "bpmn.processStep_id_seq", sequenceName = "bpmn.processStep_id_seq", allocationSize = 1)
	@Column(name = "processstepid", unique = true, nullable = false, updatable = false)
	private Integer processStepID;

	@Column(name = "name")
	private String name;

	@Column(name = "roundno")
	private Integer roundNo;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userid")
	private User user;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "actionglossaryid")
	private ActionGlossary action;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "decisionglossaryid")
	private DecisionGlossary decision;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reasonglossaryid")
	private ReasonGlossary reason;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roleplayid")
	private Roleplay roleplay;
	
	@OneToMany(mappedBy="processStep")
	private List<Document_ProcessStep> docSteps;
	
	public void addDocStep(Document_ProcessStep docStep) {
		this.docSteps.add(docStep);
	}

	@Column(name = "created")
	private Date created;

	@Column(name = "updated")
	private Date updated;

	public ProcessStep() {
	}

	public Integer getProcessStepID() {
		return processStepID;
	}

	public void setProcessStepID(Integer processStepID) {
		this.processStepID = processStepID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getRoundNo() {
		return roundNo;
	}

	public void setRoundNo(Integer roundNo) {
		this.roundNo = roundNo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ActionGlossary getAction() {
		return action;
	}

	public void setAction(ActionGlossary action) {
		this.action = action;
	}

	public DecisionGlossary getDecision() {
		return decision;
	}

	public void setDecision(DecisionGlossary decision) {
		this.decision = decision;
	}

	public ReasonGlossary getReason() {
		return reason;
	}

	public void setReason(ReasonGlossary reason) {
		this.reason = reason;
	}

	public Roleplay getRoleplay() {
		return roleplay;
	}

	public void setRoleplay(Roleplay roleplay) {
		this.roleplay = roleplay;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result
				+ ((decision == null) ? 0 : decision.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((processStepID == null) ? 0 : processStepID.hashCode());
		result = prime * result + ((reason == null) ? 0 : reason.hashCode());
		result = prime * result
				+ ((roleplay == null) ? 0 : roleplay.hashCode());
		result = prime * result + ((roundNo == null) ? 0 : roundNo.hashCode());
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
		ProcessStep other = (ProcessStep) obj;
		if (action == null) {
			if (other.action != null) {
				return false;
			}
		} else if (!action.equals(other.action)) {
			return false;
		}
		if (created == null) {
			if (other.created != null) {
				return false;
			}
		} else if (!created.equals(other.created)) {
			return false;
		}
		if (decision == null) {
			if (other.decision != null) {
				return false;
			}
		} else if (!decision.equals(other.decision)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (processStepID == null) {
			if (other.processStepID != null) {
				return false;
			}
		} else if (!processStepID.equals(other.processStepID)) {
			return false;
		}
		if (reason == null) {
			if (other.reason != null) {
				return false;
			}
		} else if (!reason.equals(other.reason)) {
			return false;
		}
		if (roleplay == null) {
			if (other.roleplay != null) {
				return false;
			}
		} else if (!roleplay.equals(other.roleplay)) {
			return false;
		}
		if (roundNo == null) {
			if (other.roundNo != null) {
				return false;
			}
		} else if (!roundNo.equals(other.roundNo)) {
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
		return "ProcessStep [processStepID=" + processStepID + ", name=" + name
				+ ", roundNo=" + roundNo + ", user=" + user + ", action="
				+ action + ", decision=" + decision + ", reason=" + reason
				+ ", roleplay=" + roleplay + ", created=" + created
				+ ", updated=" + updated + "]";
	}

	public List<Document_ProcessStep> getDocSteps() {
		return docSteps;
	}

	public void setDocSteps(List<Document_ProcessStep> docSteps) {
		this.docSteps = docSteps;
	}

}
