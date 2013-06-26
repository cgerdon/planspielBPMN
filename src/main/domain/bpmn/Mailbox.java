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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 *
 * @author christianroser
 *
 */
@Entity
@Table(name = "mailbox")
public class Mailbox implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 8901306123161063026L;

	/**
	 *
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bpmn.mailBox_id_seq")
	@SequenceGenerator(name = "bpmn.mailBox_id_seq", sequenceName = "bpmn.mailBox_id_seq",
			allocationSize = 1)
	@Column(name = "mailboxid", unique = true, nullable = false, updatable = false)
	private int mailBoxID;
	@Column(name = "pushkey", unique = true, nullable = false)
	private String pushkey;
	@Column(name = "created")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@Column(name = "updated")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated;

	/**
	 * Defaultconstructor, needed for Entity-Bean.
	 */
	public Mailbox() {

	}
	
	public Mailbox(String pushkey) {
		super();
		this.created = new Date();
		this.updated = new Date();
		this.pushkey = pushkey;
	}

	public int getMailBoxID() {
		return mailBoxID;
	}

	public void setMailBoxID(int mailBoxID) {
		this.mailBoxID = mailBoxID;
	}

	public String getPushkey() {
		return pushkey;
	}

	public void setPushkey(String pushkey) {
		this.pushkey = pushkey;
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
		result = prime * result + ((pushkey == null) ? 0 : pushkey.hashCode());
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
		Mailbox other = (Mailbox) obj;
		if (pushkey == null) {
			if (other.pushkey != null) {
				return false;
			}
		} else if (!pushkey.equals(other.pushkey)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Mailbox [mailboxid=" + mailBoxID + ", pushkey=" + pushkey
				+ ", created=" + created + ", updated=" + updated + "]";
	}

}
