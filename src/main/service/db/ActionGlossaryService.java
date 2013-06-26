package main.service.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import main.domain.wizard.ActionGlossary;
import main.util.Constants;
import main.util.EMF;

/**
 * Service Class for the table ActionGlossary.
 * @author Christoph Gerdon
 */
public class ActionGlossaryService {
	
	private EntityManagerFactory emf;
	
	private EntityManager em;
	
/**
 * gets all Actionglossaries from db.
 * @return all actionglossaries
 */
public List<ActionGlossary> getAllActionGlossarys() {
		//returns a list for ActionGlossary
		this.em = EMF.createEntityManager();

		List<ActionGlossary> contents = new ArrayList<ActionGlossary>();
		Query query = em.createQuery(Constants.ACTIONGLOSSARY_ALL);
		contents = query.getResultList();
		
		return contents;

	}

	/**
	 * gets specific actionglossary.
	 * @param ActionGlossaryID id to find
	 * @return wanted actionglossary
	 */
	public ActionGlossary getActionGlossaryById(int ActionGlossaryID) {
		//returns a single ActionGlossary object
		this.em = EMF.createEntityManager();
		
		List<ActionGlossary> content = new  ArrayList<ActionGlossary>();
		Query query = em.createQuery(Constants.ACTIONGLOSSARY_ID);
		query.setParameter("actionGlossaryID", ActionGlossaryID);
		content = query.getResultList();
		System.out.println(content.get(0));
		return content.get(0);

	}
	
	public boolean deleteActionGlossaryById(int actionGlossaryID) {
		//deletes a single ActionGlossary
		try {
			System.out.println(actionGlossaryID);
			this.em = EMF.createEntityManager();
			ActionGlossary actionglossary = em.find(ActionGlossary.class, actionGlossaryID);
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			em.remove(actionglossary);
			tx.commit();
			return true;
			
		} catch (SecurityException | IllegalStateException e) {
			e.printStackTrace();
			return false;
		}

	}
	
	public boolean updateActionGlossary(int ActionGlossaryID, String ActionGlossaryName) {
		//updates a single ActionGlossary
		try {
			
			this.em = EMF.createEntityManager();
			EntityTransaction tx = em.getTransaction();
			tx.begin();

			Query query = em.createQuery(Constants.UPDATE_ACTIONGLOSSARY);
			query.setParameter("actionGlossaryID", ActionGlossaryID);
			query.setParameter("name", ActionGlossaryName);
			query.setParameter("actionglossaryupdated", new Date());
			query.executeUpdate();
			tx.commit();
			return true;
			
		} catch (SecurityException | IllegalStateException e) {
			e.printStackTrace();
			return false;
		}

	}

}
