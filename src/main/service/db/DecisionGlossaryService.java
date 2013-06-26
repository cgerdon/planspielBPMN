package main.service.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import main.domain.wizard.DecisionGlossary;
import main.util.Constants;
import main.util.EMF;

/**
 * Service Class for the table DecisionGlossary
 * @author Christoph Gerdon
 */


public class DecisionGlossaryService {
	
	private EntityManagerFactory emf;
	
	private EntityManager em;
	
public List<DecisionGlossary> getAllDecisionGlossarys() {
		//returns a list of DecisionGlossary
		this.em = EMF.createEntityManager();

		List<DecisionGlossary> contents = new ArrayList<DecisionGlossary>();
		Query query = em.createQuery(Constants.DECISIONGLOSSARY_ALL);
		contents = query.getResultList();
		
		return contents;

	}
	
	public DecisionGlossary getDecisionGlossaryById(int DecisionGlossaryID) {
		//returns a single DecisionGlossary
		this.em = EMF.createEntityManager();
		
		List<DecisionGlossary> content = new  ArrayList<DecisionGlossary>();
		Query query = em.createQuery(Constants.ACTIONGLOSSARY_ID);
		query.setParameter("decisionGlossaryID", DecisionGlossaryID);
		content = query.getResultList();
		System.out.println(content.get(0));
		return content.get(0);

	}
	
	public boolean deleteDecisionGlossaryById(int decisionGlossaryID) {
		//deletes a single DecisionGlossary		
		try {
			
			System.out.println(decisionGlossaryID);
			this.em = EMF.createEntityManager();
			DecisionGlossary decisionglossary = em.find(DecisionGlossary.class, decisionGlossaryID);
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			em.remove(decisionglossary);
			tx.commit();
			return true;
			
		} catch (SecurityException | IllegalStateException e) {
			e.printStackTrace();
			return false;
		}

	}
	
	public boolean updateDecisionGlossary(int decisionGlossaryID, String DecisionGlossaryName) {
		//updates a single DecisionGlossary		
		try {
			
			this.em = EMF.createEntityManager();
			EntityTransaction tx = em.getTransaction();
			tx.begin();

			Query query = em.createQuery(Constants.UPDATE_DECISIONGLOSSARY);
			query.setParameter("decisionGlossaryID", decisionGlossaryID);
			query.setParameter("name", DecisionGlossaryName);
			query.setParameter("decisionglossaryupdated", new Date());
			query.executeUpdate();
			tx.commit();
			return true;
			
		} catch (SecurityException | IllegalStateException e) {
			e.printStackTrace();
			return false;
		}

	}

}
