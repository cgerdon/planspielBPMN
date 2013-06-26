package main.domain.bpmn;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author christianroser
 *
 */
@Entity
@Table(name = "userdb")
public class User implements Serializable {



	/**
	 *
	 */
	private static final long serialVersionUID = 89026983161063026L;

	/**
	 *
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bpmn.userdb_id_seq")
	@SequenceGenerator(name = "bpmn.userdb_id_seq", sequenceName = "bpmn.userdb_id_seq",
			allocationSize = 1)
	@Column(name = "userid", unique = true, nullable = false, updatable = false)
	private int userID;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mailboxid")
	private Mailbox mailbox;
	
	@Column(name = "role")
	private String role;
	
	@Column(name = "created")
	private Date created;
	@Column(name = "updated")
	private Date updated;
	@Column(name = "mailboxid")
	private Integer mailBoxID;

	/**
	 * Defaultconstructor, needed for Entity-Bean
	 */
	public User() {

	}
	
	public User(String role, Integer mailBoxID) {
		super();
		this.role = role;
		this.created = new Date();
		this.updated = new Date();
		this.mailBoxID = mailBoxID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userid) {
		this.userID = userid;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Mailbox getMailbox() {
		return mailbox;
	}

	public void setMailbox(Mailbox mailbox) {
		this.mailbox = mailbox;
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

	public int getMailBoxID() {
		return mailBoxID;
	}

	public void setMailBoxID(Integer mailBoxID) {
		this.mailBoxID = mailBoxID;
	}

	@Override
	public String toString() {
		return "User [userid=" + userID + ", role=" + role + ", mailbox="
				+ mailbox + ", created=" + created + ", updated=" + updated
				+ ", mailboxid=" + mailBoxID + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
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
		User other = (User) obj;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}
	
	
}
