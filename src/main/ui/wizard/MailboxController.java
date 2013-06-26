package main.ui.wizard;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.faces.bean.SessionScoped;
import javax.inject.Named;

import main.domain.bpmn.Mailbox;
import main.service.db.DbService;
import main.service.db.MailboxService;


/**
 * Controller for Table Mailbox.
 * @author Christoph Gerdon
 *Contains methods for creating, updating and reading Object of the type MailboxController.
 */
@Named("mailbox")
@SessionScoped
public class MailboxController  {
	private int mailBoxID;
	private Timestamp created;
	private Timestamp updated;
	private DbService db;
	private MailboxService mailbox;
	private String pushkey = "";

	/**
	 * Constructor.
	 * @throws IOException
	 */
	public MailboxController() throws IOException {
		this.db = new DbService();
		this.mailbox = new MailboxService();
	}

	/**
	 * Calles the DbService to create a new mailbox.
	 * @return
	 */
	public String createNewMailbox() {
		Mailbox newmailbox = new Mailbox(this.pushkey);		
		return db.createNewSth(newmailbox);
	}

	public MailboxService getMailbox() {
		return this.mailbox;
	}
	public void setMailboxService(MailboxService mailbox) {
		this.mailbox = mailbox;
	}

	public List<Mailbox> getAllMailboxes() {
		return this.mailbox.getAllMailboxes();
	}
	
	public Mailbox getMailBoxByID() {
		return this.mailbox.getMailboxById(this.mailBoxID);
	}

	/**
	 * Deletes a mailbox by id.
	 * @return boolean
	 */
	public boolean deleteMailbox() {
		return this.mailbox.deleteMailbox(this.mailBoxID);
	}


	public int getMailBoxID() {
		return mailBoxID;
	}


	public void setMailBoxID(int mailBoxID) {
		this.mailBoxID = mailBoxID;
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


	public String getPushkey() {
		return pushkey;
	}


	public void setPushkey(String pushkey) {
		this.pushkey = pushkey;
	}


	public void setMailbox(MailboxService mailbox) {
		this.mailbox = mailbox;
	}



}
