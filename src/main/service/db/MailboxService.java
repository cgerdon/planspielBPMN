package main.service.db;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import main.domain.bpmn.Mailbox;
import main.util.Constants;
import main.util.EMF;
/**
 * Service Class for the table MailBox
 * @author Christoph Gerdon
 */

public class MailboxService {
	private EntityManagerFactory emf;
	
	private EntityManager em;
	
	
	public List<Mailbox> getAllMailboxes() {
		this.em = EMF.createEntityManager();
//returns all Mailbox
		List<Mailbox> contents = new ArrayList<Mailbox>();
		Query query = em.createQuery(Constants.MAILBOX_ALL);
		contents = query.getResultList();
		
		return contents;
	}

	public Mailbox getMailboxById(int mailboxid) {
//returns a single Mailbox
		this.em = EMF.createEntityManager();
		
		List<Mailbox> content = new  ArrayList<Mailbox>();
		Query query = em.createQuery(Constants.MAILBOX_ID);
		query.setParameter("mailboxid", mailboxid);
		content = query.getResultList();
		System.out.println(content.get(0));
		return content.get(0);
	}

	public boolean deleteMailbox(int mailboxid) {
//deletes a Mailbox
		try {
			System.out.println("MAILBOXID" + mailboxid);
			this.em = EMF.createEntityManager();
			EntityTransaction tx = em.getTransaction();
			tx.begin();

			Query query = em.createQuery(Constants.DELETE_MAILBOX);
			query.setParameter("mailboxid", mailboxid);
			query.executeUpdate();
			tx.commit();
			return true;
			
		} catch (SecurityException | IllegalStateException e) {
			e.printStackTrace();
			return false;
		}
	}
}
